package com.example.faten.testsql.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.faten.testsql.R;
import com.example.faten.testsql.model.FicheClient;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by fatima on 20/01/2017.
 */

public class FicheClientAdapter extends ArrayAdapter<FicheClient> {


    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    NumberFormat formatter = new DecimalFormat("0.000");

    private final Activity activity;
    private final ArrayList<FicheClient> listFicheClient;

    public FicheClientAdapter(Activity activity  , ArrayList<FicheClient> listFicheClient ) {
        super(activity, R.layout.item_fiche_client, listFicheClient);
        // TODO Auto-generated constructor stub

        this.activity=activity;
        this.listFicheClient=listFicheClient;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();
        Context context = parent.getContext();
        View rowView=inflater.inflate(R.layout.item_fiche_client, null, true);


        TextView libelle_operation = (TextView) rowView.findViewById(R.id.libelle_fiche);

        TextView date_operation = (TextView)   rowView.findViewById(R.id.date_operation);
        TextView debit = (TextView) rowView.findViewById(R.id.debit);
        TextView credit = (TextView)   rowView.findViewById(R.id.credit);
        TextView solde = (TextView) rowView.findViewById(R.id.solde);

        date_operation.setText(df.format(listFicheClient.get(position).getDateOperation()));

        libelle_operation.setText(listFicheClient.get(position).getLibelle());
        debit.setText(formatter.format(listFicheClient.get(position).getDebit()));
        credit.setText(formatter.format(listFicheClient.get(position).getCredit()));


        solde.setText  (formatter.format(listFicheClient.get(position).getSoldeIncrement()));



        return rowView;

    }


}
