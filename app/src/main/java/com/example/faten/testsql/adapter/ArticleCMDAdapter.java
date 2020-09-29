package com.example.faten.testsql.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.faten.testsql.BonDeCommandeActivity;
import com.example.faten.testsql.R;
import com.example.faten.testsql.model.Article;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by fatima on 20/01/2017.
 **/

public class ArticleCMDAdapter extends RecyclerView.Adapter<ArticleCMDAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<Article> listArticle;//= new ArrayList<>();
    RecyclerView list_qt_a_commander;
    ArrayList<Article> nvListArt;
    public static String login;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    NumberFormat nf = new DecimalFormat("0.000");

    int listColor[] = {R.color.color0, R.color.color1, R.color.color2, R.color.color3, R.color.color4
            , R.color.color5, R.color.color6, R.color.color7, R.color.color8, R.color.color9
            , R.color.color10, R.color.color11, R.color.color12, R.color.color13, R.color.color14
            , R.color.color15, R.color.color16, R.color.color17, R.color.color18, R.color.color19
            , R.color.color20};

    public ArticleCMDAdapter(Activity activity, ArrayList<Article> listArticle, RecyclerView list_qt_a_commander) {

        this.activity = activity;
        this.listArticle = listArticle;
        this.list_qt_a_commander = list_qt_a_commander;
        nvListArt = new ArrayList<>();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bon_cmd, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Article article = listArticle.get(position);

        holder.code_art.setText(article.getCodeArticle());
        holder.prix.setText(nf.format(article.getPrixVenteTTC()));
        holder.textqt.setText(article.getQuantite() + "");
        holder.textrestant.setText(article.getQteRestante() + "");


        //Log.e("is_reclamed",client.getCodeClient()+" - "+client.getEstReclame())  ;
        final FragmentManager fm = activity.getFragmentManager();


        // final int QteRestante2 = article.getQteRestante();
        holder.itemSlected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (  ((article.getQteRestante()  - article.getNbrCLick())> 0) &&  (article.getQteRestante() > article.getNbrCLick())  )  {

                   // if  {

                        for (int i = 0; i < article.getQteRestante(); i++) {

                            if (article.getNbrCLick() == i) {
                                i++;
                                article.setNbrCLick(i);
                                Log.e("pos_val", "" + article.getNbrCLick());

                                if (i < 21)
                                    holder.itemSlected.setBackgroundColor(activity.getResources().getColor(listColor[i]));
                                else
                                    holder.itemSlected.setBackgroundColor(activity.getResources().getColor(listColor[20]));
                            }

                        }

                        holder.textrestant.setText  (  article.getQteRestante() - article.getNbrCLick() + "" ) ;

                        if (BonDeCommandeActivity.listArticleAcommander.get(position).getCodeArticle().equals(article.getCodeArticle()))
                            BonDeCommandeActivity.listArticleAcommander.set(position, article);

                    }
                    //  adapter of list article commandé
                   else {
                    holder.textrestant.setText       (  article.getQteRestante() - article.getNbrCLick() + "" );
                    //  holder.textrestant.setText("0");
                    dialogStockInsuffisant();
                }
                notifyDataSetChanged();
                Log.e("pos_click", position + "  : " + article.getNbrCLick() + " ; " + article.getQteRestante());
            }
        });

        for (int i = 0; i < 21; i++)

        {
            if (article.getNbrCLick() == i) {

                holder.itemSlected.setBackgroundColor(activity.getResources().getColor(listColor[i]));

            }

        }

        if (article.getNbrCLick() >= 21) {

            holder.itemSlected.setBackgroundColor(activity.getResources().getColor(listColor[20]));
        }

       // if  {
            if  ((article.getQteRestante() > article.getNbrCLick()  )  &&  ( ( article.getQteRestante()-article.getNbrCLick() ) > 0 ) )
            {

                holder.textrestant.setText(article.getQteRestante() - article.getNbrCLick() + "");
                if (BonDeCommandeActivity.listArticleAcommander.get(position).getCodeArticle().equals(article.getCodeArticle()))
                    BonDeCommandeActivity.listArticleAcommander.set(position, article);

                // adapter of list article commandé

                nvListArt.clear();
                for (Article a : BonDeCommandeActivity.listArticleAcommander) {
                    if (a.getNbrCLick() != 0)
                        nvListArt.add(a);
                }

                ArticleQtAdapterRV adapter = new ArticleQtAdapterRV(activity, nvListArt);
                list_qt_a_commander.setAdapter(adapter);


             } else {
            holder.textrestant.setText(article.getQteRestante() - article.getNbrCLick() + "");
            //holder.textrestant.setText("0");
        }


    }


    @Override
    public int getItemCount() {

        Log.e("size", "" + listArticle.size());
        return listArticle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout itemSlected;

        public TextView code_art;

        public TextView prix;
        public TextView textrestant;
        public TextView textqt;


        public ViewHolder(View itemView) {
            super(itemView);

            itemSlected = (LinearLayout) itemView.findViewById(R.id.item_slected);
            code_art = (TextView) itemView.findViewById(R.id.code_art);
            prix = (TextView) itemView.findViewById(R.id.prix);
            textrestant = (TextView) itemView.findViewById(R.id.textrestant);
            textqt = (TextView) itemView.findViewById(R.id.textqt);

        }

    }


    public void dialogStockInsuffisant() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setIcon(R.drawable.i2s);
        alert.setTitle("Commande");
        alert.setMessage("Stock insuffisant");
        alert.setNegativeButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface di, int i) {
                        di.cancel();
                    }
                });
        AlertDialog dd = alert.create();
        dd.show();
    }



}
