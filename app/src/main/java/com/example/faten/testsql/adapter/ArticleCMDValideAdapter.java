package com.example.faten.testsql.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.faten.testsql.model.Article;
import com.example.faten.testsql.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/*
 * Created by fatima on 20/01/2017.
 */
public class ArticleCMDValideAdapter extends ArrayAdapter<Article> {

    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    NumberFormat formatter = new DecimalFormat("0.000");

    private final Activity activity;
    private final ArrayList<Article> listArticles;

    public ArticleCMDValideAdapter  (Activity activity  , ArrayList<Article> listArticles ) {
        super(activity, R.layout.item_article_cmd_valide, listArticles);
        // TODO Auto-generated constructor stub
        this.activity=activity;
        this.listArticles=listArticles;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();
        Context context = parent.getContext();

        View rowView=inflater.inflate(R.layout.item_article_cmd_valide, null, true);


        Article article = listArticles.get(position);

        TextView txt_qte_a_commander         = (TextView) rowView.findViewById(R.id.txt_qt_a_commander);
        TextView txt_designation_article     = (TextView) rowView.findViewById(R.id.txt_designation_article);
        TextView txt_prix_article_unitaire   = (TextView) rowView.findViewById(R.id.txt_prix_article_unitaire);


        txt_qte_a_commander .setText(article.getNbrCLick()+"");
        txt_designation_article.setText(article.getCodeArticle());
        txt_prix_article_unitaire.setText(formatter.format(article.getPrixVenteTTC()));

        return rowView;

    }


}
