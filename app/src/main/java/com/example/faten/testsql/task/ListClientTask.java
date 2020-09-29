package com.example.faten.testsql.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.faten.testsql.Clients;
import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.adapter.ClientAdapter;
import com.example.faten.testsql.adapter.ClientSelectAdapterRV;
import com.example.faten.testsql.model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ListClientTask extends AsyncTask<String, String, String> {



    Activity activity  ;
    RecyclerView  rv_listClient  ;
    ProgressBar  pb ;
    SearchView  searchViewClient  ;

    String z = "";
    ConnectionClass connectionClass;
    String user, password, base, ip;
    ArrayList<Client> listClient  = new ArrayList<>() ;

    public ListClientTask(Activity activity, RecyclerView rv_listClient, ProgressBar pb, SearchView  searchViewClient ) {
        this.activity = activity;
        this.rv_listClient = rv_listClient;
        this.pb = pb;
        this.searchViewClient = searchViewClient  ;



        SharedPreferences prefe = activity.getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        user     = prefe.getString("user", user);
        ip       = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base     = prefe.getString("base", base);

        connectionClass = new ConnectionClass();

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
     pb.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            Connection con = connectionClass.CONN(ip, password, user, base);
            if (con == null) {
                z = "Error in connection with SQL server";
            } else {

                String  queryClient = " select CodeClient , RaisonSociale  from client ";
                PreparedStatement ps = con.prepareStatement(queryClient);
                ResultSet rs = ps.executeQuery();


                while (rs.next()) {

                    String CodeClient = rs.getString("CodeClient") ;
                    String RaisonSociale = rs.getString("RaisonSociale") ;
                    /*String Responsable = rs.getString("Responsable") ;
                    String Adresse1 = rs.getString("Adresse1") ;
                    String Tel1 = rs.getString("Tel1") ;
                    String MatriculeFiscale = rs.getString("MatriculeFiscale") ;
                    String Tel2 = rs.getString("Tel2") ;
                    String Ville1 = rs.getString("Ville1") ;
                    String Mail = rs.getString("Mail") ;*/


                    //Client         client  = new Client(CodeClient, RaisonSociale, Responsable,Adresse1 ,Tel1 , MatriculeFiscale ,Tel2 , Ville1 , Mail) ;

                    Client  client  = new Client(CodeClient, RaisonSociale,0 ) ;
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

        pb.setVisibility(View.INVISIBLE);
        ClientSelectAdapterRV   clientSelectAdapterRV  = new ClientSelectAdapterRV(activity  , listClient) ;
        rv_listClient.setAdapter(clientSelectAdapterRV);


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


                ClientSelectAdapterRV   clientSelectAdapterRV  = new ClientSelectAdapterRV(activity  , fitlerClientList) ;
                rv_listClient.setAdapter(clientSelectAdapterRV);

                return false;
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