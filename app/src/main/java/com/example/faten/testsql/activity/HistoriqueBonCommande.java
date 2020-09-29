package com.example.faten.testsql.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.R;
import com.example.faten.testsql.adapter.BonCommandeAdapter;
import com.example.faten.testsql.model.BonCommandeVente;
import com.example.faten.testsql.task.HistoriqueBCTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoriqueBonCommande extends AppCompatActivity {


    ListView lv_list_historique_bc;
    ProgressBar  pb_bc  ;

    ConnectionClass connectionClass;
    String NomUtilisateur,codeclient,rsclient="",mail="",Nbcmd="",date,nbcmd,totale;
    final Context co = this;
    String user, password, base, ip;

   public  static BonCommandeAdapter   bcAdapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        lv_list_historique_bc=(ListView)findViewById(R.id.lv_list_historique_bc);
        pb_bc  = (ProgressBar)  findViewById(R.id.pb_bc) ;

        connectionClass = new ConnectionClass();

        SharedPreferences prefe = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        user = prefe.getString("user", user);
        ip = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base = prefe.getString("base", base);
        Intent intent=getIntent();
        NomUtilisateur=intent.getStringExtra("NomUtilisateur");



        SharedPreferences pref=getSharedPreferences("usersession", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt=pref.edit();
        NomUtilisateur= pref.getString("NomUtilisateur",NomUtilisateur);
        //Button b=(Button)findViewById(R.id.btreload);


        /*listBC = new ArrayList<>() ;
        bonCommandeAdapter  = new BonCommandeAdapter(this , listBC) ;
        lv_list_historique_bc.setAdapter(bonCommandeAdapter);*/

        HistoriqueBCTask  historiqueBCTask  = new HistoriqueBCTask(this ,lv_list_historique_bc  , pb_bc) ;
        historiqueBCTask.execute() ;



    }


}
