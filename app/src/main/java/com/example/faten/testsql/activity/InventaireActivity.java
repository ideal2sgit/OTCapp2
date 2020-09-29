package com.example.faten.testsql.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.R;
import com.example.faten.testsql.model.Article;
import com.example.faten.testsql.model.ComptageAndroid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InventaireActivity extends AppCompatActivity {


    private TextInputLayout ed_code_a_barre, ed_quantite;

  public  static   EditText _ed_code_a_barre, _ed_quantite;

    TextView txt_designation;

    // SearchView _ed_code_a_barre  ;
 public  static    LinearLayout ll_recherche, ll_trouve_avec_success, ll_echec_recherche;


    String NomUtilisateur, codelivreur;
    DecimalFormat decFormat = new DecimalFormat("0.00");


    CardView btn_clear_data;
    public static CardView btn_ajouter_ligne_inventaire;


   public  static ProgressBar p_bar_ajout ;
    public static ImageView img_res;
    public static TextView txt_res;


    CardView btn_mon_inventaire;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventaire);


        txt_res = findViewById(R.id.txt_res);
        img_res = findViewById(R.id.img_res);
        p_bar_ajout = findViewById(R.id.p_bar_ajout) ;

        p_bar_ajout.setVisibility(View.GONE);
        txt_res.setVisibility(View.INVISIBLE);
        img_res.setVisibility(View.INVISIBLE);

        btn_mon_inventaire = (CardView) findViewById(R.id.btn_mon_inventaire);

        txt_designation = findViewById(R.id.txt_designation);
        ed_code_a_barre = findViewById(R.id.ed_code_a_barre);
        ed_quantite = findViewById(R.id.ed_quantie);

        _ed_code_a_barre = findViewById(R.id._ed_code_a_barre);
        _ed_quantite = findViewById(R.id._ed_qunatite);

        btn_clear_data = (CardView) findViewById(R.id.btn_clear_data);
        btn_ajouter_ligne_inventaire = (CardView) findViewById(R.id.btn_ajouter_ligne_inventaire);

        btn_ajouter_ligne_inventaire.setEnabled(false);
        btn_ajouter_ligne_inventaire.setBackgroundColor(Color.parseColor("#F0F0F0"));


        ll_recherche = findViewById(R.id.ll_recherche);
        ll_trouve_avec_success = findViewById(R.id.ll_trouve_avec_success);
        ll_echec_recherche = findViewById(R.id.ll_echec_recherche);

        ll_recherche.setVisibility(View.INVISIBLE);
        ll_echec_recherche.setVisibility(View.INVISIBLE);


        SharedPreferences pref = getSharedPreferences("usersession", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        NomUtilisateur = pref.getString("NomUtilisateur", NomUtilisateur);
        codelivreur = pref.getString("CodeLivreur", codelivreur);


        _ed_code_a_barre.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Log.e("event_enter_clav", "captured");


                    _ed_quantite.setText("1");


                    _ed_quantite.setFocusableInTouchMode(true);
                    _ed_quantite.requestFocus();

                    _ed_quantite.setEnabled(true);
                    _ed_quantite.setSelection(_ed_quantite.getText().length());


                    RechercheArticleTask1 rechercheArticleTask = new RechercheArticleTask1(InventaireActivity.this, _ed_code_a_barre.getText().toString(),
                            ll_recherche, ll_trouve_avec_success, txt_designation, ll_echec_recherche, _ed_quantite, ed_quantite);
                    rechercheArticleTask.execute();


                    return false;
                }

                return false;
            }
        });


        btn_clear_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _ed_code_a_barre.setText("");

                _ed_code_a_barre.setFocusableInTouchMode(true);
                _ed_code_a_barre.requestFocus();


                ll_echec_recherche.setVisibility(View.INVISIBLE);
                ll_recherche.setVisibility(View.INVISIBLE);
                ll_trouve_avec_success.setVisibility(View.INVISIBLE);

                txt_res.setVisibility(View.INVISIBLE);
                img_res.setVisibility(View.INVISIBLE);

                btn_ajouter_ligne_inventaire.setEnabled(false);
                btn_ajouter_ligne_inventaire.setBackgroundColor(Color.parseColor("#F0F0F0"));


            }
        });


        btn_mon_inventaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent toMonInvent = new Intent(InventaireActivity.this  ,  MonInventaireActivity.class);
                startActivity(toMonInvent);
            }
        });
    }


}


class RechercheArticleTask1 extends AsyncTask<String, String, String> {
    String z = "";


    ConnectionClass connectionClass;
    String user, password, base, ip;


    Activity activity;
    String CodeArticle;

    Article articleFound;
    LinearLayout ll_recherche;
    LinearLayout ll_art_foud_success;
    TextView txt_designation;
    LinearLayout ll_art_not_found;


    EditText ed_quantite;
    TextInputLayout lil_ed_quantite;

    boolean isFound = false;


