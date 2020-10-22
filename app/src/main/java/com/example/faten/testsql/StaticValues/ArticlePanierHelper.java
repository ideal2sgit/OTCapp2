package com.example.faten.testsql.StaticValues;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.faten.testsql.model.ArticleStock;
import com.example.faten.testsql.model.LigneBonCommandeVente;

import java.util.ArrayList;

/**
 * Created by fatima on 10/04/2016.
 */
public class ArticlePanierHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBArticlePanier.db";
    public static final String Article_panier_TABLE_NAME = "ArticlePanier";


    public static final String C_CodeArticle = "CodeArticle";
    public static final String C_DesignationArticle = "DesignationArticle";
    public static final String C_PrixVenteTTC = "PrixVenteTTC";
    public static final String C_PrixVenteHT = "PrixVenteHT";
    public static final String C_PrixAchatHT = "PrixAchatHT";
    public static final String C_CodeTVA = "CodeTVA";
    public static final String C_TauxTVA = "TauxTVA";
    public static final String C_Quantite = "Quantite";




    public ArticlePanierHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                        "create table ArticlePanier " +
                        "( CodeArticle text , DesignationArticle text  ,  PrixVenteTTC DOUBLE , PrixVenteHT DOUBLE ,PrixAchatHT DOUBLE ," +
                                " CodeTVA Integer  ,TauxTVA DOUBLE  , Quantite Integer  " +
                                "  ) ;" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS ArticlePanier");
        onCreate(db);
    }

    public boolean insertArticlePanier  (String CodeArticle, String DesignationArticle  , double PrixVenteTTC  ,double PrixVenteHT  ,
                                         double PrixAchatHT, double CodeTVA  , double TauxTVA, double Quantite   )
    {

        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("CodeArticle", CodeArticle);
        contentValues.put("DesignationArticle", DesignationArticle);

        contentValues.put("PrixVenteTTC", PrixVenteTTC);
        contentValues.put("PrixVenteHT", PrixVenteHT);

        contentValues.put("PrixAchatHT", PrixAchatHT);
        contentValues.put("CodeTVA", CodeTVA);


        contentValues.put("TauxTVA", TauxTVA);
        contentValues.put("Quantite", Quantite);


        Log.e("ArticlePanier" , " - CodeArt :"+ CodeArticle +" - QT : "+ Quantite  ) ;
        db.insert("ArticlePanier", null, contentValues);


        return true;

    }

    public Cursor getData(String CodeArticle){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from ArticlePanier where CodeArticle ="+CodeArticle+"", null );
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, Article_panier_TABLE_NAME);
        return numRows;
    }

    public boolean updateLBC (String CodeArticle, int Quantite)
    {
        SQLiteDatabase db = this.getWritableDatabase() ;
        ContentValues contentValues = new ContentValues() ;

        contentValues.put("Quantite", Quantite) ;

        db.update("ArticlePanier", contentValues, " CodeArticle = ? ", new String[] { CodeArticle  } );

        Log.e("UPDATE" , ""+ CodeArticle  +" Quantite "+Quantite);
        return true;
    }



    public Integer deleteArticlePanier(String CodeArticle)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("ArticlePanier",
                "CodeArticle = ? ",
                new String[] {CodeArticle });
    }


    public void deleteAll( )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from ArticlePanier" );
        //  return db.delete("ArticlePanier"  ,"","");
    }


    public ArrayList<ArticleStock> getListArticlePanier()
    {
        ArrayList<ArticleStock> list_article_panier = new ArrayList<ArticleStock>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery ( "select * from ArticlePanier", null ) ;
        res.moveToFirst  ();

        while  (res.isAfterLast() == false) {


            String CodeArticle = res.getString(res.getColumnIndex(C_CodeArticle)) ;
            String DesignationArticle = res.getString(res.getColumnIndex(C_DesignationArticle)) ;

            double PrixVenteHT = res.getDouble(res.getColumnIndex(C_PrixVenteHT)) ;
            int Quantite = res.getInt(res.getColumnIndex(C_Quantite)) ;

            double  PrixVenteTTC = res.getInt(res.getColumnIndex(C_PrixVenteTTC));
            double  PrixAchatHT = res.getInt(res.getColumnIndex(C_PrixVenteHT));

            int CodeTVA = res.getInt(res.getColumnIndex(C_CodeTVA)) ;
            double TauxTVA = res.getDouble(res.getColumnIndex(C_TauxTVA)) ;

            list_article_panier.add( new ArticleStock(CodeArticle,DesignationArticle , PrixVenteTTC ,PrixVenteHT ,PrixAchatHT ,  CodeTVA, TauxTVA ,Quantite  ));
            res.moveToNext();

        }

        return list_article_panier ;
    }
}