package com.example.faten.testsql.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faten.testsql.R;
import com.example.faten.testsql.model.ComptageAndroid;
import com.example.faten.testsql.model.ReservationArticleDansDepot;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by fatima on 20/01/2017.
 */
public class DemandeArticleDansDepotAdapter extends ArrayAdapter<ReservationArticleDansDepot> {


    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    NumberFormat formatter = new DecimalFormat("0.000");

    private final Activity activity;
    private final ArrayList<ReservationArticleDansDepot> list_demande;

    public DemandeArticleDansDepotAdapter(Activity activity  , ArrayList<ReservationArticleDansDepot> list_demande) {
        super(activity, R.layout.item_article_inventaire, list_demande);
        // TODO Auto-generated constructor stub

        this.activity=activity;
        this.list_demande=list_demande;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();
        Context context = parent.getContext();

        View rowView=inflater.inflate(R.layout.item_demande_reservation, null, true );

        ReservationArticleDansDepot reservationArticleDansDepot  = list_demande.get(position);


        TextView txt_depot            = (TextView) rowView.findViewById(R.id.txt_depot);
        TextView txt_article        = (TextView) rowView.findViewById(R.id.txt_article);
        TextView txt_qt_article          = (TextView) rowView.findViewById(R.id.txt_qt_article);
       // ImageView  img_valide   = (ImageView) rowView.findViewById(R.id.img_valide);

        CardView  card_demande  = (CardView) rowView.findViewById(R.id.card_demande);


        txt_depot .setText    (reservationArticleDansDepot.getDepotDemandant());
        txt_article.setText ( reservationArticleDansDepot.getCodeArticle() );
        txt_qt_article.setText   (reservationArticleDansDepot.getQuantite()+"");



        card_demande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(activity  ,""+reservationArticleDansDepot.getDesignation(),Toast.LENGTH_SHORT).show();

            }
        });


       /* if (reservationArticleDansDepot.getValider() ==0)
        {
            img_valide.setVisibility(View.INVISIBLE);
        }
        else {

            img_valide.setVisibility(View.VISIBLE);
        }*/



        return rowView;

    }


}
