package com.example.faten.testsql.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.R;
import com.example.faten.testsql.adapter.ArticlePanierAdapter;
import com.example.faten.testsql.adapter.ArticleQtDispoStockAdapterRV;
import com.example.faten.testsql.model.Article;
import com.example.faten.testsql.model.ArticleStock;
import com.example.faten.testsql.model.BonCommandeVente;
import com.example.faten.testsql.model.ComptageAndroid;
import com.example.faten.testsql.model.LigneBonCommandeVente;
import com.example.faten.testsql.model.ReservationArticleDansDepot;
import com.example.faten.testsql.task.InsertBC_LBC_ReservationTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class BonCommandeVenteAvecTerminalActivity extends AppCompatActivity {

    String CodeClient;
    String RaisonSocial;


    private TextInputLayout ed_code_a_barre,   ed_quantite;
    public static EditText _ed_code_a_barre,  _ed_quantite;
    TextView txt_designation ;


    // SearchView _ed_code_a_barre  ;
    public static LinearLayout ll_recherche, ll_trouve_avec_success, ll_echec_recherche;


    String NomUtilisateur, codelivreur, CodeDepot, Depot;
    Boolean ForcageBonCommande;
    DecimalFormat decFormat = new DecimalFormat("0.00");


    CardView btn_clear_data;

    public static CardView btn_ajouter_au_panier;
    public static CardView btn_lancer_bon_cmd;


    public static ProgressBar p_bar_ajout;


    LinearLayout ll_situation_de_stock;
    RecyclerView rv_list_qt_stock;
    TextView txt_qt_cmd;
    ProgressBar pb_list_stock;


    public static int qt_a_cmd = 0;

    public static ArrayList<ArticleStock> listArticleDispo = new ArrayList<>();
    public static ArticleQtDispoStockAdapterRV articleQtDispoStockAdapterRV;



    public static  ArrayList<ReservationArticleDansDepot> listReservation = new ArrayList<>();

    public static ArticleStock articleFoundCMD;
    public static ArrayList<ArticleStock> listArticlePanier = new ArrayList<>();
    public static ArticlePanierAdapter articlePanierAdapter;
    public static RecyclerView rv_list_panier;


    CardView btn_mon_inventaire;
    private static final int REQUEST_PHONE_CALL = 1;


    // répartition

    LinearLayout ll_repartition  ;
    TextInputLayout  ed_quantie_saisie ;
    CardView   btn_repartition ;

    TextView txt_qt_dispo ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bon_commande_vente_avec_terminal);


        CodeClient = getIntent().getStringExtra("code_client");
        RaisonSocial = getIntent().getStringExtra("raison_client");

        TextView txt_code_client = findViewById(R.id.txt_code_client);
        TextView txt_raison_client = findViewById(R.id.txt_raison_client);


        txt_code_client.setText("(" + CodeClient + ")");
        txt_raison_client.setText(RaisonSocial);


        p_bar_ajout = findViewById(R.id.p_bar_ajout);
        p_bar_ajout.setVisibility(View.GONE);
        btn_mon_inventaire = (CardView) findViewById(R.id.btn_mon_inventaire);

        txt_designation = findViewById(R.id.txt_designation);
        ed_code_a_barre = findViewById(R.id.ed_code_a_barre);
        ed_quantite = findViewById(R.id.ed_quantie);

        pb_list_stock = (ProgressBar) findViewById(R.id.pb_list_stock);
        txt_qt_cmd = (TextView) findViewById(R.id.txt_qt_commnder);
        pb_list_stock.setVisibility(View.GONE);

        ll_situation_de_stock = (LinearLayout) findViewById(R.id.ll_situation_de_stock);
        rv_list_qt_stock = (RecyclerView) findViewById(R.id.rv_list_qt_stock);
        ll_situation_de_stock.setVisibility(View.GONE);

        rv_list_qt_stock.setHasFixedSize(true);
        rv_list_qt_stock.setLayoutManager(new LinearLayoutManager(this));


        ll_repartition  = (LinearLayout)  findViewById(R.id.ll_repartition) ;
        ed_quantie_saisie  = (TextInputLayout)  findViewById(R.id.ed_quantie_saisie) ;
        btn_repartition  = (CardView)  findViewById(R.id.btn_repartition);
        txt_qt_dispo  = (TextView)  findViewById(R.id.txt_qt_dispo) ;
        ll_repartition.setVisibility(View.GONE);


        _ed_code_a_barre = findViewById(R.id._ed_code_a_barre);
        _ed_quantite = findViewById(R.id._ed_qunatite);

        btn_clear_data = (CardView) findViewById(R.id.btn_clear_data);
        btn_lancer_bon_cmd = (CardView) findViewById(R.id.btn_lance_bon_cmd);

        btn_ajouter_au_panier = (CardView) findViewById(R.id.btn_ajouter_au_panier);
        btn_ajouter_au_panier.setEnabled(false);
        btn_ajouter_au_panier.setBackgroundColor(Color.parseColor("#F0F0F0"));

        rv_list_panier = (RecyclerView) findViewById(R.id.rv_cmd_panier_article);
        rv_list_panier.setHasFixedSize(true);
        rv_list_panier.setLayoutManager(new LinearLayoutManager(this));


        btn_lancer_bon_cmd.setEnabled(false);
        btn_lancer_bon_cmd.setBackgroundColor(Color.parseColor("#F0F0F0"));


        ll_recherche = findViewById(R.id.ll_recherche);
        ll_trouve_avec_success = findViewById(R.id.ll_trouve_avec_success);
        ll_echec_recherche = findViewById(R.id.ll_echec_recherche);


        ll_recherche.setVisibility(View.INVISIBLE);
        ll_echec_recherche.setVisibility(View.INVISIBLE);
        listArticlePanier = new ArrayList<>();

        listReservation = new ArrayList<>();

        articlePanierAdapter = new ArticlePanierAdapter(this, listArticlePanier);
        rv_list_panier.setAdapter(articlePanierAdapter);

        SharedPreferences pref = getSharedPreferences("usersession", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        NomUtilisateur = pref.getString("NomUtilisateur", NomUtilisateur);
        codelivreur = pref.getString("CodeLivreur", codelivreur);
        CodeDepot = pref.getString("CodeDepot", CodeDepot);
        Depot = pref.getString("Depot", Depot);

        ForcageBonCommande = pref.getBoolean("ForcageBonCommande", false);

        Log.e("ForcageBonCommande", "" + ForcageBonCommande);


        _ed_code_a_barre.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Log.e("event_enter_clav", "captured");


                    _ed_quantite.setText("0");

                    _ed_quantite.setFocusableInTouchMode(true);
                    _ed_quantite.requestFocus();

                    _ed_quantite.setEnabled(false);
                    _ed_quantite.setSelection(_ed_quantite.getText().length());

                    if (!_ed_code_a_barre.getText().toString().equals(""))

                    {
                        RechercheArticleTask2 rechercheArticleTask = new RechercheArticleTask2(BonCommandeVenteAvecTerminalActivity.this, _ed_code_a_barre.getText().toString()
                                , ll_recherche, ll_trouve_avec_success, txt_designation, ll_echec_recherche, _ed_quantite, ed_quantite
                        );
                        rechercheArticleTask.execute();
                    }


                    return false;
                }

                return false;
            }
        });


        btn_clear_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initialiser();

            }
        });


        _ed_quantite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("ed_qt", "" + _ed_quantite.getText().toString());

                int qt_cmd = Integer.parseInt(_ed_quantite.getText().toString());









                if (qt_cmd == 0) {

                    btn_ajouter_au_panier.setEnabled(false);
                    btn_ajouter_au_panier.setBackgroundColor(Color.parseColor("#F0F0F0"));


                } else {

                    btn_ajouter_au_panier.setEnabled(true);
                    btn_ajouter_au_panier.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    btn_ajouter_au_panier.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            articleFoundCMD.setQtePanier(qt_cmd);


                            boolean existe = false;


                            for (ArticleStock art : listArticlePanier) {

                                if (art.getCodeArticle().equals(articleFoundCMD.getCodeArticle())) {

                                    existe = true;
                                    break;
                                }
                            }

                            if (existe)
                            {
                                Toast.makeText(BonCommandeVenteAvecTerminalActivity.this, " Cet Article " + articleFoundCMD.getCodeArticle() + " déja commandé ", Toast.LENGTH_LONG).show();
                            }

                            else {

                                //  insert cmd here
                                int qt_tot_stock = 0;
                                int qt_tot_cmd = 0;
                                int qt_deja_commande = 0;

                                for (ArticleStock artS : listArticleDispo) {
                                    qt_deja_commande = artS.getQteCMD();
                                    qt_tot_stock = qt_tot_stock + artS.getQuantite();
                                }

                                for (ArticleStock artS : listArticleDispo) {

                                    if (artS.getNbrCLick() > 0) {

                                        ReservationArticleDansDepot reservationArticleDansDepot = new ReservationArticleDansDepot("", artS.getCodeArticle(), artS.getDesignation(), CodeDepot,Depot , artS.getCodeDepot() ,artS.getDepot(), artS.getNbrCLick(), 0, qt_tot_stock , qt_deja_commande);
                                        listReservation.add(reservationArticleDansDepot);

                                        qt_tot_cmd = qt_tot_cmd + artS.getNbrCLick();
                                    }

                                }


                                if ((qt_tot_stock - qt_deja_commande) >= qt_tot_cmd) //  si  la qu demandé  est inf à la  qt  disponible
                                {

                                    listArticlePanier.add(articleFoundCMD);

                                    articlePanierAdapter = new ArticlePanierAdapter(BonCommandeVenteAvecTerminalActivity.this, listArticlePanier);
                                    rv_list_panier.setAdapter(articlePanierAdapter);


                                } else {

                                    if (ForcageBonCommande == false) {
                                        // forcage  de commande
                                        Toast.makeText(BonCommandeVenteAvecTerminalActivity.this, "Vous ne pouvez Commander que " + (qt_tot_stock - qt_deja_commande) + " Pièces", Toast.LENGTH_LONG).show();

                                    } else if (ForcageBonCommande == true) {

                                        Toast.makeText(BonCommandeVenteAvecTerminalActivity.this, "Forcage de Commande ", Toast.LENGTH_LONG).show();

                                        //showDialogConfirmer(qt_tot_cmd, listReservation);

                                        showDialogForcer();



                                    }

                                }

                                initialiser();


                                if (listArticlePanier.size() >0 )
                                {
                                    btn_lancer_bon_cmd.setEnabled(true);
                                    btn_lancer_bon_cmd.setBackgroundColor(Color.parseColor("#FFFFFF"));


                                    btn_lancer_bon_cmd.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            btn_lancer_bon_cmd.setEnabled(false);
                                            btn_lancer_bon_cmd.setBackgroundColor(Color.parseColor("#F0F0F0"));

                                            btn_ajouter_au_panier.setEnabled(false);
                                            btn_ajouter_au_panier.setBackgroundColor(Color.parseColor("#F0F0F0"));

                                            BonCommande();

                                        }
                                    });



                                }


                            }


                        }
                    });

                    
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        btn_repartition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  repart  here

                ed_quantie_saisie.clearFocus();
                ed_code_a_barre.clearFocus();
                ed_code_a_barre.setFocusableInTouchMode(false);

                _ed_code_a_barre.clearFocus();
                _ed_code_a_barre.setFocusableInTouchMode(false);

                hideKeyBoard(BonCommandeVenteAvecTerminalActivity.this  ,_ed_code_a_barre) ;

                ed_quantie_saisie.setError(null);

                repartition() ;


            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {

        }


    }
    public static void hideKeyBoard(Context context, View view) {
        try {
            InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialiser() {

        _ed_code_a_barre.setText("");

        _ed_quantite.setText("0");

        _ed_code_a_barre.setFocusableInTouchMode(true);
        _ed_code_a_barre.requestFocus();

        ll_echec_recherche.setVisibility(View.INVISIBLE);
        ll_recherche.setVisibility(View.INVISIBLE);
        ll_trouve_avec_success.setVisibility(View.INVISIBLE);


          btn_ajouter_au_panier.setEnabled(false);
          btn_ajouter_au_panier.setBackgroundColor(Color.parseColor("#F0F0F0"));


        listArticleDispo.clear();
         articleQtDispoStockAdapterRV  = new ArticleQtDispoStockAdapterRV(BonCommandeVenteAvecTerminalActivity.this, listArticleDispo);
        rv_list_qt_stock.setAdapter(articleQtDispoStockAdapterRV);
        txt_qt_cmd.setText("");

        ed_quantie_saisie.setError(null);
        ll_situation_de_stock.setVisibility(View.GONE);

        ll_repartition.setVisibility(View.GONE);
    }

    public void showDialogForcer() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Voulez-Vous forcer la commande ?")

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listArticlePanier.add(articleFoundCMD);

                        articlePanierAdapter = new ArticlePanierAdapter(BonCommandeVenteAvecTerminalActivity.this, listArticlePanier);
                        rv_list_panier.setAdapter(articlePanierAdapter);

                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        dialog.show();
    }



    public void showDialogConfirmer(int qt_tot_cmd, ArrayList<ReservationArticleDansDepot> listReservation) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Voulez-Vous forcer la commande ?")

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        dialog.show();
    }


    public void BonCommande() {



        ArrayList<LigneBonCommandeVente>  listLigneBonCommandeVente  = new ArrayList<>() ;



            double _total_ht = 0;
            double _total_tva = 0;
            double _total_ttc = 0;
            double _total_remise = 0;
            double _total_net_ht = 0;
            double _total_note_article = 0;

            int order = 1;

            for (ArticleStock article : listArticlePanier) {

                if (article.getQtePanier() > 0) {

                    String _num_bon_commande = "";
                    String _code_article = article.getCodeArticle();
                    String _designation_article = article.getDesignation();
                    int _num_order = order;
                    double _prix_achat_ht = article.getPrixAchatHT();
                    double _prix_vente_ht = article.getPrixVenteHT();
                    double _prix_achat_net = article.getPrixAchatHT();


                    int _quantite = article.getQtePanier();
                    double _montant_ht = _quantite * article.getPrixVenteHT();
                    double _taux_tva = article.getTauxTVA();

                    double _taux_remise = 0;
                    double _montant_remise = _montant_ht * _taux_remise / 100;

                    double _TempHT = _montant_ht - _montant_remise;


                    double _montant_tva = _TempHT * _taux_tva / 100;
                    double _montant_ttc = _montant_ht - _montant_remise + _montant_tva;

                    String _observation = "bl_android_rec";
                    double _prix_unitaireNetTTC = _montant_ttc / _quantite;

                    double _net_ht = _montant_ht;


                    LigneBonCommandeVente ligneBonCommandeVente = new LigneBonCommandeVente(_num_bon_commande, _code_article, _designation_article, _num_order, _prix_vente_ht, _quantite, _montant_ht, _taux_tva, _montant_tva, _montant_ttc, _observation, _taux_remise, _montant_remise, 0, _net_ht, _prix_achat_net);

                    listLigneBonCommandeVente.add(ligneBonCommandeVente) ;

                    _total_ht = _total_ht + _montant_ht;
                    _total_tva = _total_tva + _montant_tva;
                    _total_ttc = _total_ttc + _montant_ttc;
                    _total_remise = _total_remise + _montant_remise;
                    _total_net_ht = _total_net_ht + _net_ht;



                    order++;


                }



        }


        Date currentDate = new Date();
        BonCommandeVente bonCommandeVente = new BonCommandeVente("", currentDate, "", CodeClient, "", currentDate,
                1, _total_ht, _total_tva, _total_ttc, "E01", NomUtilisateur, currentDate, currentDate, "", _total_remise, 0, _total_net_ht, 0.0, 0.0, codelivreur);


        Log.e("CMD_ligne", listLigneBonCommandeVente.toString());
        Log.e("CMD_", bonCommandeVente.toString());
        Log.e("CMD_reservation", listReservation.toString());


        InsertBC_LBC_ReservationTask insertBC_lbc_reservationTask = new InsertBC_LBC_ReservationTask(BonCommandeVenteAvecTerminalActivity.this, bonCommandeVente, listLigneBonCommandeVente, listReservation, p_bar_ajout);
        insertBC_lbc_reservationTask.execute();


    }


    class RechercheArticleTask2 extends AsyncTask<String, String, String> {
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


        public RechercheArticleTask2(Activity activity, String codeArticle, LinearLayout ll_recherche, LinearLayout ll_art_foud_success, TextView txt_designation, LinearLayout ll_art_not_found
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

                        articleFound = new Article(CodeArticle , CodeBarre , Designation);


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


                GetQuantiteDisponible getQuantiteDisponible = new GetQuantiteDisponible(activity, articleFound.getCodeArticle(), rv_list_qt_stock, txt_qt_cmd);
                getQuantiteDisponible.execute();


            } else {


                ll_recherche.setVisibility(View.GONE);
                ll_art_foud_success.setVisibility(View.GONE);
                ll_art_not_found.setVisibility(View.VISIBLE);


                BonCommandeVenteAvecTerminalActivity.btn_lancer_bon_cmd.setEnabled(false);
                BonCommandeVenteAvecTerminalActivity.btn_lancer_bon_cmd.setBackgroundColor(Color.parseColor("#F0F0F0"));


            }


        }


    }


    class GetQuantiteDisponible extends AsyncTask<String, String, String> {

        String z = "";


        ConnectionClass connectionClass;
        String user, password, base, ip;


        Activity activity;
        String CodeArticle;

        RecyclerView rv_list_qt_dispo;
        TextView txt_qt_cmd;


        public GetQuantiteDisponible(Activity activity, String codeArticle, RecyclerView rv_list_qt_dispo, TextView txt_qt_cmd) {
            this.activity = activity;
            CodeArticle = codeArticle;
            this.rv_list_qt_dispo = rv_list_qt_dispo;
            this.txt_qt_cmd = txt_qt_cmd;


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

            ll_situation_de_stock.setVisibility(View.VISIBLE);
            pb_list_stock.setVisibility(View.VISIBLE);

        }


        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {


                    String queryArticle = " select  Depot.CodeDepot  , Depot.Libelle as Depot \n" +
                            "    , case when Depot.CodeDepot = '01'  then  ( select  Tel2 from Societe ) \n" +
                            "      else \n" +
                            "     (select top (1) Tel1 from Livreur where  CodeDepot  = Depot.CodeDepot \n" +
                            "     and CodeLivreur = (Select CodeLivreur from  Utilisateur  where CodeDepot  = Depot .CodeDepot)\n" +
                            "     ) end   as TelPhone  \n" +
                            "     \n" +
                            "     , Article.CodeArticle  , Designation , CodeTVA ,PrixVenteTTC , PrixVenteHT  , PrixAchatHT  ,Quantite ,\n" +
                            "        ( Select     0 +   SUM(QuantiteCMD) As  QuantiteCMD  from  \n" +
                            "        (\n" +
                            "        select 0 As QuantiteCMD  from Article \n" +
                            "        where CodeArticle=Article.CodeArticle \n" +
                            "        group by Article.CodeArticle\n" +
                            "        \n" +
                            "        union all \n" +
                            "        \n" +
                            "        select  SUM(LigneBonCommandeVente.Quantite) As QuantiteCMD \n" +
                            "        from LigneBonCommandeVente \n" +
                            "        inner join \n" +
                            "        BonCommandeVente on LigneBonCommandeVente.NumeroBonCommandeVente = BonCommandeVente.NumeroBonCommandeVente \n" +
                            "        where CodeArticle=  Stock.CodeArticle  and NumeroEtat = 'E01'\n" +
                            "        group by LigneBonCommandeVente.CodeArticle\n" +
                            "        \n" +
                            "        union all \n" +
                            "        \n" +
                            "        select SUM(LigneBonCommandeVente.Quantite) As QuantiteCMD \n" +
                            "        from LigneBonCommandeVente \n" +
                            "        inner join \n" +
                            "        BonCommandeVente on LigneBonCommandeVente.NumeroBonCommandeVente = BonCommandeVente.NumeroBonCommandeVente \n" +
                            "        left join LigneBonLivraisonVente on   LigneBonLivraisonVente.NumeroBonCommandeVente = LigneBonCommandeVente.NumeroBonCommandeVente\n" +
                            "        and LigneBonLivraisonVente.CodeArticle= LigneBonCommandeVente.CodeArticle\n" +
                            "        where LigneBonCommandeVente.CodeArticle= Stock.CodeArticle  and NumeroEtat='E02'  and NumeroBonLivraisonVente is null\n" +
                            "        group by LigneBonCommandeVente.CodeArticle ) As Liste \n" +
                            "        \n" +
                            "        ) AS QteCMD" +
                            "    \n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "from  Stock  \n" +
                            "inner join Depot on Depot.CodeDepot  = Stock.CodeDepot\n" +
                            "INNER JOIN Article  on Article.CodeArticle  = Stock.CodeArticle\n" +
                            "where Stock.CodeArticle  = '" + CodeArticle + "' and Quantite  != 0  " +
                            "  and (  Depot.PersonnePhysique  =  1  or  Depot.CodeDepot = '01'  )";

                    Log.e("queryArticle_x", queryArticle);

                    PreparedStatement ps = con.prepareStatement(queryArticle);
                    ResultSet rs = ps.executeQuery();

                    listArticleDispo.clear();
                    while (rs.next()) {

                        String CodeDepot = rs.getString("CodeDepot");
                        String Depot = rs.getString("Depot");
                        String TelPhone = rs.getString("TelPhone");


                        String CodeArticle = rs.getString("CodeArticle");
                        String Designation = rs.getString("Designation");
                        double PrixVenteTTC = rs.getDouble("PrixVenteTTC");
                        double PrixVenteHT = rs.getDouble("PrixVenteHT");
                        double PrixAchatHT = rs.getDouble("prixAchatHT");

                        int CodeTVA = rs.getInt("CodeTVA");

                        int Quantite = rs.getInt("Quantite");
                        int QteCMD = rs.getInt("QteCMD");


                        ArticleStock article = new ArticleStock(CodeDepot, Depot, TelPhone, CodeArticle, Designation, PrixVenteTTC, PrixVenteHT, PrixAchatHT, CodeTVA, 0, Quantite, QteCMD, 0);
                        listArticleDispo.add(article);

                        BonCommandeVenteAvecTerminalActivity.articleFoundCMD = new ArticleStock(CodeArticle, Designation, PrixVenteTTC, PrixVenteHT, PrixAchatHT, CodeTVA, 0, 0);


                    }


                }
            } catch (SQLException ex) {

                Log.e("ERROR_article", ex.getMessage().toString());
                z = "";
            }


            return z;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            pb_list_stock.setVisibility(View.GONE);

            try {

                txt_qt_cmd.setText("   " + listArticleDispo.get(0).getQteCMD() + "  CMD ");

            } catch (Exception ex) {

            }

            ArrayList<String >  listCodeDepot  = new ArrayList<>() ;
            for (ArticleStock  a  : listArticleDispo)
            {
                listCodeDepot.add(a.getCodeDepot()) ;
            }

            Log.e("listCodeDepot",listCodeDepot.toString()) ;

            Iterator<ArticleStock> iter = listArticleDispo.iterator();

            if (listCodeDepot.contains("01")  && listCodeDepot.contains(CodeDepot))
            {

                Log.e("listCodeDepot"," 01 - " +CodeDepot) ;

                 while (iter.hasNext()) {

                    ArticleStock  a = iter.next();

                    if (a.getCodeDepot().equals("01")) {

                        // replace 0   with index of
                        int  last_index = listArticleDispo.indexOf(a) ; //3

                        ArticleStock   a_copie =  listArticleDispo.get(0);

                        listArticleDispo.set(0 , a) ;
                        listArticleDispo.set(last_index , a_copie ) ;

                    }

                    if (a.getCodeDepot().equals(CodeDepot)) {


                        int  last_index = listArticleDispo.indexOf(a) ; //3

                        ArticleStock   a_copie =  listArticleDispo.get(1);

                        listArticleDispo.set(1 , a) ;
                        listArticleDispo.set(last_index , a_copie ) ;
                    }

                }

            }


           else if   ((listCodeDepot.contains("01"))  &&   !listCodeDepot.contains(CodeDepot) )
            {


                while (iter.hasNext()) {

                    ArticleStock  a = iter.next();

                    if (a.getCodeDepot().equals("01")) {

                        // replace 0   with index of
                        int  last_index = listArticleDispo.indexOf(a) ; //3

                        ArticleStock   a_copie =  listArticleDispo.get(0);

                        listArticleDispo.set(0 , a) ;
                        listArticleDispo.set(last_index , a_copie ) ;

                    }

                }
            }

            else if   (( !listCodeDepot.contains("01"))  &&    listCodeDepot.contains(CodeDepot) )
            {

                while (iter.hasNext()) {

                    ArticleStock  a = iter.next();

                    if (a.getCodeDepot().equals(CodeDepot)) {


                        int  last_index = listArticleDispo.indexOf(a) ; //3

                        ArticleStock   a_copie =  listArticleDispo.get(0);

                        listArticleDispo.set(0 , a) ;
                        listArticleDispo.set(last_index , a_copie ) ;
                    }

                }
            }


            int qt_stock  =0 ;

            for (ArticleStock  as  : listArticleDispo)
            {
                qt_stock  = qt_stock + as.getQuantite()  ;
            }


            if (listArticleDispo.size()>0)

            {
                int qt_dispo = qt_stock - listArticleDispo.get(0).getQteCMD();

                txt_qt_dispo.setText("Quantité disponible : " + qt_dispo);


                Animation topAnim = AnimationUtils.loadAnimation(BonCommandeVenteAvecTerminalActivity.this, R.anim.fade);
                txt_qt_dispo.setAnimation(topAnim);


                ll_situation_de_stock.setVisibility(View.VISIBLE);
                articleQtDispoStockAdapterRV = new ArticleQtDispoStockAdapterRV(activity, listArticleDispo);
                rv_list_qt_dispo.setAdapter(articleQtDispoStockAdapterRV);

                ll_repartition.setVisibility(View.VISIBLE);

                ed_quantie_saisie.getEditText().setText("1");


                ed_quantie_saisie.setFocusableInTouchMode(true);
                ed_quantie_saisie.requestFocus();

            }

        }


    }

    public   void   repartition  ()
    {
         // initialiser

        for (ArticleStock as  : listArticleDispo)
        {

            as.setNbrCLick(0);
        }
        articleQtDispoStockAdapterRV.notifyDataSetChanged();


        int qt_saisie=  0;

        try {
            qt_saisie  = Integer.parseInt( ed_quantie_saisie.getEditText().getText().toString() );
        }

        catch (Exception ex)
        {

            ed_quantie_saisie.setError("Saisir une quantité avant la répartition");
        }





      int qt_cmd =  listArticleDispo.get(0).getQteCMD() ;

      int qt_stock = 0  ;


       for (ArticleStock  a  : listArticleDispo)
       {

           qt_stock = qt_stock + a.getQuantite() ;

       }

       int qt_dispo  = qt_stock-qt_cmd ;


       Log.e("QT_DISPO" ,""+qt_dispo);
       if (qt_saisie <=  qt_dispo)
       {

           int qt_fil = qt_saisie ;

           for (ArticleStock  as  : listArticleDispo)

           {

               if (  qt_fil >= 0  )
               {
                   Log.e("ArticleS "+listArticleDispo.indexOf(as) ,"qt_fil "+qt_fil) ;

                   if (as.getQuantite()>  qt_fil)
                   {
                       //Log.e("ArticleS "+listArticleDispo.indexOf(as) ,"Condition 1") ;
                       as.setNbrCLick(qt_fil);
                       qt_fil = 0 ;
                   }

                 else   if (as.getQuantite()==  qt_fil)
                   {
                       //Log.e("ArticleS "+listArticleDispo.indexOf(as) ,"Condition 2") ;
                       as.setNbrCLick(qt_fil);
                       qt_fil =0;
                   }

                   else if (as.getQuantite()< qt_fil)
                   {
                       //Log.e("ArticleS "+listArticleDispo.indexOf(as) ,"Condition 3") ;
                       as.setNbrCLick(as.getQuantite());
                       qt_fil =qt_fil - as.getNbrCLick() ;
                   }



               }


               if (qt_fil == 0)
               {
                   articleQtDispoStockAdapterRV.notifyDataSetChanged();
                   break;
               }

           }

           BonCommandeVenteAvecTerminalActivity.qt_a_cmd = qt_saisie ;
           BonCommandeVenteAvecTerminalActivity._ed_quantite.setText("" + BonCommandeVenteAvecTerminalActivity.qt_a_cmd);


           articleQtDispoStockAdapterRV.notifyDataSetChanged();

       }



       else  if (qt_saisie> qt_dispo)
       {

           BonCommandeVenteAvecTerminalActivity.qt_a_cmd = 0 ;
           BonCommandeVenteAvecTerminalActivity._ed_quantite.setText("" + BonCommandeVenteAvecTerminalActivity.qt_a_cmd);

           ed_quantie_saisie.setError("Quantité disponible :" +qt_dispo);

       }




    }

}
