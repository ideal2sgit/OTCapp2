package com.example.faten.testsql.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
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
//import com.example.faten.testsql.activity.BonCommandeVenteAvecTerminalActivity;
import com.example.faten.testsql.activity.BonCommandeTerminalAutomatiqueActivity;
import com.example.faten.testsql.model.ArticleStock;

public class DialogChoixEditORDeleteDialogReserv extends DialogFragment {

    ArticleStock currentArticleCMD;
    CheckBox cb;
    FloatingActionButton fab_nbr_click;
    TextView txt_nbr_click;
    Activity  activity ;


    public void setActivity(Activity activity) {
        this.activity = activity;
    }


    public void setCurrentArticleCMD(ArticleStock currentArticleCMD) {
        this.currentArticleCMD = currentArticleCMD;
    }

    public void setCb(CheckBox cb) {
        this.cb = cb;
    }

    public void setTxt_nbr_click(TextView txt_nbr_click) {
        this.txt_nbr_click = txt_nbr_click;
    }

    public void setFab_nbr_click(FloatingActionButton fab_nbr_click) {
        this.fab_nbr_click = fab_nbr_click;
    }

    CardView btn_edit_qt, btn_delete_qt ;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View rootView = inflater.inflate(R.layout.dialog_choix_ , container);

        btn_edit_qt = (CardView) rootView.findViewById(R.id.btn_edit_qt);
        btn_delete_qt = (CardView) rootView.findViewById(R.id.btn_delete_qt);


        btn_edit_qt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(activity, "EDIT", Toast.LENGTH_SHORT).show();
                final FragmentManager fm = activity.getFragmentManager();
                DialogEditQuantiteDialogReserv dialog = new DialogEditQuantiteDialogReserv( );
                dialog.setArticleStock(currentArticleCMD);
                dialog.setCb(cb);
                dialog.setFab(fab_nbr_click);
                dialog.setTxt_nbr_click(txt_nbr_click);
                dialog.show(fm, "");
                dismiss();

            }
        });


        btn_delete_qt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSupprimerSelection(currentArticleCMD, cb,fab_nbr_click , txt_nbr_click);
                dismiss();
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
                        if (BonCommandeTerminalAutomatiqueActivity.listArticleDispo.contains(cArticleCMD)) {

                            index_article_panier = BonCommandeTerminalAutomatiqueActivity.listArticleDispo.indexOf(cArticleCMD);
                        }

                        cArticleCMD.setNbrCLick(0);
                      //  BonCommandeVenteAvecTerminalActivity.listArticleDispo.remove(index_article_panier);
                        cb.setChecked(false);
                        fab_qt.setVisibility(View.INVISIBLE);
                        txt_nbr_click.setText(cArticleCMD.getNbrCLick() + "");
                        txt_nbr_click.setVisibility(View.INVISIBLE);


                      /*  BonCommandeTerminalAutomatiqueActivity.qt_a_cmd = 0;

                        for (ArticleStock articleStock1 : BonCommandeTerminalAutomatiqueActivity.listArticleDispo) {
                            BonCommandeTerminalAutomatiqueActivity.qt_a_cmd = BonCommandeTerminalAutomatiqueActivity.qt_a_cmd + articleStock1.getNbrCLick();
                        }
                        BonCommandeTerminalAutomatiqueActivity._ed_quantite.setText( "" + BonCommandeTerminalAutomatiqueActivity.qt_a_cmd );
                        BonCommandeTerminalAutomatiqueActivity.articlePanierAutoAdapter. notifyDataSetChanged();*/



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