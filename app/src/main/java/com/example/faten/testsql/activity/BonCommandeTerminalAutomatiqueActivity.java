package com.example.faten.testsql.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.R;
import com.example.faten.testsql.StaticValues.ArticlePanierHelper;
import com.example.faten.testsql.StaticValues.Param;
import com.example.faten.testsql.StaticValues.ReservationArticleDansDepotHelper;
import com.example.faten.testsql.adapter.ArticlePanierAdapter;
import com.example.faten.testsql.adapter.ArticlePanierAutoAdapter;
import com.example.faten.testsql.adapter.ArticleQtDispoStockAdapterRV;
import com.example.faten.testsql.dialog.DialogCauseDeNonConfirmation;
import com.example.faten.testsql.dialog.DialogReservationDansAutreDepot;
import com.example.faten.testsql.dilaog.DialogDemandeNonValide;
import com.example.faten.testsql.model.Article;
import com.example.faten.testsql.model.ArticleStock;
import com.example.faten.testsql.model.BonCommandeVente;
import com.example.faten.testsql.model.LigneBonCommandeVente;
import com.example.faten.testsql.model.OrderDepotDemande;
import com.example.faten.testsql.model.ReservationArticleDansDepot;
import com.example.faten.testsql.task.DeletePieceNNVendu;
import com.example.faten.testsql.task.InsertBC_LBC_ReservationTask;
import com.example.faten.testsql.task.ListDepotDestinationParOrdre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class BonCommandeTerminalAutomatiqueActivity extends AppCompatActivity {


    String CodeClient;
    String RaisonSocial;

    private TextInputLayout ed_code_a_barre;
    public static EditText _ed_code_a_barre;
    public static LinearLayout ll_recherche, ll_trouve_avec_success, ll_echec_recherche;
    MediaPlayer mp;

    TextView txt_designation;


    public static CardView btn_lancer_bon_cmd;
    public static ProgressBar p_bar_ajout;
    public static ProgressBar p_bar_panier;

    public static ArrayList<ArticleStock> listArticleDispo;
    public static ArrayList<ReservationArticleDansDepot> listReservation;

    public static ArticleStock articleFoundCMD;
    public static ArrayList<ArticleStock> listArticlePanier;
    public static ArticlePanierAutoAdapter articlePanierAutoAdapter;
    public static RecyclerView rv_list_panier;


    CardView btn_clear_data;
    // Shared Prefrences
    String NomUtilisateur, codelivreur, CodeDepot, Depot;


    public static ArrayList<OrderDepotDemande> listOrderDepot;

    ArticlePanierHelper articlePanierHelper;
    ReservationArticleDansDepotHelper reservationArticleDansDepotHelper;
    TextView txt_sauvegard_existe;

    int CONDITION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bon_commande_terminal_automatique);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(arrow -> onBackPressed());


        articlePanierHelper = new ArticlePanierHelper(this);
        reservationArticleDansDepotHelper = new ReservationArticleDansDepotHelper(this);
        txt_sauvegard_existe = findViewById(R.id.sauvegard_exist);


        Log.e("SQLite", "panier_size " + articlePanierHelper.numberOfRows());
        Log.e("SQLite", "reservation_size " + reservationArticleDansDepotHelper.numberOfRows());


        DeletePieceNNVendu deletePieceNNVendu = new DeletePieceNNVendu(this) ;
        deletePieceNNVendu.execute() ;


        if (articlePanierHelper.numberOfRows() > 0) {
            txt_sauvegard_existe.setVisibility(View.VISIBLE);

            AlertDialog alertDialog = new AlertDialog.Builder(this)

                    .setIcon(R.drawable.ic_save_phone)

                    .setTitle("Panier en instance ")


                    .setMessage("Vous avez un panier en session ,  Voulez-vous l'importer")

                    .setPositiveButton("oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            listArticlePanier = articlePanierHelper.getListArticlePanier();
                            listReservation = reservationArticleDansDepotHelper.getList();

                            articlePanierAutoAdapter = new ArticlePanierAutoAdapter(BonCommandeTerminalAutomatiqueActivity.this, listArticlePanier);
                            rv_list_panier.setAdapter(articlePanierAutoAdapter);

                            articlePanierHelper.deleteAll();
                            reservationArticleDansDepotHelper.deleteAll();

                            txt_sauvegard_existe.setVisibility(View.INVISIBLE);

                        }

                    })

                    .setNegativeButton("non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }


                    })

                    .show();

        } else if (articlePanierHelper.numberOfRows() == 0)
            txt_sauvegard_existe.setVisibility(View.GONE);


        ed_code_a_barre = findViewById(R.id.ed_code_a_barre);
        _ed_code_a_barre = findViewById(R.id._ed_code_a_barre);


        btn_lancer_bon_cmd = findViewById(R.id.btn_lance_bon_cmd);
        p_bar_ajout = findViewById(R.id.p_bar_ajout);
        p_bar_panier = findViewById(R.id.p_bar_panier);
        p_bar_ajout.setVisibility(View.GONE);
        p_bar_panier.setVisibility(View.GONE);


        CodeClient = getIntent().getStringExtra("code_client");
        RaisonSocial = getIntent().getStringExtra("raison_client");


        SharedPreferences pref = getSharedPreferences("usersession", Context.MODE_PRIVATE);
        NomUtilisateur = pref.getString("NomUtilisateur", NomUtilisateur);
        codelivreur = pref.getString("CodeLivreur", codelivreur);
        CodeDepot = pref.getString("CodeDepot", CodeDepot);
        Depot = pref.getString("Depot", Depot);

        mp = MediaPlayer.create(this, R.raw.alarm_sonore);




        ListDepotDestinationParOrdre listDepotDestinationParOrdre = new ListDepotDestinationParOrdre(BonCommandeTerminalAutomatiqueActivity.this, CodeDepot);
        listDepotDestinationParOrdre.execute();


        btn_clear_data = (CardView) findViewById(R.id.btn_clear_data);


        // initilaisation =
        listArticleDispo = new ArrayList<>();
        listReservation = new ArrayList<>();
        listArticlePanier = new ArrayList<>();
        listOrderDepot = new ArrayList<>();


        TextView txt_code_client = findViewById(R.id.txt_code_client);
        TextView txt_raison_client = findViewById(R.id.txt_raison_client);

        txt_designation = findViewById(R.id.txt_designation);

        ll_recherche = findViewById(R.id.ll_recherche);
        ll_trouve_avec_success = findViewById(R.id.ll_trouve_avec_success);
        ll_echec_recherche = findViewById(R.id.ll_echec_recherche);

        ll_recherche.setVisibility(View.INVISIBLE);
        ll_echec_recherche.setVisibility(View.INVISIBLE);

        rv_list_panier = (RecyclerView) findViewById(R.id.rv_cmd_panier_article);
        rv_list_panier.setHasFixedSize(true);
        rv_list_panier.setLayoutManager(new LinearLayoutManager(this));


        txt_code_client.setText("(" + CodeClient + ")");
        txt_raison_client.setText(RaisonSocial);

        BonCommandeTerminalAutomatiqueActivity.btn_lancer_bon_cmd.setEnabled(false);
        BonCommandeTerminalAutomatiqueActivity.btn_lancer_bon_cmd.setBackgroundColor(Color.parseColor("#F0F0F0"));


        _ed_code_a_barre.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Log.e("event_enter_clav", "captured");

                    if (!_ed_code_a_barre.getText().toString().equals("")) {

                        RechercheArticleTask2 rechercheArticleTask
                                = new RechercheArticleTask2(BonCommandeTerminalAutomatiqueActivity.this, _ed_code_a_barre.getText().toString()
                                , ll_recherche, ll_trouve_avec_success, txt_designation, ll_echec_recherche);
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


        btn_lancer_bon_cmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_lancer_bon_cmd.setEnabled(false);
                btn_lancer_bon_cmd.setBackgroundColor(Color.parseColor("#F0F0F0"));

                BonCommande();

            }
        });

    }


    @Override
    public void onBackPressed() {


        String Message = "";


        //  condition 1  : sql lite  vide     et  panier  rempli

        if (articlePanierHelper.numberOfRows() == 0 && reservationArticleDansDepotHelper.numberOfRows() == 0
                && listArticlePanier.size() != 0 && listReservation.size() != 0) {

            CONDITION = 1;
            Message = "Avant de quitter ,\nVoulez vous sauvegarder votre panier en session ?";

        } else if (articlePanierHelper.numberOfRows() == 0 && reservationArticleDansDepotHelper.numberOfRows() == 0
                && listArticlePanier.size() == 0 && listReservation.size() == 0) {

            CONDITION = 2;
            Message = "Votre Panier est vide, Etes-Vous sûr de quitter ?";

        } else if (articlePanierHelper.numberOfRows() != 0 && reservationArticleDansDepotHelper.numberOfRows() != 0
                && listArticlePanier.size() == 0 && listReservation.size() == 0) {

            CONDITION = 3;
            Message = "Vous avez un panier en session ,\n Voulez-vous l'importer";


        } else if (articlePanierHelper.numberOfRows() != 0 && reservationArticleDansDepotHelper.numberOfRows() != 0
                && listArticlePanier.size() != 0 && listReservation.size() != 0) {

            CONDITION = 4;
            Message = "Vous avez un panier en session et vous avez apporté des modification,\n Voulez-vous le synchroniser ?";

        }


        AlertDialog alertDialog = new AlertDialog.Builder(this)

                .setIcon(R.drawable.ic_save_phone)

                .setTitle(" Quitter Bon Commande ")


                .setMessage(Message)

                .setPositiveButton("oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (CONDITION == 1) {

                            // sauvegard  dans sql lite
                            for (ArticleStock a_panier : listArticlePanier) {

                                articlePanierHelper.insertArticlePanier(a_panier.getCodeArticle(), a_panier.getDesignation(), a_panier.getPrixVenteTTC(), a_panier.getPrixVenteHT(),
                                        a_panier.getPrixAchatHT(), a_panier.getCodeTVA(), a_panier.getTauxTVA(), a_panier.getQtePanier());

                            }

                            for (ReservationArticleDansDepot resev : listReservation) {

                                reservationArticleDansDepotHelper.insertReservation(resev.getCodeArticle(), resev.getDesignation(), resev.getCodeDepotDemendeur(), resev.getDepotDemendeur(),
                                        resev.getCodeDepotDemandant(), resev.getDepotDemandant(), resev.getQuantite(), resev.getValider(), resev.getQTStock(), resev.getQtCMD(), resev.getQTDefectueuse());

                            }
                            finish();


                        } else if (CONDITION == 2) {

                            finish();

                        } else if (CONDITION == 3) {


                            listArticlePanier = articlePanierHelper.getListArticlePanier();

                            listReservation = reservationArticleDansDepotHelper.getList();


                            articlePanierAutoAdapter = new ArticlePanierAutoAdapter(BonCommandeTerminalAutomatiqueActivity.this, listArticlePanier);
                            rv_list_panier.setAdapter(articlePanierAutoAdapter);


                            articlePanierHelper.deleteAll();
                            reservationArticleDansDepotHelper.deleteAll();

                            txt_sauvegard_existe.setVisibility(View.INVISIBLE);


                        } else if (CONDITION == 4) {


                            articlePanierHelper.deleteAll();
                            reservationArticleDansDepotHelper.deleteAll();


                            for (ArticleStock a_panier : listArticlePanier) {

                                articlePanierHelper.insertArticlePanier(a_panier.getCodeArticle(), a_panier.getDesignation(), a_panier.getPrixVenteTTC(), a_panier.getPrixVenteHT(),
                                        a_panier.getPrixAchatHT(), a_panier.getCodeTVA(), a_panier.getTauxTVA(), a_panier.getQtePanier());

                            }

                            for (ReservationArticleDansDepot resev : listReservation) {

                                reservationArticleDansDepotHelper.insertReservation(resev.getCodeArticle(), resev.getDesignation(), resev.getCodeDepotDemendeur(), resev.getDepotDemendeur(),
                                        resev.getCodeDepotDemandant(), resev.getDepotDemandant(), resev.getQuantite(), resev.getValider(), resev.getQTStock(), resev.getQtCMD(), resev.getQTDefectueuse());

                            }

                            finish();

                        }

                    }
                })

                .setNegativeButton("non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked


                        if (CONDITION == 1) {

                            finish();


                        } else if (CONDITION == 2) {


                        } else if (CONDITION == 3) {


                        } else if (CONDITION == 4) {


                        }


                    }
                })
                .show();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_bc_automatique, menu);


        return true;
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.e("onStop","onStop") ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy","onDestroy") ;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem nav_vider_panier = menu.findItem(R.id.nav_vider_panier);
        MenuItem nav_load_panier = menu.findItem(R.id.nav_load_panier);

        MenuItem nav_syncroniser_panier = menu.findItem(R.id.nav_syncroniser_panier);
        MenuItem nav_sauvgard_panier = menu.findItem(R.id.nav_sauvegarder_panier);

        if (articlePanierHelper.numberOfRows() == 0 && reservationArticleDansDepotHelper.numberOfRows() == 0) {

            // here ;
            nav_vider_panier.setEnabled(false);
            nav_load_panier.setEnabled(false);

        } else {

            nav_load_panier.setEnabled(true);
            nav_vider_panier.setEnabled(true);
        }

        if (articlePanierHelper.numberOfRows() == listArticlePanier.size() && reservationArticleDansDepotHelper.numberOfRows() == listReservation.size()) {
            nav_syncroniser_panier.setEnabled(false);

        } else {
            nav_syncroniser_panier.setEnabled(true);
        }


        if (articlePanierHelper.numberOfRows() == 0 && reservationArticleDansDepotHelper.numberOfRows() == 0 && listArticlePanier.size() != 0 && listReservation.size() != 0) {
            nav_sauvgard_panier.setEnabled(true);

        } else {
            nav_sauvgard_panier.setEnabled(false);
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_load_panier) {

            listArticlePanier = articlePanierHelper.getListArticlePanier();
            listReservation = reservationArticleDansDepotHelper.getList();


            articlePanierAutoAdapter = new ArticlePanierAutoAdapter(BonCommandeTerminalAutomatiqueActivity.this, listArticlePanier);
            rv_list_panier.setAdapter(articlePanierAutoAdapter);


            articlePanierHelper.deleteAll();
            reservationArticleDansDepotHelper.deleteAll();

            txt_sauvegard_existe.setVisibility(View.INVISIBLE);


            return true;

        } else if (id == R.id.nav_syncroniser_panier) {

            articlePanierHelper.deleteAll();
            reservationArticleDansDepotHelper.deleteAll();


            for (ArticleStock a_panier : listArticlePanier) {

                articlePanierHelper.insertArticlePanier(a_panier.getCodeArticle(), a_panier.getDesignation(), a_panier.getPrixVenteTTC(), a_panier.getPrixVenteHT(),
                        a_panier.getPrixAchatHT(), a_panier.getCodeTVA(), a_panier.getTauxTVA(), a_panier.getQtePanier());

            }

            for (ReservationArticleDansDepot resev : listReservation) {

                reservationArticleDansDepotHelper.insertReservation(resev.getCodeArticle(), resev.getDesignation(), resev.getCodeDepotDemendeur(), resev.getDepotDemendeur(),
                        resev.getCodeDepotDemandant(), resev.getDepotDemandant(), resev.getQuantite(), resev.getValider(), resev.getQTStock(), resev.getQtCMD(), resev.getQTDefectueuse());

            }


            return true;
        } else if (id == R.id.nav_sauvegarder_panier) {


            return true;
        } else if (id == R.id.nav_vider_panier) {

            articlePanierHelper.deleteAll();
            reservationArticleDansDepotHelper.deleteAll();

            listArticlePanier.clear();
            listReservation.clear();

            articlePanierAutoAdapter = new ArticlePanierAutoAdapter(BonCommandeTerminalAutomatiqueActivity.this, listArticlePanier);
            rv_list_panier.setAdapter(articlePanierAutoAdapter);

            txt_sauvegard_existe.setVisibility(View.INVISIBLE);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void initialiser() {

        _ed_code_a_barre.setText("");

        _ed_code_a_barre.setFocusableInTouchMode(true);
        _ed_code_a_barre.requestFocus();

        ll_echec_recherche.setVisibility(View.INVISIBLE);
        ll_recherche.setVisibility(View.INVISIBLE);
        ll_trouve_avec_success.setVisibility(View.INVISIBLE);
        //listArticleDispo.clear();

        triePanier();

    }


    public void BonCommande() {

        ArrayList<LigneBonCommandeVente> listLigneBonCommandeVente = new ArrayList<>();

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


                LigneBonCommandeVente ligneBonCommandeVente = new LigneBonCommandeVente(_num_bon_commande, _code_article, _designation_article, _num_order, _prix_vente_ht, _quantite,
                        _montant_ht, _taux_tva, _montant_tva, _montant_ttc, _observation, _taux_remise, _montant_remise, 0, _net_ht, _prix_achat_net);

                listLigneBonCommandeVente.add(ligneBonCommandeVente);

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


        InsertBC_LBC_ReservationTask insertBC_lbc_reservationTask = new InsertBC_LBC_ReservationTask(BonCommandeTerminalAutomatiqueActivity.this, bonCommandeVente, listLigneBonCommandeVente, listReservation, p_bar_ajout);
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


        public RechercheArticleTask2(Activity activity, String codeArticle, LinearLayout ll_recherche, LinearLayout ll_art_foud_success, TextView txt_designation, LinearLayout ll_art_not_found) {
            this.activity = activity;
            CodeArticle = codeArticle;
            this.ll_recherche = ll_recherche;
            this.ll_art_foud_success = ll_art_foud_success;
            this.ll_art_not_found = ll_art_not_found;
            this.txt_designation = txt_designation;


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
                            "     where   Article.CodeArticle  = '" + CodeArticle + "'   or  Article.CodeBarre = '" + CodeArticle + "' and  Actif  = 1 ";

                    Log.e("queryArticle_x", queryArticle);

                    PreparedStatement ps = con.prepareStatement(queryArticle);
                    ResultSet rs = ps.executeQuery();


                    while (rs.next()) {

                        String CodeArticle = rs.getString("CodeArticle");
                        String CodeBarre = rs.getString("CodeBarre");
                        String Designation = rs.getString("Designation");

                        articleFound = new Article(CodeArticle, CodeBarre, Designation);

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

                ReservationArticle reservationArticle = new ReservationArticle(activity, articleFound.getCodeArticle());
                reservationArticle.execute();

            } else {

                ll_recherche.setVisibility(View.GONE);
                ll_art_foud_success.setVisibility(View.GONE);
                ll_art_not_found.setVisibility(View.VISIBLE);

                BonCommandeTerminalAutomatiqueActivity.btn_lancer_bon_cmd.setEnabled(false);
                BonCommandeTerminalAutomatiqueActivity.btn_lancer_bon_cmd.setBackgroundColor(Color.parseColor("#F0F0F0"));

            }


        }

    }


    class ReservationArticle extends AsyncTask<String, String, String> {

        String res = "";
        ConnectionClass connectionClass;
        String user, password, base, ip;
        Activity activity;
        String CodeArticle;


        public ReservationArticle(Activity activity, String codeArticle) {
            this.activity = activity;
            CodeArticle = codeArticle;


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

            p_bar_panier.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    res = "Error in connection with SQL server";
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
                            "      ,( select ISNULL  ( SUM (Quantite ) ,0   ) from  [ArticleDefectueuseDansValise]  where CodeArticle = Stock.CodeArticle  and CodeDepot  = Stock.CodeDepot  and CodeCause ='01' ) as QteDefectueuse \n" +
                            "      ,( select ISNULL  ( SUM (Quantite ) ,0   ) from  [ArticleDefectueuseDansValise]  where CodeArticle = Stock.CodeArticle  and CodeDepot  = Stock.CodeDepot  and CodeCause <> '01' and  NumeroBonCommande  = 'bc_"+NomUtilisateur+"') as QteNonVendu " +
                            "    \n" +


                            "\n" +
                            "from  Stock  \n" +
                            "inner join Depot on Depot.CodeDepot  = Stock.CodeDepot\n" +
                            "INNER JOIN Article  on Article.CodeArticle  = Stock.CodeArticle\n" +
                            "where Stock.CodeArticle  = '" + CodeArticle + "' and Quantite  != 0  " +
                            "  and (  Depot.PersonnePhysique  =  1  or  Depot.CodeDepot = '01'  )";


                    Log.e("query_article_dispo", queryArticle);

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
                        int QteDefectueuse = rs.getInt("QteDefectueuse");
                        int QteNonVendu = rs.getInt("QteNonVendu");


                        ArticleStock article = new ArticleStock(CodeDepot, Depot, TelPhone, CodeArticle, Designation, PrixVenteTTC, PrixVenteHT, PrixAchatHT, CodeTVA, 0, Quantite, QteCMD, QteDefectueuse,QteNonVendu , 0);
                        listArticleDispo.add(article);

                        BonCommandeTerminalAutomatiqueActivity.articleFoundCMD = new ArticleStock(CodeArticle, Designation, PrixVenteTTC, PrixVenteHT, PrixAchatHT, CodeTVA, 0, 0);


                    }


                }
            } catch (SQLException ex) {

                Log.e("ERROR_article", ex.getMessage().toString());
                res = ex.getMessage().toString();
            }

            return res;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // trie_list_article_dispo();

            TrieListArticleDispo();

            Log.e("lis_article_dispo " + listArticleDispo.size(), listArticleDispo.toString());


            int qt_stock_total = 0;
            int qt_cmd_instance = 0;
            int qt_total_defectueuse = 0;

            for (ArticleStock as : listArticleDispo) {

                Log.e("qt_Stock " + as.getCodeDepot(), "" + as.getQuantite());

                qt_stock_total = qt_stock_total + as.getQuantite();
                qt_cmd_instance = as.getQteCMD();
                qt_total_defectueuse = qt_total_defectueuse + as.getQteDefectueuse();

            }

                int qt_panier = 0 ;

                for (ArticleStock art_panier : listArticlePanier) {

                    if (art_panier.getCodeArticle().equals(articleFoundCMD.getCodeArticle())) {
                        qt_panier = art_panier.getQtePanier();

                        break;
                    }

                }



                Log.e("qt_panier", "" + qt_panier);



                    int qt_dispo = qt_stock_total - qt_cmd_instance - qt_panier - qt_total_defectueuse;

                    Log.e("qt_disponible", "" + qt_dispo);

                    //  re-répartition

                    Re_Repartition(qt_panier);

                    Log.e("after_re_repartitopn", "" + listArticleDispo.toString());


                    if (qt_dispo > 0) {

                        int index_article_dispo = 0;

                        for (ArticleStock as : listArticleDispo) {


                            if ((as.getQuantite() - as.getQteDefectueuse() - as.getNbrCLick()) > 0) {




                                if (Param.CodeDepotCentral.equals(listArticleDispo.get(index_article_dispo).getCodeDepot())

                                        && (listArticleDispo.get(index_article_dispo).getQuantite() - listArticleDispo.get(index_article_dispo).getQteDefectueuse()) > 0) { // reservation de mon depot

                                    Log.e("reserv", "reservation_automatique_du_depot_central");


                                   maj_panier_reservation(   qt_panier, as, qt_stock_total, qt_cmd_instance, qt_total_defectueuse);


                                } else if (CodeDepot.equals(listArticleDispo.get(index_article_dispo).getCodeDepot())

                                        && (listArticleDispo.get(index_article_dispo).getQuantite() - listArticleDispo.get(index_article_dispo).getQteDefectueuse()) > 0) { // reservation de mon depot

                                    Log.e("reserv", "reservation de mon depot");

                                    if (listArticleDispo.get(index_article_dispo).getQteNonVendu() != 0)

                                    {
                                        AlerteSurBlocageDeCommande(activity,  qt_panier, as, qt_stock_total, qt_cmd_instance, qt_total_defectueuse);
                                        vibrationAndAlerteSonore();
                                    }
                                    else {
                                        ConfirmationDeMonDepot(activity,  qt_panier, as, qt_stock_total, qt_cmd_instance, qt_total_defectueuse);
                                        vibrationAndAlerteSonore();
                                    }



                                } else {  // reservation d'autre depot

                                    if ((listArticleDispo.get(index_article_dispo).getQuantite() - listArticleDispo.get(index_article_dispo).getQteDefectueuse() - listArticleDispo.get(index_article_dispo).getNbrCLick()) > 0) {

                                        Log.e("reserv", "reservation de autre depot");
                                        ConfirmationD_autre_Depot(activity,   qt_panier, as, qt_stock_total, qt_cmd_instance, qt_total_defectueuse);
                                        vibrationAndAlerteSonore();

                                    }

                                }


                                Log.e("art_dispo", as.getCodeArticle() + " " + as.getCodeDepot() + " " + as.getDepot() + " DISPO  " + (as.getQuantite() - as.getQteDefectueuse() - as.getNbrCLick()));


                                break;
                            }

                            index_article_dispo++;


                        }


                    } else {

                        Toast.makeText(activity, "QT Non Disponible", Toast.LENGTH_LONG).show();
                    }





            Log.e("MAJ_Panier_RESERVATION " + listReservation.size(), listReservation.toString());

            if (listArticlePanier.size() > 0) {

                btn_lancer_bon_cmd.setEnabled(true);
                btn_lancer_bon_cmd.setBackgroundColor(Color.parseColor("#FFFFFF"));

            }
            p_bar_panier.setVisibility(View.GONE);

        }

    }


    public   void maj_panier_reservation ( int qt_panier, ArticleStock as, int qt_stock_total, int qt_cmd_instance, int qt_defectueuse)
    {

        try {

            ReservationArticleDansDepot reservationArticleDansDepot = new ReservationArticleDansDepot("", articleFoundCMD.getCodeArticle(), articleFoundCMD.getDesignation(), CodeDepot, Depot, as.getCodeDepot(), as.getDepot(), 1, 0, qt_stock_total, qt_cmd_instance, qt_defectueuse);
            Log.e("add_rserv", reservationArticleDansDepot.toString());

            //  maj  reservation
            boolean art_existe_ds_reservation = false;
            int index = 0;

            for (ReservationArticleDansDepot resv : listReservation) {

                if (resv.getCodeDepotDemandant().equals(as.getCodeDepot()) && resv.getCodeArticle().equals(as.getCodeArticle())) {
                    art_existe_ds_reservation = true;
                    break;

                }

                index++;

            }


            if (art_existe_ds_reservation) {

                int ancien_qt = listReservation.get(index).getQuantite();
                int nv_qt = ancien_qt + 1;
                listReservation.set(index, new ReservationArticleDansDepot("", articleFoundCMD.getCodeArticle(), articleFoundCMD.getDesignation(), CodeDepot, Depot, as.getCodeDepot(), as.getDepot(), nv_qt, 0, qt_stock_total, qt_cmd_instance, qt_defectueuse));

            } else if (!art_existe_ds_reservation) {
                listReservation.add(reservationArticleDansDepot);
            }

            //  maj panier
            boolean existe_ds_panier = false;
            int index_panier = 0;

            for (ArticleStock ap : listArticlePanier) {
                if (ap.getCodeArticle().equals(articleFoundCMD.getCodeArticle())) {
                    existe_ds_panier = true;
                    break;
                }
                index_panier++;
            }

            if (existe_ds_panier) {
                listArticlePanier.get(index_panier).setQtePanier(qt_panier + 1);
            } else {
                // add  in panier
                ArticleStock articlePanier =
                        new ArticleStock(articleFoundCMD.getCodeArticle(),
                                articleFoundCMD.getDesignation(), articleFoundCMD.getPrixVenteTTC(), articleFoundCMD.getPrixVenteHT(), articleFoundCMD.getPrixAchatHT(), articleFoundCMD.getCodeTVA(), 0, 1);
                listArticlePanier.add(articlePanier);
            }


            int nbr_click = as.getNbrCLick();
            nbr_click++;
            as.setNbrCLick(nbr_click);
            initialiser();

            // listReservation.clear();
            articlePanierAutoAdapter.notifyDataSetChanged();

        } catch (Exception ex) {

            Log.e("ERROR", "   " + ex.getMessage().toString());
        }


    }



    public void TrieListArticleDispo() {


        ArrayList<ArticleStock> listArticleStock_trie = new ArrayList<>();

        for (OrderDepotDemande order : listOrderDepot) {
            for (ArticleStock as : listArticleDispo) {

                if (as.getCodeDepot().equals(order.getCodeDepotDemandant())) {
                    listArticleStock_trie.add(as);
                    break;
                }
            }
        }

        listArticleDispo = listArticleStock_trie;

        Log.e("listArticleDispo", "" + listArticleDispo.toString());

    }

    public void Re_Repartition(int qt_panier) {
        // initialiser

        // int qt_saisie = qt_panier;

        int qt_cmd = listArticleDispo.get(0).getQteCMD();

        int qt_stock = 0;
        int qt_defectueuse = 0;

        for (ArticleStock as : listArticleDispo) {

            as.setNbrCLick(0);

            qt_stock = qt_stock + as.getQuantite();
            qt_defectueuse = qt_defectueuse + as.getQteDefectueuse();
        }


        int qt_dispo = qt_stock - qt_cmd - qt_defectueuse;


        Log.e("QT_DISPO", "" + qt_dispo);
        if (qt_panier <= qt_dispo) {

            int qt_fil = qt_panier;
            Log.e("qt_fil", "" + qt_fil);

            for (ArticleStock as : listArticleDispo) {
                if (qt_fil >= 0) {

                    Log.e("Article_repartition " + as.getCodeDepot(), "qt_fil " + qt_fil);

                    if ((as.getQuantite() - as.getQteDefectueuse()) > qt_fil) {

                        as.setNbrCLick(qt_fil);
                        qt_fil = 0;

                    } else if ((as.getQuantite() - as.getQteDefectueuse()) == qt_fil) {

                        as.setNbrCLick(qt_fil);
                        qt_fil = 0;
                    } else if ((as.getQuantite() - as.getQteDefectueuse()) < qt_fil) {

                        as.setNbrCLick(as.getQuantite() - as.getQteDefectueuse());
                        qt_fil = qt_fil - as.getNbrCLick();
                    }
                }

                if (qt_fil == 0) {

                    break;
                }
            }


        }
    }

    public void triePanier() {


        Log.e("trie_refrech", "" + listArticlePanier.size());

        ArticleStock art_permut = null;
        for (int i = 0; i < listArticlePanier.size(); i++)

        {
            for (int j = 1; j < (listArticlePanier.size() - i); j++) {

                if (listArticlePanier.get(j - 1).getQtePanier() < listArticlePanier.get(j).getQtePanier())

                {
                    art_permut = listArticlePanier.get(j - 1);
                    listArticlePanier.set((j - 1), listArticlePanier.get(j));
                    listArticlePanier.set((j), art_permut);
                }

            }

        }

        articlePanierAutoAdapter = new ArticlePanierAutoAdapter(BonCommandeTerminalAutomatiqueActivity.this, listArticlePanier);
        rv_list_panier.setAdapter(articlePanierAutoAdapter);

        //ArrayList <ArticleStock> listArticlePanier

    }

    public void AlerteSurBlocageDeCommande(Activity activity,   final int qt_panier, ArticleStock as, int qt_stock_total, int qt_cmd_instance, int qt_defectueuse) {

        AlertDialog alertDialog = new AlertDialog.Builder(activity)

                .setIcon(R.drawable.ic_alerte)

                .setTitle("Vous ne pouvez pas encore commander cet article "+as.getCodeArticle() +" ! ")

                //.setMessage("La  quantité chez Siège Ariana est  non encore disponible , Voulez-vous commander depuis votre Depot")

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                       // maj_panier_reservation(    qt_panier, as, qt_stock_total, qt_cmd_instance, qt_defectueuse);


                    }
                })


                .show();
    }


    public void ConfirmationDeMonDepot(Activity activity,   final int qt_panier, ArticleStock as, int qt_stock_total, int qt_cmd_instance, int qt_defectueuse) {

        AlertDialog alertDialog = new AlertDialog.Builder(activity)

                .setIcon(R.drawable.ic_success)

                .setTitle("Veuillez confirmer votre pièce ")

                //.setMessage("La  quantité chez Siège Ariana est  non encore disponible , Voulez-vous commander depuis votre Depot")

                .setPositiveButton("oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        maj_panier_reservation(    qt_panier, as, qt_stock_total, qt_cmd_instance, qt_defectueuse);


                    }
                })

                .setNegativeButton("non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked


                        // donner  la cause
                        final FragmentManager fm = getFragmentManager();
                        //  Open dialog  rapport suivie detail
                        DialogCauseDeNonConfirmation dialog = new DialogCauseDeNonConfirmation();
                        dialog.setArticleStock(as);
                        //dialog.setActivity(BonCommandeTerminalAutomatiqueActivity.this);
                        dialog.show(fm, "");


                    }
                })
                .show();
    }

    public void ConfirmationD_autre_Depot(Activity activity,   int qt_panier, ArticleStock as, int qt_stock_total, int qt_cmd_instance, int qt_defectueuse) {

        AlertDialog alertDialog = new AlertDialog.Builder(activity)

                .setIcon(R.drawable.ic_success)

                .setTitle("Confirmer la pièce de " + as.getDepot())
                //.setMessage("La  quantité chez Siège Ariana est  non encore disponible , Voulez-vous commander depuis votre Depot")

                .setPositiveButton("oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        maj_panier_reservation(   qt_panier, as, qt_stock_total, qt_cmd_instance, qt_defectueuse);

                    }
                })



                .show();
    }



    public void vibrationAndAlerteSonore() {
        mp.start();
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

}


