package com.example.faten.testsql;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EtatRecouvrementActivity extends AppCompatActivity {

    ConnectionClass connectionClass;
    final Context co = this;
    String user, password, base, ip;
    ListView list;
    double total= 0.0 ,restant=0.0,montantttc=0.0,montantremise=0.0,totalpaye=0.0;
    Button btreload,btdatedebut,btdatefin;
    TextView texttotal;
    String  datedebut="";
    String NomUtilisateur,codelivreur;



    TabLayout tab_mois , tab_annee ;
    int indexCurrentYear = 0  ;
    int indexCurrentMois = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etat_recouvrement);

        connectionClass = new ConnectionClass();

        connectionClass = new ConnectionClass();
        SharedPreferences prefe = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        user = prefe.getString("user", user);
        ip = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base = prefe.getString("base", base);

        SharedPreferences pref=getSharedPreferences("usersession", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt=pref.edit();
        NomUtilisateur= pref.getString("NomUtilisateur",NomUtilisateur);
        codelivreur= pref.getString("CodeLivreur",codelivreur);
        list      =(ListView)findViewById(R.id.list);
        texttotal =(TextView)findViewById(R.id.texttotal);

        tab_annee = (TabLayout)  findViewById(R.id.tab_annee) ;
        tab_mois  = (TabLayout)  findViewById(R.id.tab_mois) ;


        AnneeMoisCourantTask anneeMoisCourantTask =  new AnneeMoisCourantTask();
        anneeMoisCourantTask.execute();

        EtatRecouvrementTask etat=new EtatRecouvrementTask();
        etat.execute("");
    }





    /////////////********************************************************/////////////


    public class EtatRecouvrementTask extends AsyncTask<String, String, String> {
        String z = "";
        Boolean test = false;

        List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {
            total=0;restant=0.0;montantttc=0.0;montantremise=0.0;totalpaye=0.0;

        }



        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip,password,user,base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String query = "\n" +
                            "DECLARE\t@return_value int\n" +
                            "\n" +
                            "EXEC\t@return_value = [dbo].[CrediTousClientParLivreur]\n" +
                            "\t\t@CodeLivreur = N'"+codelivreur+"'\n" +
                            "\n" +
                            "SELECT\t'Return Value' = @return_value" ;

                    Log.e("queryRecouvrement",query) ;
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    z="e";

                    // ArrayList data1 = new ArrayList();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", rs.getString("NomClient"));
                        datanum.put("B", rs.getString("MontantTTC"));
                        datanum.put("C", rs.getString("Restant"));
                        datanum.put("D", rs.getString("MontantRemise"));
                        datanum.put("E", rs.getString("TotalPayee"));
                        datanum.put("F", rs.getString("DatePiece"));
                        datanum.put("G", rs.getString("NomPrenom"));
                        String p=rs.getString("TotalPayee");
                        String ttc=rs.getString("MontantTTC");
                        String remise=rs.getString("MontantRemise");
                        String re=rs.getString("Restant");
                        totalpaye=totalpaye+Float.parseFloat(p.substring(0,p.length()-3));
                        montantttc=montantttc+Float.parseFloat(ttc.substring(0,ttc.length()-3));
                        montantremise=montantremise+Float.parseFloat(remise.substring(0,remise.length()-3));

                        restant=restant+Float.parseFloat(re.substring(0,re.length()-3));

                        prolist.add(datanum);
                        test=true;
                        z = "Success";
                    }



                }
            } catch (SQLException ex) {
                z = "etape"+ex.toString();

            }
            return z;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String r) {



            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();

            if (test)

            {
                String[] from = {"A", "B","C","E","G"};
                int[] views = {R.id.client,  R.id.MontantTTc,R.id.restant,R.id.totalpaye,R.id.livreur};
                final SimpleAdapter ADA = new SimpleAdapter(getApplicationContext(),
                        prolist, R.layout.listrecouvrement, from,
                        views);
                list.setAdapter(ADA);
                texttotal.setText( " TTC :"+String.valueOf(montantttc)+  "   **   Restant  :"+String.valueOf(restant)+"\n");




            }
            if(r.equals("e"))
            {




            }




        }


    }



    /////////////********************************************************/////////////


    public class AnneeMoisCourantTask extends AsyncTask<String, String, String> {
        String z = "";
        Boolean test = false;


        ArrayList<String>  listAnnee = new ArrayList<>() ;
        ArrayList<String>  listMois = new ArrayList<>() ;
        ArrayList<String>  listCodeMois = new ArrayList<>() ;


        @Override
        protected void onPreExecute() {

            listMois.add("Janvier");listCodeMois.add("01");
            listMois.add("Février");listCodeMois.add("02");
            listMois.add("Mars");listCodeMois.add("03");
            listMois.add("Avril");listCodeMois.add("04");
            listMois.add("Mai");listCodeMois.add("05");
            listMois.add("Juin");listCodeMois.add("06");
            listMois.add("Juillet");listCodeMois.add("07");
            listMois.add("Août");listCodeMois.add("08");
            listMois.add("Septembre");listCodeMois.add("09");
            listMois.add("Octobre");listCodeMois.add("10");
            listMois.add("Novembre");listCodeMois.add("11");
            listMois.add("Décembre");listCodeMois.add("12");
        }



        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip,password,user,base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String  query = " select distinct (YEAR (DateBonLivraisonVente)) as annees_recouvrement \n" +
                            " from BonLivraisonVente order by (YEAR (DateBonLivraisonVente))  \n" ;

                    Log.e("les annees ",query);
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    z="e";

                    // ArrayList data1 = new ArrayList();
                    while (rs.next()) {

                        String annee  = rs.getString("annees_recouvrement") ;
                        listAnnee.add(annee);

                    }



                }
            } catch (SQLException ex) {
                z = "etape"+ex.toString();

            }
            return z;
        }



        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String s) {

            DecimalFormat df = new DecimalFormat("00") ;
            Date currentDate  = new Date()  ;
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            String  currentYear =  cal.get(Calendar.YEAR)+"" ;
            String  currentMois = df.format( cal.get(Calendar.MONTH)+1);

            Log.e("Year-Mois" ,currentYear+" * "+currentMois) ;



            int i = 0 ;
            for (String  annee : listAnnee) {
                tab_annee.addTab(tab_annee.newTab().setText(annee));
                if(annee.equals(currentYear)) {
                    Log.e("Year-Mois" ," * "+annee) ;
                    indexCurrentYear = i;
                }

                i++ ;
            }

            int j = 0 ;
            for (String mois  : listMois) {
                tab_mois.addTab(tab_mois.newTab().setText(mois));
                Log.e("list_code_mois",listCodeMois.get(j)) ;
                if (currentMois.equals(listCodeMois.get(j)))

                {
                    indexCurrentMois= j;
                    Log.e("Year-Mois" ," * "+mois ) ;
                }
                j++;
            }



            Log.e("current" , " year  "+indexCurrentYear+ " mois "+indexCurrentMois) ;

            tab_annee.getTabAt(indexCurrentYear).select();

            tab_mois.setScrollX(tab_mois.getWidth());

            new Handler().postDelayed(
                    new Runnable() {
                        @Override public void run() {

                            tab_mois.getTabAt(indexCurrentMois).select();


                        }
                    }, 500);


            tab_mois.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    indexCurrentMois = tab.getPosition() ;

                   /* SuivieMensuelActivity.SuivieMensuelTask suivieMensuelTask =new SuivieMensuelActivity.SuivieMensuelTask(listCodeMois.get(indexCurrentMois), listAnnee.get(indexCurrentYear));
                    suivieMensuelTask.execute("");*/
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            tab_annee.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    indexCurrentYear = tab.getPosition() ;

                   /* SuivieMensuelActivity.SuivieMensuelTask suivieMensuelTask =new SuivieMensuelActivity.SuivieMensuelTask(listCodeMois.get(indexCurrentMois), listAnnee.get(indexCurrentYear));
                    suivieMensuelTask.execute("");*/
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }


    }









}
