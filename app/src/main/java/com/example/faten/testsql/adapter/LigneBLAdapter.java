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
import com.example.faten.testsql.model.LigneBonLivraisonVente;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by fatima on 20/01/2017.
 */
public class LigneBLAdapter extends ArrayAdapter<LigneBonLivraisonVente> {


    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    NumberFormat formatter = new DecimalFormat("0.000");

    private final Activity activity;
    private final ArrayList<LigneBonLivraisonVente> listArticle;

    public LigneBLAdapter(Activity activity  , ArrayList<LigneBonLivraisonVente> listArticle) {
        super(activity, R.layout.item_article_lbl, listArticle);
        // TODO Auto-generated constructor stub

        this.activity=activity;
        this.listArticle=listArticle;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();
        Context context = parent.getContext();

        View rowView=inflater.inflate(R.layout.item_article_lbl, null, true);

        LigneBonLivraisonVente article  = listArticle.get(position);

        TextView txt_designation            = (TextView) rowView.findViewById(R.id.txt_designation);
        TextView txt_prix_ttc        = (TextView) rowView.findViewById(R.id.txt_prix_ttc);
        TextView txt_qt_article          = (TextView) rowView.findViewById(R.id.txt_qt);


        txt_designation .setText    (article.getDesignationArticle());
        txt_prix_ttc.setText (formatter.format( article.getMontantTTC() ) );
        txt_qt_article.setText   (article.getQuantite()+"");

        return rowView;

    }


}
