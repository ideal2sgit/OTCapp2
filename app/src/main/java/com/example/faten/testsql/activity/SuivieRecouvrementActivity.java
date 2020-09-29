package com.example.faten.testsql.activity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.R;
import com.example.faten.testsql.adapter.SuivieRecouvrementAdapter;
import com.example.faten.testsql.model.SuivieRecouvrement;

import java.lang.ref.Reference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SuivieRecouvrementActivity extends AppCompatActivity {


    ConnectionClass connectionClass;
    final Context co = this;
    String user, password, base, ip;
    String NomUtilisateur, codelivreur, CodeFonction;


    int id_DatePickerDialog = 0;
    public TextView dateView_du, dateView_au;
    ListView  lv_suivie_recouvrement ;
    public TextView txt_recu ;

    Date currentDate = new Date();
    public static int year_x1, month_x1, day_x1;
    public static int year_x2, month_x2, day_x2;
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat dfSQL = new SimpleDateFormat("yyyy-MM-dd");
    NumberFormat formatter = new DecimalFormat("00");
    public static Date datedu = null;
    public static Date dateau = null;


    public   double  total_recu =  0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suivie_recouvrement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Suivie Recouvrement ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(arrow -> onBackPressed());

        dateView_du = (TextView) findViewById(R.id.date_debut);
        dateView_au = (TextView) findViewById(R.id.date_fin);
        lv_suivie_recouvrement =(ListView) findViewById(R.id.lv_suivie_recouvrement);
        txt_recu= (TextView) findViewById(R.id.txt_recu);

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


        final Calendar cal2 = Calendar.getInstance();
        cal2.setTime(currentDate);

        year_x2 = cal2.get(Calendar.YEAR);
        month_x2 = cal2.get(Calendar.MONTH);
        day_x2 = cal2.getActualMaximum(Calendar.DAY_OF_MONTH);

        Date dateFin = new Date();
        try {
            dateFin = df.parse(day_x2+"/"+(month_x2+1)+"/" + year_x2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal2.setTime(dateFin);

        dateau = cal2.getTime();
        String _date_au = df.format(cal2.getTime());
        dateView_au.setText(_date_au);


        final Calendar cal1 = Calendar.getInstance();
        Date dateDebut = new Date();
        try {
            dateDebut = df.parse("01/"+(month_x2+1)+"/" + year_x2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal1.setTime(dateDebut);
        year_x1 = cal1.get(Calendar.YEAR);  //  current year
        month_x1 = cal1.get(Calendar.MONTH);
        day_x1 =  cal1.get(Calendar.DAY_OF_MONTH); ;


        datedu = cal1.getTime();
        String _date_du = df.format(cal1.getTime());
        dateView_du.setText(_date_du);


        dateView_du.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id_DatePickerDialog = 0;
                Log.e("month_x1", "On picker  : " + month_x1);
                DatePickerDialog datePickerDialog = new DatePickerDialog(SuivieRecouvrementActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        if (id_DatePickerDialog == 0) {
                            year_x1 = year;
                            month_x1 = monthOfYear;
                            day_x1 = dayOfMonth;

                            dateView_du.setText("" + formatter.format(day_x1) + "/" + formatter.format(month_x1 + 1) + "/" + year_x1);


                            String _date_du = formatter.format(day_x1) + "/" + formatter.format(month_x1 + 1) + "/" + year_x1 + " ";
                            String _date_au = dateView_au.getText().toString();


                            try {
                                datedu = df.parse(_date_du);
                                dateau = df.parse(_date_au);

                                //  updateListIntervention(datedu, dateau);

                                SuivieRecouvrementTask suivieRecouvrementTask = new SuivieRecouvrementTask(datedu, dateau, codelivreur);
                                suivieRecouvrementTask.execute()  ;

                            } catch (Exception e) {
                                Log.e("Exception--", " " + e.getMessage());
                            }
                        }
                    }
                }, year_x1, month_x1, day_x1);
                datePickerDialog.show();
            }
        });


        dateView_au.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id_DatePickerDialog = 1;

                DatePickerDialog datePickerDialog = new DatePickerDialog(SuivieRecouvrementActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (id_DatePickerDialog == 1) {

                            year_x2 = year;
                            month_x2 = monthOfYear;
                            day_x2 = dayOfMonth;

                            dateView_au.setText("" + formatter.format(day_x2) + "/" + formatter.format(month_x2 + 1) + "/" + year_x2);

                            String _date_au = "" + formatter.format(day_x2) + "/" + formatter.format(month_x2 + 1) + "/" + year_x2;
                            String _date_du = dateView_du.getText().toString();

                            try {
                                datedu = df.parse(_date_du);
                                dateau = df.parse(_date_au);

                                //    updateListIntervention(datedu, dateau);

                                SuivieRecouvrementTask suivieRecouvrementTask = new SuivieRecouvrementTask(datedu, dateau, codelivreur);
                                suivieRecouvrementTask.execute()  ;

                            } catch (Exception e) {
                                Log.e("Exception --", " " + e.getMessage());
                            }

                        }
                    }
                }, year_x2, month_x2, day_x2);
                datePickerDialog.show();
            }
        });

        SuivieRecouvrementTask suivieRecouvrementTask = new SuivieRecouvrementTask(datedu, dateau, codelivreur);
        suivieRecouvrementTask.execute()  ;
    }


    public class SuivieRecouvrementTask extends AsyncTask<String, String, String> {
        String z = "";
        Boolean test = false;


        Date dateDebut, dateFin;
        String CodeLivreur;

        ArrayList<SuivieRecouvrement>   listRecouvrement = new ArrayList<>()  ;


        public SuivieRecouvrementTask(Date dateDebut, Date dateFin, String codeLivreur) {
            this.dateDebut = dateDebut;
            this.dateFin = dateFin;
            CodeLivreur = codeLivreur;
        }


        double TotaRecu = 0;


        @Override
        protected void onPreExecute() {
            TotaRecu = 0;


        }


        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = ""  ;


                    if(NomUtilisateur .equals("MAJDI"))
                    {
                        query = " select CodeClient,RaisonSociale,DateReglement,CodeModeReglement,Reference,Echeance,MontantRecu \n" +
                                "\n" +
                                "from  Vue_ListeCommution \n" +
                                "where   DateReglement \n" +
                                "between '" + df.format(dateDebut) + "'  and  '" + df.format(dateFin) + "' and \n" +
                                "MontantCommution = 0  ";
                    }
                    else
                    {
                        query = " select CodeClient,RaisonSociale,DateReglement,CodeModeReglement,Reference,Echeance,MontantRecu \n" +
                                "\n" +
                                "from  Vue_ListeCommution \n" +
                                "where   DateReglement \n" +
                                "between '" + df.format(dateDebut) + "'  and  '" + df.format(dateFin) + "' and \n" +
                                "CodeLivreur ='" + codelivreur + "' and \n" +
                                "MontantCommution = 0  ";
                    }





                    Log.e("query_Recouvrement_", query);
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    z = "e";
                    test = true;


                    while (rs.next()) {

                        String  CodeClient = rs.getString("CodeClient") ;
                        String  RaisonSociale = rs.getString("RaisonSociale") ;


                        Date DateReglement = null ;
                        try {
                             DateReglement  =dfSQL .parse(rs.getString("DateReglement"))  ;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String  CodeModeReglement = rs.getString("CodeModeReglement") ;
                        String  Reference = rs.getString("Reference") ;

                        Date DateEcheance = null ;
                        try {
                            DateEcheance  =dfSQL .parse(rs.getString("Echeance"))  ;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        double MontantRecu = rs.getDouble("MontantRecu") ;


                        SuivieRecouvrement suivieRecouvrement  = new SuivieRecouvrement(CodeClient ,RaisonSociale,DateReglement,CodeModeReglement,Reference , DateEcheance ,MontantRecu) ;
                        listRecouvrement .add (suivieRecouvrement) ;

                        TotaRecu= TotaRecu  +MontantRecu ;

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

          //  Toast.makeText(getApplicationContext(), r  , Toast.LENGTH_SHORT).show();

            NumberFormat formatter = new DecimalFormat("0.000");

            if (   test    ) {


                SuivieRecouvrementAdapter suivieRecouvrementAdapter   = new SuivieRecouvrementAdapter(SuivieRecouvrementActivity.this , listRecouvrement) ;
                lv_suivie_recouvrement.setAdapter(suivieRecouvrementAdapter);

                txt_recu.setText(formatter.format(TotaRecu)+" DT ");

            }
            if (r.equals("e")) {

                Toast.makeText(SuivieRecouvrementActivity.this, "Pas de règlement ", Toast.LENGTH_LONG).show();


            }


        }


    }


}
