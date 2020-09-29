package com.example.faten.testsql.activity;

import android.app.Activity;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.R;
import com.example.faten.testsql.model.Article;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class BonLivraisonActivity extends AppCompatActivity {


    private TextInputLayout ed_code_a_barre, ed_taux_remise, ed_ttc  , ed_quantite ;

    EditText _ed_code_a_barre, _ed_taux_remise, _ed_ttc , _ed_quantite  ;

    // SearchView _ed_code_a_barre  ;
    LinearLayout ll_recherche, ll_trouve_avec_success, ll_echec_recherche;


    String NomUtilisateur, codelivreur;
    DecimalFormat decFormat = new DecimalFormat("0.00");


    TextView txt_plafond_remise;
    public static double plafondRemise = 0;

    public static double  pvHT_article_i  = 0;
    public static double  tauxTVA_article_i  = 0;

    CardView  btn_clear_data ;

    CardView   card_palfond_remise ;

    CardView   btn_ajouter_ligne ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bon_livraison);



        ed_code_a_barre = findViewById(R.id.ed_code_a_barre);
        ed_taux_remise = findViewById(R.id.ed_taux_remise);
        ed_ttc = findViewById(R.id.ed_ttc);
        ed_quantite = findViewById(R.id.ed_quantie) ;

        _ed_code_a_barre = findViewById(R.id._ed_code_a_barre);
        _ed_taux_remise = findViewById(R.id._ed_taux_remise);
        _ed_ttc = findViewById(R.id._ed_ttc);
        _ed_quantite = findViewById(R.id._ed_qunatite) ;



        btn_clear_data = (CardView)   findViewById(R.id.btn_clear_data)  ;
        card_palfond_remise  = (CardView)   findViewById(R.id.card_palfond_remise)  ;
        btn_ajouter_ligne = (CardView)   findViewById(R.id.btn_ajouter_ligne)  ;

        btn_ajouter_ligne.setEnabled(false);
        btn_ajouter_ligne.setBackgroundColor(Color.parseColor("#F0F0F0"));

        txt_plafond_remise = findViewById(R.id.txt_plafond_remise);

        ll_recherche = findViewById(R.id.ll_recherche);
        ll_trouve_avec_success = findViewById(R.id.ll_trouve_avec_success);
        ll_echec_recherche = findViewById(R.id.ll_echec_recherche);
        ll_recherche.setVisibility(View.INVISIBLE);
        ll_echec_recherche.setVisibility(View.INVISIBLE);

        card_palfond_remise.setVisibility(View.INVISIBLE);
        _ed_ttc.setEnabled(false);


        SharedPreferences pref = getSharedPreferences("usersession", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        NomUtilisateur = pref.getString("NomUtilisateur", NomUtilisateur);
        codelivreur = pref.getString("CodeLivreur", codelivreur);



        GetPlafondRemiseVente getPlafondRemiseVente = new GetPlafondRemiseVente(BonLivraisonActivity.this, NomUtilisateur, txt_plafond_remise);
        getPlafondRemiseVente.execute();


        _ed_code_a_barre.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Log.e("event_enter_clav", "captured");

                    RechercheArticleTask rechercheArticleTask = new RechercheArticleTask(BonLivraisonActivity.this, _ed_code_a_barre.getText().toString(),
                            ll_recherche, ll_trouve_avec_success, ll_echec_recherche , _ed_taux_remise  ,_ed_ttc ,_ed_quantite  ,ed_quantite);
                    rechercheArticleTask.execute();


                    return false;
                }

                return false;
            }
        });


        _ed_ttc.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Log.e("ttc_keyboard", " "+_ed_ttc.getText().toString());


                    double ttc_saisie  = Double.parseDouble(_ed_ttc.getText().toString());
                    double prix_ht_i_article   = pvHT_article_i ;
                    double taux_TVA_i_article  = tauxTVA_article_i  ;
                    double __tva  =(1+taux_TVA_i_article/100  ) ;

                    double  _montant_remise  =   (prix_ht_i_article * __tva - ttc_saisie  ) /  (__tva ) ;


                    double _tauxRemise = ( _montant_remise*100 ) /   prix_ht_i_article ;


                    _ed_taux_remise.setText(decFormat.format(_tauxRemise) +" %");


                    if (_tauxRemise> plafondRemise)
                    {
                        ed_taux_remise.setError("Vous avez dépassé votre Taux de Remise !!") ;
                        card_palfond_remise.setVisibility(View.VISIBLE);
                    }


                    return false;
                }

                return false;
            }
        });


        btn_clear_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {

                _ed_code_a_barre.setText("");
                _ed_taux_remise.setText("");
                _ed_ttc.setText("");


                _ed_code_a_barre.setFocusableInTouchMode(true);
                _ed_code_a_barre.requestFocus();

                ed_taux_remise.setError(null);
                ed_taux_remise.setErrorEnabled(false);

                card_palfond_remise.setVisibility(View.INVISIBLE);

            }
        });




    }

    private Boolean ErrorRemise() {
        String val = ed_taux_remise.getEditText().getText().toString();

        if (val.isEmpty()) {
            ed_taux_remise.setError("le champ ne peut pas être vide");
            return false;
        } else {
            ed_taux_remise.setError(null);
            ed_taux_remise.setErrorEnabled(false);
            return true;
        }
    }

}

