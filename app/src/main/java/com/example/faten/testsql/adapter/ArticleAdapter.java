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
public class ArticleAdapter extends ArrayAdapter<Article> {


    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    NumberFormat formatter = new DecimalFormat("0.000");

    private final Activity activity;
    private final ArrayList<Article> listArticle;

    public ArticleAdapter(Activity activity  , ArrayList<Article> listArticle) {
        super(activity, R.layout.item_article, listArticle);
        // TODO Auto-generated constructor stub

        this.activity=activity;
        this.listArticle=listArticle;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();
        Context context = parent.getContext();

        View rowView=inflater.inflate(R.layout.item_article, null, true);

        Article article  = listArticle.get(position);

        TextView txt_code_art            = (TextView) rowView.findViewById(R.id.txt_code_art);
        TextView txt_prix_article        = (TextView) rowView.findViewById(R.id.txt_prix_article);
        TextView txt_qt_article          = (TextView) rowView.findViewById(R.id.txt_qt_article);


        txt_code_art .setText    (article.getCodeArticle());
        txt_prix_article.setText (formatter.format(article.getPrixVenteTTC()) );
        txt_qt_article.setText   (article.getQuantite()+"");

        return rowView;

    }


}
