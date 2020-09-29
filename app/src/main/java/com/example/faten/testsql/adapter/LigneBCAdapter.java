package com.example.faten.testsql.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.faten.testsql.R;
import com.example.faten.testsql.model.LigneBonCommandeVente;
import com.example.faten.testsql.model.LigneBonLivraisonVente;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by fatima on 20/01/2017.
 */
public class LigneBCAdapter extends ArrayAdapter<LigneBonCommandeVente> {


    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    NumberFormat formatter = new DecimalFormat("0.000");

    private final Activity activity;
    private final ArrayList<LigneBonCommandeVente> listArticle;

    public LigneBCAdapter(Activity activity  , ArrayList<LigneBonCommandeVente> listArticle) {
        super(activity, R.layout.item_lbc, listArticle);

        this.activity=activity;
        this.listArticle=listArticle;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();
        Context context = parent.getContext();

        View rowView=inflater.inflate(R.layout.item_lbc  , null, true);

        LigneBonCommandeVente article  = listArticle.get(position);

        TextView txt_code_article   = (TextView) rowView.findViewById(R.id.txt_article);
        TextView txt_qt_article    = (TextView) rowView.findViewById(R.id.txt_qt_article);
        TextView txt_prix_ttc      = (TextView) rowView.findViewById(R.id.txt_prix_ttc);



        txt_code_article .setText    (article.getCodeArticle());
        txt_prix_ttc.setText (formatter.format( article.getMontantTTC() ) );
        txt_qt_article.setText   (article.getQuantite()+"");

        return rowView;

    }


}