class RechercheArticleTask extends AsyncTask<String, String, String> {
    String z = "";


    ConnectionClass connectionClass;
    String user, password, base, ip;


    Activity activity;
    String CodeArticle;

    Article articleFound;
    LinearLayout ll_recherche;
    LinearLayout ll_art_foud_success;
    LinearLayout ll_art_not_found;

    EditText ed_Remise ;
    EditText ed_Prix_vente_ttc  ;
    EditText ed_quantite  ;
    TextInputLayout lil_ed_quantite ;

    boolean isFound = false;


    public RechercheArticleTask(Activity activity, String codeArticle, LinearLayout ll_recherche, LinearLayout ll_art_foud_success, LinearLayout ll_art_not_found,EditText ed_Remise ,
            EditText ed_Prix_vente_ttc ,EditText ed_quantite ,  TextInputLayout lil_ed_quantite ) {
        this.activity = activity;
        CodeArticle = codeArticle;
        this.ll_recherche = ll_recherche;
        this.ll_art_foud_success = ll_art_foud_success;
        this.ll_art_not_found = ll_art_not_found;
        this.ed_Remise = ed_Remise  ;
        this.ed_Prix_vente_ttc = ed_Prix_vente_ttc  ;
        this.ed_quantite  = ed_quantite  ;
        this.lil_ed_quantite  = lil_ed_quantite  ;

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




                String queryArticle = " SELECT  Article.CodeArticle  , Designation ,   Article.CodeTVA , TVA.TauxTVA   , PrixVenteTTC , PrixVenteHT  , PrixAchatHT  , Quantite   ,\n" +
                        "    (Select   Quantite - SUM(QuantiteCMD) As QuantiteCMD from  \n" +
                        "    (\n" +
                        "    select 0 As QuantiteCMD  from Article\n" +
                        "    where CodeArticle=Article.CodeArticle   \n" +
                        "    group by Article.CodeArticle\n" +
                        "    \n" +
                        "    union all \n" +
                        "    \n" +
                        "    select  SUM(LigneBonCommandeVente.Quantite) As QuantiteCMD \n" +
                        "    from LigneBonCommandeVente \n" +
                        "    inner join \n" +
                        "    BonCommandeVente on LigneBonCommandeVente.NumeroBonCommandeVente = BonCommandeVente.NumeroBonCommandeVente \n" +
                        "    where CodeArticle=Article.CodeArticle  and NumeroEtat = 'E01'\n" +
                        "    group by LigneBonCommandeVente.CodeArticle\n" +
                        "    \n" +
                        "    union all \n" +
                        "    \n" +
                        "    select SUM(LigneBonCommandeVente.Quantite) As QuantiteCMD \n" +
                        "    from LigneBonCommandeVente \n" +
                        "    inner join \n" +
                        "    BonCommandeVente on LigneBonCommandeVente.NumeroBonCommandeVente = BonCommandeVente.NumeroBonCommandeVente \n" +
                        "    left join LigneBonLivraisonVente on   LigneBonLivraisonVente.NumeroBonCommandeVente = LigneBonCommandeVente.NumeroBonCommandeVente\n" +
                        "    and LigneBonLivraisonVente.CodeArticle= LigneBonCommandeVente.CodeArticle\n" +
                        "    where LigneBonCommandeVente.CodeArticle=Article.CodeArticle  and NumeroEtat='E02'  and NumeroBonLivraisonVente is null\n" +
                        "    group by LigneBonCommandeVente.CodeArticle ) As Liste \n" +
                        "    \n" +
                        "    ) AS QteRestante\n" +
                        "       \n" +
                        "    FROM Article\n" +
                        "    inner join  Stock on Article.CodeArticle=Stock.CodeArticle \n" +
                        "    inner  join TVA  on TVA.CodeTVA  =  Article.CodeTVA \n " +
                        "    where Quantite >0  and  Article.CodeArticle  = '" + CodeArticle + "' ";

                Log.e("queryArticle_x", queryArticle);

                PreparedStatement ps = con.prepareStatement(queryArticle);
                ResultSet rs = ps.executeQuery();


                while (rs.next()) {


                    String CodeArticle = rs.getString("CodeArticle");
                    String Designation = rs.getString("Designation");

                    int CodeTVA = rs.getInt("CodeTVA");
                    Double TauxTVA = rs.getDouble("TauxTVA");

                    Double PrixVenteTTC = rs.getDouble("PrixVenteTTC");
                    Double PrixVenteHT = rs.getDouble("PrixVenteHT");
                    int Quantite = rs.getInt("Quantite");
                    int QteRestante = rs.getInt("QteRestante");

                    articleFound = new Article(CodeArticle, Designation, CodeTVA, TauxTVA, PrixVenteTTC, PrixVenteHT, Quantite, QteRestante);

                    Log.e("article", "" + articleFound.toString());

                    z = "Success";
                    isFound = true;
                }


            }
        } catch (SQLException ex) {

            Log.e("ERROR_article" , ex.getMessage().toString()) ;
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

            ed_Remise.setText(decFormat.format(0) + " %");

            ed_Prix_vente_ttc.setText(articleFound.getPrixVenteTTC()+"");


            ed_Prix_vente_ttc.setFocusableInTouchMode(true);
            ed_Prix_vente_ttc.requestFocus();

            ed_Prix_vente_ttc.setEnabled(true);
            ed_Prix_vente_ttc.setSelection(ed_Prix_vente_ttc.getText().length());


            BonLivraisonActivity.pvHT_article_i = articleFound.getPrixVenteHT()  ;
            BonLivraisonActivity.tauxTVA_article_i = articleFound.getTauxTVA()  ;



          //  lil_ed_quantite.setError  (  " / "+articleFound.getQuantite()  ) ;



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


        }
        else {


            ll_recherche.setVisibility(View.GONE);
            ll_art_foud_success.setVisibility(View.GONE);
            ll_art_not_found.setVisibility(View.VISIBLE);
        }


    }


}


        class GetPlafondRemiseVente extends AsyncTask<String, String, String> {
    String z = "";


    ConnectionClass connectionClass;
    String user, password, base, ip;


    Activity activity;
    String NomUtilisateur;
    TextView txt_plafond_Taux_Remise;

    Double PlafondTauxRemiseVente;

    public GetPlafondRemiseVente(Activity activity, String NomUtilisateur, TextView txt_plafond_Taux_Remise) {

        this.activity = activity;
        this.NomUtilisateur = NomUtilisateur;
        this.txt_plafond_Taux_Remise = txt_plafond_Taux_Remise;


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


    }


    @Override
    protected String doInBackground(String... params) {

        try {
            Connection con = connectionClass.CONN(ip, password, user, base);
            if (con == null) {
                z = "Error in connection with SQL server";
            } else {

                String queryArticle = "  select  PlafondTauxRemiseVente  from    Utilisateur  where NomUtilisateur  = '" + NomUtilisateur + "'";

                Log.e("queryArticle", queryArticle);

                PreparedStatement ps = con.prepareStatement(queryArticle);
                ResultSet rs = ps.executeQuery();


                while (rs.next()) {

                    PlafondTauxRemiseVente = rs.getDouble("PlafondTauxRemiseVente");

                    Log.e("PlafondTauxRemiseVente", "" + PlafondTauxRemiseVente);

                    z = "Success";
                }


            }
        } catch (SQLException ex) {
            z = "list" + ex.toString();

        }
        return z;
    }


    @Override
    protected void onPostExecute(String r) {

        BonLivraisonActivity.plafondRemise = PlafondTauxRemiseVente;

        DecimalFormat decFormat = new DecimalFormat("0.00");

        txt_plafond_Taux_Remise.setText(decFormat.format(BonLivraisonActivity.plafondRemise) + " %");


    }


}