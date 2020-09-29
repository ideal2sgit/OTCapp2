package com.example.faten.testsql.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.faten.testsql.R;
import com.example.faten.testsql.model.SuivieMensuel;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 ** Created by fatima on 20/01/2017.
 **/


public class MarqueVenduAdapter extends ArrayAdapter<SuivieMensuel> {


    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    NumberFormat formatter = new DecimalFormat("0.000");

    private final Activity activity;
    private final ArrayList<SuivieMensuel> listMarqueVendu;

    public MarqueVenduAdapter(Activity activity, ArrayList<SuivieMensuel> listMarqueVendu) {
        super(activity, R.layout.item_marque_vendu, listMarqueVendu);
        // TODO Auto-generated constructor stub

        this.activity = activity;
        this.listMarqueVendu = listMarqueVendu;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        Context context = parent.getContext();

        View rowView = inflater.inflate(R.layout.item_marque_vendu, null, true);

        SuivieMensuel marqueVendu = listMarqueVendu.get(position);

        TextView txt_marque             = (TextView) rowView.findViewById(R.id.txt_marque);
        TextView txt_code_famille       = (TextView) rowView.findViewById(R.id.txt_famille);
        TextView txt_qt                 = (TextView) rowView.findViewById(R.id.qt);
        TextView txt_total_ht           = (TextView) rowView.findViewById(R.id.txt_total_ht);


        txt_marque.setText(marqueVendu.getMarque());
        txt_code_famille.setText(marqueVendu.getCodeFamille());
        txt_qt.setText(marqueVendu.getQt_vendu()+"");
        txt_total_ht.setText(formatter.format(marqueVendu.getTotal_ht())+" Dt");


        return rowView;

    }


}
