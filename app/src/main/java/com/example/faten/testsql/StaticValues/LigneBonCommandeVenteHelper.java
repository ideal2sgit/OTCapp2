package com.example.faten.testsql.StaticValues;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.faten.testsql.model.LigneBonCommandeVente;

import java.util.ArrayList;

/**
 * Created by fatima on 10/04/2016.
 */
public class LigneBonCommandeVenteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String LBC_TABLE_NAME = "LigneBonCommande";


    public static final String C_CodeArticle = "CodeArticle";
    public static final String C_DesignationArticle = "DesignationArticle";
    public static final String C_NumeroOrdre = "NumeroOrdre";
    public static final String C_PrixVenteHT = "PrixVenteHT";
    public static final String C_Quantite = "Quantite";
    public static final String C_MontantHT = "MontantHT";
    public static final String C_TauxTVA = "TauxTVA";
    public static final String C_MontantTVA = "MontantTVA";
    public static final String C_MontantTTC = "MontantTTC";
    public static final String C_Observation = "Observation";
    public static final String C_TauxRemise = "TauxRemise";
    public static final String C_MontantRemise = "MontantRemise";
    public static final String C_MontantFodec = "MontantFodec";
    public static final String C_NetHT = "NetHT";
    public static final String C_PrixAchatNet = "PrixAchatNet";


    public LigneBonCommandeVenteHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                        "create table LigneBonCommande " +
                        "( CodeArticle text , DesignationArticle text, NumeroOrdre Integer  , PrixVenteHT DOUBLE , Quantite Integer  ,MontantHT DOUBLE ,TauxTVA DOUBLE , MontantTVA DOUBLE ," +
                                "MontantTTC DOUBLE ,Observation text , TauxRemise DOUBLE , MontantRemise DOUBLE , MontantFodec DOUBLE , NetHT  DOUBLE ,PrixAchatNet DOUBLE ) ;" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS LigneBonCommande");
        onCreate(db);
    }

    public boolean insertLigneBonCommande  (String CodeArticle, String DesignationArticle  , int NumeroOrdre  , double PrixVenteHT  , int Quantite, double MontantHT  , double TauxTVA,
                                            double MontantTVA ,String  Observation ,double TauxRemise ,  double MontantRemise  , double  MontantFodec ,double NetHT , double PrixAchatNet  )
    {

        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("CodeArticle", CodeArticle);
        contentValues.put("DesignationArticle", DesignationArticle);
        contentValues.put("NumeroOrdre", NumeroOrdre);
        contentValues.put("PrixVenteHT", PrixVenteHT);
        contentValues.put("Quantite", Quantite);
        contentValues.put("MontantHT", MontantHT);
        contentValues.put("TauxTVA", TauxTVA);

        contentValues.put("MontantTVA", MontantTVA);
        contentValues.put("Observation", Observation);
        contentValues.put("TauxRemise", TauxRemise);
        contentValues.put("MontantRemise", MontantRemise);

        contentValues.put("MontantFodec", MontantFodec);
        contentValues.put("NetHT", NetHT);
        contentValues.put("PrixAchatNet", PrixAchatNet);


        Log.e("LigneBonCommande" , " - CodeArt :"+ CodeArticle +" - QT : "+ Quantite +" - Order :"+ NumeroOrdre  ) ;
        db.insert("LigneBonCommande", null, contentValues);


        return true;

    }

    public Cursor getData(String CodeArticle){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from LigneBonCommande where CodeArticle ="+CodeArticle+"", null );
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, LBC_TABLE_NAME);
        return numRows;
    }

    public boolean updateLBC (String CodeArticle, int Quantite)
    {
        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues contentValues = new ContentValues() ;

        contentValues.put("Quantite", Quantite) ;

        db.update("LigneBonCommande", contentValues, " CodeArticle = ? ", new String[] { CodeArticle  } );

        Log.e("UPDATE" , ""+ CodeArticle  +" Quantite "+Quantite);
        return true;
    }



    public Integer deleteLBC (String CodeArticle)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("LigneBonCommande",
                "CodeArticle = ? ",
                new String[] {CodeArticle });
    }



    public ArrayList<LigneBonCommandeVente> getLigneBonCommande()
    {
        ArrayList<LigneBonCommandeVente> list_lbc = new ArrayList<LigneBonCommandeVente>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery ( "select * from LigneBonCommande", null ) ;
        res.moveToFirst  ();

        while  (res.isAfterLast() == false) {

            String CodeArticle = res.getString(res.getColumnIndex(C_CodeArticle)) ;
            String DesignationArticle = res.getString(res.getColumnIndex(C_DesignationArticle)) ;
            int NumeroOrdre = res.getInt(res.getColumnIndex(C_NumeroOrdre)) ;
            double PrixVenteHT = res.getDouble(res.getColumnIndex(C_PrixVenteHT)) ;
            int Quantite = res.getInt(res.getColumnIndex(C_Quantite)) ;
            double  MontantHT = res.getInt(res.getColumnIndex(C_MontantHT)) ;
            double TauxTVA = res.getDouble(res.getColumnIndex(C_TauxTVA)) ;

            double MontantTVA = res.getDouble(res.getColumnIndex(C_MontantTVA)) ;
            double MontantTTC = res.getDouble(res.getColumnIndex(C_MontantTTC)) ;

            String Observation = res.getString(res.getColumnIndex(C_Observation)) ;
            double TauxRemise = res.getDouble(res.getColumnIndex(C_TauxRemise)) ;
            double MontantRemise = res.getDouble(res.getColumnIndex(C_MontantRemise)) ;

            double MontantFodec = res.getDouble(res.getColumnIndex(C_MontantFodec)) ;

            double NetHT = res.getDouble(res.getColumnIndex(C_NetHT)) ;
            double PrixAchatNet = res.getDouble(res.getColumnIndex(C_PrixAchatNet)) ;


            list_lbc.add( new LigneBonCommandeVente("",CodeArticle,DesignationArticle,NumeroOrdre , PrixVenteHT ,Quantite ,MontantHT, TauxTVA ,
                    MontantTVA ,MontantTTC ,Observation ,TauxRemise ,MontantRemise ,MontantFodec ,NetHT,PrixAchatNet ));
            res.moveToNext();
        }
        return list_lbc;
    }
}