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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddClient extends AppCompatActivity {
    String user, password, base, ip;
    ConnectionClass connectionClass;
    Button btinfo,btcontact,btbanq,btenregistrer,btannuler;
    final Context co = this;
    String tel1,mail,site,pays,ville1,adresse1,codepostal1,codezoneclent,matricule,rsbanque,codbanque,rip,seuilcredit,seuilmax,seuilmin,solderfiche,responsable,rsclient;
    EditText edtrsclient,edtresponsable ,edtmatricule;
    CheckBox checkfodec,checkassujettu,checkexport,checkpaytva;
    String fodec,assujetti,export ,paytva;
int nbcompteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionClass = new ConnectionClass();
        setContentView(R.layout.activity_add_client);
        btinfo=(Button)findViewById(R.id.btinfo);
        btcontact=(Button)findViewById(R.id.btcontact);
        btannuler=(Button)findViewById(R.id.btannuler);
        btbanq=(Button)findViewById(R.id.btbanq);
        btenregistrer=(Button)findViewById(R.id.btenregistrer);
        edtrsclient=(EditText)findViewById(R.id.rsclient);
        edtmatricule=(EditText)findViewById(R.id.matricule);
        edtresponsable=(EditText)findViewById(R.id.responsable);
        checkfodec=(CheckBox)findViewById(R.id.fodec);
        checkassujettu=(CheckBox)findViewById(R.id.assujetti);
        checkexport=(CheckBox)findViewById(R.id.export);
        checkpaytva=(CheckBox)findViewById(R.id.paytva);

        btinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(co);
                View px = li.inflate(R.layout.diaginfoclient, null);
                AlertDialog.Builder alt = new AlertDialog.Builder(co);
                alt.setIcon(R.drawable.i2s);
                alt.setTitle("Informations");
                alt.setView(px);
               final Spinner spinregion=(Spinner)px.findViewById(R.id.spinregion);
                final EditText edtadresse=(EditText) px.findViewById(R.id.adresse1);
                final EditText edtcodepostal=(EditText) px.findViewById(R.id.codepostale);
                final EditText edtcodepays=(EditText) px.findViewById(R.id.pays);

                /////////////////////////////////////////
                SharedPreferences pref = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = pref.edit();
                user = pref.getString("user", user);
                ip = pref.getString("ip", ip);
                password = pref.getString("password", password);
                base = pref.getString("base", base);



//*********************************************************************************************************///
                //********************************zonefr***********************************////////////
                //********************************************************************////////////

                String queryregion = "select * from RegionClient ";

                try {
                    Connection connect = connectionClass.CONN(ip, password, user, base);
                    PreparedStatement stmt;
                    stmt = connect.prepareStatement(queryregion);
                    ResultSet rs = stmt.executeQuery();
                    ArrayList<String> data = new ArrayList<String>();
                    while (rs.next()) {
                        String id = rs.getString("Libelle");
                        data.add(id);

                    }
                    String[] array = data.toArray(new String[0]);
                    ArrayAdapter NoCoreAdapter = new ArrayAdapter(getApplicationContext(),
                            android.R.layout.simple_list_item_1, data);
                    spinregion.setAdapter(NoCoreAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                spinregion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {


                        ville1= spinregion.getSelectedItem().toString();


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                ////////////////////////////////////


                alt.setCancelable(false)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface di, int i) {
                                        adresse1=edtadresse.getText().toString();
                                        codepostal1=edtcodepostal.getText().toString();
                                        pays=edtcodepays.getText().toString();

                                    }
                                })
                        .setNegativeButton("Annuler",
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



        btcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(co);
                View px = li.inflate(R.layout.diagcontactclient, null);
                AlertDialog.Builder alt = new AlertDialog.Builder(co);
                alt.setIcon(R.drawable.i2s);
                alt.setTitle("Informations");
                alt.setView(px);
                final EditText edttel=(EditText) px.findViewById(R.id.tel1);
                final EditText edtmail=(EditText) px.findViewById(R.id.mail);
                final EditText edtweb=(EditText) px.findViewById(R.id.site);

                alt.setCancelable(false)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface di, int i) {
                                        tel1=edttel.getText().toString();
                                        mail=edtmail.getText().toString();
                                        site=edtweb.getText().toString();

                                    }
                                })
                        .setNegativeButton("Annuler",
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


        ////////
        btbanq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(co);
                View px = li.inflate(R.layout.diaginfobanquaire, null);
                AlertDialog.Builder alt = new AlertDialog.Builder(co);
                alt.setIcon(R.drawable.i2s);
                alt.setTitle("Informations");
                alt.setView(px);

                final Spinner spinbanq=(Spinner)px.findViewById(R.id.spincodbanque);
                final EditText edtrip=(EditText) px.findViewById(R.id.rip);
                final EditText edtseuilcredit=(EditText) px.findViewById(R.id.seuilcredit);
                final EditText edtseuilmax=(EditText) px.findViewById(R.id.seuilmax);
                final EditText edtseuilmin=(EditText) px.findViewById(R.id.seuilmin);
                final CheckBox chechsolderfiche=(CheckBox)px.findViewById(R.id.solderfiche);



                /////////////////////////////////////////


                //********************************zonefr***********************************////////////
                //********************************************************************////////////

                String queryzone = "select * from Banque ";

                try {
                    Connection connect =  connectionClass.CONN(ip, password, user, base);
                    PreparedStatement stmt;
                    stmt = connect.prepareStatement(queryzone);
                    ResultSet rs = stmt.executeQuery();
                    ArrayList<String> data = new ArrayList<String>();
                    while (rs.next()) {
                        String id = rs.getString("RaisonSociale");
                        data.add(id);

                    }
                    String[] array = data.toArray(new String[0]);
                    ArrayAdapter NoCoreAdapter = new ArrayAdapter(getApplicationContext(),
                            android.R.layout.simple_list_item_1, data);
                    spinbanq.setAdapter(NoCoreAdapter);
                } catch (SQLException e) {

                }

                spinbanq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {



                    rsbanque=  spinbanq.getSelectedItem().toString();


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                alt.setCancelable(false)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface di, int i) {
                                        rip=edtrip.getText().toString();
                                        seuilcredit=edtseuilcredit.getText().toString();
                                        seuilmax=edtseuilmax.getText().toString();
                                        seuilmin=edtseuilmin.getText().toString();
                                        if(chechsolderfiche.isChecked())
                                        {solderfiche="1";}
                                        else{solderfiche="0";}

                                    }
                                })
                        .setNegativeButton("Annuler",
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


   btenregistrer.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
         rsclient=edtrsclient.getText().toString();
         responsable=edtresponsable.getText().toString();
           matricule=edtmatricule.getText().toString();
           CodebanqueSelect cds=new CodebanqueSelect();
           cds.execute("");
           CodeZoneSelect czs=new CodeZoneSelect();
           czs.execute("");

           if(checkexport.isChecked())
           {export="1";}
           else{export="0";}
           if(checkassujettu.isChecked())
           {assujetti="1";}
           else{assujetti="0";}
           if(checkfodec.isChecked())
           {fodec="1";}
           else{fodec="0";}
           if(checkpaytva.isChecked())
           {paytva="1";}
           else{paytva="0";}
           Compteur comp=new Compteur();
           comp.execute("");
        Addclient add=new Addclient();
          add.execute("");

       }
   });
