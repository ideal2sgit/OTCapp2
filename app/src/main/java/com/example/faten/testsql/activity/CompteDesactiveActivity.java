package com.example.faten.testsql.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.faten.testsql.R;

public class CompteDesactiveActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte_desactive);


        RelativeLayout btn_autre_session= findViewById(R.id.btn_autre_session) ;
        TextView txt_nom_user  = findViewById(R.id.txt_nom_user) ;
        TextView  txt_fonction  = findViewById(R.id.txt_fonction) ;


        String nomPrenom = getIntent().getStringExtra("nom_user") ;
        String LibelleFonction = getIntent().getStringExtra("fonction") ;


        txt_nom_user.setText(nomPrenom);
        txt_fonction.setText(LibelleFonction);


        btn_autre_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences pref = getSharedPreferences("usersession", Context.MODE_PRIVATE);
                SharedPreferences.Editor edt  = pref.edit();
                edt.putBoolean("etatuser",false);
                edt.commit();

                Intent  toLogin = new Intent(CompteDesactiveActivity.this  , LoginActivity.class) ;
                startActivity(toLogin);
                finish();
            }
        });

    }
}
