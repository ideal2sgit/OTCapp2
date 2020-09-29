package com.example.faten.testsql;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.faten.testsql.activity.Accueil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddArticle extends AppCompatActivity {
    Spinner spinMarque,spintype,spinfamille,spinfournisseur,spincouleur;
    ConnectionClass connectionClass;
    String codart, codemarque,typeart ,familleart,  marque,rsfournisseur,codefournisseur,codecouleur,refrence,designation;
    final Context co = this;
       private static final int RESULT_LOAD_IMAGE = 1;
    String DernierPrixAchatHT="",TauxRemise="",PrixAchatHT="",LiteTauxRemise="",Actif="",Stockable="",Destockage="",DernierPrixDevise="",prixTTC  ="";
    EditText textereference,textdesignation;
    CheckBox destockchheck,actifcheck,stockchheck;
    String user, password, base, ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);
        connectionClass = new ConnectionClass();

        spinMarque = (Spinner) findViewById(R.id.spinmarque);
        spinfournisseur = (Spinner) findViewById(R.id.spinfournisseur);
        spincouleur = (Spinner) findViewById(R.id.spincouleur);
        spinfamille = (Spinner) findViewById(R.id.spinfamille);
        spintype= (Spinner) findViewById(R.id.spintype);

        Button btprix=(Button)findViewById(R.id.btprix) ;
        Button btvalider=(Button)findViewById(R.id.btnvalider) ;
        Button btannuler=(Button)findViewById(R.id.btannuler) ;

        textdesignation=(EditText)findViewById(R.id.designation);
        textereference=(EditText)findViewById(R.id.reference);

        actifcheck=(CheckBox)findViewById(R.id.actif);
        stockchheck=(CheckBox)findViewById(R.id.stockable);
        destockchheck=(CheckBox)findViewById(R.id.destokage);

        SharedPreferences pref = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        user = pref.getString("user", user);
        ip = pref.getString("ip", ip);
        password = pref.getString("password", password);
        base = pref.getString("base", base);

        btprix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(co);
                View px = li.inflate(R.layout.prixarticle, null);
                AlertDialog.Builder alt = new AlertDialog.Builder(co);
                alt.setIcon(R.drawable.i2s);
                alt.setView(px);
                alt.setTitle("Prix ");
                final EditText prix=(EditText)px.findViewById(R.id.prixttc);
                final EditText prixachatht=(EditText)px.findViewById(R.id.prixachatht);
                final EditText tauxremise=(EditText)px.findViewById(R.id.tauxremise);
                final EditText Literemise=(EditText)px.findViewById(R.id.remise);
                final EditText dernierprixdevise=(EditText)px.findViewById(R.id.prixdevise);
                final EditText dernierprixht=(EditText)px.findViewById(R.id.dernierpaht);

                alt.setCancelable(false)
                        .setPositiveButton("Valider",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface di, int i) {
                                        PrixAchatHT=prixachatht.getText().toString();
                                        DernierPrixAchatHT=dernierprixht.getText().toString();
                                        DernierPrixDevise=dernierprixdevise.getText().toString();
                                        TauxRemise=tauxremise.getText().toString();
                                        LiteTauxRemise=Literemise.getText().toString();
                                        prixTTC=prix.getText().toString();

                                    }
                                })  .setNegativeButton("Annuler",
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


        //********************************famille***********************************////////////
        //********************************************************************////////////

        String queryfamille = "select * from FamilleArticle ";

        try {
            Connection connect = connectionClass.CONN(ip, password, user, base);
            PreparedStatement stmt;
            stmt = connect.prepareStatement(queryfamille);
            ResultSet rs = stmt.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
            while (rs.next()) {
                String id = rs.getString("CodeFamille");
                data.add(id);
            }
            String[] array = data.toArray(new String[0]);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, data);
            spinfamille.setAdapter(NoCoreAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spinfamille.setSelection(2);
        spinfamille.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {


                familleart= spinfamille.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

         //*****************************************************************************************///
        //********************************type***********************************////////////
        //********************************************************************////////////

        String querytype = "select * from TypeArticle ";

        try {
            Connection connect = connectionClass.CONN(ip, password, user, base);
            PreparedStatement stmt;
            stmt = connect.prepareStatement(querytype);
            ResultSet rs = stmt.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
            while (rs.next()) {
                String id = rs.getString("CodeType");
                data.add(id);

            }
            String[] array = data.toArray(new String[0]);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, data);
            spintype.setAdapter(NoCoreAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spintype.setSelection(2);
        spintype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {



             typeart= spintype.getSelectedItem().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //*****************************************************************************///
        //************************************Marque********************************////////////
        //********************************* ***********************************////////////

        String query = "select * from MarqueArticle";

        try {
            Connection connect = connectionClass.CONN(ip, password, user, base);
            PreparedStatement stmt;
            stmt = connect.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
            while (rs.next()) {
                String id = rs.getString("Libelle");
                data.add(id);

            }
            String[] array = data.toArray(new String[0]);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, data);
            spinMarque.setAdapter(NoCoreAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spinMarque.setSelection(0);
        spinMarque.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                marque = spinMarque.getSelectedItem().toString();
                CodeMarqueArtSelect c=new CodeMarqueArtSelect();
                c.execute("");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //**************************************************************************************///
        //********************************famille***********************************////////////
        //********************************************************************////////////

        String queryfournisseur = "select * from Fournisseur ";

        try {
            Connection connect = connectionClass.CONN(ip, password, user, base);
            PreparedStatement stmt;
            stmt = connect.prepareStatement(queryfournisseur);
            ResultSet rs = stmt.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
            while (rs.next()) {
                String id = rs.getString("RaisonSociale");
                data.add(id);

            }
            String[] array = data.toArray(new String[0]);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, data);
            spinfournisseur.setAdapter(NoCoreAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spinfournisseur.setSelection(2);
        spinfournisseur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {


                rsfournisseur=  spinfournisseur.getSelectedItem().toString();
                CodeFournisseurSelect c=new CodeFournisseurSelect();
                c.execute("");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       //****************************************************************************************///
        //********************************************************************////////////
        //*********************************Marque ***********************************////////////
        //********************************************************************////////////

        String querycouleur = "select * from Couleur";

        try {
            Connection connect =  connectionClass.CONN(ip, password, user, base);
            PreparedStatement stmt;
            stmt = connect.prepareStatement(querycouleur);
            ResultSet rs = stmt.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
            while (rs.next()) {
                String id = rs.getString("CodeCouleur");
                data.add(id);

            }
            String[] array = data.toArray(new String[0]);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, data);
            spincouleur.setAdapter(NoCoreAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spincouleur.setSelection(0);
        spincouleur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                codecouleur = spincouleur.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//*********************************************************************************************************///


        btvalider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                designation=textdesignation.getText().toString();
                refrence=textereference.getText().toString();
                codart=refrence+" "+codecouleur;

                if(actifcheck.isSelected())
                {
                    Actif="1";
                };
                if(stockchheck.isSelected())
                {
                    Stockable="1";
                }
                if(destockchheck.isSelected())
                {
                    Destockage="1";

                }
                Addart ad=new Addart();
                ad.execute("");




            }
        });

        btannuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Accueil.class);
                startActivity(intent);
            }
        });


    } //fin oncreate




    /////////////*******************************************/////////////
    /////////////********************************************************/////////////
    /////////////************************codeMarque ********************************/////////////
    /////////////***************************************************/////////////

    public class CodeMarqueArtSelect extends AsyncTask<String, String, String> {
        String z = "";


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

            //    Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();
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
                    String query = "select *  from MarqueArticle where Libelle='"+marque+"'";
                    stmt = connect.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        codemarque= rs.getString("CodeMarque");
                    }

                    z = "Success code marque from table marque article "+codemarque;
                }
            } catch (Exception ex) {
                z = "Error retrieving code marque from table marque article";

            }
            return z;
        }
    }


    /////////////******************** fin code marque ***********************/////////////
    /////////////********************************************************/////////////



    /////////////********************codeFournisseurselect  ***********************/////////////
    /////////////********************************************************/////////////
    /////////////********************************************************/////////////
    /////////////***************************************************/////////////

    public class CodeFournisseurSelect extends AsyncTask<String, String, String> {
        String z = "";


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

            //    Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();
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
                    String query = "select *  Fournisseur where RaisonSociale='"+rsfournisseur+"'";
                    stmt = connect.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        codefournisseur= rs.getString("CodeFournisseur");
                    }



                    z = "Success code";
                }
            } catch (SQLException ex) {
                z = ex.toString();

            }
            return z;
        }
    }


    /////////////********************************************************/////////////
    /////////////********************************************************/////////////
    /////////////***************************************************/////////////


    public class Addart extends AsyncTask<String, String, String> {

        String z = "",tt="";
        Boolean isSuccess = false;


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();


        }

        @Override
        protected String doInBackground(String... params) {
            if (false)
            {}
            else {
                try {
                    Connection con = connectionClass.CONN(ip, password, user, base);
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {


                        String query = "insert into Article (CodeArticle,CodeFamille ,CodeSousFamille ,CodeType ,CodeMarque ,CodeUnite    ," +
                                "CodeUniteVente ,ValeurUniteVente ,Designation    ,DernierPrixAchatHT  ,DernierTauxRemise ,PrixAchatHT  ,Marge," +
                                " PrixVenteHT,CodeTVA,PrixVenteTTC,LiteTauxRemise ,Fodec  ,Stockable  ,Actif ,TailleCouleur  , Observation " +
                                " ,NumeroSerie,CodeBarre,ImageCodeBarre ,Logo  ,CodeFournisseur   ,CodeSexe , DernierPrixDevise,Reference," +
                                "CodeCouleur,CodeCalibre ,CodeEtui,Destockage)\n" +
                                "    values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.setString(1,codart);
                        preparedStatement.setString(2, familleart);
                        preparedStatement.setString(3,"0");
                        preparedStatement.setString(4, typeart);
                        preparedStatement.setString(5, codemarque);
                        preparedStatement.setString(6, "0");
                        preparedStatement.setString(7, "0");
                        preparedStatement.setString(8, "0");
                        preparedStatement.setString(9, designation);
                        preparedStatement.setString(10,DernierPrixAchatHT );
                        preparedStatement.setString(11, TauxRemise);
                        preparedStatement.setString(12, PrixAchatHT);
                        preparedStatement.setString(13, "0");
                        preparedStatement.setString(14, "100");
                        preparedStatement.setString(15, "3");
                        preparedStatement.setString(16, prixTTC);
                        preparedStatement.setString(17, LiteTauxRemise);
                        preparedStatement.setString(18, "0");
                        preparedStatement.setString(19,Stockable);
                        preparedStatement.setString(20, Actif);
                        preparedStatement.setString(21,  "0");
                        preparedStatement.setString(22, "0");
                        preparedStatement.setString(23, "0");
                        preparedStatement.setString(24, "0");
                        preparedStatement.setObject(25,null);
                        preparedStatement.setObject(26,null);
                        preparedStatement.setString(27, "00");
                        preparedStatement.setString(28, "");
                        preparedStatement.setString(29, DernierPrixDevise);
                        preparedStatement.setString(30, refrence);
                        preparedStatement.setString(31, codecouleur);
                        preparedStatement.setString(32, "");
                        preparedStatement.setString(33, "");
                        preparedStatement.setString(34, Destockage);
                        preparedStatement.executeUpdate();
                        z = "Added Successfully ";
                        isSuccess = true;
                    }
                } catch ( SQLException ex) {
                    isSuccess = false;
                    z = ex.toString();
                }
            }
            return z;
        }
    }






}
