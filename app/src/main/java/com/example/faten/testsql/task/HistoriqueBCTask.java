package com.example.faten.testsql.task;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.activity.HistoriqueBonCommande;
import com.example.faten.testsql.R;
import com.example.faten.testsql.activity.HistoriqueLigneBonCommandeActivity;
import com.example.faten.testsql.adapter.BonCommandeAdapter;
import com.example.faten.testsql.model.BonCommandeVente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoriqueBCTask extends AsyncTask<String, String, String> {


    Activity activity;

    ListView lv_hist_bc;
    ProgressBar pb;


    String z = "";
    ConnectionClass connectionClass;
    String user, password, base, ip;

    String NomUtilisateur ;
    DateFormat dtfSQL    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


     ArrayList<BonCommandeVente> listBonCommandeVente = new ArrayList<>() ;

    public HistoriqueBCTask(Activity activity, ListView lv_hist_bc, ProgressBar pb) {
        this.activity = activity;
        this.lv_hist_bc = lv_hist_bc;
        this.pb = pb;


        SharedPreferences prefe = activity.getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        user = prefe.getString("user", user);
        ip = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base = prefe.getString("base", base);



        SharedPreferences pref=activity.getSharedPreferences("usersession", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt=pref.edit();
        NomUtilisateur= pref.getString("NomUtilisateur",NomUtilisateur);


        connectionClass = new ConnectionClass();

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            Connection con = connectionClass.CONN(ip, password, user, base);
            if (con == null) {
                z = "Error in connection with SQL server";
            } else {

                String queryClient = "select *,client.RaisonSociale from BonCommandeVente " +
                        "  INNER JOIN Client  on Client.CodeClient=BonCommandeVente.CodeClient " +
                        "  where  " +
                        "  NomUtilisateur='"+NomUtilisateur+"' " +
                        " order by DateBonCommandeVente desc  ";

                PreparedStatement ps = con.prepareStatement(queryClient);
                ResultSet rs = ps.executeQuery();



                while (rs.next()) {

                    String NumeroBonCommandeVente = rs.getString("NumeroBonCommandeVente");
                    String RaisonSociale = rs.getString("RaisonSociale");
                    double TotalTTC = rs.getDouble("TotalTTC");
                    Date DateBonCommandeVente = dtfSQL.parse(rs.getString("DateBonCommandeVente"));
                    String NumeroEtat = rs.getString("NumeroEtat");


                    BonCommandeVente bonCommandeVente  = new BonCommandeVente(NumeroBonCommandeVente,DateBonCommandeVente ,RaisonSociale ,TotalTTC,NumeroEtat) ;
                    listBonCommandeVente.add(bonCommandeVente)  ;


                }


                z = "Success";
            }
        } catch (Exception ex) {
            z = "Error retrieving data from table";

        }
        return z;
    }


    @Override
    protected void onPostExecute(String r) {

        pb.setVisibility(View.INVISIBLE);


        HistoriqueBonCommande.   bcAdapter = new BonCommandeAdapter(activity, listBonCommandeVente);
        lv_hist_bc.setAdapter(  HistoriqueBonCommande.bcAdapter );


        lv_hist_bc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent , View view , int position, long id) {

                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                NumberFormat formatter = new DecimalFormat("0.000");

                BonCommandeVente bonCommandeVente = listBonCommandeVente.get(position);

                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setIcon(R.drawable.i2s);
                alert.setTitle("Bon De Commande");
                alert.setMessage("Client : " + bonCommandeVente.getReferenceClient());


                alert.setNegativeButton("Détail",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface di, int i) {
                                //di.cancel();

                                SimpleDateFormat  sdf  = new SimpleDateFormat("dd/MM/yyyy") ;
                                Intent  toLigneBonCommande  = new Intent(activity  , HistoriqueLigneBonCommandeActivity.class);
                                toLigneBonCommande.putExtra("cle_numero_bon_cmd_vente",bonCommandeVente.getNumeroBonCommandeVente()) ;
                                toLigneBonCommande.putExtra("cle_raison_sociale",bonCommandeVente.getReferenceClient()) ;
                                toLigneBonCommande.putExtra("cle_total_ttc",bonCommandeVente.getTotalTTC()) ;
                                toLigneBonCommande.putExtra("cle_date_bc",  sdf.format( bonCommandeVente.getDateBonCommandeVente())) ;
                                activity.startActivity(toLigneBonCommande);


                            }
                        });


                if (bonCommandeVente.getNumeroEtat().equals("E00")) {

                    alert.setNeutralButton("Annulé",null) ;

                } else {

                    alert.setNeutralButton("Annuler",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface di, int i) {

                                    //di.cancel();

                                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(activity)

                                            .setIcon(R.drawable.ic_annulation_bl)

                                            .setTitle("Annulation")

                                            .setMessage("êtes-vous sûr d'annuler ce Bon de livraison ?")

                                            .setPositiveButton("oui", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    AnnulationBCTask annulationBLTask = new AnnulationBCTask(activity, bonCommandeVente);
                                                    annulationBLTask.execute();

                                                }
                                            })

                                            .setNegativeButton("non", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    //set what should happen when negative button is clicked

                                                }
                                            })
                                            .show();


                                }
                            });


                    AlertDialog dd = alert.create();

                    dd.show();


                }
            }
        });


    }



    public class AnnulationBCTask extends AsyncTask<String, String, String> {

        Connection con;
        String res;

        Activity activity;

        BonCommandeVente bonCommandeVente;



        Boolean isSuccess = false;


        ConnectionClass connectionClass;
        String user, password, base, ip;

        String NomUtilisateur;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        public AnnulationBCTask(Activity activity,   BonCommandeVente bonCommandeVente ) {
            this.activity = activity;
            this.bonCommandeVente = bonCommandeVente;



            SharedPreferences pref = activity.getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
            //SharedPreferences.Editor edt = pref.edit();
            user = pref.getString("user", user);
            ip = pref.getString("ip", ip);
            password = pref.getString("password", password);
            base = pref.getString("base", base);
            connectionClass = new ConnectionClass();


            SharedPreferences prefUser = activity.getSharedPreferences("usersession", Context.MODE_PRIVATE);
            NomUtilisateur = prefUser.getString("NomUtilisateur", NomUtilisateur);


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            Toast.makeText(activity, "Annulation en cours ", Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                Connection con = connectionClass.CONN( ip, password, user, base );

                Log.e("con", "" + con );
                if (con == null) {
                    res = "Check Your Internet Access!";
                } else {

                    String NoteInternete = "0 Annuler à cause de: faute de livraison (Android) "; // (Android)

                    String queryUpdate = " update BonCommandeVente  set NumeroEtat =  'E00'    where NumeroBonCommandeVente = '" + bonCommandeVente.getNumeroBonCommandeVente() + "'  and NumeroEtat !=  'E00'   \n   ";

                    String queryUpdateReserv = " update ReservationArticleDansDepot  set Annuler =  1   ,  Valider = 0   where  NumeroBonCommandeVente  = '"+bonCommandeVente.getNumeroBonCommandeVente()+"'   \n   ";


                    queryUpdate =

                            " BEGIN TRANSACTION annulation_bc \n" +
                                    " \n" +
                                    "DECLARE @error INT   \n" +
                                    "DECLARE @ID_INSERTION NUMERIC(19,0)  \n" +
                                    " \n" +
                                    " SET @error = 0\n" +
                                    "  \n" +
                                    "   IF EXISTS (\n" +
                                    "       select * from BonCommandeVente where NumeroBonCommandeVente = '" + bonCommandeVente.getNumeroBonCommandeVente() + "'  and NumeroEtat <>  'E00' )\n" +
                                    "  \n" +
                                    "       begin  \n  " +
                                    queryUpdate
                                    + "   SET @error = @error + @@error  \n "+
                                    queryUpdateReserv
                                   + "   SET @error = @error + @@error  \n "

                                    + "   end  \n   " +
                                    "    IF @error = 0  \n" +
                                    "    COMMIT TRANSACTION annulation_bc  \n" +
                                    "     ELSE   \n" +
                                    "    ROLLBACK TRANSACTION annulation_bc   ";


                    Log.e("query_annul_bl", queryUpdate);
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(queryUpdate);

                    isSuccess = true;

                }
                con.close();
            } catch (Exception ex) {

                isSuccess = false;
                res = ex.getMessage();
                Log.e("ERROR_annul_bl", res.toString());

            }
            return res;

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            if (isSuccess) {

                bonCommandeVente.setNumeroEtat("E00");
                HistoriqueBonCommande. bcAdapter. notifyDataSetChanged();

                Toast.makeText(activity, "Annulation effectué avec success ", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(activity, "Problème d'Annulation Bon Livraison \n" + s, Toast.LENGTH_LONG).show();
            }

        }


    }
}