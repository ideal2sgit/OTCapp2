package com.example.faten.testsql;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faten.testsql.activity.Accueil;
import com.example.faten.testsql.adapter.ArticleCMDValideAdapter;
import com.example.faten.testsql.model.Article;
import com.example.faten.testsql.model.BonCommandeVente;
import com.example.faten.testsql.model.LigneBonCommandeVente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Validationcmd extends AppCompatActivity {

    Button    btn_annuler,btn_enregistrer,btn_btmail;
    TextView  txt_client ,txt_total ;
    ListView  lv_list_article_valide;



    /// bon Commande OBJECT
    BonCommandeVente bonCommandeVente = new BonCommandeVente()  ;
    ArrayList<LigneBonCommandeVente> lignesBonCommandeVentes = new ArrayList<>() ;

    ConnectionClass connectionClass;

    String codearticle="";
    final Context co = this;
    double totalprix ;

    String annee  ,anciencompteur,NumeroBonCommnade, incompteur,NomUtilisateur,CodeClient,CodeLivreur;
    String prefixebn,rsclient;
    String PayerTVA  ="";
    EditText note;
    String obser="",mail="",nvmail="ggh";
    String user, password, base, ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validationcmd);


        btn_annuler      = (Button)findViewById(R.id.annuler);
        btn_enregistrer  = (Button)findViewById(R.id.enregistrement);
        btn_btmail       = (Button)findViewById(R.id.btmail);
        lv_list_article_valide           = (ListView) findViewById(R.id.lv_list_article_valide);
        txt_total             = (TextView)findViewById(R.id.txt_total);
        txt_client       = (TextView)findViewById(R.id.txt_client);


        SharedPreferences prefe = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        user = prefe.getString("user", user);
        ip = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base = prefe.getString("base", base);


        Intent intent=getIntent();
        NomUtilisateur =intent.getStringExtra("NomUtilisateur");
        rsclient       =intent.getStringExtra("rsclient");
        CodeClient     =intent.getStringExtra("CodeClient");
        CodeLivreur    =intent.getStringExtra("CodeLivreur");
        PayerTVA       = intent.getStringExtra("PayerTVA" );
        txt_client.setText(rsclient);

        SharedPreferences pref=getSharedPreferences("usersession", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt=pref.edit();
        NomUtilisateur= pref.getString("NomUtilisateur",NomUtilisateur);
        prefixebn="TL"+NomUtilisateur;

        connectionClass = new ConnectionClass();



        NouvelCompteur nc=new NouvelCompteur();  //  préparation nouvel Compteur
        nc.execute("");

        AffichageListArticleCMD affichageListArticleCMDtask = new AffichageListArticleCMD();
        affichageListArticleCMDtask.execute();




        MailClient m=new MailClient();
        m.execute("");


        btn_annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent inte = new Intent(getApplicationContext(),Accueil.class);
                BonDeCommandeActivity.getInstance().finish();
                Choixclient.getInstance().finish();
                finish();
                startActivity(inte);

            }
        });



        btn_enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LayoutInflater li = LayoutInflater.from(co);
                View px = li.inflate(R.layout.notes, null);
                AlertDialog.Builder alt = new AlertDialog.Builder(co);
                alt.setIcon(R.drawable.i2s);
                alt.setView(px);
                alt.setTitle("Notes ");

                note = (EditText) px.findViewById(R.id.note);

                alt.setCancelable(false)
                        .setPositiveButton("Valider",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface di, int i) {

                                obser=note.getText().toString();
                                bonCommandeVente.setObservation(obser);

                                /*BonCMDclient BD=new BonCMDclient(); //  insertion BonCommandeVente
                                BD.execute("");

                                UpdateBonCommandeVente updateBonCommandeVente=new UpdateBonCommandeVente(); // update les  totaux
                                updateBonCommandeVente.execute("");

                                updateNumLigne updateNumLigne=new updateNumLigne(); // update TLa --> CV18/00195
                                updateNumLigne.execute();

                                deletecmdTL tl=new deletecmdTL();
                                tl.execute("");*/

                                InsertBonCommandeAndLignes insertBonCommandeAndLignes = new InsertBonCommandeAndLignes();
                                insertBonCommandeAndLignes.execute() ;

                                UpdateCompteur updateCompteurTask = new UpdateCompteur();// Compteur incrémenté
                                updateCompteurTask.execute("");

                                Intent inte = new Intent(getApplicationContext(),Accueil.class);
                                BonDeCommandeActivity.getInstance().finish();
                                Choixclient.getInstance().finish();
                                finish();
                                startActivity(inte);


                            }
                        })

                        .setNeutralButton("Annuler",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface di, int i) {
                                  di.cancel();

                                    }
                                });
                AlertDialog d = alt.create();
                d.show();

            }
        });



        btn_btmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(getApplicationContext(),rsclient,Toast.LENGTH_SHORT).show();

                if(!mail.equals("")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(co);
                    alert.setIcon(R.drawable.i2s);
                    alert.setTitle("Bon De Commande");
                    alert.setMessage("Voulez vous envoyer la commande par Mail ");
                    alert.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface di, int i) {
                                    String dates = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                                            .format(Calendar.getInstance().getTime());
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setData(Uri.parse("mailto:"));
                                    String[] TO = {mail};
                                    intent.putExtra(Intent.EXTRA_EMAIL, TO);
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "BonCommandeVente ");
                                    intent.putExtra(Intent.EXTRA_TEXT, " CLIENT : " + rsclient + "\n Date :" + dates + "\n Totale TTC =" + totalprix);
                                    intent.setType("message/rfc822");
                                    Intent chooser = Intent.createChooser(intent, "Send mail");
                                    startActivity(chooser);

                                }
                            });
                    alert.setNegativeButton("Annuler",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface di, int i) {
                                    di.cancel();


                                }
                            });
                    AlertDialog dd = alert.create();

                    dd.show();
                }else{

                    LayoutInflater li = LayoutInflater.from(co);
                    View px = li.inflate(R.layout.diagmail, null);
                    AlertDialog.Builder alt = new AlertDialog.Builder(co);
                    alt.setIcon(R.drawable.i2s);
                    alt.setView(px);
                    alt.setTitle("Bon De Commande");
                    final EditText edtmail=(EditText)px.findViewById(R.id.edtmail);
                    alt.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface di, int i) {
                                    nvmail=edtmail.getText().toString();
                               //    Toast.makeText(getApplicationContext(),rsclient,Toast.LENGTH_SHORT).show();
                                 //   Toast.makeText(getApplicationContext(),nvmail,Toast.LENGTH_SHORT).show();
                                     up upp=new up();
                                     upp.execute("");
                                    String dates = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                                            .format(Calendar.getInstance().getTime());

                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setData(Uri.parse("mailto:"));
                                    String[] TO = {nvmail};
                                    intent.putExtra(Intent.EXTRA_EMAIL, TO);
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "BonCommandeVente " );
                                    intent.putExtra(Intent.EXTRA_TEXT, " CLIENT : " + rsclient + "\n Date :" + dates + "\n Totale TTC =" + totalprix);
                                    intent.setType("message/rfc822");
                                    Intent chooser = Intent.createChooser(intent, "Send mail");
                                    startActivity(chooser);

                                }
                            });
                    alt.setNegativeButton("Annuler",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface di, int i) {
                                    di.cancel();


                                }
                            });




                    AlertDialog d = alt.create();

                    d.show();


                }





            }
        });



    }


    public class AffichageListArticleCMD extends AsyncTask<String, String, String> {

        String res  = "";


        ArrayList<Article> listArticle  = new ArrayList<>() ;
        double SUM_MontantHT  =0 ;
        double SUM_MontantTVA =0 ;
        double SUM_MontantTTC =0 ;

        double SUM_MontantRemise  =0 ;
        double SUM_MontantFodec   =0 ;
        double SUM_NetHT          =0 ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {

            double TauxTVA = 19.0;  //  par  défaut
            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    res = "Error in connection with SQL server";
                } else {

                    lignesBonCommandeVentes.clear();
                    int num_order = 1;
                    for ( Article a : BonDeCommandeActivity.listArticleAcommander ) {

                        if (a.getNbrCLick() != 0) {
                            Log.e("ClienTVA" , ""+PayerTVA) ;
                            listArticle.add(a);
                            double montantHT = a.getNbrCLick() * a.getPrixVenteHT ( );
                            double TauxRemise  = 0.0 ;
                            double montantRemise =montantHT * TauxRemise / 100;
                            double netHT = montantHT - montantRemise ;


                            if(PayerTVA . equals("1"))
                            {
                                String queyTVA = "  Select * from TVA WHERE CodeTVA = '"+a.getCodeTVA()+"' ";
                                Log.e("query", queyTVA);
                                PreparedStatement ps = con.prepareStatement(queyTVA);
                                ResultSet rs = ps.executeQuery();
                                while (rs.next()) {
                                    TauxTVA = rs.getDouble("TauxTVA") ;
                                }

                                Log.e("TVA",""+TauxTVA);
                            }
                            else  if(PayerTVA . equals("0")){

                                TauxTVA = 0 ;
                            }


                            double montantTVA = netHT * TauxTVA / 100;
                            double montantTTC  = netHT +  montantTVA ;
                            double prixAchatNet = a.getPrixAchatHT()  ;

                            LigneBonCommandeVente ligneBonCommandeVente = new LigneBonCommandeVente(NumeroBonCommnade, a.getCodeArticle(), a.getDesignation(), num_order, a.getPrixVenteHT()
                                    , a.getNbrCLick(), montantHT, TauxTVA, montantTVA,
                                    montantTTC, "", TauxRemise, montantRemise, 0.0, netHT, prixAchatNet);

                            Log.e("ligneBonCommandeVente", ligneBonCommandeVente.toString());
                            lignesBonCommandeVentes.add(ligneBonCommandeVente);

                            SUM_MontantHT  = SUM_MontantHT + montantHT;
                            SUM_MontantTVA = SUM_MontantTVA + montantTVA;
                            SUM_MontantTTC = SUM_MontantTTC + montantTTC;
                            SUM_MontantRemise = SUM_MontantRemise + montantRemise;
                            SUM_MontantFodec = SUM_MontantFodec + 0.0;
                            SUM_NetHT = SUM_NetHT + netHT;

                            num_order++;
                        }

                    }
                    Date currentDate = new Date();
                    bonCommandeVente = new BonCommandeVente(NumeroBonCommnade, currentDate, "", CodeClient, "", currentDate,
                            1, SUM_MontantHT, SUM_MontantTVA, SUM_MontantTTC, "E01", NomUtilisateur, currentDate, currentDate, "", SUM_MontantRemise, SUM_MontantFodec, SUM_NetHT, 0.0, 0.0, CodeLivreur);

                    Log.e("BonCommandeVente", bonCommandeVente.toString());

                }
            }
            catch (SQLException ex) {

                res = " ERROR " + ex.toString();

            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ArticleCMDValideAdapter adapter = new ArticleCMDValideAdapter(Validationcmd.this , listArticle);
            lv_list_article_valide.setAdapter(adapter);



            lv_list_article_valide.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                    Toast.makeText(co ," article "+position ,Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder alt = new AlertDialog.Builder(co);
                    alt.setIcon(R.drawable.i2s);
                    alt.setTitle("Commande");
                    alt.setMessage("Supprimer la demande de cet article");
                    alt.setPositiveButton("Supprimer",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface di, int i) {


                                    Article articleToDelete = listArticle.get(position) ;
                                    articleToDelete.setNbrCLick(0);
                                    //listArticle.remove(articleToDelete) ;
                                    int indexArticleToDelete =  BonDeCommandeActivity.listArticleAcommander.indexOf(articleToDelete) ;
                                    BonDeCommandeActivity.listArticleAcommander.get(indexArticleToDelete).setNbrCLick(0);

                                    //  update list
                                    AffichageListArticleCMD affichageListArticleCMDtask = new AffichageListArticleCMD();
                                    affichageListArticleCMDtask.execute();


                                   /* ArticleCMDAdapter adapter = new ArticleCMDAdapter(ValidationCMDActivity.this, BonDeCommandeActivity.listArticle, BonDeCommandeActivity.list_article_qt_a_commander);
                                    BonDeCommandeActivity.listArticleCMD.setAdapter(adapter);*/

                                }
                            });

                    alt.setNegativeButton("annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }) ;
                    AlertDialog d = alt.create();
                    d.show();
                }
            });

            DecimalFormat df = new DecimalFormat("0.000") ;
            txt_total.setText(df.format(SUM_MontantTTC));

            totalprix = SUM_MontantTTC ;

        }
    }



    public class InsertBonCommandeAndLignes extends AsyncTask<String, String, String> {
        String z = "";

        DateFormat dfSQL = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con =  connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String queryBonCommandeVente = "INSERT INTO [BonCommandeVente]\n" +
                            "           ([NumeroBonCommandeVente],[DateBonCommandeVente],[NumeroDevisVente],[CodeClient],[ReferenceClient],[DateReferenceClient]\n" +
                            "           ,[DelaiLivraison] ,[TotalHT],[TotalTVA],[TotalTTC],[NumeroEtat],[NomUtilisateur],[DateCreation],[HeureCreation] ,[Observation]\n" +
                            "           ,[TotalRemise],[TotalFodec] ,[TotalNetHT] ,[TauxRemiseExceptionnel],[MontantRemiseExceptionnel] ,[CodeLivreur])\n" +
                            "     VALUES\n" +
                            "           ('"+bonCommandeVente.getNumeroBonCommandeVente()+"'\n" + //1
                            "           ,'"+dfSQL.format(bonCommandeVente.getDateBonCommandeVente())+"'\n" + //2
                            "           ,'"+bonCommandeVente.getNumeroDevisVente()+"'\n" + //3
                            "           ,'"+bonCommandeVente.getCodeClient()+"'\n" + //4
                            "           ,'"+bonCommandeVente.getReferenceClient()+"'\n" +//5
                            "           ,'"+dfSQL.format(bonCommandeVente.getDateReferenceClient())+"'\n" +//6
                            "           ,'"+bonCommandeVente.getDelaiLivraison()+"'\n" +//7
                            "           ,'"+bonCommandeVente.getTotalHT()+"'\n" +//8
                            "           ,'"+bonCommandeVente.getTotalTVA()+"'\n" +//9
                            "           ,'"+bonCommandeVente.getTotalTTC()+"'\n" +//10
                            "           ,'"+bonCommandeVente.getNumeroEtat()+"'\n" +//11
                            "           ,'"+bonCommandeVente.getNomUtilisateur()+"'\n" +//12
                            "           ,'"+dfSQL.format(bonCommandeVente.getDateCreation())+"'\n" +//13
                            "           ,'"+dfSQL.format(bonCommandeVente.getHeureCreation())+"'\n" +//14
                            "           ,'"+bonCommandeVente.getObservation()+"'\n" +//15
                            "           ,'"+bonCommandeVente.getTotalRemise()+"'\n" +//16
                            "           ,'"+bonCommandeVente.getTotalFodec()+"'\n" +//17
                            "           ,'"+bonCommandeVente.getTotalNetHT()+"'\n" +//18
                            "           ,'0'\n" +//19
                            "           ,'0'\n" +//20
                            "           ,'"+bonCommandeVente.getCodeLivreur()+"')";//21

                    Statement stmt = con.createStatement();
                    Log.e("query", queryBonCommandeVente) ;
                    stmt.executeUpdate(queryBonCommandeVente);

                    Log.e("INSERT","BonCommandeVente") ;


                    for(LigneBonCommandeVente l : lignesBonCommandeVentes)
                    {

                        String queryLigneBonCommandeVente = "INSERT INTO  [LigneBonCommandeVente]\n" +
                                "           ([NumeroBonCommandeVente],[CodeArticle],[DesignationArticle],[NumeroOrdre],[PrixVenteHT],[Quantite] ,[MontantHT]\n" +
                                "           ,[TauxTVA],[MontantTVA],[MontantTTC],[Observation],[TauxRemise],[MontantRemise],[MontantFodec],[NetHT],[PrixAchatNet])\n" +
                                "     VALUES\n" +
                                "           ('"+l.getNumeroBonCommandeVente()+"'\n" + //1
                                "           ,'"+l.getCodeArticle()+"'\n" + //2
                                "           ,'"+l.getDesignationArticle()+"'\n" + //3
                                "           ,'"+l.getNumeroOrdre()+"'\n" + //4
                                "           ,'"+l.getPrixVenteHT()+"'\n" + //5
                                "           ,'"+l.getQuantite()+"'\n" + //6
                                "           ,'"+l.getMontantHT()+"'\n" + //7
                                "           ,'"+l.getTauxTVA()+"'\n" + //8
                                "           ,'"+l.getMontantTVA()+"'\n" + //9
                                "           ,'"+l.getMontantTTC()+"'\n" + //10
                                "           ,'"+l.getObservation()+"'\n" + //11
                                "           ,'"+l.getTauxRemise()+"'\n" + //12
                                "           ,'"+l.getMontantRemise()+"'\n" + //13
                                "           ,'"+l.getMontantFodec()+"'\n" + //14
                                "           ,'"+l.getNetHT()+"'\n" + //15
                                "           ,'"+l.getPrixAchatNet()+"')" ; //16

                        Statement stmtLigne = con.createStatement();
                        Log.e("query", queryLigneBonCommandeVente) ;
                        stmtLigne.executeUpdate(queryLigneBonCommandeVente);
                        Log.e("INSERT","LigneBonCommandeVente") ;
                    }



                    z = "Success TOTAL ";
                }
            } catch (SQLException ex) {
                z = ex.toString();

                Log.e("ERROR" , z) ;

            }
            return z;
        }
    }


    public class UpdateCompteur extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;
        String e="";

        @Override
        protected String doInBackground(String... params) {
            if (false)
               {
                z = "Erreur ";
               }
            else {
                try {
                    Connection con =  connectionClass.CONN(ip, password, user, base);
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {

                        String dates = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                                .format(Calendar.getInstance().getTime());



                        String query = "update CompteurPiece  set Compteur='"+incompteur+"' where NomPiecer='BonCommandeVente'";
                        PreparedStatement preparedStatement = con.prepareStatement(query);


                        preparedStatement.executeUpdate();
                        z = "Compteur+";
                        isSuccess = true;
                    }
                } catch ( SQLException ex) {
                    isSuccess = false;
                    z ="echec update compteur"+ ex.toString();
                }
            }
            return z;
        }
    }


    public class NouvelCompteur extends AsyncTask<String, String, String> {


        Boolean isSuccess = false;
        String z="";

        @Override
        protected String doInBackground(String... params) {
            if (false)

            {
                z = "Erreur ";
            }
            else {
                try {
                    Connection con =  connectionClass.CONN(ip, password, user, base);
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {


                        String query = "  Select * from CompteurPiece  where NomPiecer='BonCommandeVente' ";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        while (rs.next()) {
                            anciencompteur = rs.getString("Compteur");
                            annee = rs.getString("Annee");
                        }
                        Float comp=Float.parseFloat(anciencompteur);
                        comp++;

                        DecimalFormat numberFormat = new DecimalFormat("00000");
                        incompteur = numberFormat.format(comp);


                        NumeroBonCommnade="CV"+annee+""+incompteur;


                        Log.e("NumeroBonCommnade" , NumeroBonCommnade+"") ;
                        z = "ancien Compteur"+anciencompteur;
                        isSuccess = true;
                    }
                } catch ( SQLException ex) {
                    isSuccess = false;
                    z = "echec import ancien compteur"+ex.toString();
                    Log.e("ERROR",z);
                }
            }
            return z;
        }
    }


    public class MailClient extends AsyncTask<String, String, String> {
        String z = "";


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

          //  Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con =  connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    Connection connect =  connectionClass.CONN(ip, password, user, base);
                    PreparedStatement stmt;
                    String query = "select *  from Client where RaisonSociale='"+rsclient+"'";
                    stmt = connect.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        mail= rs.getString("Mail");
                    }

                    z = "Success mail ";
                }
            } catch (Exception ex) {
                z = "Error";

            }
            return z;
        }
    }

    public class up extends AsyncTask<String, String, String> {
        String z = "";


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

        // Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con =  connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String query = "Update Client set Mail='"+nvmail+"' where RaisonSociale='"+rsclient+"'";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();
                    z = "insert Mail Successfully";

                }
            } catch (SQLException ex) {
                z = ex.toString();

            }
            return z;
        }
    }




}
