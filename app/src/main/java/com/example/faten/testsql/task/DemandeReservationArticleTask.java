package com.example.faten.testsql.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.adapter.ClientSelectAdapterRV;
import com.example.faten.testsql.adapter.DemandeArticleDansDepotAdapter;
import com.example.faten.testsql.adapter.DemandeArticleDansDepotAdapterRV;
import com.example.faten.testsql.model.Client;
import com.example.faten.testsql.model.ReservationArticleDansDepot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DemandeReservationArticleTask extends AsyncTask<String, String, String> {


    Activity activity  ;

    RecyclerView rv_list_demande  ;
    SearchView  search_demande  ;
    ProgressBar  pb ;
    int   index_tab;
    Date  date_debut  , date_fin  ;



    String z = "";
    ConnectionClass connectionClass;
    String user, password, base, ip;
    ArrayList<ReservationArticleDansDepot> listreservation = new ArrayList<>() ;


    String  CodeDepot ;

    SimpleDateFormat  sdf  =  new SimpleDateFormat("yyyy-MM-dd") ;

    public DemandeReservationArticleTask ( Activity activity, RecyclerView rv_list_demande,   SearchView  search_demande  , ProgressBar pb, int index_tab, Date date_debut, Date date_fin) {
        this.activity = activity;
        this.rv_list_demande = rv_list_demande;
        this.search_demande=search_demande ;
        this.pb = pb;
        this.index_tab = index_tab;
        this.date_debut = date_debut;
        this.date_fin = date_fin;


        SharedPreferences prefe = activity.getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        user     = prefe.getString("user", user);
        ip       = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base     = prefe.getString("base", base);

        connectionClass = new ConnectionClass();


        SharedPreferences prefUser = activity.getSharedPreferences("usersession", Context.MODE_PRIVATE);
        CodeDepot = prefUser.getString("CodeDepot", CodeDepot);

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
                String  queryReservation = "select \n" +
                        "ReservationArticleDansDepot.NumeroBonCommandeVente  , CodeArticle  , Designation   " +
                        "    \n" +
                        "        , CodeDepotDemendeur  \n" +
                        "        ,(select  Libelle  from Depot  where CodeDepot  = CodeDepotDemendeur ) as  DepotDemandeur  \n" +
                        "        ,case when CodeDepotDemendeur= '01'  then  (select  Tel2 from Societe   ) \n" +
                        "         else \n" +
                        "        (select Tel1 from Livreur where  CodeDepot  = CodeDepotDemendeur ) end   as TelPhoneDemendeur          \n" +
                        "        \n" +
                        "        \n" +
                        "        , CodeDepotDemandant   \n" +
                        "        ,(select  Libelle  from Depot  where CodeDepot  = CodeDepotDemandant ) as  DepotDemandant  \n" +
                        "        ,case when CodeDepotDemandant= '01'  then  (select  Tel2 from Societe   ) \n" +
                        "         else \n" +
                        "        (select Tel1 from Livreur where  CodeDepot  = CodeDepotDemandant ) end   as TelPhoneDemandant \n" +


                        "    ,Quantite  ,Valider  , Annuler ,Cloturer , telephone \n" +
                        " \n" +
                        "from  ReservationArticleDansDepot \n" +
                        "inner join BonCommandeVente on BonCommandeVente.NumeroBonCommandeVente  = ReservationArticleDansDepot.NumeroBonCommandeVente\n" +
                        "where CONVERT (Date , DateBonCommandeVente)  between  '"+sdf.format(date_debut)+"' and '"+sdf .format(date_fin)+"' \n" +
                        " -- and   CodeDepotDemendeur  <> CodeDepotDemandant\n" ;



                if (index_tab ==0)


                   queryReservation  = queryReservation +
                        "and CodeDepotDemendeur  = '"+CodeDepot+"'\n ";

                else   if ( index_tab ==1 )


                    queryReservation  = queryReservation +    " and   CodeDepotDemendeur  <> CodeDepotDemandant  " +
                                                              "\n  and CodeDepotDemandant  = '"+CodeDepot+"'\n ";


                Log.e("queryReservation",""+queryReservation) ;
                PreparedStatement ps = con.prepareStatement(queryReservation);
                ResultSet rs = ps.executeQuery();


                while (rs.next()) {

                    String NumeroBonCommandeVente = rs.getString("NumeroBonCommandeVente") ;
                    String CodeArticle = rs.getString("CodeArticle") ;
                    String Designation = rs.getString("Designation") ;

                    String  CodeDepotDemendeur = rs.getString("CodeDepotDemendeur") ;
                    String  DepotDemandeur    = rs.getString("DepotDemandeur") ;
                    String  TelPhoneDemendeur    = rs.getString("TelPhoneDemendeur") ;

                    String  CodeDepotDemandant = rs.getString("CodeDepotDemandant") ;
                    String  DepotDemandant = rs.getString("DepotDemandant") ;
                    String  TelPhoneDemandant    = rs.getString("TelPhoneDemandant") ;

                    int  Quantite = rs.getInt("Quantite") ;
                    int Valider = rs.getInt("Valider") ;
                    int telephone = rs.getInt("telephone") ;
                    int Annuler = rs.getInt("Annuler") ;
                    int Cloturer = rs.getInt("Cloturer") ;

                    ReservationArticleDansDepot   reservationArticleDansDepot  = new ReservationArticleDansDepot(NumeroBonCommandeVente, CodeArticle, Designation,CodeDepotDemendeur ,DepotDemandeur,TelPhoneDemendeur ,CodeDepotDemandant ,DepotDemandant,TelPhoneDemandant, Quantite ,Valider ,Annuler , Cloturer ) ;
                    listreservation.add(reservationArticleDansDepot);

                }


                z = "Success";
            }
        } catch (Exception ex) {
            z = "Error retrieving data from table";

            Log.e("ERROR",""+ex.getMessage().toString()) ;
        }
        return z;
    }



    @Override
    protected void onPostExecute(String r) {

        pb.setVisibility(View.INVISIBLE);

        DemandeArticleDansDepotAdapterRV demandeArticleDansDepotAdapter  = new DemandeArticleDansDepotAdapterRV(activity  , listreservation,index_tab);
        rv_list_demande.setAdapter(demandeArticleDansDepotAdapter);


        search_demande.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(!search_demande.isIconified())
                {
                    search_demande.setIconified(true);
                }
                return  false ;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                final  ArrayList<ReservationArticleDansDepot>   fitlerReservation  = filterClientDivers  (listreservation , query) ;
                DemandeArticleDansDepotAdapterRV demandeArticleDansDepotAdapter  = new DemandeArticleDansDepotAdapterRV(activity  , fitlerReservation, index_tab);
                rv_list_demande.setAdapter(demandeArticleDansDepotAdapter);


                return false;
            }
        });

    }

    private ArrayList<ReservationArticleDansDepot> filterClientDivers (ArrayList<ReservationArticleDansDepot>  listreservation  , String term )  {

        term = term.toLowerCase() ;
        final ArrayList<ReservationArticleDansDepot> filetrListReserv  = new ArrayList<>() ;

        for (ReservationArticleDansDepot r : listreservation)
        {
            final  String  txtCodeArticle =  r.getCodeArticle().toLowerCase()  ;
            final  String  txtDepotDemandant =  r.getDepotDemandant().toLowerCase()  ;

            if ( txtCodeArticle.contains(term)   ||  txtDepotDemandant.contains(term))
            {
                filetrListReserv.add(r) ;
            }
        }
        return  filetrListReserv ;

    }



}