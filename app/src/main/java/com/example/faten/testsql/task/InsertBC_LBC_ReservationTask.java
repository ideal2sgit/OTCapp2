package com.example.faten.testsql.task;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.adapter.ClientSelectAdapterRV;
import com.example.faten.testsql.dialog.DialogChoixAutomatiqueORParChoix;
import com.example.faten.testsql.dialog.DialogReservationDansAutreDepot;
import com.example.faten.testsql.model.BonCommandeVente;
import com.example.faten.testsql.model.Client;
import com.example.faten.testsql.model.LigneBonCommandeVente;
import com.example.faten.testsql.model.ReservationArticleDansDepot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class InsertBC_LBC_ReservationTask extends AsyncTask<String, String, String> {


    Activity activity  ;
    BonCommandeVente  bonCommandeVente  ;
   ArrayList<LigneBonCommandeVente>  listligneBonCommandeVente ;
    ArrayList<ReservationArticleDansDepot> listReservation  ;

    ProgressBar  pb ;


    String z = "";
    ConnectionClass connectionClass;
    String user, password, base, ip;

    String CompteurBC  ;

    DateFormat dfSQL = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    Boolean isSuccess = false;

    public InsertBC_LBC_ReservationTask(Activity activity, BonCommandeVente bonCommandeVente,    ArrayList<LigneBonCommandeVente>  listligneBonCommandeVente , ArrayList<ReservationArticleDansDepot> listReservation, ProgressBar pb) {
        this.activity = activity;
        this.bonCommandeVente = bonCommandeVente;
        this.listligneBonCommandeVente = listligneBonCommandeVente;
        this.listReservation = listReservation;
        this.pb = pb;

        SharedPreferences prefe = activity.getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        user     = prefe.getString("user", user);
        ip       = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base     = prefe.getString("base", base);

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

                //  get Last Compteur

                String query = " select * from  CompteurPiece  where NomPiecer  =  'BonCommandeVente'  ";
                Log.e("query", query);

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {

                    String PrefixPiece = rs.getString("PrefixPiece");
                    String Annee = rs.getString("Annee");
                    String Compteur = rs.getString("Compteur");

                    CompteurBC = PrefixPiece + Annee + Compteur;
                }



                String  insertBC  =  "INSERT INTO [BonCommandeVente]\n" +
                        "           ([NumeroBonCommandeVente],[DateBonCommandeVente],[NumeroDevisVente],[CodeClient],[ReferenceClient],[DateReferenceClient]\n" +
                        "           ,[DelaiLivraison] ,[TotalHT],[TotalTVA],[TotalTTC],[NumeroEtat],[NomUtilisateur],[DateCreation],[HeureCreation] ,[Observation]\n" +
                        "           ,[TotalRemise],[TotalFodec] ,[TotalNetHT] ,[TauxRemiseExceptionnel],[MontantRemiseExceptionnel] ,[CodeLivreur])\n" +
                        "     VALUES\n" +
                        "           ('"+CompteurBC+"'\n" + //1
                        "           ,'"+dfSQL.format(bonCommandeVente.getDateBonCommandeVente())+"'\n" + //2
                        "           ,'"+bonCommandeVente.getNumeroDevisVente()+"'\n" + //3
                        "           ,'"+bonCommandeVente.getCodeClient()+"'\n" + //4
                        "           ,'"+bonCommandeVente.getReferenceClient()+"'\n" +//5
                        "           ,'"+dfSQL.format(bonCommandeVente.getDateReferenceClient())+"'\n" +//6
                        "           ,'"+bonCommandeVente.getDelaiLivraison()+"'\n" +//7
                        "           ,'"+bonCommandeVente.getTotalHT()+"'\n" +//8
                        "           ,'"+bonCommandeVente.getTotalTVA()+"'\n" +//9
                        "           ,'"+bonCommandeVente.getTotalTTC()+"'\n" +//10
                        "           ,'"+bonCommandeVente.getNumeroEtat()+"'\n" +//11
                        "           ,'"+bonCommandeVente.getNomUtilisateur()+"'\n" +//12
                        "           ,'"+dfSQL.format(bonCommandeVente.getDateCreation())+"'\n" +//13
                        "           ,'"+dfSQL.format(bonCommandeVente.getHeureCreation())+"'\n" +//14
                        "           ,'"+bonCommandeVente.getObservation()+"'\n" +//15
                        "           ,'"+bonCommandeVente.getTotalRemise()+"'\n" +//16
                        "           ,'"+bonCommandeVente.getTotalFodec()+"'\n" +//17
                        "           ,'"+bonCommandeVente.getTotalNetHT()+"'\n" +//18
                        "           ,'0'\n" +//19
                        "           ,'0'\n" +//20
                        "           ,'"+bonCommandeVente.getCodeLivreur()+"')";//21;


                String  insertLBC  =  "" ;

                for (LigneBonCommandeVente  ligneBonCommandeVente   : listligneBonCommandeVente)

                {

                    String  insert  =  "INSERT INTO  [LigneBonCommandeVente]\n" +
                            "           ([NumeroBonCommandeVente],[CodeArticle],[DesignationArticle],[NumeroOrdre],[PrixVenteHT],[Quantite] ,[MontantHT]\n" +
                            "           ,[TauxTVA],[MontantTVA],[MontantTTC],[Observation],[TauxRemise],[MontantRemise],[MontantFodec],[NetHT],[PrixAchatNet])\n" +
                            "     VALUES\n" +
                            "           ('"+CompteurBC+"'\n" + //1
                            "           ,'"+ligneBonCommandeVente.getCodeArticle()+"'\n" + //2
                            "           ,'"+ligneBonCommandeVente.getDesignationArticle()+"'\n" + //3
                            "           ,'"+ligneBonCommandeVente.getNumeroOrdre()+"'\n" + //4
                            "           ,'"+ligneBonCommandeVente.getPrixVenteHT()+"'\n" + //5
                            "           ,'"+ligneBonCommandeVente.getQuantite()+"'\n" + //6
                            "           ,'"+ligneBonCommandeVente.getMontantHT()+"'\n" + //7
                            "           ,'"+ligneBonCommandeVente.getTauxTVA()+"'\n" + //8
                            "           ,'"+ligneBonCommandeVente.getMontantTVA()+"'\n" + //9
                            "           ,'"+ligneBonCommandeVente.getMontantTTC()+"'\n" + //10
                            "           ,'"+ligneBonCommandeVente.getObservation()+"'\n" + //11
                            "           ,'"+ligneBonCommandeVente.getTauxRemise()+"'\n" + //12
                            "           ,'"+ligneBonCommandeVente.getMontantRemise()+"'\n" + //13
                            "           ,'"+ligneBonCommandeVente.getMontantFodec()+"'\n" + //14
                            "           ,'"+ligneBonCommandeVente.getNetHT()+"'\n" + //15
                            "           ,'"+ligneBonCommandeVente.getPrixAchatNet()+"') \n\n" ;


                    insertLBC=insertLBC +insert ;
                }




                String insertReservation  = "" ;

                for (ReservationArticleDansDepot  reserv  : listReservation)
                {

                    String query_reserv = "  INSERT INTO  [ReservationArticleDansDepot]\n" +
                            "           ([NumeroBonCommandeVente]\n" +
                            "           ,[CodeArticle]\n" +
                            "           ,[Designation]\n" +
                            "           ,[CodeDepotDemendeur]\n" +
                            "           ,[CodeDepotDemandant]\n" +
                            "           ,[Quantite]\n" +
                            "           ,[Valider]\n" +
                            "           ,[telephone])\n" +
                            "     VALUES\n" +
                            "           ('"+CompteurBC+"'\n" +
                            "           ,'"+reserv.getCodeArticle()+"'\n" +
                            "           ,'"+reserv.getDesignation()+"'\n" +
                            "           ,'"+reserv.getCodeDepotDemendeur()+"'\n" +
                            "           ,'"+reserv.getCodeDepotDemandant()+"'\n" +
                            "           ,'"+reserv.getQuantite()+"'\n" +
                            "           ,'"+reserv.getValider()+"'\n" +
                            "           ,'0') \n\n\n" +
                            " " ;


                    insertReservation = insertReservation + query_reserv ;

                }

                String    queryInsert = " " +
                        " BEGIN TRANSACTION insert_bc  \n" +
                        " \n" +
                        "DECLARE @error INT  \n" +
                        "DECLARE @ID_INSERTION NUMERIC(19,0)  \n" +
                        " \n" +
                        " SET @error = 0\n" +
                        "\n"
                        +  insertBC
                        +"   SET @error = @error + @@error  \n  "
                        + insertLBC
                        +"   SET @error = @error + @@error  \n  "
                        + insertReservation
                        +"   SET @error = @error + @@error  \n  "

                        + "  IF @error = 0  \n" +
                        "    COMMIT TRANSACTION insert_bc  \n" +
                        "\t  ELSE \n" +
                        "    ROLLBACK TRANSACTION insert_bc   " ;


                Log.e("query_INSERT_BC", queryInsert);
                Statement stmt2 = con.createStatement();
                stmt2.executeUpdate(queryInsert);

                isSuccess = true  ;



                z = "Success";
            }
        } catch (Exception ex) {
            z = "Error "+ex.getMessage().toString() ;
            isSuccess = false ;
        }
        return z;
    }



    @Override
    protected void onPostExecute(String r) {
        Log.e("METHOD","onPostExecute(String r)") ;

        incrementCompteurBC() ;

        if (isSuccess) {

            Toast.makeText(activity, "Bon Commande avec succès", Toast.LENGTH_LONG).show();
            activity.finish();


           // DialogChoixAutomatiqueORParChoix.newInstance().dismiss();



        }

        else {

            Toast.makeText(activity, "Problème de sauvegard "+r, Toast.LENGTH_LONG).show();

        }


    }


    public void incrementCompteurBC() {
        Log.e("METHOD","incrementCompteurBC()") ;

        ConnectionClass connectionClass;
        String user = "", password = "", base = "", ip = "";

        SharedPreferences prefe = activity.getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        user     = prefe.getString("user", user);
        ip       = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base     = prefe.getString("base", base);

        connectionClass = new ConnectionClass();

        try {
            Connection con = connectionClass.CONN(ip, password, user, base);  // Connect to database
            if (con == null) {
                Log.e("ERROR", " Error in connection with SQL server ");

            } else {

                String query = "  select  * from [CompteurPiece] where [NomPiecer] = 'BonCommandeVente'  ";
                Log.e("query", query);

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {

                    String Compteur = rs.getString("Compteur");

                    int _Compteur = Integer.parseInt(Compteur);
                    _Compteur++;

                    DecimalFormat df = new DecimalFormat("00000");
                    String CompteurIcremented = df.format(_Compteur);

                    Log.e("CompteurIcremented",""+CompteurIcremented) ;

                    String queryUPDATE = " update CompteurPiece set Compteur = '" + CompteurIcremented + "' where  NomPiecer = 'BonCommandeVente' ";
                    Statement stmtUPDATE = con.createStatement();
                    stmtUPDATE.executeUpdate(queryUPDATE);

                }
            }
        } catch (SQLException ex) {
            Log.e("ERROR", ex.getMessage());
        }


    }


}