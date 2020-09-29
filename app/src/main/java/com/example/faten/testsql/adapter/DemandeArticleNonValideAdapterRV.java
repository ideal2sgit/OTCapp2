package com.example.faten.testsql.adapter;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.faten.testsql.R;
import com.example.faten.testsql.model.ReservationArticleDansDepot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by fatima on 20/01/2017 .
 */
public class DemandeArticleNonValideAdapterRV extends RecyclerView.Adapter<DemandeArticleNonValideAdapterRV.ViewHolder> {


    private final Activity activity;
    private ArrayList<ReservationArticleDansDepot> listDemande = new ArrayList<>();
    public static String login;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final int REQUEST_PHONE_CALL = 1;

    public DemandeArticleNonValideAdapterRV(Activity activity, ArrayList<ReservationArticleDansDepot> listDemande) {

        this.activity = activity;
        this.listDemande = listDemande;

    }


    @Override
    public ViewHolder onCreateViewHolder ( ViewGroup parent, int viewType ) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demande_reservation_non_valide, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ReservationArticleDansDepot demandeArticle = listDemande.get(position);

        holder.txt_depot.setText(demandeArticle.getDepotDemendeur());
        holder.txt_article.setText(demandeArticle.getCodeArticle());
        holder.txt_qt_article.setText(demandeArticle.getQuantite() + "");

        if (demandeArticle.getValider() == 1) {
            holder.cb_valide.setChecked(true);
        } else {
            holder.cb_valide.setChecked(false);
        }


        if (demandeArticle.getAnnuler() == 1) {
            holder.btn_cancel.setImageResource(R.drawable.ic_cancel_rouge);

        } else {
            holder.btn_cancel.setImageResource(R.drawable.ic_cancel_gris);
        }



        holder.img_call_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + demandeArticle.getTelPhoneDemendeur()));

                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);

                } else {
                    activity.startActivity(intent);
                }

            }
        });


        holder.cb_valide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {

                    demandeArticle.setValider(1);
                    demandeArticle.setAnnuler(0);
                    holder.btn_cancel.setImageResource(R.drawable.ic_cancel_gris);

                } else if (!isChecked) {
                    demandeArticle.setValider(0);

                }



            }
        });


        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int is_cancel = demandeArticle.getAnnuler();

                if (is_cancel== 1)
                {
                    demandeArticle.setAnnuler(0);
                    holder.btn_cancel.setImageResource(R.drawable.ic_cancel_gris);
                }
                else {

                    demandeArticle.setAnnuler(1);
                    holder.btn_cancel.setImageResource(R.drawable.ic_cancel_rouge);

                    demandeArticle.setValider(0);
                    holder.cb_valide.setChecked(false);

                }

               // notifyDataSetChanged();

            }
        });


    }


    @Override
    public int getItemCount() {

        Log.e("size", "" + listDemande.size());
        return listDemande.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView txt_depot;
        public TextView txt_article;
        public TextView txt_qt_article;
        public ImageView img_call_phone;

        // public ImageView img_valide ;
        public CheckBox cb_valide;
        public ImageView btn_cancel;


        public ViewHolder(View itemView) {
            super(itemView);


            txt_depot = (TextView) itemView.findViewById(R.id.txt_depot);
            txt_article = (TextView) itemView.findViewById(R.id.txt_article);
            txt_qt_article = (TextView) itemView.findViewById(R.id.txt_qt_article);
            img_call_phone = (ImageView) itemView.findViewById(R.id.img_call_phone);

            cb_valide = (CheckBox) itemView.findViewById(R.id.cb_select_demande);
            btn_cancel = (ImageView) itemView.findViewById(R.id.btn_cancel);


        }


    }

}
