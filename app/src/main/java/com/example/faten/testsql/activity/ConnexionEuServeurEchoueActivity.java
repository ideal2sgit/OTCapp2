package com.example.faten.testsql.activity;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.faten.testsql.R;

public class ConnexionEuServeurEchoueActivity extends Activity {

   RelativeLayout btn_paramètrage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion_eu_serveur_echoue);

        btn_paramètrage  = (RelativeLayout)  findViewById(R.id.btn_parametrage) ;
        btn_paramètrage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toParam = new Intent(ConnexionEuServeurEchoueActivity.this  , ParametrageActivity.class) ;
                startActivity(toParam);
                finish();
            }
        });



    }
}
