package com.example.faten.testsql.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.faten.testsql.R;
import com.example.faten.testsql.model.BonCommandeVente;
import com.example.faten.testsql.model.Client;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by fatima on 20/01/2017.
 */
public class BonCommandeAdapter extends ArrayAdapter<BonCommandeVente> {


    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    NumberFormat formatter = new DecimalFormat("0.000");

    private final Activity activity;
    private final ArrayList<BonCommandeVente> listBonCommandeVente;

    public BonCommandeAdapter(Activity activity  , ArrayList<BonCommandeVente> listBonCommandeVente) {
        super(activity, R.layout.item_bon_commande, listBonCommandeVente);
        // TODO Auto-generated constructor stub

        this.activity=activity;
        this.listBonCommandeVente=listBonCommandeVente;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();
        Context context = parent.getContext();

        View rowView=inflater.inflate(R.layout.item_bon_commande, null, true);


        BonCommandeVente bc = listBonCommandeVente.get(position);

        CardView  card_cmd  = (CardView) rowView.findViewById(R.id.item_bon_commande);
        TextView txt_num_bc        = (TextView) rowView.findViewById(R.id.txt_num_bc);
        TextView txt_raison_client       = (TextView) rowView.findViewById(R.id.txt_raison_client);
        TextView txt_ttc          = (TextView) rowView.findViewById(R.id.txt_prix_ttc);
        TextView txt_date_bc         = (TextView) rowView.findViewById(R.id.txt_date_bc);


        txt_num_bc .setText (bc.getNumeroBonCommandeVente());
        txt_raison_client.setText(bc.getReferenceClient());
        txt_ttc.setText   (formatter.format(bc.getTotalTTC())+" TTC");
        txt_date_bc.setText(df.format(bc.getDateBonCommandeVente()));



        if (bc.getNumeroEtat().equals("E00"))
        {
            card_cmd.setCardBackgroundColor(activity.getResources().getColor(R.color.back_rouge_));
        }

        return rowView ;

    }


}
