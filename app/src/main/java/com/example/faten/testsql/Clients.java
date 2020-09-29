package com.example.faten.testsql;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faten.testsql.activity.FicheClientActivity;
import com.example.faten.testsql.adapter.ClientAdapter;
import com.example.faten.testsql.model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Clients extends AppCompatActivity {
    ConnectionClass connectionClass;
    Button show;
    String user, password, base, ip;
    ListView lv_client   ;
    String proid;
    final Context co = this;
    EditText textrecherche;
    String zonederecherche;
    String query;
    String NVMAIL = "";

    SearchView searchViewClient  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        connectionClass = new ConnectionClass();



        SharedPreferences prefe = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        user     = prefe.getString("user", user);
        ip       = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base     = prefe.getString("base", base);

        searchViewClient = (SearchView) findViewById(R.id.search_bar_client) ;
        lv_client = (ListView) findViewById(R.id.lstproducts);

        ListClientTask  listClientTask  = new ListClientTask() ;
        listClientTask.execute() ;



    }



public class ListClientTask extends AsyncTask<String, String, String> {
        String z = "";



        ArrayList<Client> listClient  = new ArrayList<>() ;


            @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String  queryClient = " select * from client ";
                    PreparedStatement ps = con.prepareStatement(queryClient);
                    ResultSet rs = ps.executeQuery();


                    while (rs.next()) {

                        String CodeClient = rs.getString("CodeClient") ;
                        String RaisonSociale = rs.getString("RaisonSociale") ;
                        String Responsable = rs.getString("Responsable") ;
                        String Adresse1 = rs.getString("Adresse1") ;
                        String Tel1 = rs.getString("Tel1") ;
                        String MatriculeFiscale = rs.getString("MatriculeFiscale") ;
                        String Tel2 = rs.getString("Tel2") ;
                        String Ville1 = rs.getString("Ville1") ;
                        String Mail = rs.getString("Mail") ;

                        Client         client  = new Client(CodeClient, RaisonSociale, Responsable,Adresse1 ,Tel1 , MatriculeFiscale ,Tel2 , Ville1 , Mail) ;
                        listClient.add(client);
                    }


                    z = "Success";
                }
            } catch (Exception ex) {
                z = "Error retrieving data from table";

            }
            return z;
        }



        @Override
        protected void onPostExecute(String r) {

            ClientAdapter  adapter = new ClientAdapter(Clients.this   ,listClient);
            lv_client.setAdapter(adapter);

            searchViewClient.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    if(!searchViewClient.isIconified())
                    {
                        searchViewClient.setIconified(true);
                    }
                    return  false ;
                }

                @Override
                public boolean onQueryTextChange(String query) {

                    final  ArrayList<Client>   fitlerClientList = filterClientDivers  (listClient , query) ;

                    ClientAdapter  adapter = new ClientAdapter(Clients.this   ,fitlerClientList);
                    lv_client.setAdapter(adapter);


                    lv_client.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1,
                                                int position, long arg3) {

                            Client client = fitlerClientList.get(position);
                            showDialog(client) ;

                        }
                    });

                    return false;
                }
            });


            lv_client.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {

                    Client client = listClient.get(position);
                    showDialog(client) ;

                }
            });

        }

        private ArrayList<Client> filterClientDivers (ArrayList<Client>  listClient  , String term )  {

            term = term.toLowerCase()  ;
            final ArrayList<Client> filetrListClient  = new ArrayList<>() ;

            for (Client c : listClient)
            {
                final  String  txtRaisonSocial =  c.getRaisonSociale().toLowerCase()  ;


                if ( txtRaisonSocial.contains(term)  )
                {
                    filetrListClient.add(c) ;
                }
            }
            return  filetrListClient ;

        }



    }




    public class update extends AsyncTask<String, String, String> {
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

                    String query = "Update Client set Mail='"+NVMAIL+"' where CodeClient='"+proid+"'";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();
                    z = "Updated Successfully";

                    ;
                }
            } catch (SQLException ex) {
                z = ex.toString();

            }
            return z;
        }
    }



    public  void showDialog (Client  client){

        proid            = client.getCodeClient();
        final String rs  = client.getRaisonSociale();
        String resp      = client.getResponsable();
        String adr       = client.getAdresse1() ;
        final String tel = client.getTel1();
        String tel2      = client.getTel2();
        String matricule = client.getMatriculeFiscale();
        String ville     = client.getVille1();
        final String MAIL= client.getMail();

        //creer un inflater pour appeller la layout xml on prepare la place
        LayoutInflater li = LayoutInflater.from(co);
        //appeler le fichier xml
        View px = li.inflate(R.layout.dialgclient, null);
        //creer l'alertDialogue
        AlertDialog.Builder alt = new AlertDialog.Builder(co);
        alt.setIcon(R.drawable.i2s);
        alt.setTitle("Client");
        alt.setView(px);
        connectionClass = new ConnectionClass();

        TextView trs        = (TextView) px.findViewById(R.id.rs);
        TextView tresp      = (TextView) px.findViewById(R.id.resp);
        TextView tadr       = (TextView) px.findViewById(R.id.adr);
        TextView ttel       = (TextView) px.findViewById(R.id.tel);
        final TextView mail = (TextView) px.findViewById(R.id.mail);
        TextView TRIP       = (TextView) px.findViewById(R.id.tel2);
        TextView codb       = (TextView) px.findViewById(R.id.Ville1);
        TextView mat        = (TextView) px.findViewById(R.id.matricule);

        mat.setText(matricule);
        codb.setText(tel2);
        TRIP.setText(ville);
        mail.setText(MAIL);
        trs.setText(rs);
        tresp.setText(resp);
        tadr.setText(adr);
        ttel.setText(tel);

        alt.setCancelable(true)
                .setPositiveButton("Appel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface di, int i) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:"+tel));
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                    return;
                                }
                                startActivity(callIntent);



                            }
                        })
                .setNeutralButton("Fiche",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface di, int i) {
                                Intent intent=new Intent(getApplicationContext(),FicheClientActivity.class);
                                intent.putExtra("codeclient",proid);
                                intent.putExtra("rsclient",rs);
                                startActivity(intent);



                            }
                        })

                .setNegativeButton("Email",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface di, int i) {
                                String m=mail.getText().toString();


                                if(!m.equals("")) {
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setData(Uri.parse("mailto:"));
                                    String[] TO = {MAIL};
                                    intent.putExtra(Intent.EXTRA_EMAIL, TO);
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "I2S_OPTIC");
                                    intent.putExtra(Intent.EXTRA_TEXT, " CLIENT : "+rs );
                                    intent.setType("message/rfc822");
                                    Intent  chooser = Intent.createChooser(intent, "Send mail");
                                    startActivity(chooser);

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
                                                    NVMAIL=edtmail.getText().toString();
                                                    update up=new update();
                                                    up.execute("");

                                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                                    intent.setData(Uri.parse("mailto:"));
                                                    String[] TO = {NVMAIL};
                                                    intent.putExtra(Intent.EXTRA_EMAIL, TO);
                                                    intent.putExtra(Intent.EXTRA_SUBJECT, "I2S_OPTIC");
                                                    intent.putExtra(Intent.EXTRA_TEXT, " CLIENT : "+rs );
                                                    intent.setType("message/rfc822");
                                                    Intent  chooser = Intent.createChooser(intent, "Send mail");
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




                            }})




        ;
        AlertDialog d = alt.create();
        d.show();

    }






}