    public RechercheArticleTask1(Activity activity, String codeArticle, LinearLayout ll_recherche, LinearLayout ll_art_foud_success, TextView txt_designation, LinearLayout ll_art_not_found
            , EditText ed_quantite, TextInputLayout lil_ed_quantite) {
        this.activity = activity;
        CodeArticle = codeArticle;
        this.ll_recherche = ll_recherche;
        this.ll_art_foud_success = ll_art_foud_success;
        this.ll_art_not_found = ll_art_not_found;
        this.txt_designation = txt_designation;

        this.ed_quantite = ed_quantite;
        this.lil_ed_quantite = lil_ed_quantite;

        SharedPreferences pref = activity.getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        user = pref.getString("user", user);
        ip = pref.getString("ip", ip);
        password = pref.getString("password", password);
        base = pref.getString("base", base);
        connectionClass = new ConnectionClass();

    }


    @Override
    protected void onPreExecute() {

        ll_recherche.setVisibility(View.VISIBLE);
        ll_art_foud_success.setVisibility(View.INVISIBLE);
        ll_art_not_found.setVisibility(View.INVISIBLE);
    }


    @Override
    protected String doInBackground(String... params) {

        try {
            Connection con = connectionClass.CONN(ip, password, user, base);
            if (con == null) {
                z = "Error in connection with SQL server";
            } else {



                String queryArticle = " SELECT  Article.CodeArticle , Article.CodeBarre , Designation   \n" +
                        "     FROM Article\n" +
                        "     where   Article.CodeArticle  = '"+CodeArticle+"'   or  Article.CodeBarre = '"+CodeArticle+"' and  Actif  = 1 ";

                Log.e("queryArticle_x", queryArticle);

                PreparedStatement ps = con.prepareStatement(queryArticle);
                ResultSet rs = ps.executeQuery();


                while (rs.next()) {


                    String CodeArticle = rs.getString("CodeArticle");
                    String CodeBarre = rs.getString("CodeBarre");

                    String Designation = rs.getString("Designation");



                    articleFound = new Article(CodeArticle , CodeBarre , Designation  );



                    Log.e("article", "" + articleFound.toString());

                    z = "Success";
                    isFound = true;
                }


            }
        } catch (SQLException ex) {

            Log.e("ERROR_article", ex.getMessage().toString());
            z = "";
            isFound = false;

        }
        return z;
    }


    @Override
    protected void onPostExecute(String r) {


        // Toast.makeText(activity, r, Toast.LENGTH_SHORT).show();

        if (isFound == true) {
            ll_recherche.setVisibility(View.GONE);
            ll_art_foud_success.setVisibility(View.VISIBLE);
            ll_art_not_found.setVisibility(View.GONE);


            DecimalFormat decFormat = new DecimalFormat("0.00");

            txt_designation.setText(articleFound.getDesignation());
            BonLivraisonActivity.pvHT_article_i = articleFound.getPrixVenteHT();
            BonLivraisonActivity.tauxTVA_article_i = articleFound.getTauxTVA();


            lil_ed_quantite.setError(" / " + articleFound.getQuantite());


            InventaireActivity.btn_ajouter_ligne_inventaire.setEnabled(true);
            InventaireActivity.btn_ajouter_ligne_inventaire.setBackgroundColor(Color.parseColor("#FFFFFF"));


            InventaireActivity.btn_ajouter_ligne_inventaire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String _code_article = articleFound.getCodeArticle();
                    String _designation_article = articleFound.getDesignation();
                    String _code_a_barre = articleFound.getCodeArticle();
                    int _quantite = Integer.parseInt(ed_quantite.getText().toString());
                    Date date_creation = new Date();

                    SharedPreferences pref = activity.getSharedPreferences("usersession", Context.MODE_PRIVATE);

                    String _NomUtilisateur = pref.getString("NomUtilisateur", "");
                    String _valeur_unite_vente = "";


                    ComptageAndroid comptageAndroid = new ComptageAndroid(_code_article, _designation_article, _code_a_barre, _quantite, date_creation, _NomUtilisateur, _valeur_unite_vente);


                    InsertComptage insertComptage = new InsertComptage(activity, comptageAndroid , InventaireActivity.p_bar_ajout);
                    insertComptage.execute();


                }
            });




            /*String _code_article = articleFound.getCodeArticle();
            String _designation_article = articleFound.getDesignation();

            double _prix_achat_ht = articleFound.getPrixAchatHT();
            double _prix_vente_ht = articleFound.getPrixVenteHT();
            double _prix_achat_net = articleFound.getPrixAchatHT();

            int     _quantite = articleFound.getQuantite()  ;
            double _montant_ht = articleFound.getPrixVenteHT();
            double _taux_tva = articleFound.getTauxTVA();

            double _taux_remise =  0 ;
            double _montant_remise = _montant_ht * _taux_remise /100 ;

            double _TempHT = _montant_ht - _montant_remise  ;// net ht


            double _montant_tva = _TempHT    *_taux_tva / 100   ;
            double _montant_ttc = _montant_ht + _montant_tva - _montant_remise;

            */


        } else {


            ll_recherche.setVisibility(View.GONE);
            ll_art_foud_success.setVisibility(View.GONE);
            ll_art_not_found.setVisibility(View.VISIBLE);


            InventaireActivity.  btn_ajouter_ligne_inventaire.setEnabled(false);
            InventaireActivity.  btn_ajouter_ligne_inventaire.setBackgroundColor(Color.parseColor("#F0F0F0"));
        }


    }


}


