package com.example.faten.testsql.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faten.testsql.R;
import com.example.faten.testsql.activity.BonCommandeTerminalAutomatiqueActivity;
import com.example.faten.testsql.activity.BonCommandeVenteAvecTerminalActivity;
import com.example.faten.testsql.adapter.ArticleQtDispoStockAdapterRVDialog;
import com.example.faten.testsql.adapter.ArticleQtDispoStockAdapterRVDialogView;
import com.example.faten.testsql.adapter.DemandeArticleNonValideAdapterRV;
import com.example.faten.testsql.adapter.DemandeArticleViewAdapterRV;
import com.example.faten.testsql.model.Article;
import com.example.faten.testsql.model.ArticleStock;
import com.example.faten.testsql.model.BonCommandeVente;
import com.example.faten.testsql.model.ReservationArticleDansDepot;
import com.example.faten.testsql.task.ListDemandeNonValideTask;
import com.example.faten.testsql.task.UpdateDemandeNNValideTask;

import java.util.ArrayList;


public class DialogReservationDansAutreDepot extends DialogFragment {

    Activity activity ;

    public   static  RecyclerView rv_list_article_dans_depot ;

    public   static  ArrayList<ReservationArticleDansDepot>  listReservations  ;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    Button btn_valider ;

    TextView  txt_code_article  , txt_qt_stock  , txt_qt_cmd  , txt_qt_dispo  ;


    ArticleStock  article ;
    String  CodeArticle ;


    int QTDispo =0 ;


    public void setCodeArticle(String codeArticle) {
        CodeArticle = codeArticle;
    }


    public void setArticle(ArticleStock article) {
        this.article = article;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_reservation_dans_autre_depot, container);

        rv_list_article_dans_depot  = (RecyclerView) rootView.findViewById(R.id.rv_list_article_dans_depot) ;
        rv_list_article_dans_depot.setHasFixedSize(true);
        rv_list_article_dans_depot.setLayoutManager(new LinearLayoutManager(getActivity()));

        txt_code_article    = (TextView)  rootView.findViewById(R.id.txt_code_article) ;
        txt_qt_stock        = (TextView)  rootView.findViewById(R.id.txt_qt_stock) ;
        txt_qt_cmd          = (TextView)  rootView.findViewById(R.id.txt_qt_commnder) ;
        txt_qt_dispo        = (TextView)  rootView.findViewById(R.id.txt_qt_dispo) ;


        //  filtrage des  article en cours

        ArrayList<ReservationArticleDansDepot> listReservationArticle = new ArrayList<>();


        try {
            Log.e(  "list_article_dispo", "" + BonCommandeTerminalAutomatiqueActivity.listReservation.toString()  ) ;
            for (ReservationArticleDansDepot  as  :BonCommandeTerminalAutomatiqueActivity.listReservation )
            {
                if (  as.getCodeArticle().equals(CodeArticle)  )
                {
                    listReservationArticle.add(as) ;
                }
            }
        }
        catch (Exception ex)
        {

        }


        try {
            Log.e(  "list_article_dispo", "" + BonCommandeVenteAvecTerminalActivity.listReservation.toString()  ) ;
            for (ReservationArticleDansDepot  as  :BonCommandeVenteAvecTerminalActivity.listReservation )
            {
                if (  as.getCodeArticle().equals(CodeArticle)  )
                {
                    listReservationArticle.add(as) ;
                }
            }
        }
        catch (Exception ex)
        {
        }



        Log.e("list_article_dispo","" + listReservationArticle.toString () ) ;

        txt_code_article.setText ( listReservationArticle.get(0) . getCodeArticle() );

        QTDispo =   listReservationArticle.get(0).getQTStock() - listReservationArticle.get(0).getQtCMD() ;

        txt_qt_cmd.setText(listReservationArticle.get(0).getQtCMD()+"");
        txt_qt_stock.setText(listReservationArticle.get(0).getQTStock()+"");
        txt_qt_dispo.setText(QTDispo+"");


        DemandeArticleViewAdapterRV adapter  =  new DemandeArticleViewAdapterRV(activity  , listReservationArticle );
        rv_list_article_dans_depot.setAdapter(adapter);

        return rootView;

    }


}
