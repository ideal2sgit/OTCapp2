package com.example.faten.testsql.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.activity.BonCommandeTerminalAutomatiqueActivity;
import com.example.faten.testsql.model.Client;
import com.example.faten.testsql.model.OrderDepotDemande;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ListDepotDestinationParOrdre extends AsyncTask<String, String, String> {


    Activity activity;
    String CodeDepot;
    String res;
    ConnectionClass connectionClass;
    String user, password, base, ip;

    public ListDepotDestinationParOrdre(Activity activity, String CodeDepot) {
        this.activity = activity;
        this.CodeDepot = CodeDepot;


        SharedPreferences prefe = activity.getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        user = prefe.getString("user", user);
        ip = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base = prefe.getString("base", base);

        connectionClass = new ConnectionClass();
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

                String queryClient = "  select * from  [OrderDepotDemande]\n" +
                        "  where CodeDepotDemandeur = '" + CodeDepot + "'   \n" +
                        "  ORDER BY    codeDepotDemandeur  , ordre ";
                PreparedStatement ps = con.prepareStatement(queryClient);
                ResultSet rs = ps.executeQuery();


                while (rs.next()) {

                    String CodeDepotDemandeur = rs.getString("CodeDepotDemandeur");
                    String CodeDepotDemandant = rs.getString("CodeDepotDemandant");
                    int ordre = rs.getInt("ordre");

                    OrderDepotDemande orderDepotDemande = new OrderDepotDemande(CodeDepotDemandeur ,CodeDepotDemandant ,ordre) ;
                    BonCommandeTerminalAutomatiqueActivity.listOrderDepot .add(orderDepotDemande) ;

                }


                res = "Success";
            }
        } catch (Exception ex) {
            res = "Error retrieving data from table";

        }
        return res;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


    }


}
