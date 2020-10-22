package com.example.faten.testsql.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.activity.BonCommandeTerminalAutomatiqueActivity;
import com.example.faten.testsql.model.OrderDepotDemande;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DeletePieceNNVendu extends AsyncTask<String, String, String> {


    Activity activity;

    String res;
    ConnectionClass connectionClass;
    String user, password, base, ip;
    String NomUtilisateur ;
    public DeletePieceNNVendu(Activity activity ) {
        this.activity = activity;

        SharedPreferences prefe = activity.getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        user = prefe.getString("user", user);
        ip = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base = prefe.getString("base", base);

        SharedPreferences pref = activity.getSharedPreferences("usersession", Context.MODE_PRIVATE);
        NomUtilisateur = pref.getString("NomUtilisateur", NomUtilisateur);


        connectionClass = new ConnectionClass();

        Log.e("DeletePieceNNVendu","DeletePieceNNVendu") ;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... strings) {
        try {
            Connection con = connectionClass.CONN(ip, password, user, base);
            if (con == null) {
                res = "Error in connection with SQL server";
            } else {

                String queryDelete= " delete  [ArticleDefectueuseDansValise]  where NumeroBonCommande = 'bc_"+NomUtilisateur+"'  and  CodeCause <> '01' ";

                Log.e("queryDelete",""+queryDelete) ;

                Statement stmt2 = con.createStatement();
                stmt2.executeUpdate(queryDelete);

                res = "Success";
            }
        } catch (Exception ex) {
            res = "Error retrieving data from table";
            Log.e("ERROR" ,ex.getMessage().toString()) ;

        }
        return res;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


    }


}
