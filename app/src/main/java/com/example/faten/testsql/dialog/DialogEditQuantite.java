package com.example.faten.testsql.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.faten.testsql.R;
import com.example.faten.testsql.activity.BonCommandeVenteAvecTerminalActivity;
import com.example.faten.testsql.model.ArticleStock;


public class DialogEditQuantite extends DialogFragment {


    Button btn_annuler, btn_valider;
    TextInputLayout ed_quantite_;


    ArticleStock articleStock;
    FloatingActionButton fab;
    TextView txt_nbr_click;
    CheckBox cb;


    public void setArticleStock(ArticleStock articleStock) {
        this.articleStock = articleStock;
    }


    public void setTxt_nbr_click(TextView txt_nbr_click) {
        this.txt_nbr_click = txt_nbr_click;
    }

    public void setCb(CheckBox cb) {
        this.cb = cb;
    }


    public void setFab(FloatingActionButton fab) {
        this.fab = fab;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_edit_quantite_1, container);

        ed_quantite_ = rootView.findViewById(R.id.ed_quantite_);
        btn_valider = rootView.findViewById(R.id.btn_valider);
        btn_annuler = rootView.findViewById(R.id.btn_annuler);


        ed_quantite_.getEditText().setText(articleStock.getNbrCLick() + "");


        btn_annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!valideQuantite()) {
                    return;
                }

                String _quantite_ed = ed_quantite_.getEditText().getText().toString();

                int qt_edit = 0;
                try {
                    qt_edit = Integer.parseInt(_quantite_ed);

                } catch (Exception ex) {

                }


                articleStock.setNbrCLick(qt_edit);


                if (articleStock.getNbrCLick() == 0) {

                    cb.setChecked(false);
                    fab.setVisibility(View.INVISIBLE);
                    txt_nbr_click.setVisibility(View.INVISIBLE);

                } else if (articleStock.getNbrCLick() >= 1) {

                    cb.setChecked(true);
                    fab.setVisibility(View.VISIBLE);
                    txt_nbr_click.setVisibility(View.VISIBLE);
                    txt_nbr_click.setText(articleStock.getNbrCLick() + "");

                }



                BonCommandeVenteAvecTerminalActivity.qt_a_cmd = 0;

                for (ArticleStock articleStock1 : BonCommandeVenteAvecTerminalActivity.listArticleDispo) {
                    BonCommandeVenteAvecTerminalActivity.qt_a_cmd = BonCommandeVenteAvecTerminalActivity.qt_a_cmd + articleStock1.getNbrCLick();
                }

                BonCommandeVenteAvecTerminalActivity._ed_quantite.setText("" + BonCommandeVenteAvecTerminalActivity.qt_a_cmd);




                dismiss();


              /* boolean existe  = false ;
               int pos  =0 ;
               for (LigneBonLivraisonVente artRetour  : SelectBLPourRetourActivity.ligne_retour)
               {

                    if(artRetour.getNumeroBonLivraisonVente().equals(articleRetour.getNumeroBonLivraisonVente()) &&   (artRetour.getCodeArticle().equals(articleRetour.getCodeArticle())) )

                    {
                        existe = true ;
                        break;

                    }
                   pos++;
               }

               if (existe)
               {
                   SelectBLPourRetourActivity.ligne_retour.set(pos,articleRetour) ;

                   if (articleRetour.getQuantiteRetour() ==0)
                   {
                       SelectBLPourRetourActivity.ligne_retour.remove(pos) ;

                   }

               }
               else
               {

                   SelectBLPourRetourActivity.  ligne_retour.add ( articleRetour ) ;
                   SelectBLPourRetourActivity. ligneBonRetourAdapterRV.notifyDataSetChanged();
                   SelectBLPourRetourActivity.pieceARetourAdapterRV.notifyDataSetChanged();

                   dismiss();
               }*/


            }
        });


        return rootView;
    }


    private Boolean valideQuantite() {
        String val = ed_quantite_.getEditText().getText().toString();

        int qt_saisie = 0;
        try {
            qt_saisie = Integer.parseInt(val);

        } catch (Exception ex) {

        }

        if (qt_saisie > articleStock.getQuantite()) {
            ed_quantite_.setError("Quantité saisie supérieur à la quantité livré");
            return false;
        } else if (qt_saisie == 0) {
            ed_quantite_.setError("Quantité saisie doit être different à 0");
            return false;
        } else {
            ed_quantite_.setError(null);
            ed_quantite_.setErrorEnabled(false);
            return true;
        }
    }

}
