package com.example.faten.testsql.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.R ;
import com.example.faten.testsql.adapter.ComptageAndroidAdapter;
import com.example.faten.testsql.model.ComptageAndroid;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MonInventaireActivity extends AppCompatActivity {


    ProgressBar  pb  ;
    ListView  lv_list_inventaire  ;
    String  NomUtilisateur ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_inventaire);



        pb  = findViewById(R.id.p_bar) ;
        lv_list_inventaire  = findViewById(R.id.lv_inventaire) ;

        SharedPreferences pref = getSharedPreferences("usersession", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        NomUtilisateur = pref.getString("NomUtilisateur", NomUtilisateur);


        MonComptageTask monComptageTask  = new MonComptageTask(this , NomUtilisateur ,lv_list_inventaire , pb) ;
        monComptageTask.execute() ;

    }



}


class MonComptageTask extends AsyncTask<String, String, String> {

    ConnectionClass connectionClass;
    String user, password, base, ip;


    Activity activity;
    String  NomUtilisateur ;
    ListView  lv_list_comptage  ;
    ProgressBar  pb  ;



    String res;
    Boolean isSuccess = false;

    DateFormat dfSQL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat df  = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    ArrayList<ComptageAndroid>  listComptage  = new ArrayList<>() ;


    public MonComptageTask(Activity activity, String  NomUtilisateur    , ListView  lv_list_comptage  ,  ProgressBar  pb    ) {
        this.activity = activity;
        this.NomUtilisateur = NomUtilisateur;
        this.lv_list_comptage= lv_list_comptage  ;
        this.pb=pb  ;



        SharedPreferences pref = activity.getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        user = pref.getString("user", user);
        ip = pref.getString("ip", ip);
        password = pref.getString("password", password);
        base = pref.getString("base", base);
        connectionClass = new ConnectionClass();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            Connection con = connectionClass.CONN(ip, password, user, base);

            Log.e("con", "" + con);
            if (con == null) {
                res = "Check Your Internet Access!";
            } else {

                String query  = "  select CodeArticle  ,Designation , CodeBarre  ,Quantite  ,DateCreation  ,NomUtilisateur , ValeurUniteVente from ComptageAndroid " +
                        "where NomUtilisateur = '"+NomUtilisateur+"' " ;
                Log.e("query_list_Comptage", query);

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {

                    String CodeArticle  = rs.getString("CodeArticle");
                    String Designation  = rs.getString("Designation");

                    String CodeBarre  = rs.getString("CodeBarre");
                    int Quantite  = rs.getInt("Quantite");

                    String _DateCreation  = rs.getString("DateCreation");
                    String NomUtilisateur  = rs.getString("NomUtilisateur");

                    String ValeurUniteVente  = rs.getString("ValeurUniteVente");



                 Date DateCreation   = dfSQL.parse(_DateCreation) ;



                    ComptageAndroid article  = new ComptageAndroid(CodeArticle ,Designation ,CodeBarre,Quantite,DateCreation,NomUtilisateur ,ValeurUniteVente );
                   listComptage.add(article) ;

                }

            }
            con.close();
        } catch (Exception ex) {

            isSuccess = false;
            res = ex.getMessage();
            Log.e("ERROR_BL", res.toString());

        }
        return res;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        pb.setVisibility(View.INVISIBLE);
        ComptageAndroidAdapter comptageAndroidAdapter  = new ComptageAndroidAdapter(activity  ,listComptage)  ;
        lv_list_comptage.setAdapter(comptageAndroidAdapter);


    }


}