btannuler.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getApplicationContext(),Accueil.class);
        startActivity(intent);
    }
});




    }//fin oncreate



    /////////////********************codebanque ***********************/////////////
    /////////////********************************************************/////////////
    /////////////********************************************************/////////////
    /////////////***************************************************/////////////

    public class CodebanqueSelect extends AsyncTask<String, String, String> {
        String z = "";


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

           Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();
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
                    String query = "select *  from Banque where RaisonSociale='"+rsbanque+"'";
                    stmt = connect.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        codbanque= rs.getString("CodeBanque");
                    }

                    z = "Success code"+codbanque;
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

    /////////////********************codebanque ***********************/////////////
    /////////////********************************************************/////////////
    /////////////********************************************************/////////////
    /////////////***************************************************/////////////

    public class CodeZoneSelect extends AsyncTask<String, String, String> {
        String z = "";


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();
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
                    String query = "select *  from RegionClient where Libelle='"+ville1+"'";
                    stmt = connect.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                     codezoneclent= rs.getString("CodeRegionClient");
                    }



                    z = "Success code"+codezoneclent;
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


    /////////////********************************************************/////////////
    /////////////********************************************************/////////////
    /////////////***************************************************/////////////


    public class Addclient extends AsyncTask<String, String, String> {

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
                    Connection con =  connectionClass.CONN(ip, password, user, base);
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {

                        String dates = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)
                                .format(Calendar.getInstance().getTime());

                        String query = "insert into Client ( CodeClient  ,CodeFamille ,CodeForme,RaisonSociale     ,Responsable  ,MatriculeFiscale     ,RegistreCommerce  ,CodeBanque\n" +
                                "      ,RibBanquaire  ,Adresse1     ,CodePostal1   ,Ville1   ,Pays1  ,Tel1  ,Fax1  ,Adresse2,CodePostal2,Ville2 ,Pays2 ,Tel2  ,Fax2 ,Mail\n" +
                                "      ,SiteWeb ,Exoneration ,NumeroExoneration ,DateDebutExoneration ,DateFinExoneration,PayerTVA  ,Assujetti,Fodec\n" +
                                "      ,Export ,TimbreFiscal  ,Observation  ,SolderFiche  ,SeuilCredit  ,Gros ,RegimeReel  ,Ambilant  ,SeuilMaximum  ,SeuilMinimum ,CodeComptable   ,Bloquer   ,Activite   ,CodeLivreur  ,CodeZoneClient  ,CodeClientParent  ,Autorise,DateDernierBL"+
                                "   ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.setString(1,  "C00"+nbcompteur);
                        preparedStatement.setString(2, "");
                        preparedStatement.setString(3,"");
                        preparedStatement.setString(4, rsclient);
                        preparedStatement.setString(5, responsable);
                        preparedStatement.setString(6, matricule);
                        preparedStatement.setString(7, "0");
                        preparedStatement.setString(8, codbanque);
                        preparedStatement.setString(9, rip);
                        preparedStatement.setString(10,adresse1);
                        preparedStatement.setString(11, codepostal1);
                        preparedStatement.setString(12, ville1);
                        preparedStatement.setString(13, pays);
                        preparedStatement.setString(14, tel1);
                        preparedStatement.setString(15, "");
                        preparedStatement.setString(16, "0");
                        preparedStatement.setString(17, "0");
                        preparedStatement.setString(18, "0");
                        preparedStatement.setString(19,"0");
                        preparedStatement.setString(20, "0");
                        preparedStatement.setString(21,  "0");
                        preparedStatement.setString(22, mail);
                        preparedStatement.setString(23, site);
                        preparedStatement.setString(24, "0");
                        preparedStatement.setString(25,"0");
                        preparedStatement.setString(26,dates);
                        preparedStatement.setString(27,dates);
                        preparedStatement.setString(28, paytva);
                        preparedStatement.setString(29, assujetti);
                        preparedStatement.setString(30, fodec);
                        preparedStatement.setString(31,export);
                        preparedStatement.setString(32, "0");
                        preparedStatement.setString(33, "0");
                        preparedStatement.setString(34,solderfiche);
                        preparedStatement.setString(35,"0");
                        preparedStatement.setString(36,"0");
                        preparedStatement.setString(37,"0");
                        preparedStatement.setString(38,"0");
                        preparedStatement.setString(39,seuilmax);
                        preparedStatement.setString(40,seuilmin);
                        preparedStatement.setString(41,"0");
                        preparedStatement.setString(42,"0");
                        preparedStatement.setString(43,"0");
                        preparedStatement.setString(44,"0");
                        preparedStatement.setString(45,codezoneclent);
                        preparedStatement.setString(46,"0");
                        preparedStatement.setString(47,"0");
                        preparedStatement.setString(48,dates);
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



/////////////********************************************************/////////////



/////////////***************************************************/////////////

    public class Compteur extends AsyncTask<String, String, String> {
        String z = "";


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

          Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();
        }



        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    Connection connect =  connectionClass.CONN(ip, password, user, base);
                    PreparedStatement stmt;
                    String query = "select   max (CodeClient)from client";
                    stmt = connect.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        String cod= rs.getString("");
                        String  code=cod.substring(1,cod.length());
                        nbcompteur=Integer.parseInt(code);
                        nbcompteur++;
                    }



                    z = "Success code"+nbcompteur;
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










}
