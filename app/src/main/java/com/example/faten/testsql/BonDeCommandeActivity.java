package com.example.faten.testsql;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faten.testsql.adapter.ArticleCMDAdapter;
import com.example.faten.testsql.model.Article;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BonDeCommandeActivity extends AppCompatActivity {
    ConnectionClass connectionClass;
    public static Activity fa;

    String marque;

    final Context co = this;
    String codart = "1";

    String  prixttc;
    String codeclient = "", prefixebn = "";
    String PayerTVA = "";
    String NomUtilisateur, codelivreur = "";

    String zonederecherche;
    EditText  utilisateur;

    String queryslist,  rsclient;

    String user, password, base, ip;


    RecyclerView listArticleCMD;
    RecyclerView list_article_qt_a_commander;
    Button btn_valider;
    SearchView searchViewArticle  ;
    public static ArrayList<Article> listArticleAcommander = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bon_cmd);
        setTitle("Bon de Commande");
        searchViewArticle = (SearchView)  findViewById(R.id.search_bar_article) ;
        list_article_qt_a_commander = (RecyclerView) findViewById(R.id.list_article_qt_a_commander);
        listArticleCMD = (RecyclerView) findViewById(R.id.list_article_cmd);
        btn_valider = (Button) findViewById(R.id.btn_valider);

        listArticleCMD.setHasFixedSize(true);
        listArticleCMD.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        list_article_qt_a_commander.setHasFixedSize(true);
        list_article_qt_a_commander.setLayoutManager(layoutManager);


        fa = this;
        connectionClass = new ConnectionClass();
        SharedPreferences prefe = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();

        user     = prefe.getString("user", user);
        ip       = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base     = prefe.getString("base", base);



        Intent intent = getIntent();
        NomUtilisateur = intent.getStringExtra("NomUtilisateur");
        codelivreur    = intent.getStringExtra("CodeLivreur");
        codeclient     = intent.getStringExtra("codeclient");
        rsclient       = intent.getStringExtra("clientrs");
        PayerTVA       = intent.getStringExtra("PayerTVA"  );

        SharedPreferences pref = getSharedPreferences("usersession", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        NomUtilisateur = pref.getString("NomUtilisateur", NomUtilisateur);
        codelivreur = pref.getString("CodeLivreur", codelivreur);




        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent IN = new Intent(getApplicationContext(), Validationcmd.class);
                IN.putExtra("NomUtilisateur", NomUtilisateur);
                IN.putExtra("CodeClient", codeclient);
                IN.putExtra("CodeLivreur", codelivreur);
                IN.putExtra("rsclient", rsclient);
                IN.putExtra("PayerTVA", PayerTVA);
                startActivity(IN);
            }
        });


        prefixebn = "TL" + NomUtilisateur;
        if (prefixebn.equals("TL")) {

            LayoutInflater li = LayoutInflater.from(co);
            View px = li.inflate(R.layout.logintest, null);
            AlertDialog.Builder alt = new AlertDialog.Builder(co);
            alt.setIcon(R.drawable.i2s);
            alt.setView(px);
            utilisateur = (EditText) px.findViewById(R.id.nomutilisateur);

            alt.setTitle("Identification");
            alt.setPositiveButton("ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface di, int i) {

                            NomUtilisateur = utilisateur.getText().toString();

                        }
                    });


            AlertDialog d = alt.create();
            d.show();


        }



        ListArticleStockTask1 listArticleStockTask = new ListArticleStockTask1();
        listArticleStockTask.execute();

    }




    public class ListArticleStockTask1 extends AsyncTask<String, String, String> {
        String z = "";
        Boolean t = false;

        ArrayList<Article> listArticle = new ArrayList<>();

        // List<Map<String, String>> prolist  = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {


        }


        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    //String query = "select * from Vue_RechercheArticleQuantite where Quantite != 0 and  CodeFamille ='"+familleart+"' and CodeMarque='"+codemarque+"'";

                    String query = "SELECT  Article.CodeArticle  , Designation , CodeTVA ,PrixVenteTTC , PrixVenteHT  , PrixAchatHT  , Quantite   ,\n" +
                            "(Select   Quantite - SUM(QuantiteCMD) As QuantiteCMD from  \n" +
                            "(\n" +
                            "select 0 As QuantiteCMD  from Article\n" +
                            "where CodeArticle=Article.CodeArticle   \n" +
                            "group by Article.CodeArticle\n" +
                            "\n" +
                            "union all \n" +
                            "\n" +
                            "select  SUM(LigneBonCommandeVente.Quantite) As QuantiteCMD \n" +
                            "from LigneBonCommandeVente \n" +
                            "inner join \n" +
                            "BonCommandeVente on LigneBonCommandeVente.NumeroBonCommandeVente = BonCommandeVente.NumeroBonCommandeVente \n" +
                            "where CodeArticle=Article.CodeArticle  and NumeroEtat = 'E01'\n" +
                            "group by LigneBonCommandeVente.CodeArticle\n" +
                            "\n" +
                            "union all \n" +
                            "\n" +
                            "select SUM(LigneBonCommandeVente.Quantite) As QuantiteCMD \n" +
                            "from LigneBonCommandeVente \n" +
                            "inner join \n" +
                            "BonCommandeVente on LigneBonCommandeVente.NumeroBonCommandeVente = BonCommandeVente.NumeroBonCommandeVente \n" +
                            "left join LigneBonLivraisonVente on   LigneBonLivraisonVente.NumeroBonCommandeVente = LigneBonCommandeVente.NumeroBonCommandeVente\n" +
                            "and LigneBonLivraisonVente.CodeArticle= LigneBonCommandeVente.CodeArticle\n" +
                            "where LigneBonCommandeVente.CodeArticle=Article.CodeArticle  and NumeroEtat='E02'  and NumeroBonLivraisonVente is null\n" +
                            "group by LigneBonCommandeVente.CodeArticle ) As Liste \n" +
                            "\n" +
                            ") AS QteRestante\n" +
                            "   \n" +
                            "FROM Article\n" +
                            "inner join  Stock on Article.CodeArticle=Stock.CodeArticle \n" +
                            "where Quantite>0 ";


                    Log.e("query_article", query);
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    int i = 0;
                    while (rs.next()) {

                        String CodeArticle = rs.getString("CodeArticle");
                        String Designation = rs.getString("Designation");
                        double PrixVenteTTC = rs.getDouble("PrixVenteTTC");
                        double PrixVenteHT = rs.getDouble("PrixVenteHT");
                        double PrixAchatHT = rs.getDouble("prixAchatHT");

                        int    CodeTVA = rs.getInt("CodeTVA");

                        int Quantite         = rs.getInt("Quantite");
                        int QuantiteRestante = rs.getInt("QteRestante");


                        Article article = new Article( CodeArticle , Designation ,PrixVenteTTC ,PrixVenteHT ,PrixAchatHT ,CodeTVA ,Quantite ,QuantiteRestante ,0 );
                        listArticle.add(article);


                    }
                    if (i == 0) {
                        t = true;
                    }

                    z = "Success";
                }
            } catch (SQLException ex) {

                z = "list" + ex.toString();

            }
            return z;
        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();

            listArticleAcommander = listArticle;
            ArticleCMDAdapter adapter = new ArticleCMDAdapter(BonDeCommandeActivity.this, listArticle, list_article_qt_a_commander);
            listArticleCMD.setAdapter(adapter);


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

                    final  ArrayList<Article>   fitlerArticleList = filterArticleDivers( listArticle , query) ;

                    ArticleCMDAdapter adapter = new ArticleCMDAdapter(BonDeCommandeActivity.this, fitlerArticleList, list_article_qt_a_commander);
                    listArticleCMD.setAdapter(adapter);



                    return false;
                }
            });



        }

        private ArrayList<Article> filterArticleDivers (ArrayList<Article>  listArticle  , String term )  {

            term = term.toLowerCase()  ;
            final ArrayList<Article> filetrListArticle  = new ArrayList<>() ;

            for (Article a : listArticle)
            {
                final  String  textDesignation =  a.getDesignation().toLowerCase()  ;
                final  String  textCodeArticle =  a.getCodeArticle().toLowerCase()  ;

                if ( textDesignation.contains(term)  ||  textCodeArticle.contains(term) )
                {
                    filetrListArticle.add(a) ;
                }
            }
            return  filetrListArticle ;

        }
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("BONCommande","onRestart") ;

        ListArticleStockTaskOnRestart     listArticleStockTaskOnRestart = new ListArticleStockTaskOnRestart();
        listArticleStockTaskOnRestart.execute();

        //spinqt.setSelection(0);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // spinqt.setSelection(0);
    }

    public static Activity getInstance() {
        return fa;
    }

    public class ListArticleStockTaskOnRestart extends AsyncTask<String, String, String> {
        String z = "";
        Boolean t = false;


        @Override
        protected void onPreExecute() {


        }


        @Override
        protected String doInBackground(String... params) {


            return "ON restart";
        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();


            ArticleCMDAdapter adapter = new ArticleCMDAdapter(BonDeCommandeActivity.this, listArticleAcommander, list_article_qt_a_commander);
            listArticleCMD.setAdapter(adapter);


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

                    final  ArrayList<Article>   fitlerArticleList = filterArticleDivers( listArticleAcommander , query) ;

                    ArticleCMDAdapter adapter = new ArticleCMDAdapter(BonDeCommandeActivity.this, fitlerArticleList, list_article_qt_a_commander);
                    listArticleCMD.setAdapter(adapter);



                    return false;
                }
            });



        }

        private ArrayList<Article> filterArticleDivers (ArrayList<Article>  listArticle  , String term )  {

            term = term.toLowerCase()  ;
            final ArrayList<Article> filetrListArticle  = new ArrayList<>() ;

            for (Article a : listArticle)
            {
                final  String  textDesignation =  a.getDesignation().toLowerCase()  ;
                final  String  textCodeArticle =  a.getCodeArticle().toLowerCase()  ;

                if ( textDesignation.contains(term)  ||  textCodeArticle.contains(term) )
                {
                    filetrListArticle.add(a) ;
                }
            }
            return  filetrListArticle ;
        }
    }


}
