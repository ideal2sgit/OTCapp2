package com.example.faten.testsql.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.faten.testsql.R;
import com.example.faten.testsql.task.HistoriqueLBCTask;

import java.text.DecimalFormat;


public class HistoriqueLigneBonCommandeActivity extends AppCompatActivity {


    TextView txt_num_bc, txt_date_bc, txt_client, txt_ttc;
    ListView lv_ligne_bc;
    ProgressBar pb;

    DecimalFormat decF = new DecimalFormat("0.000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_ligne_bon_commande);


        String NumeroBC = getIntent().getStringExtra("cle_numero_bon_cmd_vente");
        setTitle(NumeroBC);

        String RaisonSociale = getIntent().getStringExtra("cle_raison_sociale");
        double ttc = getIntent().getDoubleExtra("cle_total_ttc", 0);
        String date_cmd = getIntent().getStringExtra("cle_date_bc");

        txt_num_bc = (TextView) findViewById(R.id.txt_num_bc);
        txt_date_bc = (TextView) findViewById(R.id.txt_date_bc);
        txt_client = (TextView) findViewById(R.id.txt_raison_client);
        txt_ttc = (TextView) findViewById(R.id.txt_prix_ttc);

        lv_ligne_bc = (ListView) findViewById(R.id.lv_ligne_bon_commande);
        pb = (ProgressBar) findViewById(R.id.pb);
        pb.setVisibility(View.INVISIBLE);

        txt_client.setText(RaisonSociale);
        txt_ttc.setText(decF.format(ttc));
        txt_date_bc.setText(date_cmd);
        txt_num_bc.setText(NumeroBC);

        HistoriqueLBCTask historiqueLBCTask  = new HistoriqueLBCTask (this  ,lv_ligne_bc ,NumeroBC , pb) ;
        historiqueLBCTask.execute();

    }
}
