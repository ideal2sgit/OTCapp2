package com.example.faten.testsql;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import com.example.faten.testsql.adapter.ClientAdapter;
import com.example.faten.testsql.adapter.ClientChoixAdapter;
import com.example.faten.testsql.model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Choixclient extends AppCompatActivity {

     public static   Activity fa ;
    ConnectionClass connectionClass;
    Button show;
    String user, password, base, ip;
    ListView lv_list_client;
    SearchView searchView_client ;
    String proid;
    final Context co = this;


    String query;
    String rs,resp ;
    String PayerTVA  ;
    String NomUtilisateur,codelivreur="",
            texterecherche, zonederecherche;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choixclient);

        fa = this ;
        Intent intent = getIntent();
        NomUtilisateur = intent.getStringExtra("NomUtilisateur");
        codelivreur = intent.getStringExtra("CodeLivreur");
        SharedPreferences pref = getSharedPreferences("usersession", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        NomUtilisateur = pref.getString("NomUtilisateur", NomUtilisateur);
        codelivreur = pref.getString("CodeLivreur", codelivreur);
        connectionClass = new ConnectionClass();




        SharedPreferences prefe = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        user = prefe.getString("user", user);
        ip = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base = prefe.getString("base", base);


        lv_list_client = (ListView) findViewById(R.id.lv_list_client);
        searchView_client = (SearchView) findViewById(R.id.search_bar_client);

        ListClientTask  listClientTask  = new ListClientTask() ;
        listClientTask  .execute();




    }




    public class ListClientTask extends AsyncTask<String, String, String> {
        String z = "";

      ArrayList<Client> listClient  = new ArrayList<>();

        @Override
        protected void onPreExecute() {

            ///pbbar.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con =  connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String queryClient  = "select * from client" ;
                    PreparedStatement ps = con.prepareStatement(queryClient);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {

                        String CodeClient     = rs.getString("CodeClient") ;
                        String RaisonSociale  = rs.getString("RaisonSociale") ;
                        String Responsable    = rs.getString("Responsable") ;
                        String PayerTVA       = rs.getString("PayerTVA") ;

                        Client  client  = new Client(CodeClient ,RaisonSociale,Responsable ,PayerTVA) ;
                        listClient.add(client) ;

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


            ClientChoixAdapter  adapter = new ClientChoixAdapter(Choixclient.this  , listClient);
            lv_list_client.setAdapter(adapter);


            lv_list_client.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Client client   = listClient.get(position) ;

                    showDialog(client);
                }
            });


            searchView_client.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    if(!searchView_client.isIconified())
                    {
                        searchView_client.setIconified(true);
                    }
                    return  false ;
                }

                @Override
                public boolean onQueryTextChange(String query) {

                    final  ArrayList<Client>   fitlerClientList = filterClientDivers  (listClient , query) ;

                    ClientChoixAdapter adapter = new ClientChoixAdapter(Choixclient.this   ,fitlerClientList);
                    lv_list_client.setAdapter(adapter);


                    lv_list_client.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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


        }
    }


    public static Activity getInstance() {
        return fa;
    }


    public  void  showDialog (final Client  client)
    {
        AlertDialog.Builder alt = new AlertDialog.Builder(co);
        alt.setIcon(R.drawable.i2s);
        alt.setTitle(proid);
        alt.setMessage("Client : "+client.getRaisonSociale());
        alt.setPositiveButton("Valider",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface di, int i) {
                        Intent in =new Intent(getApplicationContext(),BonDeCommandeActivity.class);
                        in.putExtra("codeclient",client.getCodeClient());
                        in.putExtra("NomUtilisteur",NomUtilisateur);
                        in.putExtra("clientrs",client.getRaisonSociale());
                        in.putExtra("CodeLivreur",codelivreur);
                        in.putExtra("PayerTVA",client.getPayerTVA());
                        startActivity(in);


                    }
                });
        alt.setNegativeButton("annuler",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface di, int i) {
                        di.cancel();


                    }
                });
        AlertDialog d = alt.create();
        d.show();


    }

    private ArrayList<Client> filterClientDivers (ArrayList<Client>  listClient  , String term )  {

        term = term.toLowerCase()  ;
        final ArrayList<Client> filetrListClient  = new ArrayList<>() ;

        for (Client c : listClient)
        {
            final  String  txtRaisonSocial =  c.getRaisonSociale().toLowerCase()  ;

            if (txtRaisonSocial.contains(term))
            {
                filetrListClient.add(c) ;
            }
        }
        return  filetrListClient ;

    }

}