package com.example.faten.testsql.dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.faten.testsql.R;
import com.example.faten.testsql.activity.BonCommandeVenteAvecTerminalActivity;
import com.example.faten.testsql.activity.LoginActivity;
import com.example.faten.testsql.model.ArticleDefectueuseDansValise;
import com.example.faten.testsql.model.ArticleStock;
import com.example.faten.testsql.task.InsertArticleDefectueueseTask;
import com.example.faten.testsql.task.ListCauseForSpinnerTask;


public class DialogCauseDeNonConfirmation extends DialogFragment {

    TextView  txt_code_article  ;

    Spinner sp_cause ;
    TextView txt_error ;
    //TextInputLayout ed_cause;
    Button btn_valider ;
    ProgressBar  pb_valide_cause  ;

    ArticleStock  articleStock  ;

    public  static   String  CodeCauseSelected ="";
    public  static   String  LibelleCauseSelected=""  ;

    String  NomUtilisateur ;

    public void setArticleStock(ArticleStock articleStock) {
        this.articleStock = articleStock;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_cause_nn_confirmite, container);

        txt_code_article = rootView.findViewById(R.id.txt_code_article) ;
        sp_cause = rootView.findViewById(R.id.sp_cause) ;
        txt_error= rootView.findViewById(R.id.txt_error) ;
       // ed_cause = rootView.findViewById(R.id.ed_cause);
        btn_valider = rootView.findViewById(R.id.btn_valider);
        pb_valide_cause = rootView.findViewById(R.id.pb_valider);
        txt_error.setVisibility(View.GONE);


        pb_valide_cause.setVisibility(View.GONE);
        txt_code_article .setText(articleStock.getCodeArticle());


        SharedPreferences pref_user = getActivity().getSharedPreferences("usersession", Context.MODE_PRIVATE);

        NomUtilisateur = pref_user.getString("NomUtilisateur", NomUtilisateur) ;



        ListCauseForSpinnerTask listCauseForSpinnerTask  = new ListCauseForSpinnerTask(getActivity() , sp_cause) ;
        listCauseForSpinnerTask.execute() ;

        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!valideCause()) {
                    return;
                }

                //String  _cause  = ed_cause.getEditText().getText().toString() ;

                ArticleDefectueuseDansValise  articleDefectueuse = new ArticleDefectueuseDansValise("bc_"+NomUtilisateur,articleStock.getCodeArticle() ,articleStock.getCodeDepot() ,articleStock.getDesignation() ,
                        1,CodeCauseSelected,LibelleCauseSelected ,0,0,0);


                InsertArticleDefectueueseTask insertArticleDefectueueseTask  = new InsertArticleDefectueueseTask(getActivity() ,DialogCauseDeNonConfirmation.this ,articleDefectueuse ,btn_valider  ,pb_valide_cause ) ;
                insertArticleDefectueueseTask.execute() ;


            }
        });


        return rootView;
    }


    private Boolean valideCause() {
        String val = CodeCauseSelected +LibelleCauseSelected  ;


        if (val.isEmpty()  ) {

            txt_error.setVisibility(View.VISIBLE);

            //ed_cause.setError("la cause ne peut pas être vide");

            return false;
        } else {

            txt_error.setVisibility(View.GONE);
            //ed_cause.setError(null);
            //ed_cause.setErrorEnabled(false);
            return true;
        }

    }

}
