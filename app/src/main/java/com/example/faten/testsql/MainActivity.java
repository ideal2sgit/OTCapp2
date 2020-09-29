package com.example.faten.testsql;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.faten.testsql.activity.Accueil;
import com.example.faten.testsql.activity.ParametrageActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edtuserid,edtpass;
    Button btnlogin,server,back,btuser,btnpass;
    final Context co = this;
    ConnectionClass connectionClass;
    String codelivreur , CodeFonction;
    String user, password, base, ip;
    ResultSet rs;
    Boolean test;
    String CodeSociete,NomUtilisateur;
    boolean st = false;
    String prefname = "usersession";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectionClass = new ConnectionClass();

        btnlogin = (Button) findViewById(R.id.btnlogin);
      //  pbbar = (ProgressBar) findViewById(R.id.pbbar);


        SharedPreferences pref = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        user     = pref.getString("user", user);
        ip       = pref.getString("ip", ip);
        password = pref.getString("password", password);
        base     = pref.getString("base", base);



        //////////////////////////////////////////////////

        String query = "select * from CompteurPiece where CodeSociete='"+CodeSociete+"'";

        try {
            Connection connect = connectionClass.CONN(ip, password, user, base);
            PreparedStatement stmt;
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
            while (rs.next()) {
                String id = rs.getString("RaisonSociale");
                data.add(id);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //////////////////////////////////////////////////











        final Context co = this;
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(co);
                View px = li.inflate(R.layout.diagutilisateur, null);
                AlertDialog.Builder alt = new AlertDialog.Builder(co);
                alt.setIcon(R.drawable.i2s);
                alt.setTitle("LOGIN");
                alt.setView(px);

                connectionClass = new ConnectionClass();

                edtuserid = (EditText)px. findViewById(R.id.edtuserid);
                edtpass = (EditText) px.findViewById(R.id.edtpass);

                alt.setCancelable(false)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface di, int i) {
                                        DoLogin doLogin = new DoLogin();
                                        doLogin.execute("");
                                    }
                                })
                        .setNegativeButton("Annuler",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface di, int i) {
                                        di.cancel();
                                    }
                                });
                AlertDialog d = alt.create();
                d.show();

            }
        });




        //********************************************************************////////////
        //********************************************************************////////////



    }




    public class DoLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;


        String userid = edtuserid.getText().toString();
        String pass = edtpass.getText().toString();


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(MainActivity.this,r,Toast.LENGTH_SHORT).show();

            if(isSuccess) {
                SharedPreferences prefs = MainActivity.this.getSharedPreferences(prefname, Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = prefs.edit();
                edt.putBoolean("etat", true);
                edt.putString("CodeSociete",CodeSociete);
                edt.putString("NomUtilisateur",NomUtilisateur);
                edt.putString("CodeLivreur",codelivreur);
                edt.putString("CodeFonction",CodeFonction);
                edt.commit();

                Intent i = new Intent(MainActivity.this, Accueil.class);
                i.putExtra("CodeSociete",CodeSociete);
                i.putExtra("NomUtilisateur",NomUtilisateur);
                i.putExtra("CodeLivreur",codelivreur);
                i.putExtra("CodeFonction",CodeFonction);
                startActivity(i);
                finish();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            if(userid.trim().equals("")|| password.trim().equals(""))
                z = "Please enter User Id and Password";
            else
            {
                try {
                    Connection con = connectionClass.CONN(ip, password, user, base);
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query = "select * from Utilisateur where NomUtilisateur='" + userid + "' and MotDePasse='" + pass + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        if(rs.next())
                        {
                            CodeSociete=rs.getString("CodeSociete");
                            NomUtilisateur=rs.getString("NomUtilisateur");
                            codelivreur=rs.getString("CodeLivreur");
                            CodeFonction=rs.getString("CodeFonction");
                            z = "Login successfull";
                            isSuccess=true;
                        }
                        else
                        {
                            z = "Invalid Credentials";
                            isSuccess = false;
                        }

                    }
                }
                catch (SQLException ex)
                {
                    isSuccess = false;
                    z = ex.toString();
                }
            }
            return z;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences p = getSharedPreferences(prefname, Context.MODE_PRIVATE);
        st = p.getBoolean("etat", false);
        if (st == true) {
            Intent i = new Intent(MainActivity.this, Accueil.class);
            startActivity(i);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.accueil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            LayoutInflater li = LayoutInflater.from(co);
            View px = li.inflate(R.layout.print, null);
            final AlertDialog.Builder alt = new AlertDialog.Builder(co);
            alt.setIcon(R.drawable.i2s);
            alt.setTitle("LOGIN");
            alt.setView(px);

            connectionClass = new ConnectionClass();

            final EditText edtuserid = (EditText) px.findViewById(R.id.edtuserid);
            final EditText edtpass = (EditText) px.findViewById(R.id.edtpass);

            alt.setCancelable(false)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface di, int i) {

                                    if (edtuserid.getText().toString().equals("admin") && edtpass.getText().toString().equals("admin")) {
                                        SharedPreferences pref = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor edt = pref.edit();
                                        edt.putBoolean("etatsql", false);
                                        edt.commit();
                                        Intent inte = new Intent(getApplicationContext(), ParametrageActivity.class);
                                        startActivity(inte);
                                    } else {


                                        Toast.makeText(getApplicationContext(), "Erreur login", Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                    .setNegativeButton("Annuler",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface di, int i) {
                                    di.cancel();
                                }
                            });
            final AlertDialog d = alt.create();


            d.setOnShowListener( new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                 //   d.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.bt);
                  //  d.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.bt);



                }
            });

            d.show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }





}