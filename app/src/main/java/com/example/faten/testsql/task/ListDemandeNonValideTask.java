package com.example.faten.testsql.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.adapter.ClientSelectAdapterRV;
import com.example.faten.testsql.adapter.DemandeArticleNonValideAdapterRV;
import com.example.faten.testsql.dilaog.DialogDemandeNonValide;
import com.example.faten.testsql.model.Client;
import com.example.faten.testsql.model.ReservationArticleDansDepot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListDemandeNonValideTask extends AsyncTask<String, String, String> {


    Activity activity  ;
    RecyclerView  rv_listDemande ;
    ProgressBar  pb ;
    SearchView  searchViewArticle ;

    String  CodeDepot  ;

    String z = "";
    ConnectionClass connectionClass;
    String user, password, base, ip;
    //ArrayList<ReservationArticleDansDepot> listdemande  = new ArrayList<>() ;
    SimpleDateFormat sdf  = new SimpleDateFormat("dd/MM/yyyy") ;

    public ListDemandeNonValideTask(Activity activity, RecyclerView rv_listDemande, ProgressBar pb, SearchView  searchViewArticle ) {
        this.activity = activity;
        this.rv_listDemande = rv_listDemande ;
        this.pb = pb;
        this.searchViewArticle = searchViewArticle  ;


        SharedPreferences prefe = activity.getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        user     = prefe.getString("user", user);
        ip       = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base     = prefe.getString("base", base);

        connectionClass = new ConnectionClass();

        SharedPreferences prefe_user = activity.getSharedPreferences("usersession", Context.MODE_PRIVATE);
        CodeDepot      = prefe_user.getString("CodeDepot", CodeDepot);


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
                z = " Error in connection with SQL server ";
            } else {

                String  queryReservation = " \n" +

                        "    select \n" +
                        "    ReservationArticleDansDepot.NumeroBonCommandeVente  , CodeArticle  , Designation       \n" +
                        "   , CodeDepotDemendeur  \n" +
                        "   ,(select  Libelle  from Depot  where CodeDepot  = CodeDepotDemendeur ) as  DepotDemandeur  \n" +
                        "   ,case when CodeDepotDemendeur= '01'  then  (select  Tel2 from Societe   ) \n" +
                        "    else \n" +
                        "   (select Tel1 from Livreur where  CodeDepot  = CodeDepotDemendeur ) end   as TelPhoneDemendeur   \n" +
                        "    \n" +
                        "    \n" +
                        "    , CodeDepotDemandant   \n" +
                        "    ,(select  Libelle  from Depot  where CodeDepot  = CodeDepotDemandant ) as  DepotDemandant  \n" +
                        "    ,case when CodeDepotDemandant= '01'  then  (select  Tel2 from Societe   ) \n" +
                        "     else \n" +
                        "    (select Tel1 from Livreur where  CodeDepot  = CodeDepotDemandant ) end   as TelPhoneDemandant \n" +
                        "    ,Quantite  ,Valider  ,   Annuler ,Cloturer  ,  telephone\n" +
                        " " +
                        "    from ReservationArticleDansDepot \n" +
                        "    \n" +
                        "inner join  BonCommandeVente  on  BonCommandeVente.NumeroBonCommandeVente  =  ReservationArticleDansDepot.NumeroBonCommandeVente\n" +
                        "where CONVERT (    date ,BonCommandeVente.DateBonCommandeVente )  < '"+sdf.format(new Date())+"'\n" +
                        "and CodeDepotDemandant  = '"+CodeDepot+"'  --and  CodeDepotDemandant  <>  CodeDepotDemendeur\n" +
                        "and Valider=0  and Annuler  =0   ";


                Log.e("queryReservation",queryReservation) ;

                PreparedStatement ps = con.prepareStatement(queryReservation);
                ResultSet rs = ps.executeQuery();

                DialogDemandeNonValide.listReservNonValide.clear();
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

                    int Annuler = rs.getInt("Annuler") ;
                    int Cloturer = rs.getInt("Cloturer") ;
                    int telephone = rs.getInt("telephone") ;


                    ReservationArticleDansDepot   reservationArticleDansDepot  = new ReservationArticleDansDepot(NumeroBonCommandeVente, CodeArticle, Designation,CodeDepotDemendeur ,DepotDemandeur,TelPhoneDemendeur ,CodeDepotDemandant ,DepotDemandant,TelPhoneDemandant, Quantite ,Valider,Annuler ,Cloturer  ) ;
                    DialogDemandeNonValide.listReservNonValide.add(reservationArticleDansDepot);
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

        DialogDemandeNonValide.demandeArticleNonValideAdapterRV  = new DemandeArticleNonValideAdapterRV(activity  , DialogDemandeNonValide.listReservNonValide);
        DialogDemandeNonValide.rv_list_demande.setAdapter(  DialogDemandeNonValide.demandeArticleNonValideAdapterRV);


        searchViewArticle.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(!searchViewArticle.isIconified())
                {
                    searchViewArticle.setIconified(true);
                }
                return  false ;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                final  ArrayList<ReservationArticleDansDepot>   fitlerReservationList = filterReservation  ( DialogDemandeNonValide.listReservNonValide , query) ;



                DialogDemandeNonValide.demandeArticleNonValideAdapterRV  = new DemandeArticleNonValideAdapterRV(activity  ,fitlerReservationList);
                DialogDemandeNonValide.rv_list_demande.setAdapter(  DialogDemandeNonValide.demandeArticleNonValideAdapterRV);



                return false;
            }
        });



    }

    private ArrayList<ReservationArticleDansDepot> filterReservation (ArrayList<ReservationArticleDansDepot>  listReservationArticleDansDepot  , String term )  {

        term = term.toLowerCase()  ;
        final ArrayList<ReservationArticleDansDepot> filetrListReservation  = new ArrayList<>() ;

        for (ReservationArticleDansDepot r : listReservationArticleDansDepot)
        {
            final  String  txt_code_article =  r.getCodeArticle().toLowerCase()  ;

            if ( txt_code_article.contains(term)  )
            {
                filetrListReservation.add(r) ;
            }
        }
        return  filetrListReservation ;

    }



}