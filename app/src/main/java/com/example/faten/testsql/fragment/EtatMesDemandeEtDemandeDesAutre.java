package com.example.faten.testsql.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.faten.testsql.R;
import com.example.faten.testsql.task.DemandeReservationArticleTask;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class EtatMesDemandeEtDemandeDesAutre extends Fragment {


    public static ProgressBar pb;
    public static RecyclerView rv_list_demande;
    SearchView search_demande;
    public static String CodeRepresentant, CodeDepot;

    public TextView txt_date_debut, txt_date_fin;


    TabLayout tab_demande;

    int id_DatePickerDialog = 0;
    Date currentDate = new Date();
    public static int year_x1, month_x1, day_x1;
    public static int year_x2, month_x2, day_x2;

    public static Date date_debut = null;
    public static Date date_fin = null;
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    NumberFormat formatter = new DecimalFormat("00");

    public static int tab_selected;
    public static String Code_rec_selected;


    TextView txt_demandeur_demandant;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_mes_demandes_et_demande_des_autre, container, false);

        txt_date_debut = fragmentView.findViewById(R.id.txt_date_debut);
        txt_date_fin = fragmentView.findViewById(R.id.txt_date_fin);
        search_demande = fragmentView.findViewById(R.id.search_bar_demande);
        tab_demande = fragmentView.findViewById(R.id.tab_demande);
        txt_demandeur_demandant = fragmentView.findViewById(R.id.txt_demandeur_demandant) ;

        tab_selected = 0;
        tab_demande.addTab(tab_demande.newTab().setText("Mes Demandes")); //0
        tab_demande.addTab(tab_demande.newTab().setText("Demande des autre"));//1
        txt_demandeur_demandant.setText("Demandant");


        rv_list_demande = (RecyclerView) fragmentView.findViewById(R.id.rv_list_demande);
        pb = (ProgressBar) fragmentView.findViewById(R.id.pb);

        rv_list_demande.setHasFixedSize(true);
        rv_list_demande.setLayoutManager(new LinearLayoutManager(getActivity()));


        SharedPreferences pref = getActivity().getSharedPreferences("usersession", Context.MODE_PRIVATE);
        Code_rec_selected = pref.getString("CodeRepresentant", Code_rec_selected);
        CodeDepot = pref.getString("CodeDepot", CodeDepot);


        final Calendar cal1 = Calendar.getInstance();
        cal1.setTime(currentDate);
        //cal1.add(Calendar.YEAR, -1);
        year_x1 = cal1.get(Calendar.YEAR);
        month_x1 = cal1.get(Calendar.MONTH);
        day_x1 = cal1.get(Calendar.DAY_OF_MONTH);


        final Calendar cal2 = Calendar.getInstance();
        cal2.setTime(currentDate);
        //  cal2.add(Calendar.DAY_OF_YEAR, +7);
        year_x2 = cal2.get(Calendar.YEAR);
        month_x2 = cal2.get(Calendar.MONTH);
        day_x2 = cal2.get(Calendar.DAY_OF_MONTH);


        date_debut = cal1.getTime();
        String _date_du = df.format(cal1.getTime());
        txt_date_debut.setText(_date_du);

        date_fin = cal2.getTime();
        String _date_au = df.format(cal2.getTime());
        txt_date_fin.setText(_date_au);


        updateChargement();


        txt_date_debut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id_DatePickerDialog = 0;
                Log.e("month_x1", "On picker  : " + month_x1);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        if (id_DatePickerDialog == 0) {
                            year_x1 = year;
                            month_x1 = monthOfYear;
                            day_x1 = dayOfMonth;

                            txt_date_debut.setText("" + formatter.format(day_x1) + "/" + formatter.format(month_x1 + 1) + "/" + year_x1);

                            String _date_du = formatter.format(day_x1) + "/" + formatter.format(month_x1 + 1) + "/" + year_x1 + " ";
                            String _date_au = txt_date_fin.getText().toString();


                            try {
                                date_debut = df.parse(_date_du);
                                date_fin = df.parse(_date_au);

                                updateChargement();


                            } catch (Exception e) {
                                Log.e("Exception--", " " + e.getMessage());
                            }
                        }
                    }
                }, year_x1, month_x1, day_x1);
                datePickerDialog.show();
            }
        });

        txt_date_fin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                id_DatePickerDialog = 1;

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (id_DatePickerDialog == 1) {

                            year_x2 = year;
                            month_x2 = monthOfYear;
                            day_x2 = dayOfMonth;

                            txt_date_fin.setText("" + formatter.format(day_x2) + "/" + formatter.format(month_x2 + 1) + "/" + year_x2);

                            String _date_au = "" + formatter.format(day_x2) + "/" + formatter.format(month_x2 + 1) + "/" + year_x2;
                            String _date_du = txt_date_debut.getText().toString();

                            try {
                                date_debut = df.parse(_date_du);
                                date_fin = df.parse(_date_au);


                                updateChargement();

                            } catch (Exception e) {
                                Log.e("Exception --", " " + e.getMessage());
                            }

                        }
                    }
                }, year_x2, month_x2, day_x2);
                datePickerDialog.show();
            }
        });


        tab_demande.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tab_selected = tab.getPosition();

                if (tab_selected == 0) {
                    txt_demandeur_demandant.setText("Demandant");

                } else if (tab_selected == 1) {
                    txt_demandeur_demandant.setText("Demandeur");

                }

                updateChargement();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        }  );

        return fragmentView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Demande de réservation");

    }


    public void updateChargement() {

        DemandeReservationArticleTask demandeReservationArticleTask = new DemandeReservationArticleTask(getActivity(), rv_list_demande, search_demande, pb, tab_selected, date_debut, date_fin);
        demandeReservationArticleTask.execute();

    }

}
