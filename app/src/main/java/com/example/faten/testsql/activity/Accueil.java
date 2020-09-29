package com.example.faten.testsql.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faten.testsql.Choixclient;
import com.example.faten.testsql.Clients;
import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.R;
import com.example.faten.testsql.SuivieMensuelActivity;

import com.example.faten.testsql.dilaog.DialogDemandeNonValide;
import com.example.faten.testsql.fragment.PrincipalFragment;
import com.example.faten.testsql.fragment.RechercheClientCommande;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Accueil extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener {

    final Context co = this;
    String CodeSociete, NomUtilisateur, prefixecmd ;

    String CodeLivreur  ,NomPrenom  ,MotDePasse ,CodeDepot ,Depot;

    String user, password, base, ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        // searchViewArticle = (SearchView) findViewById(R.id.search_bar_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        displaySelectedScreen (R.id.nav_home) ;
        SharedPreferences pref = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        user = pref.getString("user", user);
        ip = pref.getString("ip", ip);
        password = pref.getString("password", password);
        base = pref.getString("base", base);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        SharedPreferences prefe = getSharedPreferences("usersession", Context.MODE_PRIVATE);

        NomUtilisateur = prefe.getString("NomUtilisateur", NomUtilisateur);
        CodeSociete    = prefe.getString("CodeSociete", CodeSociete);
        CodeLivreur    = prefe.getString("CodeLivreur", CodeLivreur);
        NomPrenom      = prefe.getString("NomPrenom", NomPrenom);
        MotDePasse     = prefe.getString("MotDePasse", MotDePasse);
        CodeDepot      = prefe.getString("CodeDepot", CodeDepot);
        Depot          = prefe.getString("Depot", Depot);


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.user);
        TextView txt_depot = (TextView) headerView.findViewById(R.id.txt_depot);
        navUsername.setText(NomPrenom);
        txt_depot.setText(Depot);


        HaveDemandeNonvalide haveDemandeNonvalide  = new HaveDemandeNonvalide(this ,CodeDepot)  ;
        haveDemandeNonvalide.execute();

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


            d.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    //   d.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.bt);
                    //  d.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.bt);


                }
            });

            d.show();


            return true;
        } else if (id == R.id.action_deconnecter) {

            SharedPreferences pref = getSharedPreferences("usersession", Context.MODE_PRIVATE);
            SharedPreferences.Editor edt = pref.edit();
            edt.putBoolean("etatuser", false);
            edt.commit();
            Intent inte = new Intent(getApplicationContext(), SplachScreenActivity.class);
            startActivity(inte);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        displaySelectedScreen(  id) ;
        return true;
    }



    private void displaySelectedScreen(int itemId) {

        // Handle navigation view item clicks here.
        int id = itemId;


        if (id==R.id.nav_home)

        {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment    fragment = new PrincipalFragment();

            //replacing the fragment
            if (fragment != null) {

                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        }
        else  if (id == R.id.nav_1) {

            Intent in = new Intent(getApplicationContext(), Clients.class);
            startActivity(in);


        } else if (id == R.id.nav_2) {


        }


      /*  else if (id == R.id.nav_3) {
            Intent in = new Intent(getApplicationContext(), Choixclient.class);
            in.putExtra("NomUtilisateur", NomUtilisateur);
            startActivity(in);

        } */


        else if (id == R.id.nav_4) {

            Intent in = new Intent(getApplicationContext(), HistoriqueBonCommande.class);
            in.putExtra("NomUtilisateur", NomUtilisateur);
            in.putExtra("CodeLivreur", CodeLivreur);
            startActivity(in);

        } else if (id == R.id.etatrep) {

            Intent in = new Intent(getApplicationContext(), SuivieMensuelActivity.class);
            startActivity(in);

        } else if (id == R.id.nav_suivie_recouvrement) {

            Intent in = new Intent(getApplicationContext(), SuivieRecouvrementActivity.class);
            startActivity(in);

        }
/*
        else if (id == R.id.nav_bon_livraison_vente) {

            Intent in = new Intent(getApplicationContext(), BonLivraisonActivity.class);
            startActivity(in);

        }*/

        else if (id == R.id.nav_inventaire) {

            Intent in = new Intent(getApplicationContext(), InventaireActivity.class);
            startActivity(in);

        }

        else if (id == R.id.nav_commande_vente) {


            Fragment fragment = null;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fragment = new RechercheClientCommande();

            //replacing the fragment
            if (fragment != null) {

                ft.replace(R.id.content_frame, fragment);
                ft.commit();

            }

            /*listArticleTask.cancel(true) ;
            Intent in = new Intent(getApplicationContext(), InventaireActivity.class);
            startActivity(in);*/

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = new PrincipalFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (fragment != null) {
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        }
    }





    public   class  HaveDemandeNonvalide extends AsyncTask<String, String, String> {


        Activity  activity  ;
        String NomUtilisateur  ;
        String CodeDepot    ;
        ConnectionClass connectionClass;
        String user, password, base, ip;

        String  res  = "" ;
        int  nbr_demande_non_valide ;

        SimpleDateFormat  sdf  = new SimpleDateFormat("dd/MM/yyyy") ;


        public HaveDemandeNonvalide(Activity activity,  String CodeDepot     ) {
            this.activity = activity;
            this.CodeDepot = CodeDepot;


            SharedPreferences prefe = activity.getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
            SharedPreferences.Editor edte = prefe.edit();
            user     = prefe.getString("user", user);
            ip       = prefe.getString("ip", ip);
            password = prefe.getString("password", password);
            base     = prefe.getString("base", base);

            connectionClass = new ConnectionClass();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    res = "Error in connection with SQL server";
                } else {

                    String  queryClient = " select COUNT (*)  as nbr_demande_non_valide   \n" +
                            "from ReservationArticleDansDepot  \n" +
                            "inner join  BonCommandeVente  on  BonCommandeVente.NumeroBonCommandeVente  =  ReservationArticleDansDepot.NumeroBonCommandeVente\n" +
                            "where CONVERT (    date ,BonCommandeVente.DateBonCommandeVente )  < '"+sdf.format(new Date())+"'\n" +
                            "and CodeDepotDemandant  = '"+CodeDepot+"'  --and  CodeDepotDemandant  <>  CodeDepotDemendeur\n" +
                            "and Valider=0  and Annuler  =0 \n ";


                    PreparedStatement ps = con.prepareStatement(queryClient);
                    ResultSet rs = ps.executeQuery();


                    while (rs.next()) {

                          nbr_demande_non_valide = rs.getInt("nbr_demande_non_valide") ;


                    }


                    res = "Success";
                }
            } catch (Exception ex) {
                res = "Error retrieving data from table";

            }
            return res;



        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if(nbr_demande_non_valide == 0 )
            {


            }
            else {

                final FragmentManager fm = getFragmentManager();
                //  Open dialog  rapport suivie detail
                DialogDemandeNonValide dialogDemandeNonValide = new DialogDemandeNonValide();
                dialogDemandeNonValide.setActivity(Accueil.this);
                dialogDemandeNonValide.show(fm, "");

            }

        }


    }



}