class InsertComptage extends AsyncTask<String, String, String> {

    ConnectionClass connectionClass;
    String user, password, base, ip;


    Activity activity;
    ComptageAndroid comptageAndroid;
    ProgressBar pb_ajout ;

    String res;
    Boolean isSuccess = false;

    DateFormat dfSQL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public InsertComptage(Activity activity, ComptageAndroid comptageAndroid ,ProgressBar pb_ajout ) {
        this.activity = activity;
        this.comptageAndroid = comptageAndroid;
        this.pb_ajout=pb_ajout ;


        SharedPreferences pref = activity.getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        user = pref.getString("user", user);
        ip = pref.getString("ip", ip);
        password = pref.getString("password", password);
        base = pref.getString("base", base);
        connectionClass = new ConnectionClass();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pb_ajout.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            Connection con = connectionClass.CONN(ip, password, user, base);

            Log.e("con", "" + con);
            if (con == null) {
                res = "Check Your Internet Access!";
            } else {

                String queryInsert = "" +
                        "INSERT INTO  [ComptageAndroid]\n" +
                        "           ([CodeArticle]\n" +
                        "           ,[Designation]\n" +
                        "           ,[CodeBarre]\n" +
                        "           ,[Quantite]\n" +
                        "           ,[DateCreation]\n" +
                        "           ,[NomUtilisateur]\n" +
                        "           ,[ValeurUniteVente])\n" +
                        "     VALUES\n" +
                        "           ('" + comptageAndroid.getCodeArticle() + "'\n" +
                        "           ,'" + comptageAndroid.getDesignation() + "'\n" +
                        "           ,'" + comptageAndroid.getCodeBarre() + "'\n" +
                        "           ,'" + comptageAndroid.getQuantite() + "'\n" +
                        "           ,'" + df.format(comptageAndroid.getDateCreation()) + "'\n" +
                        "           ,'" + comptageAndroid.getNomUtilisateur() + "'\n" +
                        "           ,'0')\n" +
                        " " +
                        "  ";
                Log.e("query_INSERT_Comptage", queryInsert);
                Statement stmt = con.createStatement();
                stmt.executeUpdate(queryInsert);

                isSuccess = true;

            }
            con.close();
        } catch (Exception ex) {

            isSuccess = false;
            res = ex.getMessage();
            Log.e("ERROR_BL", res.toString());

        }
        return res;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        pb_ajout.setVisibility(View.GONE);
        if (isSuccess) {

            Toast.makeText(activity, "Article ajouté ajouté avec success ", Toast.LENGTH_LONG).show();


            InventaireActivity.txt_res.setText("Article ajouté avec Succès");
            InventaireActivity.txt_res.setTextColor(activity.getResources().getColor(R.color.GreenCall));
            InventaireActivity.img_res.setImageResource(R.drawable.ic_success);


            InventaireActivity.txt_res.setVisibility(View.VISIBLE);
            InventaireActivity.img_res.setVisibility(View.VISIBLE);



            InventaireActivity. _ed_code_a_barre.setText("");

            InventaireActivity. _ed_code_a_barre.setFocusableInTouchMode(true);
            InventaireActivity._ed_code_a_barre.requestFocus();


            InventaireActivity. ll_echec_recherche.setVisibility(View.INVISIBLE);
            InventaireActivity.   ll_recherche.setVisibility(View.INVISIBLE);
            InventaireActivity. ll_trouve_avec_success.setVisibility(View.INVISIBLE);

            InventaireActivity.txt_res.setVisibility(View.INVISIBLE);
            InventaireActivity. img_res.setVisibility(View.INVISIBLE);

            InventaireActivity. btn_ajouter_ligne_inventaire.setEnabled(false);
            InventaireActivity. btn_ajouter_ligne_inventaire.setBackgroundColor(Color.parseColor("#F0F0F0"));



        } else {
            Toast.makeText(activity, "Error  " + s, Toast.LENGTH_LONG).show();


            InventaireActivity.txt_res.setText("Problème de Sauvegarde  : \n" + s);
            InventaireActivity.txt_res.setTextColor(activity.getResources().getColor(R.color.RedMail));
            InventaireActivity.img_res.setImageResource(R.drawable.wrong);


            InventaireActivity.txt_res.setVisibility(View.VISIBLE);
            InventaireActivity.img_res.setVisibility(View.VISIBLE);


            InventaireActivity.  btn_ajouter_ligne_inventaire.setEnabled(false);
            InventaireActivity.  btn_ajouter_ligne_inventaire.setBackgroundColor(Color.parseColor("#F0F0F0"));
        }


    }


}
