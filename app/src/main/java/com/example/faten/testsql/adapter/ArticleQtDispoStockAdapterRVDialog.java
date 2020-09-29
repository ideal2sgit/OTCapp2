package com.example.faten.testsql.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.faten.testsql.R;
import com.example.faten.testsql.activity.BonCommandeVenteAvecTerminalActivity;
import com.example.faten.testsql.dialog.DialogChoixEditORDeleteChoix;
import com.example.faten.testsql.dialog.DialogChoixEditORDeleteDialogReserv;
import com.example.faten.testsql.model.ArticleStock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by fatima on 20/01/2017.
 */
public class ArticleQtDispoStockAdapterRVDialog
        extends RecyclerView.Adapter<ArticleQtDispoStockAdapterRVDialog.ViewHolder> {


    private final Activity activity;
    private ArrayList<ArticleStock> listArticle = new ArrayList<>();
    public static String login ;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final int REQUEST_PHONE_CALL = 1;

    public ArticleQtDispoStockAdapterRVDialog(Activity activity, ArrayList<ArticleStock> listArticle) {

        this.activity = activity;
        this.listArticle = listArticle;


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout. item_qt_par_depot, parent, false);
        return new ViewHolder(v);

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ArticleStock articleStock = listArticle.get(position);

        holder.txt_depot.setText(articleStock.getDepot() + "");
        holder.txt_qt.setText(articleStock.getQuantite() + "");
        //holder.txt_qt_dispo.setText(articleStock.getQteRestante()+"");

        SharedPreferences pref = activity.getSharedPreferences("usersession", Context.MODE_PRIVATE);
        String  codeDepotCurrent  = pref.getString("CodeDepot", "");

        if (articleStock.getNbrCLick() == 0) {

            holder.cb_select_article.setChecked(false);
            holder.fab_cercel_qt.setVisibility(View.INVISIBLE);
            holder.txt_nbr_click.setVisibility(View.INVISIBLE);

        } else if (articleStock.getNbrCLick() >= 1) {

            holder.cb_select_article.setChecked(true);
            holder.fab_cercel_qt.setVisibility(View.VISIBLE);
            holder.txt_nbr_click.setVisibility(View.VISIBLE);
            holder.txt_nbr_click.setText(articleStock.getNbrCLick() + "");

        }



        holder.card_depot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int nbr_click = articleStock.getNbrCLick();

                if (nbr_click < articleStock.getQuantite()) {

                    nbr_click++;
                    articleStock.setNbrCLick(nbr_click);

                    holder.fab_cercel_qt.setVisibility(View.VISIBLE);
                    holder.cb_select_article.setChecked(true);
                    holder.txt_nbr_click.setText(articleStock.getNbrCLick() + "");

                    holder.txt_nbr_click.setVisibility(View.VISIBLE);

/*
                    BonCommandeVenteAvecTerminalActivity.qt_a_cmd = 0;

                    for (ArticleStock articleStock1 : listArticle) {
                        BonCommandeVenteAvecTerminalActivity.qt_a_cmd = BonCommandeVenteAvecTerminalActivity.qt_a_cmd + articleStock1.getNbrCLick();
                    }

                    BonCommandeVenteAvecTerminalActivity._ed_quantite.setText("" + BonCommandeVenteAvecTerminalActivity.qt_a_cmd);

*/



                } else {
                    showDialogQtNonDispo();
                }


            }
        });


        holder.item_tel_depot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!codeDepotCurrent.equals(articleStock.getCodeDepot())) {


                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + articleStock.getTelPhone()));

                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);


                    } else {

                        activity.startActivity(intent);

                    }
                }
            }
        });


        holder.card_depot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                int nbr_click = articleStock.getNbrCLick();

                if (articleStock.getQuantite() != 0) {

                    Log.e("Long_click", "Long_click_pressed");
                    try {

                        final FragmentManager fm = activity.getFragmentManager();
                        DialogChoixEditORDeleteDialogReserv dialog = new DialogChoixEditORDeleteDialogReserv();
                        dialog.setActivity(activity);
                        dialog.setCb(holder.cb_select_article);
                        dialog.setFab_nbr_click(holder.fab_cercel_qt);
                        dialog.setCurrentArticleCMD(articleStock);

                        dialog.setTxt_nbr_click(holder.txt_nbr_click);
                        dialog.show(fm, "");


                    } catch (Exception ex) {
                        Log.e("Exception", ex.toString());
                    }

                } else {
                    showDialogQtNonDispo();
                }


                return true;
            }
        });




        if (position % 2 == 0) // paire
        {
            holder.card_depot.setBackgroundColor(Color.parseColor("#F0F0F0"));
        }



        if (codeDepotCurrent.equals(articleStock.getCodeDepot()))
        {
            holder.card_depot.setBackgroundColor(Color.parseColor("#fde7e7"));
            holder.img_tel.setVisibility(View.INVISIBLE);

            holder.card_depot.setEnabled(false);
            holder.fab_cercel_qt.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(activity , R.color.color_g_4)));
            holder.cb_select_article.setButtonTintList(ColorStateList.valueOf(activity.getColor(R.color.gris))) ;
        }

        if (articleStock.getCodeDepot().equals("01"))
        {

            holder.card_depot.setEnabled(false);
            holder.fab_cercel_qt.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(activity , R.color.color_g_4)));
            holder.cb_select_article.setButtonTintList(ColorStateList.valueOf(activity.getColor(R.color.gris))) ;

        }


        /*if(position  ==  (listDepot.size()-1))
        {
            holder .card_depot.setBackgroundColor(Color.parseColor("#EDFCF0F0"));
        }
        */

    }


    public void showDialogQtNonDispo() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                .setTitle("Stock insuffisant")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        dialog.show();

    }

    @Override
    public int getItemCount() {

        Log.e("size", "" + listArticle.size());
        return listArticle.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView txt_depot;
        public TextView txt_qt;
        //public TextView txt_qt_dispo;
        public CardView card_depot;

public ImageView  img_tel  ;

        public LinearLayout item_tel_depot;

        public TextView txt_nbr_click;
        public CheckBox cb_select_article;
        public FloatingActionButton fab_cercel_qt;

        public ViewHolder(View itemView) {
            super(itemView);


            cb_select_article = (CheckBox) itemView.findViewById(R.id.cb_select_article);
            fab_cercel_qt = (FloatingActionButton) itemView.findViewById(R.id.fab_cercel_qt);
            txt_nbr_click = (TextView) itemView.findViewById(R.id.txt_nbr_click);


            card_depot = (CardView) itemView.findViewById(R.id.card_depot);
            txt_depot = (TextView) itemView.findViewById(R.id.txt_libelle_depot);
            txt_qt = (TextView) itemView.findViewById(R.id.txt_quantite);

            item_tel_depot = (LinearLayout) itemView.findViewById(R.id.item_tel_depot);
            img_tel = (ImageView)  itemView.findViewById(R.id.img_tel)  ;
            // txt_qt_dispo = (TextView) itemView.findViewById(R.id.txt_qt_dispo );


        }


    }

}
