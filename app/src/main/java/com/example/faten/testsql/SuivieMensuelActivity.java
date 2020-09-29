package com.example.faten.testsql;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faten.testsql.adapter.MarqueVenduAdapter;
import com.example.faten.testsql.model.SuivieMensuel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SuivieMensuelActivity extends AppCompatActivity {
    ConnectionClass connectionClass;
    final Context co = this;
    String user, password, base, ip;

    ListView lv_list_marque_vendu_par_mois;
    double total = 0.0, restant = 0.0, montantttc = 0.0, montantremise = 0.0, totalpaye = 0.0;
    Button btreload, btdatedebut, btdatefin;
    TextView texttotal;

    String datedebut = "";
    String NomUtilisateur, codelivreur, CodeFonction;
    TabLayout tab_mois_annee;
    TextView txt_total_ttc  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suivie_mensuel);


        connectionClass = new ConnectionClass();
        SharedPreferences prefe = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);

        user = prefe.getString("user", user);
        ip = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base = prefe.getString("base", base);

        SharedPreferences pref = getSharedPreferences("usersession", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        NomUtilisateur = pref.getString("NomUtilisateur", NomUtilisateur);
        codelivreur = pref.getString("CodeLivreur", codelivreur);
        CodeFonction = pref.getString("CodeFonction", CodeFonction);
        lv_list_marque_vendu_par_mois = (ListView) findViewById(R.id.lv_list_marque_vendu_par_mois);
        tab_mois_annee = (TabLayout) findViewById(R.id.tab_mois_annee);
        txt_total_ttc = (TextView)  findViewById(R.id.txt_total_ttc)  ;

        AnneeMoisCourantTask anneeMoisCourantTask = new AnneeMoisCourantTask();
        anneeMoisCourantTask.execute();


    }

    /////////////********************************************************/////////////


    public class SuivieMensuelTask extends AsyncTask<String, String, String> {
        String z = "";
        Boolean test = false;

        String mois, year;

        ArrayList<SuivieMensuel> listMarqueVendu = new ArrayList<>();
         double  TotalTTC = 0   ;

        public SuivieMensuelTask(String mois, String year) {
            this.mois = mois;
            this.year = year;
        }



        @Override
        protected void onPreExecute() {
            TotalTTC = 0;


        }


        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String query = "";
                    if (CodeFonction.equals("01")) {
                        query = " select  LibelleMarque,CodeFamille,ISNULL(SUM(Quantite),0) As QT_VENDU, \n" +
                                "SUM(NetHT) AS  Total_HT  from Vue_ListeVenteArticle \n" +
                                "where Month(DatePiece) =" + mois + " and YEAR(DatePiece) =  " + year + "   \n" +
                                "group  by CodeFamille,LibelleMarque\n" +
                                "order by LibelleMarque";
                    } else {

                        query = " select  LibelleMarque,CodeFamille,ISNULL(SUM(Quantite),0) As QT_VENDU, \n" +
                                "SUM(NetHT) AS  Total_HT from Vue_ListeVenteArticle \n" +
                                "where Month(DatePiece) =" + mois + " and YEAR(DatePiece) =  " + year + "   and CodeLivreur  = '" + codelivreur + "'\n" +
                                "group  by CodeFamille,LibelleMarque\n" +
                                "order by LibelleMarque";

                    }

                    Log.e("CodeFonction", CodeFonction);


                    Log.e("querySuivieMensuel", query);
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    z = "e";
                    test = true;


                    while (rs.next()) {


                        String marque = rs.getString("LibelleMarque");
                        String CodeFamille = rs.getString("CodeFamille");
                        int QT_VENDU = rs.getInt("QT_VENDU");
                        double Total_HT = rs.getDouble("Total_HT");

                        TotalTTC = TotalTTC  + Total_HT  ;
                        SuivieMensuel     marqueVendu = new SuivieMensuel(marque  , CodeFamille  ,QT_VENDU ,Total_HT)  ;

                        listMarqueVendu.add(marqueVendu)   ;



                        z = "Success";
                    }


                }
            } catch (SQLException ex) {
                z = "etape" + ex.toString();
                Log.e("EXECEPTION", z);
                test = false;
            }
            return z;
        }


        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();

            NumberFormat formatter = new DecimalFormat("0.000");

            if (test) {

                txt_total_ttc .setText(formatter.format(TotalTTC)+" DT");

                MarqueVenduAdapter marqueVenduAdapter  = new MarqueVenduAdapter(SuivieMensuelActivity.this  , listMarqueVendu)  ;
                lv_list_marque_vendu_par_mois.setAdapter(marqueVenduAdapter);



            }
            if (r.equals("e")) {


            }


        }


    }


    public class AnneeMoisCourantTask extends AsyncTask<String, String, String> {
        String z = "";
        Boolean test = false;


        ArrayList<String> listAnnee = new ArrayList<>();
        ArrayList<String> listMois = new ArrayList<>();
        ArrayList<String> listCodeMois = new ArrayList<>();


        @Override
        protected void onPreExecute() {

            listMois.add("Janvier");
            listCodeMois.add("01");
            listMois.add("Février");
            listCodeMois.add("02");
            listMois.add("Mars");
            listCodeMois.add("03");
            listMois.add("Avril");
            listCodeMois.add("04");
            listMois.add("Mai");
            listCodeMois.add("05");
            listMois.add("Juin");
            listCodeMois.add("06");
            listMois.add("Juillet");
            listCodeMois.add("07");
            listMois.add("Août");
            listCodeMois.add("08");
            listMois.add("Septembre");
            listCodeMois.add("09");
            listMois.add("Octobre");
            listCodeMois.add("10");
            listMois.add("Novembre");
            listCodeMois.add("11");
            listMois.add("Décembre");
            listCodeMois.add("12");
        }


        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String query = " select distinct (YEAR (DateBonLivraisonVente)) as annees_livraison_vente from BonLivraisonVente order by (YEAR (DateBonLivraisonVente)) \n";

                    Log.e("les annees ", query);
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    z = "e";

                    // ArrayList data1 = new ArrayList();
                    while (rs.next()) {

                        String annee = rs.getString("annees_livraison_vente");
                        listAnnee.add(annee);

                    }


                }
            } catch (SQLException ex) {
                z = "etape" + ex.toString();

            }
            return z;
        }


        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String s) {

            DecimalFormat df = new DecimalFormat("00");
            Date currentDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            String currentYear = cal.get(Calendar.YEAR) + "";
            int int_current_mois = cal.get(Calendar.MONTH) + 1;
            String currentMois = df.format(int_current_mois);

            Log.e("Year-Mois", currentYear + " * " + currentMois);


            tab_mois_annee.addTab(tab_mois_annee.newTab().setText(listMois.get(int_current_mois - 1) + " " + currentYear));

            SuivieMensuelTask suivieMensuelTask = new SuivieMensuelTask(currentMois, currentYear);
            suivieMensuelTask.execute("");


        }


    }


}
