package com.example.faten.testsql.task;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.R;
import com.example.faten.testsql.activity.HistoriqueBonCommande;
import com.example.faten.testsql.activity.HistoriqueLigneBonCommandeActivity;
import com.example.faten.testsql.adapter.BonCommandeAdapter;
import com.example.faten.testsql.adapter.LigneBCAdapter;
import com.example.faten.testsql.model.BonCommandeVente;
import com.example.faten.testsql.model.LigneBonCommandeVente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoriqueLBCTask extends AsyncTask<String, String, String> {


    Activity activity;

    ListView lv_list_lbc;
    String NumeroBC ;
    ProgressBar pb;


    String z = "";
    ConnectionClass connectionClass;
    String user, password, base, ip;

    String NomUtilisateur="" ;
    DateFormat dtfSQL    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    ArrayList<LigneBonCommandeVente> listLigneBonCommandeVente = new ArrayList<>() ;

    public HistoriqueLBCTask(Activity activity , ListView lv_list_lbc , String NumeroBC , ProgressBar pb) {
        this.activity = activity;
        this.lv_list_lbc = lv_list_lbc;
        this.NumeroBC = NumeroBC  ;
        this.pb = pb;


        SharedPreferences prefe = activity.getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        user = prefe.getString("user", user);
        ip = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base = prefe.getString("base", base);



        SharedPreferences pref=activity.getSharedPreferences("usersession", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt=pref.edit();
        NomUtilisateur= pref.getString("NomUtilisateur",NomUtilisateur);


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

                String queryClient = "  select \n" +
                        "NumeroBonCommandeVente  , CodeArticle  ,Quantite   , MontantTTC \n" +
                        " from  LigneBonCommandeVente  where NumeroBonCommandeVente  = '"+NumeroBC+"'   " ;

                PreparedStatement ps = con.prepareStatement(queryClient);
                ResultSet rs = ps.executeQuery();



                while (rs.next()) {

                    String NumeroBonCommandeVente = rs.getString("NumeroBonCommandeVente");
                    String CodeArticle = rs.getString("CodeArticle");
                    int Quantite = rs.getInt("Quantite");
                    double MontantTTC = rs.getDouble("MontantTTC");

                    LigneBonCommandeVente lbc  = new LigneBonCommandeVente(NumeroBonCommandeVente  ,CodeArticle ,Quantite , MontantTTC) ;
                    listLigneBonCommandeVente.add(lbc) ;

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

        LigneBCAdapter  ligneBCAdapter  = new LigneBCAdapter(activity  , listLigneBonCommandeVente) ;
        lv_list_lbc.setAdapter(ligneBCAdapter);

    }



}