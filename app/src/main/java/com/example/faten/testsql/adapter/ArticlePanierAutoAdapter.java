package com.example.faten.testsql.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.faten.testsql.R;
import com.example.faten.testsql.activity.BonCommandeTerminalAutomatiqueActivity;
import com.example.faten.testsql.activity.BonCommandeVenteAvecTerminalActivity;
import com.example.faten.testsql.dialog.DialogReservationDansAutreDepot;
import com.example.faten.testsql.model.ArticleStock;
import com.example.faten.testsql.model.ReservationArticleDansDepot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by fatima on 20/01/2017.
 */
public class ArticlePanierAutoAdapter extends RecyclerView.Adapter<ArticlePanierAutoAdapter.ViewHolder> {


    private final Activity activity;
    private ArrayList<ArticleStock> listArticle = new ArrayList<>();
    public static String login;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");


    public ArticlePanierAutoAdapter(Activity activity, ArrayList<ArticleStock> listArticle) {

        this.activity = activity;
        this.listArticle = listArticle;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_panier, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ArticleStock articleCMD = listArticle.get(position);

        holder.txt_code_article.setText(articleCMD.getCodeArticle());
        holder.txt_qt_panier.setText(articleCMD.getQtePanier() + "");

        holder.img_delete_ligne_panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogDelete(listArticle, articleCMD);

            }
        });



        holder.img_view_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FragmentManager fm = activity.getFragmentManager();
                //  Open dialog  rapport suivie detail
                DialogReservationDansAutreDepot dialog = new DialogReservationDansAutreDepot();
                dialog.setArticle(articleCMD);
                dialog.setCodeArticle(articleCMD.getCodeArticle());
                 // dialog.setQTStock(qt_stock_total);
                dialog.setActivity(activity);
                dialog.show(fm, "");

            }
        });


    }


    @Override
    public int getItemCount() {

        Log.e("size", "" + listArticle.size());
        return listArticle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_code_article;
        public TextView txt_qt_panier;
        public ImageView img_delete_ligne_panier;
        public ImageView  img_view_reservation ;

        public CardView card_article_cmd;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_code_article = (TextView) itemView.findViewById(R.id.txt_article);
            txt_qt_panier = (TextView) itemView.findViewById(R.id.txt_qt_article);

            img_delete_ligne_panier = (ImageView) itemView.findViewById(R.id.img_delete_ligne);

            img_view_reservation = (ImageView) itemView.findViewById(R.id.img_view_reservation);
        }


    }


    public void showDialogDelete(ArrayList<ArticleStock> listArticle, ArticleStock articlePanier) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                .setTitle("Etes-Vous sûre d'annuler cet article ?")

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       listArticle.remove(articlePanier);


                        Iterator<ReservationArticleDansDepot> iter = BonCommandeTerminalAutomatiqueActivity.listReservation.iterator();

                        while (iter.hasNext()) {

                            ReservationArticleDansDepot p = iter.next();
                            if (p.getCodeArticle().equals(articlePanier.getCodeArticle())) {

                                iter.remove();
                                Log.e("remove", "--" + p.getCodeArticle());
                            }
                        }

                        Log.e("LIST_RESERV "+BonCommandeTerminalAutomatiqueActivity.listReservation.size() ,""+BonCommandeTerminalAutomatiqueActivity.listReservation.toString());

                        Log.e("RESECATION", "" + BonCommandeTerminalAutomatiqueActivity.listReservation.size());

                        if ( listArticle.size()==0 )
                        {
                            BonCommandeTerminalAutomatiqueActivity.btn_lancer_bon_cmd.setEnabled(false);
                            BonCommandeTerminalAutomatiqueActivity.btn_lancer_bon_cmd.setBackgroundColor(Color.parseColor("#F0F0F0"));
                        }

                        notifyDataSetChanged();

                        //  BonCommande(qt_tot_cmd, listReservation);

                    }
                })

                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        dialog.show();
    }
}
