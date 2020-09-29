package com.example.faten.testsql.model;

import android.util.Log;

import com.example.faten.testsql.ConnectionClass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CompteurPiece {
    ConnectionClass connectionClass;
    String res = "" ;
    Boolean isSuccess = false;
    String PrefixPiece ;
    String Annee ;
    String Compteur ;
    public String getNewNumeroCV  (String user,String password,String base,String ip)
    {

        String NumeroCV ="";
        try {
            Connection con =  connectionClass.CONN(ip, password, user, base);       // Connect to database
            Log.e("con", "" + con);
            if (con == null) {
                res = "Check Your Internet Access!";
            } else {
                String query = "select * from CompteurPiece where CodeSociete = 'SOC001' and NomPiecer = 'BonCommandeVente'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {


                    Compteur =rs.getString("Compteur") ;
                    PrefixPiece  =rs.getString("PrefixPiece") ;
                    Annee  =rs.getString("Annee") ;

                    int new_Compteur= Integer.parseInt(Compteur) + 1 ;
                    Log.e ("new_new_Compteur" ,""+new_Compteur) ;

                    String CompteurFinal = "" ;

                    if (new_Compteur<10)
                        CompteurFinal = "0000"+new_Compteur ;
                    else if (new_Compteur<100)
                        CompteurFinal = "000"+new_Compteur ;
                    else if (new_Compteur<1000)
                        CompteurFinal = "00"+new_Compteur ;
                    else if (new_Compteur<10000)
                        CompteurFinal = "0"+new_Compteur ;
                    else if (new_Compteur<100000)
                        CompteurFinal = ""+new_Compteur ;


                    NumeroCV = PrefixPiece+""+Annee+""+Compteur ;



                    //  update compteur dans la base
                    String queryUpdate = "UPDATE CompteurPiece SET  Compteur = '"+CompteurFinal+"' where CodeSociete = 'SOC001' and NomPiecer = 'BonCommandeVente'";
                    Statement stmtUpdate = con.createStatement();
                    stmtUpdate.executeQuery(queryUpdate);



                    isSuccess = true;

                }
            }
            con.close();


            return NumeroCV ;

        } catch (Exception ex) {
            isSuccess = false;

            res = ex.getMessage();
            return NumeroCV ;
        }

    }


}
