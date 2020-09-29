package com.example.faten.testsql.dilaog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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

import com.example.faten.testsql.R ;
import com.example.faten.testsql.adapter.DemandeArticleNonValideAdapterRV;
import com.example.faten.testsql.model.ReservationArticleDansDepot;
import com.example.faten.testsql.task.ListDemandeNonValideTask;
import com.example.faten.testsql.task.UpdateDemandeNNValideTask;

import java.util.ArrayList;


public class DialogDemandeNonValide extends DialogFragment {

    Activity activity ;

    ProgressBar progressBar;
    SearchView search_v_article;
    String origine;

    public   static  RecyclerView rv_list_demande;
    public   static  ArrayList<ReservationArticleDansDepot>  listReservNonValide ;
    public   static  DemandeArticleNonValideAdapterRV demandeArticleNonValideAdapterRV ;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    int click  = 0 ;
    CheckBox  cb_select_tt  ;
    ImageView  btn_cancel_tt  ;


    Button   btn_valider  ;
    TextView  txt_nn_valide  ;
    ProgressBar  pb_valide   ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_demande_non_valide, container);

        rv_list_demande = (RecyclerView) rootView.findViewById(R.id.rv_list_demande);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_chargement);
        search_v_article = (SearchView) rootView.findViewById(R.id.search_v_article);
        cb_select_tt = (CheckBox)  rootView.findViewById(R.id.cb_select_tout_demande)  ;
        btn_cancel_tt  = (ImageView)  rootView.findViewById(R.id.btn_cancel_tout)  ;

        btn_valider = (Button)  rootView.findViewById(R.id.btn_valider) ;
        txt_nn_valide = (TextView)  rootView.findViewById(R.id.txt_non_valide)  ;
        pb_valide  =  (ProgressBar)  rootView.findViewById(R.id.pb_valider)  ;


        pb_valide.setVisibility(View.GONE);
        txt_nn_valide.setVisibility(View.INVISIBLE);

        rv_list_demande.setHasFixedSize(true);
        rv_list_demande.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar.setVisibility(View.INVISIBLE);

        setCancelable(false);

        listReservNonValide = new ArrayList<>() ;
        demandeArticleNonValideAdapterRV  = new DemandeArticleNonValideAdapterRV(getActivity()  , listReservNonValide)  ;
        rv_list_demande.setAdapter(demandeArticleNonValideAdapterRV);


        ListDemandeNonValideTask  listDemandeNonValideTask = new ListDemandeNonValideTask(getActivity()  , rv_list_demande,progressBar , search_v_article ) ;
        listDemandeNonValideTask.execute() ;


        cb_select_tt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    for (ReservationArticleDansDepot  reserv  : listReservNonValide)
                    {
                        reserv.setValider(1);
                        reserv.setAnnuler(0);
                    }
                    btn_cancel_tt.setImageResource(R.drawable.ic_cancel_gris);
                    click =0 ;

                }
                else {

                    for (ReservationArticleDansDepot  reserv  : listReservNonValide)
                    {
                        reserv.setValider(0);
                    }
                }


                demandeArticleNonValideAdapterRV.notifyDataSetChanged();

            }
        });





        btn_cancel_tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (click == 0 )
                {

                    for (ReservationArticleDansDepot  reserv  : listReservNonValide)
                    {
                        reserv.setValider(0);
                        reserv.setAnnuler(1);
                    }
                    click = 1 ;

                    cb_select_tt.setChecked(false);
                    btn_cancel_tt.setImageResource(R.drawable.ic_cancel_rouge);
                }
                else if (click == 1)
                {

                    for (ReservationArticleDansDepot  reserv  : listReservNonValide)
                    {

                        reserv.setAnnuler(0);
                    }
                    click = 0 ;
                    btn_cancel_tt.setImageResource(R.drawable.ic_cancel_gris);
                }

                demandeArticleNonValideAdapterRV.notifyDataSetChanged();

            }
        });




        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean  list_valide  = true  ;
                for (ReservationArticleDansDepot  reser  : listReservNonValide)
                {

                    if (reser.getValider() == 0   &&  reser.getAnnuler() == 0)
                    {

                        list_valide = false ;
                        break;

                    }
                }


                Log.e("List_valide "  ,""+list_valide) ;


                if (list_valide )
                {
                    txt_nn_valide.setVisibility(View.INVISIBLE);
                    pb_valide.setVisibility(View.VISIBLE);

                    UpdateDemandeNNValideTask  updateDemandeNNValideTask  = new UpdateDemandeNNValideTask(getActivity()  ,DialogDemandeNonValide.this  ,listReservNonValide ,pb_valide, btn_valider) ;
                    updateDemandeNNValideTask.execute() ;


                }
                else {


                    Toast.makeText(getActivity() , "" ,Toast.LENGTH_LONG).show();
                    txt_nn_valide.setVisibility(View.VISIBLE);
                }

            }
        });

        return rootView;
    }



}
