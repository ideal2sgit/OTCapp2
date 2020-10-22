package com.example.faten.testsql.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.adapter.DemandeArticleNonValideAdapterRV;
import com.example.faten.testsql.adapter.SpinnerAdapter;
import com.example.faten.testsql.dialog.DialogCauseDeNonConfirmation;
import com.example.faten.testsql.dilaog.DialogDemandeNonValide;
import com.example.faten.testsql.model.CauseAnnulationReservation;
import com.example.faten.testsql.model.ReservationArticleDansDepot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListCauseForSpinnerTask extends AsyncTask<String, String, String> {


    Activity activity;

    Spinner sp_cause;


    String z = "";

    ConnectionClass connectionClass;
    String user, password, base, ip;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    ArrayList<CauseAnnulationReservation> listCause = new ArrayList<>();

    ArrayList<String> listLibelleCause =  new ArrayList<>() ;

    public ListCauseForSpinnerTask(Activity activity, Spinner sp_cause) {
        this.activity = activity;
        this.sp_cause = sp_cause;

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
    protected String doInBackground(String... params) {

        try {
            Connection con = connectionClass.CONN(ip, password, user, base);
            if (con == null) {
                z = " Error in connection with SQL server ";
            } else {

                String queryReservation = "  select  * from  CauseAnnulationReservation   ";


                Log.e("queryReservation", queryReservation);

                PreparedStatement ps = con.prepareStatement(queryReservation);
                ResultSet rs = ps.executeQuery();


                while (rs.next()) {

                    String CodeCause = rs.getString("CodeCause");
                    String Libelle = rs.getString("Libelle");


                    CauseAnnulationReservation cause = new CauseAnnulationReservation(CodeCause, Libelle);
                    listLibelleCause.add(Libelle) ;

                    listCause.add(cause);
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



        SpinnerAdapter adapter = new SpinnerAdapter( activity  , listLibelleCause )  ;
        sp_cause.setAdapter(adapter);


        sp_cause.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                CauseAnnulationReservation causeSelected = listCause.get(position );
                DialogCauseDeNonConfirmation.CodeCauseSelected = causeSelected.getCodeCause();
                DialogCauseDeNonConfirmation.LibelleCauseSelected = causeSelected.getLibelle();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }



}