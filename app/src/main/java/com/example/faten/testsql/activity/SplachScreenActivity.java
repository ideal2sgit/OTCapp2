package com.example.faten.testsql.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.R ;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class SplachScreenActivity extends AppCompatActivity {


    boolean etatSQL = false;
    boolean etatUser = false;

    ConnectionClass connectionClass;
    String NomUtilisateur, NomPrenom, MotDePasse, CodeLivreur , CodeDepot, Depot , CodeFonction ;
    String user, password, base, ip;
    boolean actif = false ;
    boolean
    ForcageBonCommande = false  ;
    private static int splash_screen = 1000 ;
    Animation topAnim;
    ImageView logo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach_screen);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        logo = findViewById(R.id.logo);
        logo.setAnimation(topAnim);



        SharedPreferences prefUser = getSharedPreferences("usersession", Context.MODE_PRIVATE);

        //  get PARAM USER
        NomUtilisateur = prefUser.getString("NomUtilisateur", "0");
        NomPrenom = prefUser.getString("NomPrenom", NomPrenom);
        MotDePasse = prefUser.getString("MotDePasse", "0");
        CodeLivreur = prefUser.getString("CodeLivreur", CodeLivreur);
        CodeDepot = prefUser.getString("CodeDepot", CodeDepot);
        Depot = prefUser.getString("CodeDepot", Depot);

        etatUser = prefUser.getBoolean("etatuser", false);


        Log.e("Spach_etat_user", etatUser + "");
        SharedPreferences prefs = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        etatSQL = prefs.getBoolean("etatsql", false);


    }


    @Override
    protected void onPostResume() {
        super.onPostResume();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences p = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
                etatSQL = p.getBoolean("etatsql", false);


                if (etatSQL == true) {

                    SharedPreferences p_ = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);

                    ///  get param SQL
                    user = p_.getString("user", user);
                    ip = p_.getString("ip", ip);
                    password = p_.getString("password", password);
                    base = p_.getString("base", base);


                    if (etatUser == true) {


                        SharedPreferences prefUser = getSharedPreferences("usersession", Context.MODE_PRIVATE);

                        //  get PARAM USER
                        NomUtilisateur = prefUser.getString("NomUtilisateur", "0");
                        MotDePasse = prefUser.getString("MotDePasse", "0");

                        Log.e("onResume_Parm", "CheckLogin");

                        CheckLogin checkLogin = new CheckLogin(NomUtilisateur, MotDePasse);
                        checkLogin.execute();


                    } else {

                        Log.e("onResume_Parm", "LoginActivity");
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        finish();
                    }


                } else {

                    Intent i = new Intent(getApplicationContext(), ParametrageActivity.class);
                    startActivity(i);

                }



            }
        }, splash_screen);





    }

    public class CheckLogin extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;


        String _login;
        String _password;

        public CheckLogin(String _login, String _password) {
            this._login = _login;
            this._password = _password;


            SharedPreferences pref = getSharedPreferences("usersessionsql" , Context.MODE_PRIVATE);
            //SharedPreferences.Editor edt = pref.edit();
            user = pref.getString("user", user);
            ip = pref.getString("ip", ip);
            password = pref.getString("password", password);
            base = pref.getString("base", base);
            connectionClass = new ConnectionClass();

        }

        @Override
        protected void onPreExecute() {
            //progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... params) {

            if (_login.trim().equals("") || _password.trim().equals(""))
                z = "Please enter User Id and Password";

            else {
                try {
                    Connection con = connectionClass.CONN(ip, password, user, base);
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {


                        String query =  "  select  \n" +
                                "Utilisateur. NomUtilisateur ,  Livreur.NomPrenom as NomPrenom  , MotDePasse , Utilisateur.CodeLivreur  ,  Depot.CodeDepot  , Depot.Libelle  as Depot , Utilisateur.Actif , Utilisateur.ForcageBonCommande,Utilisateur.CodeFonction  \n" +
                                "from  Utilisateur\n" +
                                "inner Join Livreur on Utilisateur.CodeLivreur  = Livreur.CodeLivreur\n" +
                                "inner Join Depot on Depot.CodeDepot  = Utilisateur.CodeDepot\n" +
                                " where    \n" +
                                " NomUtilisateur  = '" + _login + "' and  MotDePasse  = '" + _password + "' ";


                        Log.e("query_login", query);

                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);


                        if (rs.next()) {

                            boolean TEST = false;

                            NomUtilisateur = rs.getString("NomUtilisateur");
                            NomPrenom = rs.getString("NomPrenom");
                            MotDePasse = rs.getString("MotDePasse");
                            CodeLivreur = rs.getString("CodeLivreur");
                            CodeDepot = rs.getString("CodeDepot");
                            Depot = rs.getString("Depot");

                            actif = rs.getBoolean("Actif");

                            ForcageBonCommande = rs.getBoolean("ForcageBonCommande");
                            CodeFonction  = rs.getString("CodeFonction") ;

                            if (actif) {
                                isSuccess = true;
                                z = "Login avec Succès";
                                Log.e("login_", z.toString());
                            } else if (!actif) {
                                isSuccess = false;
                                z = "Login échoué";
                                Log.e("login_ERROR", z.toString());
                            }

                        }


                    }
                } catch (SQLException ex) {

                    isSuccess = false;
                    z = "Login échoué";
                    Log.e("ERROR", ex.getMessage().toString());
                }
            }
            return z;
        }


        @Override
        protected void onPostExecute(String r) {

            //  Toast.makeText(SplashScreenActivity.this, r, Toast.LENGTH_SHORT).show();

            // progressBar.setVisibility(View.INVISIBLE);
            // txt_error.setVisibility(View.INVISIBLE);

            if (r.equals("Error in connection with SQL server")) {
                Intent ToConnexionFailed = new Intent(SplachScreenActivity.this, ConnexionEuServeurEchoueActivity.class);
                startActivity(ToConnexionFailed);
            } else {

                if (isSuccess) {

                    // Toast.makeText(getApplicationContext(), NomUtilisateur, Toast.LENGTH_LONG).show();

                    SharedPreferences prefs = getSharedPreferences("usersession", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edt = prefs.edit();

                    edt.putBoolean("etatuser", true);
                    edt.putString("NomUtilisateur", NomUtilisateur);
                    edt.putString("NomPrenom", NomPrenom);
                    edt.putString("MotDePasse", MotDePasse);
                    edt.putString("CodeLivreur", CodeLivreur);
                    edt.putString("CodeDepot", CodeDepot);
                    edt.putString("Depot", Depot);
                    edt.putBoolean("ForcageBonCommande", ForcageBonCommande);
                    edt.putString("CodeDepot", CodeDepot);
                    edt.putString("CodeFonction", CodeFonction);


                    edt.commit();


                    SharedPreferences p = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
                    etatSQL = p.getBoolean("etatsql", false);

                    if (etatSQL == true) {


                        if (etatUser == false) {

                            Log.e("onResume_Parm", "CheckLogin " + etatUser);
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                            finish();

                        } else if (etatUser == true) {


                            if  (actif)
                            {
                                Intent i = new Intent(getApplicationContext(), Accueil.class);
                                startActivity(i);
                                finish();

                            }
                            else  {
                                Intent ToCompteDesactive = new Intent(SplachScreenActivity.this, CompteDesactiveActivity.class);
                                ToCompteDesactive.putExtra("nom_user", NomPrenom);

                                startActivity(ToCompteDesactive);


                            }

                            /*CheckUserPointed checkUserPointed = new CheckUserPointed(NomUtilisateur);
                            checkUserPointed.execute();*/

                        }

                    }

                } else {

                    Intent ToCompteDesactive = new Intent(SplachScreenActivity.this, CompteDesactiveActivity.class);
                    ToCompteDesactive.putExtra("nom_user", NomPrenom);

                    startActivity(ToCompteDesactive);

                    // Toast.makeText(getApplicationContext()," Verifiez Vos Données ",Toast.LENGTH_SHORT).show();
                    // txt_error.setVisibility(View.VISIBLE);


                }
            }
        }
    }

}
