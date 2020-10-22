package com.example.faten.testsql.task;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.model.ArticleDefectueuseDansValise;
import com.example.faten.testsql.model.BonCommandeVente;
import com.example.faten.testsql.model.LigneBonCommandeVente;
import com.example.faten.testsql.model.ReservationArticleDansDepot;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class InsertArticleDefectueueseTask extends AsyncTask<String, String, String> {


    Activity activity  ;
    DialogFragment  dialog  ;
    ArticleDefectueuseDansValise ad   ;
    Button  btn_valide_cause  ;
    ProgressBar  pb ;


    String z = "";
    ConnectionClass connectionClass;
    String user, password, base, ip;



    DateFormat dfSQL = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    Boolean isSuccess = false;

    public InsertArticleDefectueueseTask (Activity activity,  DialogFragment  dialog  ,  ArticleDefectueuseDansValise ad , Button  btn_valide_cause ,ProgressBar pb) {
        this.activity = activity;
        this.dialog = dialog  ;
        this.ad = ad;
        this.btn_valide_cause= btn_valide_cause ;
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
     btn_valide_cause.setEnabled(false);
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            Connection con = connectionClass.CONN(ip, password, user, base);
            if (con == null) {
                z = "Error in connection with SQL server";
            } else {


                String  insertArticleDefectueuse  =  " IF EXISTS   (select top(1) * from ArticleDefectueuseDansValise   where CodeArticle = '"+ad.getCodeArticle()+"' and CodeDepot = '"+ad.getCodeDepot()+"' )\n" +
                        " BEGIN \n" +
                        " \n" +
                        " \n" +
                        " INSERT INTO  [ArticleDefectueuseDansValise]\n" +  //
                        "           ([NumeroBonCommande]\n" +
                        "           ,[CodeArticle]\n" +
                        "           ,[CodeDepot]\n" +
                        "           ,[NumeroOrdre]\n" +
                        "           ,[Designation]\n" +
                        "           ,[Quantite]\n" +
                        "           ,[CodeCause]\n" +
                        "           ,[LibelleCause]\n" +
                        "           ,[Valider]\n" +
                        "           ,[Cloturer]\n" +
                        "           ,[Annuler])\n" +
                        "     VALUES\n" +
                        "           ('"+ad.getNumeroBonCommande()+"'\n" +
                        "           ,'"+ad.getCodeArticle()+"'\n" +
                        "           ,'"+ad.getCodeDepot()+"'\n" +
                        "           ,(select  top(1) ( [NumeroOrdre] +1 )  from ArticleDefectueuseDansValise   where CodeArticle = '"+ ad.getCodeArticle()+"'  and CodeDepot = '"+ad.getCodeDepot()+"'  order BY [NumeroOrdre]  DESC )\n" +
                        "           ,'"+ad.getDesignation().replace("'","''")+"'\n" +
                        "           ,'1'\n" +
                        "           ,'"+ad.getCodeCause() +"'\n" +
                        "           ,'"+ad.getLibelleCause().replace("'","''")+"'\n" +
                        "           ,'0'\n" +
                        "           ,'0'\n" +
                        "           ,'0')\n" +
                        " \n" +
                        " \n" +
                        " END \n" +
                        " \n" +
                        " ELSE\n" +
                        "\n" +
                        "BEGIN \n" +
                        "\n" +
                        "INSERT INTO  [ArticleDefectueuseDansValise]\n" +
                        "           ([NumeroBonCommande]\n" +
                        "           ,[CodeArticle]\n" +
                        "           ,[CodeDepot]\n" +
                        "           ,[NumeroOrdre]\n" +
                        "           ,[Designation]\n" +
                        "           ,[Quantite]\n" +
                        "           ,[CodeCause]\n" +
                        "           ,[LibelleCause]\n" +
                        "           ,[Valider]\n" +
                        "           ,[Cloturer]\n" +
                        "           ,[Annuler])\n" +
                        "     VALUES\n" +
                        "           ('"+ad.getNumeroBonCommande()+"'\n" +
                        "           ,'"+ad.getCodeArticle()+"'\n" +
                        "           ,'"+ad.getCodeDepot()+"'\n" +
                        "           ,'1'\n" +
                        "           ,'"+ad.getDesignation()+"'\n" +
                        "           ,'1'\n" +
                        "           ,'"+ad.getCodeCause() +"'\n" +
                        "           ,'"+ad.getLibelleCause().replace("'","''")+"'\n" +
                        "           ,'0'\n" +
                        "           ,'0'\n" +
                        "           ,'0')\n" +
                        "\n" +
                        " \n" +
                        " END " ;



                String    queryInsert = " " +
                        " BEGIN TRANSACTION insert_bc  \n" +
                        " \n" +
                        "DECLARE @error INT  \n" +
                        "DECLARE @ID_INSERTION NUMERIC(19,0)  \n" +
                        " \n" +
                        " SET @error = 0\n" +
                        "\n"
                        +  insertArticleDefectueuse
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

        if (isSuccess) {

            Toast.makeText(activity, "Cause sauvegardé avec succès", Toast.LENGTH_LONG).show();
            dialog.dismiss();

        }

        else {

            Toast.makeText(activity, "Problème de sauvegard "+r, Toast.LENGTH_LONG).show();

        }


    }



}