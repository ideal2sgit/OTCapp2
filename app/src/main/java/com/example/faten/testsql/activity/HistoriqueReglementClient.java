package com.example.faten.testsql.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.R;
import com.example.faten.testsql.model.ListReference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HistoriqueReglementClient extends AppCompatActivity {
    ConnectionClass connectionClass;
    final Context co = this;
    String user, password, base, ip;
    String NomUtilisateur, codelivreur, CodeFonction;
    ProgressBar progressBar;
    GridView gridDetail, grid_cheque, grid_ligne_reg;
    String querysearch = "", CodeClient_select = "", Raison_client = "";
    Button bt_bl, bt_facture, bt_passage;
    ArrayList<String> data_CodeClient, data_NomClient;
    ArrayList<String> data_CodeReglement, data_NomReglement;
    ArrayList<String> data_CodeBanque, data_NomBanque;
    Spinner spinClient, spinReglement, spinBanque;

    TextView txt_datedebut, txt_datefin, txt_total, txt_retenu, txt_mnt_final, txt_total_a_paye, txt_titre;
    String datedebut = "", datefin = "", NumReglemnt_select = "",NumReglemnt_delete="";
    DatePicker datePicker;
    Boolean testBL = true, testFacture = false, testPassage = false;
    Button bt_passage_reg;
    LinearLayout layout_info, layout_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_reglement_client);

        connectionClass = new ConnectionClass();
        SharedPreferences prefe = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);

        user = prefe.getString("user", user);
        ip = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base = prefe.getString("base", base);

        SharedPreferences pref = getSharedPreferences("usersession", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        NomUtilisateur = pref.getString("NomUtilisateur", NomUtilisateur);
        codelivreur = pref.getString("CodeLivreur", codelivreur);
        CodeFonction = pref.getString("CodeFonction", CodeFonction);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        gridDetail = (GridView) findViewById(R.id.grid_detail);
        txt_datedebut = (TextView) findViewById(R.id.txt_date_debut);
        txt_datefin = (TextView) findViewById(R.id.txt_date_fin);

        CardView card_date_debut = (CardView) findViewById(R.id.card_date_debut);
        CardView card_date_fin = (CardView) findViewById(R.id.card_date_fin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        



        Button bt_historique=(Button)findViewById(R.id.btn_historique_bc) ;
        bt_historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillList fillList=new FillList();
                fillList.execute("");
            }
        });
        card_date_debut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(co);
                View px = li.inflate(R.layout.diagcalend, null);
                AlertDialog.Builder alt = new AlertDialog.Builder(co);
                alt.setIcon(R.drawable.i2s);
                alt.setView(px);
                alt.setTitle("date");
                datePicker = (DatePicker) px.findViewById(R.id.datedebut);
                alt.setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface di, int i) {

                                Date d = new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth());
                                datedebut = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)
                                        .format(d);

                                txt_datedebut.setText(datedebut);

                                FillList fillList = new FillList();
                                fillList.execute("");


                            }
                        });

                AlertDialog dd = alt.create();
                dd.show();
            }
        });


        card_date_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(co);
                View px = li.inflate(R.layout.diagcalend, null);
                AlertDialog.Builder alt = new AlertDialog.Builder(co);
                alt.setIcon(R.drawable.i2s);
                alt.setView(px);
                alt.setTitle("date");
                datePicker = (DatePicker) px.findViewById(R.id.datedebut);
                alt.setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface di, int i) {

                                Date d = new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth());
                                datefin = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)
                                        .format(d);

                                txt_datefin.setText(datefin);


                                FillList fillList = new FillList();
                                fillList.execute("");

                            }
                        });

                AlertDialog dd = alt.create();
                dd.show();
            }
        });


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        datefin = sdf.format(calendar.getTime());
        calendar.add(Calendar.DATE, -1);
        datedebut = sdf.format(calendar.getTime());
        txt_datedebut.setText(datedebut);
        txt_datefin.setText(datefin);
        FillList fillList = new FillList();
        fillList.execute("");

    }


    public class FillList extends AsyncTask<String, String, String> {
        String z = "  ";

        List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();
        float total = 0, total_retenu = 0;

        @Override
        protected void onPreExecute() {
            //  Log.e("frs", querylist);
            progressBar.setVisibility(View.VISIBLE);

            total = 0;
            total_retenu = 0;
        }

        @Override
        protected void onPostExecute(String r) {
            progressBar.setVisibility(View.GONE);
            //   Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();


//            final NumberFormat instance = NumberFormat.getNumberInstance(Locale.FRENCH);
//            instance.setMinimumFractionDigits(3);
//            instance.setMaximumFractionDigits(3);
//            txt_total.setText(instance.format(total));
//            txt_retenu.setText(instance.format(total_retenu));
//            txt_mnt_final.setText(instance.format(total - total_retenu));
            String[] from = {"NumeroReglementClient", "DateCreation", "NomUtilisateur", "CodeClient", "RaisonSociale", "TotalNet", "TotalRecu"};
            int[] views = {R.id.txt_libelle, R.id.txt_numpiece, R.id.txt_date, R.id.debit, R.id.credit, R.id.solde};
            final SimpleAdapter ADA = new SimpleAdapter(getApplicationContext(),
                    prolist, R.layout.item_list_reglement_client, from,
                    views);

            final BaseAdapter baseAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return prolist.size();
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }


                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    final LayoutInflater layoutInflater = LayoutInflater.from(co);
                    convertView = layoutInflater.inflate(R.layout.item_list_reglement_client, null);
                    final TextView txt_numpiece = (TextView) convertView.findViewById(R.id.txt_numpiece);
                    final TextView txt_date = (TextView) convertView.findViewById(R.id.txt_date);
                    final TextView txt_total_net = (TextView) convertView.findViewById(R.id.txt_total_net);
                    final TextView txt_total_recu = (TextView) convertView.findViewById(R.id.txt_total_recu);

                    final TextView txt_libelle = (TextView) convertView.findViewById(R.id.txt_libelle);
                    final TextView txt_nomutilisateur = (TextView) convertView.findViewById(R.id.txt_nomutilisateur);
                    final Button bt_detail_reglement = (Button) convertView.findViewById(R.id.bt_detail_reglement);
                    final Button bt_annulation_reglement = (Button) convertView.findViewById(R.id.bt_delete);


                    final HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(position);

                    //   NumeroReglementClient,DateCreation,NomUtilisateur ,CodeClient,RaisonSociale,TotalNet,TotalRecu

                    String Libelle = (String) obj.get("RaisonSociale");
                    String DatePiece = (String) obj.get("DateCreation");
                    String NumeroPiece = (String) obj.get("NumeroReglementClient");
                    String TotalNet = (String) obj.get("TotalNet");
                    String TotalRecu = (String) obj.get("TotalRecu");
                    String NomUtilisateurreglement = (String) obj.get("NomUtilisateur");
                    txt_libelle.setText(Libelle);
                    txt_date.setText(DatePiece);
                    txt_numpiece.setText(NumeroPiece);
                    txt_total_net.setText(TotalNet);
                    txt_nomutilisateur.setText(NomUtilisateurreglement);
                    txt_total_recu.setText(TotalRecu);
                    bt_detail_reglement.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NumReglemnt_select = NumeroPiece;
                            FillListDetail fillListDetail = new FillListDetail();
                            fillListDetail.execute("");

                        }
                    });

                    bt_annulation_reglement.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder alt = new AlertDialog.Builder(co);
                            alt.setIcon(R.drawable.i2s);
                            alt.setMessage("Voulez vous vraiment supprimer le Regle de "+txt_libelle.getText().toString());
                            alt.setTitle("Reglement N° "+txt_numpiece.getText().toString());
                            alt.setCancelable(true);
                            alt.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   NumReglemnt_delete=txt_numpiece.getText().toString();
                                    AnnulationReglement annulationReglement=new AnnulationReglement();
                                    annulationReglement.execute("");

                                }
                            });
                            alt.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dd = alt.create();
                            dd.show();

                        }
                    });

                    return convertView;
                }
            };


            gridDetail.setAdapter(baseAdapter);


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    //select isnull(TauxRetenu,0)  as TauxRetenu from RetenuSource where CodeRetenu='RET001'
                    String q = "select  NumeroReglementClient,DateCreation,NomUtilisateur ,CodeClient,RaisonSociale,TotalNet,TotalRecu from ReglementClient \n" +
                            "where ReglementClient.DateCreation between '" + datedebut + "' and '" + datefin + "'\n" +
                            " ";

                    PreparedStatement pss = con.prepareStatement(q);

                    ResultSet rs = pss.executeQuery();

                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();


                        datanum.put("NumeroReglementClient", rs.getString("NumeroReglementClient"));
                        datanum.put("NomUtilisateur", rs.getString("NomUtilisateur"));
                        Date d = rs.getDate("DateCreation");
                        String DATE_FORMAT_2 = "dd-MM-yyyy";
                        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_2);


                        datanum.put("DateCreation", dateFormat.format(d) + "");
                        datanum.put("CodeClient", rs.getString("CodeClient"));
                        datanum.put("RaisonSociale", rs.getString("RaisonSociale"));
                        datanum.put("TotalNet", rs.getString("TotalNet"));
                        datanum.put("TotalRecu", rs.getString("TotalRecu"));


                        prolist.add(datanum);
                        z = "Success";
                    }


                }
            } catch (SQLException ex) {
                z = "list" + ex.toString();
                Log.e("erreursql", z);

            } catch (Exception e) {
                Log.e("erreur", e.toString());
            }
            return z;
        }
    }


    public class FillListDetail extends AsyncTask<String, String, String> {
        String z = "  ";

        List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();
        float total = 0, total_retenu = 0;
        List<List<Map<String, String>>> list_grid_ligne = new ArrayList<List<Map<String, String>>>();
        List<Map<String, String>> prolist_ligne = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {
            //  Log.e("frs", querylist);
            progressBar.setVisibility(View.VISIBLE);


        }

        @Override
        protected void onPostExecute(String r) {
            progressBar.setVisibility(View.GONE);
            //   Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();


//            final NumberFormat instance = NumberFormat.getNumberInstance(Locale.FRENCH);
//            instance.setMinimumFractionDigits(3);
//            instance.setMaximumFractionDigits(3);
//            txt_total.setText(instance.format(total));
//            txt_retenu.setText(instance.format(total_retenu));
//            txt_mnt_final.setText(instance.format(total - total_retenu));

            String[] from = {"CodeModeReglement", "Reference", "Echeance", "MontantRecu", "MontantRestant", "MontantVerser", "Etat"};
            int[] views = {R.id.txt_libelle, R.id.txt_numpiece, R.id.txt_date, R.id.debit, R.id.credit, R.id.solde};
            final SimpleAdapter ADA = new SimpleAdapter(getApplicationContext(),
                    prolist, R.layout.item_historique_detail_reglement, from,
                    views);

            final BaseAdapter baseAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return prolist.size();
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }


                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    final LayoutInflater layoutInflater = LayoutInflater.from(co);
                    convertView = layoutInflater.inflate(R.layout.item_historique_detail_reglement, null);
                    final TextView txt_numpiece = (TextView) convertView.findViewById(R.id.txt_numpiece);
                    final TextView txt_date = (TextView) convertView.findViewById(R.id.txt_date);
                    final TextView txt_montant_recu = (TextView) convertView.findViewById(R.id.txt_montant_recu);
                    final TextView txt_montant_restant = (TextView) convertView.findViewById(R.id.txt_montant_restant);
                    final TextView txt_montant_verser = (TextView) convertView.findViewById(R.id.txt_montant_verser);

                    final TextView txt_libelle = (TextView) convertView.findViewById(R.id.txt_libelle);
                    final TextView txt_nomutilisateur = (TextView) convertView.findViewById(R.id.txt_nomutilisateur);
                    final TextView txt_etat = (TextView) convertView.findViewById(R.id.txt_etat);
                    final ImageView icon_img = (ImageView) convertView.findViewById(R.id.icon_img);
                    final Button bt_modif_cheque = (Button) convertView.findViewById(R.id.bt_modif_cheque);
                    final LinearLayout layout_list = (LinearLayout) convertView.findViewById(R.id.layout_list);
                    grid_ligne_reg = (GridView) convertView.findViewById(R.id.grid_ligne_reg);

                    if (position == prolist.size()-1) {
                        String[] from = {"NumeroPiece", "TotalTVA", "MontantPieceTTC", "TotalRecu", "TotalRestant", "TotalPayee"};

                        int[] views = {R.id.txt_libelle, R.id.txt_tva, R.id.txt_ttc, R.id.txt_montant_recu, R.id.txt_montant_restant,R.id.txt_montant_paye};
                        final SimpleAdapter ADA_ligne = new SimpleAdapter(getApplicationContext(),
                                list_grid_ligne.get(position), R.layout.item_historique_ligne_reglement, from,
                                views);

                        grid_ligne_reg.setAdapter(ADA_ligne);
                        ViewGroup.LayoutParams layoutParams = grid_ligne_reg.getLayoutParams();
                        layoutParams.height = 300*list_grid_ligne.get(position).size(); //this is in pixels
                        grid_ligne_reg.setLayoutParams(layoutParams);
                    }else{
                        grid_ligne_reg.setVisibility(View.GONE);
                        layout_list.setVisibility(View.GONE);
                    }

                    final HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(position);

                    //   NumeroReglementClient,DateCreation,NomUtilisateur ,CodeClient,RaisonSociale,TotalNet,TotalRecu
// String[] from = {"CodeModeReglement", "Reference", "Echeance", "MontantRecu", "MontantRestant", "MontantVerser", "Etat" };
//
                    String CodeModeReglement = (String) obj.get("CodeModeReglement");
                    String Reference = (String) obj.get("Reference");
                    String Echeance = (String) obj.get("Echeance");
                    String MontantRecu = (String) obj.get("MontantRecu");
                    String MontantRestant = (String) obj.get("MontantRestant");
                    String MontantVerser = (String) obj.get("MontantVerser");
                    String Etat = (String) obj.get("Etat");

                    txt_libelle.setText(CodeModeReglement);
                    txt_numpiece.setText(Reference);
                    txt_date.setText(Echeance);
                    txt_montant_recu.setText(MontantRecu);
                    txt_montant_verser.setText(MontantVerser);
                    txt_montant_restant.setText(MontantRestant);
                    txt_etat.setText(Etat);
                    if (CodeModeReglement.equals("E")) {
                        icon_img.setBackgroundResource(R.drawable.ic_ttc);
                    } else {
                        icon_img.setBackgroundResource(R.drawable.cheque);

                    }

                    bt_modif_cheque.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            LayoutInflater li = LayoutInflater.from(co);
                            View px = li.inflate(R.layout.diag_modif_reglement_fiche, null);
                            AlertDialog.Builder alt = new AlertDialog.Builder(co);
                            alt.setIcon(R.drawable.i2s);
                            alt.setView(px);
                            alt.setTitle("Modifier");
                            alt.setPositiveButton("fermer", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            AlertDialog dd = alt.create();
                            dd.show();
                        }
                    });
                    return convertView;
                }
            };


            gridDetail.setAdapter(baseAdapter);


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    //select isnull(TauxRetenu,0)  as TauxRetenu from RetenuSource where CodeRetenu='RET001'
                    String q = "  select  CodeModeReglement,Reference,Echeance,MontantRecu ,MontantRestant,MontantVerser\n" +
                            " ,( select Libelle from Etat where Etat.NumeroEtat=DetailReglementClient.NumeroEtat)as Etat\n" +
                            " from DetailReglementClient where NumeroReglementClient='" + NumReglemnt_select + "'\n";

                    Log.e("detail",q);
                    PreparedStatement pss = con.prepareStatement(q);

                    ResultSet rs = pss.executeQuery();

                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();


                        datanum.put("CodeModeReglement", rs.getString("CodeModeReglement"));
                        datanum.put("Reference", rs.getString("Reference"));
                        Date d = rs.getDate("Echeance");
                        String DATE_FORMAT_2 = "dd-MM-yyyy";
                        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_2);


                        datanum.put("Echeance", dateFormat.format(d) + "");
                        datanum.put("MontantRecu", rs.getString("MontantRecu"));
                        datanum.put("MontantRestant", rs.getString("MontantRestant"));
                        datanum.put("MontantVerser", rs.getString("MontantVerser"));
                        datanum.put("Etat", rs.getString("Etat"));
                        prolist.add(datanum);

                        String query = " select  NumeroReglementClient,NumeroPiece,TotalTVA,MontantPieceTTC,TotalRecu,TotalRestant,TotalPayee   from LigneReglementClient where NumeroReglementClient='" + NumReglemnt_select + "'\n";
                        Log.e("ligne",q);
                        PreparedStatement p = con.prepareStatement(query);

                        ResultSet rss = p.executeQuery();

                        while (rss.next()) {
                            Map<String, String> data = new HashMap<String, String>();


                            data.put("NumeroReglementClient", rss.getString("NumeroReglementClient"));
                            data.put("NumeroPiece", rss.getString("NumeroPiece"));

                            data.put("TotalTVA", rss.getString("TotalTVA"));
                            data.put("MontantPieceTTC", rss.getString("MontantPieceTTC"));
                            data.put("TotalRecu", rss.getString("TotalRecu"));
                            data.put("TotalRestant", rss.getString("TotalRestant"));
                            data.put("TotalPayee", rss.getString("TotalPayee"));


                            prolist_ligne.add(data);
                            z = "Success";
                        }
                        list_grid_ligne.add(prolist_ligne);
                        z = "Success";
                    }


                }
            } catch (SQLException ex) {
                z = "list" + ex.toString();
                Log.e("erreursql", z);

            } catch (Exception e) {
                Log.e("erreur", e.toString());
            }
            return z;
        }
    }



    public class AnnulationReglement extends AsyncTask<String, String, String> {
        String z = "";



        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), "inseret Validé", Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);
            FillList fillList=new FillList();
            fillList.execute("");



        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";

                } else {



                    //reglement
                    String q = "delete from DetailReglementClient where NumeroReglementClient='"+NumReglemnt_delete+"'\n" +
                            "delete   from LigneReglementClient where NumeroReglementClient='"+NumReglemnt_delete+"'\n" +
                            "delete from ReglementClient where NumeroReglementClient='"+NumReglemnt_delete+"'\n" ;
                    PreparedStatement preparedStatement_entete = con.prepareStatement(q);
                    Log.e("AnnulationReglement", q);

                    preparedStatement_entete.executeUpdate();




                }
            } catch (SQLException ex) {
                z = "list" + ex.toString();
                Log.e("erreurquery", ex.toString());

            }
            return z;
        }
    }



}
