package com.example.faten.testsql.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.faten.testsql.R;
import com.example.faten.testsql.model.Article;
import com.example.faten.testsql.model.Client;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by fatima on 20/01/2017.
 */
public class ClientAdapter extends ArrayAdapter<Client> {


    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    NumberFormat formatter = new DecimalFormat("0.000");

    private final Activity activity;
    private final ArrayList<Client> listClient;

    public ClientAdapter(Activity activity  , ArrayList<Client> listClient) {
        super(activity, R.layout.item_client_tel, listClient);
        // TODO Auto-generated constructor stub

        this.activity=activity;
        this.listClient=listClient;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();
        Context context = parent.getContext();

        View rowView=inflater.inflate(R.layout.item_client_tel, null, true);


        Client client = listClient.get(position);

        TextView txt_code_client         = (TextView) rowView.findViewById(R.id.txt_code_client);
        TextView txt_raison_client       = (TextView) rowView.findViewById(R.id.txt_raison_client);
        TextView txt_tel_client          = (TextView) rowView.findViewById(R.id.txt_tel_client);


        txt_code_client .setText (client.getCodeClient());
        txt_raison_client.setText(client.getRaisonSociale());
        txt_tel_client.setText   (client.getTel1());

        return rowView;

    }


}
