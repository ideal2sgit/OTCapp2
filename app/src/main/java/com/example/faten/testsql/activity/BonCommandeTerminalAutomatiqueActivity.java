package com.example.faten.testsql.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.R;
import com.example.faten.testsql.adapter.ArticlePanierAdapter;
import com.example.faten.testsql.adapter.ArticlePanierAutoAdapter;
import com.example.faten.testsql.adapter.ArticleQtDispoStockAdapterRV;
import com.example.faten.testsql.dialog.DialogReservationDansAutreDepot;
import com.example.faten.testsql.dilaog.DialogDemandeNonValide;
import com.example.faten.testsql.model.Article;
import com.example.faten.testsql.model.ArticleStock;
import com.example.faten.testsql.model.BonCommandeVente;
import com.example.faten.testsql.model.LigneBonCommandeVente;
import com.example.faten.testsql.model.OrderDepotDemande;
import com.example.faten.testsql.model.ReservationArticleDansDepot;
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

    TextView txt_designation;

    public static CardView btn_lancer_bon_cmd;
    public static ProgressBar p_bar_ajout;
    public static ProgressBar p_bar_panier;

    public static ArrayList<ArticleStock> listArticleDispo ;
    public static ArrayList<ReservationArticleDansDepot> listReservation ;

    public static ArticleStock articleFoundCMD;
    public static ArrayList<ArticleStock> listArticlePanier;
    public static ArticlePanierAutoAdapter articlePanierAutoAdapter;
    public static RecyclerView rv_list_panier;

    CardView btn_clear_data;
    // Shared Prefrences
    String NomUtilisateur, codelivreur, CodeDepot, Depot;


    public static ArrayList<OrderDepotDemande> listOrderDepot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bon_commande_terminal_automatique);

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

                    if (!_ed_code_a_barre.getText().toString().equals(""))
                    {
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

    public void initialiser() {

        _ed_code_a_barre.setText("");

        _ed_code_a_barre.setFocusableInTouchMode(true);
        _ed_code_a_barre.requestFocus();

        ll_echec_recherche.setVisibility(View.INVISIBLE);
        ll_recherche.setVisibility(View.INVISIBLE);
        ll_trouve_avec_success.setVisibility(View.INVISIBLE);
        listArticleDispo.clear();

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
                            "     where   Article.CodeArticle  = '"+CodeArticle+"'   or  Article.CodeBarre = '"+CodeArticle+"' and  Actif  = 1 ";

                    Log.e("queryArticle_x", queryArticle);

                    PreparedStatement ps = con.prepareStatement(queryArticle);
                    ResultSet rs = ps.executeQuery();


                    while (rs.next()) {

                        String CodeArticle = rs.getString("CodeArticle");
                        String CodeBarre = rs.getString("CodeBarre");
                        String Designation = rs.getString("Designation");

                        articleFound = new Article(CodeArticle  , CodeBarre ,  Designation);

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

            for (ArticleStock as : listArticleDispo) {

                Log.e("qt_Stock " + as.getCodeDepot(), "" + as.getQuantite());

                qt_stock_total = qt_stock_total + as.getQuantite();
                qt_cmd_instance = as.getQteCMD();

            }

            if (listArticlePanier.size() == 0) {
                // ajout d'une ligne après me control de stock
                // control de stock

                Log.e("qt_CMD ", "" + qt_cmd_instance);
                int qt_disponible = qt_stock_total - qt_cmd_instance;
                Log.e("qt_disponible", "" + qt_disponible);


                if (qt_disponible > 0) {
                    listArticleDispo.get(0).setNbrCLick(1);
                    ReservationArticleDansDepot reservationArticleDansDepot = new ReservationArticleDansDepot("", articleFoundCMD.getCodeArticle(), articleFoundCMD.getDesignation(), CodeDepot,Depot , listArticleDispo.get(0).getCodeDepot(), listArticleDispo.get(0).getDepot(), 1, 0,qt_stock_total , qt_cmd_instance);

                    ArticleStock articlePanier = new ArticleStock(articleFoundCMD.getCodeArticle(),
                            articleFoundCMD.getDesignation(), articleFoundCMD.getPrixVenteTTC(), articleFoundCMD.getPrixVenteHT(), articleFoundCMD.getPrixAchatHT(), articleFoundCMD.getCodeTVA(), 0, 1);


                    Log.e("First_rserv", reservationArticleDansDepot.toString());
                    listReservation.add(reservationArticleDansDepot);
                    listArticlePanier.add(articlePanier);
                    initialiser();

                    articlePanierAutoAdapter = new ArticlePanierAutoAdapter(BonCommandeTerminalAutomatiqueActivity.this, listArticlePanier);
                    rv_list_panier.setAdapter(articlePanierAutoAdapter);

                } else {

                    Toast.makeText(activity, "QT Non Disponible", Toast.LENGTH_LONG).show();
                }

            } else {
                //  teser  si  le nouveau article  existe  dans le  panier
                boolean existe = false;

                int qt_panier = 0;

                for (ArticleStock art_panier : listArticlePanier) {
                    if (art_panier.getCodeArticle().equals(articleFoundCMD.getCodeArticle())) {
                        qt_panier = art_panier.getQtePanier();
                        existe = true;
                        break;
                    }

                }


                //  l'article choisi  existe  déja  dans  le panier
                if (existe) {
                    //  maj  panier et reservation

                    int qt_dispo = qt_stock_total - qt_cmd_instance - qt_panier;


                    //  re répartition

                    Re_Repartition(qt_panier);


                    if (qt_dispo > 0) {

                        int index_article_dispo = 0;

                        for (ArticleStock as : listArticleDispo) {


                            if ((as.getQuantite() - as.getNbrCLick()) > 0) {


                                Log.e("art_dispo", as.getCodeArticle() + " " + as.getCodeDepot() + " - " + as.getNbrCLick());

                                // maj  panier  et rservation

                                qt_panier++;

                                int index_art_panier = 0;

                                for (ArticleStock ap : listArticlePanier) {
                                    if (ap.getCodeArticle().equals(articleFoundCMD.getCodeArticle())) {

                                        break;

                                    }

                                    index_art_panier++;
                                }


                                Log.e("MAJ_Panier", "article " + listArticlePanier.get(index_art_panier).getCodeArticle());
                                Log.e("MAJ_Panier", "qt_nv_panier " + qt_panier);
                                // listArticlePanier.get(index_art_panier).setQtePanier(qt_panier);

                                boolean reserv_article_depot_exist = false;

                                for (ReservationArticleDansDepot reserv : listReservation) {

                                    Log.e("MAJ_Panier", " reserv " + reserv.getCodeArticle() + " as  " + as.getCodeArticle());
                                    Log.e("MAJ_Panier", " reserv " + reserv.getCodeDepotDemandant() + " as  " + as.getCodeDepot());


                                    if (reserv.getCodeArticle().equals(as.getCodeArticle()) && reserv.getCodeDepotDemandant().equals(as.getCodeDepot())) {

                                        reserv_article_depot_exist = true;

                                        int qt_reservation_ds_depot = reserv.getQuantite();

                                        if (qt_reservation_ds_depot < as.getQuantite() && qt_reservation_ds_depot != 0) {

                                            qt_reservation_ds_depot++;

                                            reserv.setQuantite(qt_reservation_ds_depot);
                                            listArticlePanier.get(index_art_panier).setQtePanier(qt_panier);

                                            int nbr_click = as.getNbrCLick();
                                            nbr_click++;
                                            as.setNbrCLick(nbr_click);


                                            initialiser();
                                            break;

                                        }


                                    }

                                }

                                Log.e("reserv_art_dep_exist", "" + reserv_article_depot_exist);

                                if (reserv_article_depot_exist == false) {

                                   /* if (index_article_dispo >= 2) {

                                        final FragmentManager fm = getFragmentManager();
                                        //  Open dialog  rapport suivie detail
                                        DialogReservationDansAutreDepot dialog = new DialogReservationDansAutreDepot();
                                        dialog.setQTStock(qt_stock_total);
                                        dialog.setActivity(BonCommandeTerminalAutomatiqueActivity.this);
                                        dialog.show(fm, "");

                                    } else {*/

                                        Log.e("add_reserv", as.getCodeDepot());
                                        ReservationArticleDansDepot reservationArticleDansDepot = new ReservationArticleDansDepot("", articleFoundCMD.getCodeArticle(), articleFoundCMD.getDesignation(), CodeDepot,Depot , as.getCodeDepot(),as.getDepot() , 1, 0,qt_stock_total , qt_cmd_instance);
                                        Log.e("add_rserv", reservationArticleDansDepot.toString());
                                        listReservation.add(reservationArticleDansDepot);

                                        listArticlePanier.get(index_art_panier).setQtePanier(qt_panier);

                                        int nbr_click = as.getNbrCLick();
                                        nbr_click++;
                                        as.setNbrCLick(nbr_click);

                                        initialiser();

                                    //}
                                }

                                articlePanierAutoAdapter.notifyDataSetChanged();
                                break;
                            }

                            index_article_dispo++;
                        }


                    } else {

                        Toast.makeText(activity, "QT Non Disponible", Toast.LENGTH_LONG).show();
                    }


                } else {

                    // ajout d'une nvelle  ligne

                    ReservationArticleDansDepot reservationArticleDansDepot = new ReservationArticleDansDepot("", articleFoundCMD.getCodeArticle(), articleFoundCMD.getDesignation(), CodeDepot,Depot , listArticleDispo.get(0).getCodeDepot(),listArticleDispo.get(0).getDepot() , 1, 0,qt_stock_total , qt_cmd_instance);

                    ArticleStock articlePanier = new ArticleStock
                            (articleFoundCMD.getCodeArticle(), articleFoundCMD.getDesignation(), articleFoundCMD.getPrixVenteTTC(),
                                    articleFoundCMD.getPrixVenteHT(), articleFoundCMD.getPrixAchatHT(), articleFoundCMD.getCodeTVA(), 0, 1);


                    Log.e("First_rserv", reservationArticleDansDepot.toString());


                    listReservation.add(reservationArticleDansDepot);
                    listArticlePanier.add(articlePanier);


                    articlePanierAutoAdapter = new ArticlePanierAutoAdapter(BonCommandeTerminalAutomatiqueActivity.this, listArticlePanier);
                    rv_list_panier.setAdapter(articlePanierAutoAdapter);

                }

            }


            Log.e("MAJ_Panier_RESERVATION " + listReservation.size(), listReservation.toString());

            if (listArticlePanier.size()> 0)
            {

                btn_lancer_bon_cmd.setEnabled(true);
                btn_lancer_bon_cmd.setBackgroundColor(Color.parseColor("#FFFFFF"));

            }
            p_bar_panier.setVisibility(View.GONE);

        }

    }



    public void trie_list_article_dispo() {

        ArrayList<String> listCodeDepot = new ArrayList<>();
        for (ArticleStock a : listArticleDispo) {
            listCodeDepot.add(a.getCodeDepot());
        }

        Log.e("listCodeDepot", listCodeDepot.toString());

        Iterator<ArticleStock> iter = listArticleDispo.iterator();

        if (listCodeDepot.contains("01") && listCodeDepot.contains(CodeDepot)) {

            Log.e("listCodeDepot", " 01 - " + CodeDepot);

            while (iter.hasNext()) {

                ArticleStock a = iter.next();

                if (a.getCodeDepot().equals("01")) {

                    // replace 0   with index of
                    int last_index = listArticleDispo.indexOf(a); //3

                    ArticleStock a_copie = listArticleDispo.get(0);

                    listArticleDispo.set(0, a);
                    listArticleDispo.set(last_index, a_copie);

                }

                if (a.getCodeDepot().equals(CodeDepot)) {


                    int last_index = listArticleDispo.indexOf(a); //3

                    ArticleStock a_copie = listArticleDispo.get(1);

                    listArticleDispo.set(1, a);
                    listArticleDispo.set(last_index, a_copie);
                }

            }

        } else if ((listCodeDepot.contains("01")) && !listCodeDepot.contains(CodeDepot)) {


            while (iter.hasNext()) {

                ArticleStock a = iter.next();

                if (a.getCodeDepot().equals("01")) {

                    // replace 0   with index of
                    int last_index = listArticleDispo.indexOf(a); //3

                    ArticleStock a_copie = listArticleDispo.get(0);

                    listArticleDispo.set(0, a);
                    listArticleDispo.set(last_index, a_copie);

                }

            }
        } else if ((!listCodeDepot.contains("01")) && listCodeDepot.contains(CodeDepot)) {

            while (iter.hasNext()) {

                ArticleStock a = iter.next();

                if (a.getCodeDepot().equals(CodeDepot)) {


                    int last_index = listArticleDispo.indexOf(a); //3

                    ArticleStock a_copie = listArticleDispo.get(0);

                    listArticleDispo.set(0, a);
                    listArticleDispo.set(last_index, a_copie);
                }

            }
        }


    }


    public void TrieListArticleDispo() {


        ArrayList<ArticleStock> listArticleStock_trie =new ArrayList<>() ;

        for (OrderDepotDemande order : listOrderDepot)
        {
            for (ArticleStock as : listArticleDispo) {

                if (as.getCodeDepot().equals(order.getCodeDepotDemandant()))
                {
                    listArticleStock_trie.add(as) ;
                    break;
                }
            }
        }

        listArticleDispo= listArticleStock_trie ;

        Log.e("listArticleDispo",""+listArticleDispo.toString()) ;

        }


    public void Re_Repartition ( int qt_panier)
        {
            // initialiser

            for (ArticleStock as : listArticleDispo) {

                as.setNbrCLick(0);
            }


            int qt_saisie = qt_panier;


            int qt_cmd = listArticleDispo.get(0).getQteCMD();

            int qt_stock = 0;


            for (ArticleStock a : listArticleDispo) {

                qt_stock = qt_stock + a.getQuantite();

            }

            int qt_dispo = qt_stock - qt_cmd;


            Log.e("QT_DISPO", "" + qt_dispo);
            if (qt_saisie <= qt_dispo) {

                int qt_fil = qt_saisie;

                for (ArticleStock as : listArticleDispo)
                {
                    if (qt_fil >= 0) {
                        Log.e("ArticleS " + listArticleDispo.indexOf(as), "qt_fil " + qt_fil);

                        if (as.getQuantite() > qt_fil) {
                            //Log.e("ArticleS "+listArticleDispo.indexOf(as) ,"Condition 1") ;
                            as.setNbrCLick(qt_fil);
                            qt_fil = 0;
                        } else if (as.getQuantite() == qt_fil) {
                            //Log.e("ArticleS "+listArticleDispo.indexOf(as) ,"Condition 2") ;
                            as.setNbrCLick(qt_fil);
                            qt_fil = 0;
                        } else if (as.getQuantite() < qt_fil) {
                            //Log.e("ArticleS "+listArticleDispo.indexOf(as) ,"Condition 3") ;
                            as.setNbrCLick(as.getQuantite());
                            qt_fil = qt_fil - as.getNbrCLick();
                        }
                    }
                    if (qt_fil == 0) {

                        break;
                    }
                }
            }
        }


    }
