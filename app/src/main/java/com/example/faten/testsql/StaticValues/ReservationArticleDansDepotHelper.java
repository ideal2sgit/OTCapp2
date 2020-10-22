package com.example.faten.testsql.StaticValues;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.faten.testsql.model.ArticleStock;
import com.example.faten.testsql.model.ReservationArticleDansDepot;

import java.util.ArrayList;

/**
 * Created by fatima on 10/04/2016.
 */
public class ReservationArticleDansDepotHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBReservation.db";
    public static final String reservation_TABLE_NAME = "Reservation";


    public static final String C_CodeArticle = "CodeArticle";
    public static final String C_DesignationArticle = "DesignationArticle";

    public static final String C_CodeDepotDemendeur = "codeDepotDemendeur";
    public static final String C_DepotDemandeur = "DepotDemandeur";

    public static final String C_CodeDepotDemandant = "codeDepotDemandant";
    public static final String C_DepotDemandant = "DepotDemandant";


    public static final String C_Quantite = "quantite";
    public static final String C_Valider= "valider";

    public static final String C_QTStock = "QTStock";
    public static final String C_QtCMD= "QtCMD";
    public static final String C_QtDefectueuse= "QtDefectuse";


    public ReservationArticleDansDepotHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                        "create table Reservation " +
                        "( CodeArticle text , DesignationArticle text  ,  codeDepotDemendeur text , DepotDemandeur text ,codeDepotDemandant text ," +
                        " DepotDemandant text  ,quantite Integer  , valider Integer  , QTStock Integer  , QtCMD Integer   , QtDefectuse  Integer " +
                        "  ) ;" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS Reservation");
        onCreate(db);
    }

    public boolean insertReservation  (String CodeArticle, String DesignationArticle  , String codeDepotDemendeur
                                      ,String DepotDemandeur  , String codeDepotDemandant, String DepotDemandant  , int quantite, int  valider
                                      , int QTStock, int  QtCMD , int QtDefectuse )
    {

        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("CodeArticle", CodeArticle);
        contentValues.put("DesignationArticle", DesignationArticle);

        contentValues.put("codeDepotDemendeur", codeDepotDemendeur);
        contentValues.put("DepotDemandeur", DepotDemandeur);

        contentValues.put("codeDepotDemandant", codeDepotDemandant);
        contentValues.put("DepotDemandant", DepotDemandant);

        contentValues.put("quantite", quantite);
        contentValues.put("valider", valider);

        contentValues.put("QTStock", QTStock);
        contentValues.put("QtCMD", QtCMD);
        contentValues.put("QtDefectuse", QtDefectuse);

        Log.e("Reservation" , " - CodeArt :"+ CodeArticle +" - QT : "+ quantite  ) ;
        db.insert("Reservation", null, contentValues);


        return true;

    }

    public Cursor getData(String CodeArticle){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Reservation where CodeArticle = "+CodeArticle+"", null );
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, reservation_TABLE_NAME);
        return numRows;
    }

    public boolean update (String CodeArticle, int Quantite)
    {
        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues contentValues = new ContentValues() ;

        contentValues.put("Quantite", Quantite) ;

        db.update("Reservation", contentValues, " CodeArticle = ? ", new String[] { CodeArticle  } );

        Log.e("UPDATE" , ""+ CodeArticle  +" Quantite "+Quantite);
        return true;
    }


    public void deleteAll( )
    {
        SQLiteDatabase db = this.getWritableDatabase();
           db.execSQL("delete from Reservation" );
      //  return db.delete("ArticlePanier"  ,"","");
    }


    public Integer deleteReservation(String CodeArticle)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Reservation",
                "CodeArticle = ? ",
                new String[] {CodeArticle });
    }



    public ArrayList<ReservationArticleDansDepot> getList()
    {
        ArrayList<ReservationArticleDansDepot> list_reservation = new ArrayList<ReservationArticleDansDepot>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery ( "select * from Reservation ", null ) ;
        res.moveToFirst  ();

        while  (res.isAfterLast() == false) {


            String CodeArticle = res.getString(res.getColumnIndex(C_CodeArticle)) ;
            String DesignationArticle = res.getString(res.getColumnIndex(C_DesignationArticle)) ;

            String CodeDepotDemendeur = res.getString(res.getColumnIndex(C_CodeDepotDemendeur)) ;
            String  DepotDemandeur = res.getString(res.getColumnIndex(C_DepotDemandeur)) ;

            String CodeDepotDemandant = res.getString(res.getColumnIndex(C_CodeDepotDemandant)) ;
            String  DepotDemandant = res.getString(res.getColumnIndex(C_DepotDemandant)) ;

            int Quntite = res.getInt(res.getColumnIndex(C_Quantite)) ;
            int Valider = res.getInt(res.getColumnIndex(C_Valider)) ;

            int QuntiteStock = res.getInt(res.getColumnIndex(C_QTStock)) ;
            int QtCMD = res.getInt(res.getColumnIndex(C_QtCMD)) ;
            int QtDefectuse = res.getInt(res.getColumnIndex(C_QtDefectueuse)) ;

            list_reservation.add( new ReservationArticleDansDepot("",CodeArticle,DesignationArticle , CodeDepotDemendeur ,DepotDemandeur ,CodeDepotDemandant ,  DepotDemandant , Quntite ,Valider , QuntiteStock , QtCMD , QtDefectuse));

            res.moveToNext();

        }

        return list_reservation ;
    }
}