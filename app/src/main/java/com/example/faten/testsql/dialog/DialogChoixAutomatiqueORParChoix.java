package com.example.faten.testsql.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faten.testsql.R;
import com.example.faten.testsql.activity.BonCommandeTerminalAutomatiqueActivity;
import com.example.faten.testsql.activity.BonCommandeVenteAvecTerminalActivity;
import com.example.faten.testsql.model.ArticleStock;

public class DialogChoixAutomatiqueORParChoix extends DialogFragment {


    Activity  activity ;
    CardView btn_bc_automatique   , btn_bc_avec_choix ;
    String  CodeClient  , RaisonSociale  ;



    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setCodeClient(String codeClient) {
        CodeClient = codeClient;
    }

    public void setRaisonSociale(String raisonSociale) {
        RaisonSociale = raisonSociale;
    }


    public static DialogChoixAutomatiqueORParChoix newInstance(){
        DialogChoixAutomatiqueORParChoix df = new DialogChoixAutomatiqueORParChoix();
        return df;
    }




    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View rootView = inflater.inflate(R.layout.dialog_choix_automatique_or_choix , container);

        btn_bc_automatique = (CardView) rootView.findViewById(R.id.btn_bc_automatique);
        btn_bc_avec_choix = (CardView) rootView.findViewById(R.id.btn_bc_avec_choix);

        btn_bc_automatique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentToLivraisonClient = new Intent(activity, BonCommandeTerminalAutomatiqueActivity.class);
                intentToLivraisonClient.putExtra("code_client", CodeClient);
                intentToLivraisonClient.putExtra("raison_client", RaisonSociale);
                activity.startActivity(intentToLivraisonClient);

            }
        });

        btn_bc_avec_choix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentToLivraisonClient = new Intent(activity, BonCommandeVenteAvecTerminalActivity.class);
                intentToLivraisonClient.putExtra("code_client", CodeClient);
                intentToLivraisonClient.putExtra("raison_client", RaisonSociale);
                activity.startActivity(intentToLivraisonClient);

            }
        });


        return rootView ;
    }


    public void showDialogSupprimerSelection (final ArticleStock cArticleCMD, final CheckBox cb, final FloatingActionButton fab_qt, final TextView txt_nbr_click) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                .setTitle("Voulez-vous annuler la selection")
                .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int index_article_panier = 0;
                        if (BonCommandeVenteAvecTerminalActivity.listArticleDispo.contains(cArticleCMD)) {

                            index_article_panier = BonCommandeVenteAvecTerminalActivity.listArticleDispo.indexOf(cArticleCMD);

                        }

                        cArticleCMD.setNbrCLick(0);
                      //  BonCommandeVenteAvecTerminalActivity.listArticleDispo.remove(index_article_panier);
                        cb.setChecked(false);
                        fab_qt.setVisibility(View.INVISIBLE);
                        txt_nbr_click.setText(cArticleCMD.getNbrCLick() + "");
                        txt_nbr_click.setVisibility(View.INVISIBLE);


                        BonCommandeVenteAvecTerminalActivity.qt_a_cmd = 0;
                        for (ArticleStock articleStock1 : BonCommandeVenteAvecTerminalActivity.listArticleDispo) {
                            BonCommandeVenteAvecTerminalActivity.qt_a_cmd = BonCommandeVenteAvecTerminalActivity.qt_a_cmd + articleStock1.getNbrCLick();
                        }
                        BonCommandeVenteAvecTerminalActivity._ed_quantite.setText( "" + BonCommandeVenteAvecTerminalActivity.qt_a_cmd );
                        BonCommandeVenteAvecTerminalActivity.articleQtDispoStockAdapterRV. notifyDataSetChanged();



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