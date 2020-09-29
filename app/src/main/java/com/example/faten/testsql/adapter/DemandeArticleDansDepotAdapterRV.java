package com.example.faten.testsql.adapter;


import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.R;
import com.example.faten.testsql.activity.BonCommandeVenteAvecTerminalActivity;
import com.example.faten.testsql.model.ReservationArticleDansDepot;

import java.sql.Connection;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by fatima on 20/01/2017.
 */
public class DemandeArticleDansDepotAdapterRV extends RecyclerView.Adapter<DemandeArticleDansDepotAdapterRV.ViewHolder> {


    private final Activity activity;
    private ArrayList<ReservationArticleDansDepot> listDemande = new ArrayList<>();
    int index_tab;
    public static String login;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final int REQUEST_PHONE_CALL = 1;

    public DemandeArticleDansDepotAdapterRV(Activity activity, ArrayList<ReservationArticleDansDepot> listDemande, int index_tab) {

        this.activity = activity;
        this.listDemande = listDemande;
        this.index_tab = index_tab;


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demande_reservation, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ReservationArticleDansDepot demandeArticle = listDemande.get(position);


        if (index_tab == 0) {
            holder.txt_depot.setText(demandeArticle.getDepotDemandant());
        } else if (index_tab == 1) {
            holder.txt_depot.setText(demandeArticle.getDepotDemendeur());
        }


        holder.txt_article.setText(demandeArticle.getCodeArticle());
        holder.txt_qt_article.setText(demandeArticle.getQuantite() + "");


        if (index_tab == 0) {

            holder.cb_valide.setEnabled(false);
            holder.cb_annuler.setEnabled(false);

        } else if (index_tab == 1) {



            holder.cb_valide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogValider(demandeArticle) ;
                }
            });



            holder.cb_annuler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showDialogAnnuler(demandeArticle);
                }
            });


        }


        if (demandeArticle.getValider() == 1) {
            holder.cb_valide.setChecked(true);
        } else {
            holder.cb_valide.setChecked(false);
        }


        if (demandeArticle.getAnnuler() == 1) {
            holder.cb_annuler.setImageResource(R.drawable.ic_cancel_rouge);

        } else {
            holder.cb_annuler.setImageResource(R.drawable.ic_cancel_gris);
        }


        holder.img_call_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (index_tab == 0) {
                    intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + demandeArticle.getTelPhoneDemandant()));
                } else {
                    intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + demandeArticle.getTelPhoneDemendeur()));
                }


                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);


                } else {

                    activity.startActivity(intent);

                }

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

        public CheckBox cb_valide;

        public ImageView cb_annuler;


        public ViewHolder(View itemView) {
            super(itemView);


            txt_depot = (TextView) itemView.findViewById(R.id.txt_depot);
            txt_article = (TextView) itemView.findViewById(R.id.txt_article);
            txt_qt_article = (TextView) itemView.findViewById(R.id.txt_qt_article);
            img_call_phone = (ImageView) itemView.findViewById(R.id.img_call_phone);
            cb_valide = (CheckBox) itemView.findViewById(R.id.cb_valide);
            cb_annuler = (ImageView) itemView.findViewById(R.id.cb_annuler);


        }


    }


    public void showDialogValider(ReservationArticleDansDepot demandeArticle) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                .setTitle("Voulez-Vous Valider cette  Réservation  ?")

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        demandeArticle.setValider(1);
                        demandeArticle.setAnnuler(0);


                        UpdateDemandeReserv  updateDemandeReserv  = new UpdateDemandeReserv(activity  ,demandeArticle) ;
                        updateDemandeReserv.execute();

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


    public void showDialogAnnuler(ReservationArticleDansDepot demandeArticle) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                .setTitle("Voulez-Vous Annuler cette  Réservation  ?")

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        demandeArticle.setAnnuler(1);
                        demandeArticle.setValider(0);

                        UpdateDemandeReserv  updateDemandeReserv  = new UpdateDemandeReserv(activity  ,demandeArticle) ;
                        updateDemandeReserv.execute();
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


    public class  UpdateDemandeReserv extends AsyncTask<String, String, String> {

        Activity activity ;
        ReservationArticleDansDepot   rservationToUpdate ;

        String res  = "" ;
        Boolean  isSuccess = false ;

        ConnectionClass connectionClass;
        String user, password, base, ip;


        public UpdateDemandeReserv(Activity activity, ReservationArticleDansDepot rservationToUpdate) {
            this.activity = activity;
            this.rservationToUpdate = rservationToUpdate;


            SharedPreferences prefe = activity.getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
            SharedPreferences.Editor edte = prefe.edit();
            user = prefe.getString("user", user);
            ip = prefe.getString("ip", ip);
            password = prefe.getString("password", password);
            base = prefe.getString("base", base);

            connectionClass = new ConnectionClass();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(activity  , "Modification en cours ...",Toast.LENGTH_SHORT).show(); ;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    res = "Error in connection with SQL server";
                } else {

                        String query_reserv = "   update ReservationArticleDansDepot  set  Valider  =    " + rservationToUpdate.getValider() + " ,  Annuler  = " + rservationToUpdate.getAnnuler() + "  \n" +
                                " where CodeDepotDemandant   = '" + rservationToUpdate.getCodeDepotDemandant() + "'  and NumeroBonCommandeVente  = '" + rservationToUpdate.getNumeroBonCommandeVente() + "'   and CodeArticle = '" + rservationToUpdate.getCodeArticle() + "' ";

                    String queryUpate = " " +
                            " BEGIN TRANSACTION update_reserv  \n" +
                            " \n" +
                            "DECLARE @error INT  \n" +
                            "DECLARE @ID_INSERTION NUMERIC(19,0)  \n" +
                            " \n" +
                            " SET @error = 0\n" +
                            "\n"
                            + query_reserv
                            + "   SET @error = @error + @@error  \n  "
                            + "  IF @error = 0  \n" +
                            "    COMMIT TRANSACTION update_reserv  \n" +
                            "\t  ELSE \n" +
                            "    ROLLBACK TRANSACTION update_reserv   ";


                    Log.e("query_UPDATE_reserv", queryUpate);
                    Statement stmt2 = con.createStatement();
                    stmt2.executeUpdate(queryUpate);

                    isSuccess = true;


                   res = "Success";
                }
            } catch (Exception ex) {
                res = "Error " + ex.getMessage().toString();
                isSuccess = false;
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            notifyDataSetChanged();
            Toast.makeText(activity  , "Modifié ...",Toast.LENGTH_SHORT).show();

        }


    }
}
