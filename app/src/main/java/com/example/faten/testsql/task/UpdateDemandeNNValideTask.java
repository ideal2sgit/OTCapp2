package com.example.faten.testsql.task;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.model.BonCommandeVente;
import com.example.faten.testsql.model.LigneBonCommandeVente;
import com.example.faten.testsql.model.ReservationArticleDansDepot;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class UpdateDemandeNNValideTask extends AsyncTask<String, String, String> {


    Activity activity;
    DialogFragment dialogFragment;


    ArrayList<ReservationArticleDansDepot> listReservation;

    ProgressBar pb;
    Button btn_valider;

    String z = "";
    ConnectionClass connectionClass;
    String user, password, base, ip;


    DateFormat dfSQL = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    Boolean isSuccess = false;

    public UpdateDemandeNNValideTask(Activity activity, DialogFragment dialogFragment, ArrayList<ReservationArticleDansDepot> listReservation, ProgressBar pb , Button btn_valider) {
        this.activity = activity;
        this.dialogFragment = dialogFragment;
        this.listReservation = listReservation;
        this.pb = pb;
        this.btn_valider =btn_valider  ;

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
        pb.setVisibility(View.VISIBLE);
        btn_valider.setEnabled(false);
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            Connection con = connectionClass.CONN(ip, password, user, base);
            if (con == null) {
                z = "Error in connection with SQL server";
            } else {

                String updateReservation = "";

                for (ReservationArticleDansDepot reserv : listReservation) {

                    String query_reserv = "   update ReservationArticleDansDepot  set  Valider  =    " + reserv.getValider() + " ,  Annuler  = " + reserv.getAnnuler() + "  \n" +
                            " where CodeDepotDemandant   = '" + reserv.getCodeDepotDemandant() + "'  and NumeroBonCommandeVente  = '" + reserv.getNumeroBonCommandeVente() + "'   and CodeArticle = '" + reserv.getCodeArticle() + "' ";


                    updateReservation = updateReservation + query_reserv;

                }

                String queryUpate = " " +
                        " BEGIN TRANSACTION update_reserv  \n" +
                        " \n" +
                        "DECLARE @error INT  \n" +
                        "DECLARE @ID_INSERTION NUMERIC(19,0)  \n" +
                        " \n" +
                        " SET @error = 0\n" +
                        "\n"


                        + updateReservation
                        + "   SET @error = @error + @@error  \n  "


                        + "  IF @error = 0  \n" +
                        "    COMMIT TRANSACTION update_reserv  \n" +
                        "\t  ELSE \n" +
                        "    ROLLBACK TRANSACTION update_reserv   ";


                Log.e("query_UPDATE_reserv", queryUpate);
                Statement stmt2 = con.createStatement();
                stmt2.executeUpdate(queryUpate);

                isSuccess = true;


                z = "Success";
            }
        } catch (Exception ex) {
            z = "Error " + ex.getMessage().toString();
            isSuccess = false;
        }
        return z;
    }


    @Override
    protected void onPostExecute(String r) {
        Log.e("METHOD", "onPostExecute(String r)");

        pb.setVisibility(View.GONE);

        if (isSuccess) {
            Toast.makeText(activity, "Vlidation avec succès", Toast.LENGTH_LONG).show();
            dialogFragment.dismiss();
        } else {
            btn_valider.setEnabled(true);
            Toast.makeText(activity, "Problème de sauvegard " + r, Toast.LENGTH_LONG).show();

        }


    }


}