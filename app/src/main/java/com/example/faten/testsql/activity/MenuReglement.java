package com.example.faten.testsql.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;


import com.example.faten.testsql.R;

public class MenuReglement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_reglement);
        CardView btn_reglement=(CardView)findViewById(R.id.btn_reglement);
        CardView btn_historique=(CardView)findViewById(R.id.btn_historique);
        btn_historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),HistoriqueReglementClient.class);
                startActivity(intent);
            }
        });


        btn_reglement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),PassationDeReglementActivity.class);
                startActivity(intent);
            }
        });




    }
}
