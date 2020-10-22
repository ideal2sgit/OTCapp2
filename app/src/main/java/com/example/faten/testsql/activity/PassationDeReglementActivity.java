package com.example.faten.testsql.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.R;
import com.example.faten.testsql.model.DetailReglementClient;
import com.example.faten.testsql.model.ListReference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Ref;
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


public class PassationDeReglementActivity extends AppCompatActivity {
    ConnectionClass connectionClass;
    final Context co = this;
    String user, password, base, ip;
    String NomUtilisateur, codelivreur, CodeFonction;
    ProgressBar progressBar;
    GridView gridDetail, grid_cheque;
    String querysearch = "", CodeClient_select = "", Raison_client = "";
    Button bt_bl, bt_facture, bt_passage;
    ArrayList<String> data_CodeClient, data_NomClient;
    ArrayList<String> data_CodeReglement, data_NomReglement;
    ArrayList<String> data_CodeBanque, data_NomBanque;
    Spinner spinClient, spinReglement, spinBanque;

    TextView txt_datedebut, txt_datefin, txt_total, txt_retenu, txt_mnt_final, txt_total_a_paye, txt_titre;
    String datedebut = "", datefin = "";
    DatePicker datePicker;
    Boolean testBL = true, testFacture = false, testPassage = false;
    Button bt_passage_reg;
    LinearLayout layout_info, layout_total;
    String codeModeRegSelect = "", codeBanqueSelect = "", query_ligne_detail_reglemnt = "";
    public static ArrayList<ListReference> listReferences = new ArrayList<>();
    public static ArrayList<DetailReglementClient> listDetailReglementClient = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passation_de_reglement);
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
        txt_titre = (TextView) findViewById(R.id.txt_titre);
        txt_retenu = (TextView) findViewById(R.id.txt_retenu);
        txt_total = (TextView) findViewById(R.id.txt_total);
        txt_mnt_final = (TextView) findViewById(R.id.txt_mnt_final);
        txt_total_a_paye = (TextView) findViewById(R.id.txt_total_a_paye);
        bt_bl = (Button) findViewById(R.id.bt_bl);
        bt_passage = (Button) findViewById(R.id.bt_passage);
        bt_facture = (Button) findViewById(R.id.bt_facture);
        bt_passage_reg = (Button) findViewById(R.id.bt_passage_reg);
        spinClient = (Spinner) findViewById(R.id.spinnerclient);
        layout_info = (LinearLayout) findViewById(R.id.layout_info);
        layout_total = (LinearLayout) findViewById(R.id.layout_total);



        bt_passage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CodeClient_select.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Choisir un client ",Toast.LENGTH_LONG).show();
                }else {

                    testPassage = true;
                    layout_info.setVisibility(View.GONE);
                    layout_total.setVisibility(View.GONE);
                    bt_passage.setVisibility(View.GONE);
                    bt_passage_reg.setVisibility(View.VISIBLE);
                    txt_total_a_paye.setVisibility(View.VISIBLE);
                    String t = "";
                    if (testBL) {
                        t = "BL :" + Raison_client+"DU "+datedebut+" Au "+datefin;
                    } else {
                        t = "FACTURE :" + Raison_client+"DU "+datedebut+" Au "+datefin;;
                    }
                    txt_titre.setText(t);
                    txt_titre.setVisibility(View.VISIBLE);
                    FillList fillList = new FillList();
                    fillList.execute("");
                }

            }
        });

        bt_passage_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Float.parseFloat(txt_total_a_paye.getText().toString()) == 0) {


                    Toast.makeText(getApplicationContext(), "Montant reglement égale à zero  ", Toast.LENGTH_LONG).show();


                } else {


                    final android.support.v7.app.AlertDialog.Builder alt = new android.support.v7.app.AlertDialog.Builder(co);
                    LayoutInflater li = LayoutInflater.from(co);

                    View px = li.inflate(R.layout.diag_reglement_fiche, null);
                    alt.setView(px);
                    alt.setIcon(R.drawable.i2s);
                    alt.setTitle("Passation de Reglement");
                    alt.setCancelable(false);
                    spinReglement = (Spinner) px.findViewById(R.id.spinmodepayement);
                    spinBanque = (Spinner) px.findViewById(R.id.spinbanque);
                    grid_cheque = (GridView) px.findViewById(R.id.grid_cheque);
                    final EditText numcheque = (EditText) px.findViewById(R.id.numcheque);
                    final EditText nb_tranche = (EditText) px.findViewById(R.id.nb_tranche);
                    final TextView txt_mnt_select = (TextView) px.findViewById(R.id.txt_mnt_select);

                    final EditText montant = (EditText) px.findViewById(R.id.montant);
                    final DatePicker datePicker = (DatePicker) px.findViewById(R.id.dateecheance);
                    final LinearLayout layout_cheque = (LinearLayout) px.findViewById(R.id.layout_cheque);
                    final Button btajout = (Button) px.findViewById(R.id.btajout);
                    final Button bt_valider = (Button) px.findViewById(R.id.bt_valider);
                    grid_cheque = (GridView) px.findViewById(R.id.grid_cheque);

                    txt_mnt_select.setText(txt_total_a_paye.getText().toString());
                    layout_cheque.setVisibility(View.GONE);
                    GetDataSpinnerReglement getDataSpinnerReglement = new GetDataSpinnerReglement();
                    getDataSpinnerReglement.execute("");
                    GetDataSpinnerbanque getDataSpinnerbanque = new GetDataSpinnerbanque();
                    getDataSpinnerbanque.execute("");

                    spinBanque.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            codeBanqueSelect = data_CodeBanque.get(position);
                            Log.e("codeBanqueSelect", codeBanqueSelect);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    spinReglement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            codeModeRegSelect = data_CodeReglement.get(position);
                            if (!codeModeRegSelect.equals("E") && position != 0) {
                                layout_cheque.setVisibility(View.VISIBLE);

                            } else {
                                nb_tranche.setText("1");
                                layout_cheque.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    btajout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String montant_saisi = montant.getText().toString();
                            String nbtranche_saisi = nb_tranche.getText().toString();
                            Date d = new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth());
                            String date_echeance = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH).format(d);

                            Boolean testLigne = true;


                            if (montant_saisi.equals("")) {
                                android.support.v7.app.AlertDialog.Builder alt = new android.support.v7.app.AlertDialog.Builder(co);

                                alt.setIcon(R.drawable.i2s);
                                alt.setTitle("Erreur  ");
                                alt.setMessage(" Saisir un montant ");
                                alt.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                android.support.v7.app.AlertDialog alertDialog = alt.create();
                                alertDialog.show();
                                testLigne = false;


                            }
                            //numcheque vide
                            if (codeModeRegSelect.equals("C") && numcheque.getText().toString().equals("")) {
                                final android.support.v7.app.AlertDialog.Builder alt = new android.support.v7.app.AlertDialog.Builder(co);
                                alt.setIcon(R.drawable.i2s);
                                alt.setTitle("Date  ");
                                alt.setMessage("Date Echeance et le numero de cheque est obligatoire");
                                alt.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                android.support.v7.app.AlertDialog dd = alt.create();
                                dd.show();

                                testLigne = false;
                            }

                            if(codeModeRegSelect.equals("E"))
                            {
                                numcheque.setText("");
                            }



                            float total = 0;
                            for (int i = 0; i < listDetailReglementClient.size(); i++) {

                                String Reference = listDetailReglementClient.get(i).getReference();
                                String Montant = listDetailReglementClient.get(i).getMontant();
                                float m = Float.parseFloat(Montant);
                                total += m;


                            }
                            float totalapaye = Float.parseFloat(txt_total_a_paye.getText().toString());
                            Log.e("total",totalapaye+"/"+total);
                            if (total >totalapaye) {


                                Toast.makeText(getApplicationContext(), "Verifiez le total des montants  ", Toast.LENGTH_LONG).show();
                                testLigne=false;

                            }







                            if (testLigne) {
                                query_ligne_detail_reglemnt = "DECLARE\t@return_value int\n" +
                                        "\n" +
                                        "EXEC\t@return_value = [dbo].[AjoutReglementInstance]\n" +
                                        "\t\t@Reference = '" + numcheque.getText().toString() + "',\n" +
                                        "\t\t@Montant = " + montant_saisi + ",\n" +
                                        "\t\t@NbTranche =" + nbtranche_saisi + ",\n" +
                                        "\t\t@DateDebut = '" + date_echeance + "',\n" +
                                        "\t\t@CodeBanque = N'" + codeBanqueSelect + "',\n" +
                                        "\t\t@CodeModeReglement = N'" + codeModeRegSelect + "'";
                                Log.e("lignedetail", query_ligne_detail_reglemnt);
                                FillListDetailReglement fillListDetailReglement = new FillListDetailReglement();
                                fillListDetailReglement.execute("");
                                nb_tranche.setText(1+"");
                                numcheque.setText("");
                                montant.setText("");
                            }





                        }
                    });

                    alt.setPositiveButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listReferences.clear();
                            listDetailReglementClient.clear();


                            dialog.cancel();
                        }
                    });
                    bt_valider.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            float total = 0;
                            for (int i = 0; i < listDetailReglementClient.size(); i++) {

                                String Reference = listDetailReglementClient.get(i).getReference();
                                String Montant = listDetailReglementClient.get(i).getMontant();
                                float m = Float.parseFloat(Montant);
                                total += m;


                            }
                            float totalapaye = Float.parseFloat(txt_total_a_paye.getText().toString());
                            if (total == totalapaye) {
                                InsertDetail insertDetail = new InsertDetail();
                                insertDetail.execute("");
                            } else {

                                Toast.makeText(getApplicationContext(), "Verifiez le total des montants  ", Toast.LENGTH_LONG).show();


                            }

                        }
                    });




                    android.support.v7.app.AlertDialog dd = alt.create();
                    dd.show();


                }
            }
        });


        bt_facture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testFacture = true;
                testBL = false;
                bt_bl.setBackgroundResource(R.drawable.buttonround);
                bt_facture.setBackgroundResource(R.drawable.round_rouge);
                if (!CodeClient_select.equals("")) {
                    FillList fillList = new FillList();
                    fillList.execute("");
                }
            }
        });
        bt_bl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testFacture = false;
                testBL = true;
                bt_facture.setBackgroundResource(R.drawable.buttonround);
                bt_bl.setBackgroundResource(R.drawable.round_rouge);
                if (!CodeClient_select.equals("")) {
                    FillList fillList = new FillList();
                    fillList.execute("");
                }
            }
        });
        CardView card_date_debut = (CardView) findViewById(R.id.card_date_debut);
        CardView card_date_fin = (CardView) findViewById(R.id.card_date_fin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
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
                                if (!CodeClient_select.equals("")) {
                                    FillList fillList = new FillList();
                                    fillList.execute("");
                                }


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

                                if (!CodeClient_select.equals("")) {
                                    FillList fillList = new FillList();
                                    fillList.execute("");
                                }
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
        GetDataSpinner getDataSpinner = new GetDataSpinner();
        getDataSpinner.execute("");
        spinClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String Code = data_CodeClient.get(position);
                    Raison_client = data_NomClient.get(position);
                    CodeClient_select = Code;

                    FillList fillList = new FillList();
                    fillList.execute("");
                } else {
                    CodeClient_select = "";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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


            final NumberFormat instance = NumberFormat.getNumberInstance(Locale.FRENCH);
            instance.setMinimumFractionDigits(3);
            instance.setMaximumFractionDigits(3);
            txt_total.setText(instance.format(total));
            txt_retenu.setText(instance.format(total_retenu));
            txt_mnt_final.setText(instance.format(total - total_retenu));
            String[] from = {"Libelle", "DatePiece", "NumeroPiece", "Debit", "Credit", "Solde", "DepasseRetenu", "check", "payer"};
            int[] views = {R.id.txt_libelle, R.id.txt_numpiece, R.id.txt_date, R.id.debit, R.id.credit, R.id.solde};
            final SimpleAdapter ADA = new SimpleAdapter(getApplicationContext(),
                    prolist, R.layout.item_fiche_client_bl, from,
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
                    convertView = layoutInflater.inflate(R.layout.item_fiche_client_bl, null);
                    final TextView txt_numpiece = (TextView) convertView.findViewById(R.id.txt_numpiece);
                    final TextView txt_date = (TextView) convertView.findViewById(R.id.txt_date);
                    final TextView txt_debit = (TextView) convertView.findViewById(R.id.debit);
                    final TextView tx_Credit = (TextView) convertView.findViewById(R.id.credit);
                    final TextView txt_solde = (TextView) convertView.findViewById(R.id.solde);
                    final TextView txt_libelle = (TextView) convertView.findViewById(R.id.txt_libelle);

                    final LinearLayout layout_item = (LinearLayout) convertView.findViewById(R.id.layout_item);
                    final CheckBox check_choisi = (CheckBox) convertView.findViewById(R.id.check_choisi);


                    final HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(position);
                    //            String[] from = {"Libelle", "DatePiece", "NumeroPiece", "Debit","Credit","Solde" };
                    String Libelle = (String) obj.get("Libelle");
                    String DatePiece = (String) obj.get("DatePiece");
                    String NumeroPiece = (String) obj.get("NumeroPiece");

                    String Debit = (String) obj.get("Debit");
                    String Credit = (String) obj.get("Credit");
                    String Solde = (String) obj.get("Solde");
                    String DepasseRetenu = (String) obj.get("DepasseRetenu");
                    String check = (String) obj.get("check");
                    String payer = (String) obj.get("payer");
                    if (DepasseRetenu.equals("1")) {
                        layout_item.setBackgroundResource(R.color.color_retenu);
                    }


                    txt_libelle.setText(Libelle);
                    txt_date.setText(DatePiece);
                    txt_numpiece.setText(NumeroPiece);
                    txt_debit.setText(Debit);
                    tx_Credit.setText(Credit);
                    txt_solde.setText(Solde);

                    if (testPassage) {

                        if (check.equals("0")) {
                            check_choisi.setVisibility(View.GONE);
                        } else {
                            check_choisi.setVisibility(View.VISIBLE);
                        }
                    } else {
                        check_choisi.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < listReferences.size(); i++) {
                        String reference = listReferences.get(i).getReference();
                        if(reference.equals(NumeroPiece))
                        {
                            check_choisi.setChecked(true);
                        }



                    }




                    check_choisi.setOnCheckedChangeListener
                            (new CheckBox.OnCheckedChangeListener() {

                                 @Override
                                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                     float total_apaye = Float.parseFloat(txt_total_a_paye.getText().toString());


                                     try {
                                         String typ = "";
                                         if (testBL) {
                                             typ = "BL";
                                         } else {
                                             typ = "FACTURE";
                                         }
                                         ListReference l = new ListReference(NumeroPiece, CodeClient_select, typ);


                                         if (isChecked) {
                                             total_apaye += Float.parseFloat(Debit) - Float.parseFloat(Credit);

                                             listReferences.add(l);


                                         } else {

                                             total_apaye -= Float.parseFloat(Debit) - Float.parseFloat(Credit);
                                             listReferences.remove(l);
                                             Log.e("remove", l.getReference());
                                             for (int i = 0; i < listReferences.size(); i++) {
                                                 String reference = listReferences.get(i).getReference();
                                                 String codeclient = listReferences.get(i).getCodeClient();
                                                 String type = listReferences.get(i).getType();
                                                 if (l.getReference().equals(reference)) {
                                                     listReferences.remove(i);
                                                 }


                                             }
                                             for (int i = 0; i < listReferences.size(); i++) {
                                                 String reference = listReferences.get(i).getReference();
                                                 String codeclient = listReferences.get(i).getCodeClient();
                                                 String type = listReferences.get(i).getType();
                                                 Log.e("afterremve", i + ":" + reference);


                                             }


                                         }


                                         txt_total_a_paye.setText(total_apaye + "");
                                     } catch (Exception e) {
                                         Log.e("number", e.toString());
                                     }


                                 }
                             }
                            );


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
                    String q = "select isnull(TauxRetenu,0)  as TauxRetenu from RetenuSource where CodeRetenu='RET001'";

                    PreparedStatement pss = con.prepareStatement(q);

                    ResultSet rss = pss.executeQuery();

                    float TauxRetenu = 0;
                    while (rss.next()) {
                        TauxRetenu = rss.getFloat("TauxRetenu");
                    }
                    if (testBL) {
                        querysearch = "DECLARE\t@return_value int\n" +
                                "\n" +
                                "EXEC\t@return_value = [dbo].[FicheClientbl]\n" +
                                "\t\t@CodeClient = N'" + CodeClient_select + "',\n" +
                                "\t\t@DateDebut = '" + datedebut + "',\n" +
                                "\t\t@DateFin = '" + datefin + "'";

                    } else {


                        querysearch = "DECLARE\t@return_value int\n" +
                                "\n" +
                                "EXEC\t@return_value = [dbo].[FicheClient]\n" +
                                "\t\t@CodeClient = N'" + CodeClient_select + "',\n" +
                                "\t\t@DateDebut = '" + datedebut + "',\n" +
                                "\t\t@DateFin = '" + datefin + "'";
                    }

                    PreparedStatement ps = con.prepareStatement(querysearch);
                    Log.e("querysearch", querysearch);
                    ResultSet rs = ps.executeQuery();

                    ArrayList data1 = new ArrayList();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();

                        //            String[] from = {"Libelle", "DatePiece", "NumeroPiece", "Debit","Credit","Solde" };
                        datanum.put("Libelle", rs.getString("Libelle"));
                        datanum.put("NumeroPiece", rs.getString("NumeroPiece"));
                        Date d = rs.getDate("DatePiece");
                        String DATE_FORMAT_2 = "dd-MM-yyyy";
                        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_2);


                        datanum.put("DatePiece", dateFormat.format(d) + "");
                        datanum.put("Debit", rs.getString("Debit"));
                        datanum.put("TypeVente", rs.getString("TypeVente"));
                        datanum.put("Credit", rs.getString("Credit"));
                        float solde = Float.valueOf(rs.getString("Debit")) - Float.valueOf(rs.getString("Credit"));

                        final NumberFormat instance = NumberFormat.getNumberInstance(Locale.FRENCH);
                        instance.setMinimumFractionDigits(3);
                        instance.setMaximumFractionDigits(3);
                        String depasse_retenu = "0";
                        float mnt_retenu = 0;
                        if (solde > 1000) {
                            depasse_retenu = "1";
                            mnt_retenu = solde * TauxRetenu / 100;
                            total_retenu += mnt_retenu;


                        }

                        datanum.put("DepasseRetenu", depasse_retenu);
                        datanum.put("Solde", instance.format(solde) + "");
                        total += solde;
                        /////////////test payement////////////////////////
                        String query_payement = "", numpiece = rs.getString("NumeroPiece");
                        if (testBL) {
                            query_payement = "  select NumeroEtat from BonLivraisonVente where NumeroBonLivraisonVente='" + numpiece + "'  ";
                        } else {
                            query_payement = "select NumeroEtat from FactureVente where NumeroFactureVente='" + numpiece + "'";
                        }

                        PreparedStatement ps_payement = con.prepareStatement(query_payement);
                        ResultSet rs_payement = ps_payement.executeQuery();
                        String NumeroEtat = "";
                        Boolean test_payement = false;
                        while (rs_payement.next()) {

                            NumeroEtat = rs_payement.getString("NumeroEtat");
                            if (NumeroEtat.equals("E06") || NumeroEtat.equals("E08")) {
                                test_payement = true;
                            }
                        }

                        ///////////////////////////////////////////////////
                        String check = "", TypeVente = rs.getString("TypeVente"), libelle = rs.getString("Libelle"), payer = "";
                        if (testBL) {
                            if (!TypeVente.equals("VENTE")) {
                                check = "0";
                                payer = "";
                            } else {
                                if (test_payement) {
                                    check = "0";
                                    payer = "";
                                } else {
                                    check = "1";
                                    payer = "Non payé";
                                }
                            }


                        } else {
                            //par facture
                            if (!libelle.equals("FACTURE")) {
                                check = "0";
                            } else if (test_payement) {
                                check = "0";
                            } else {
                                check = "1";
                                payer = "Non payé";
                            }


                        }
                        ///report
                        if (numpiece.equals("")) {
                            check = "0";
                        }


                        datanum.put("check", check);
                        datanum.put("payer", payer);
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


    public class GetDataSpinner extends AsyncTask<String, String, String> {
        String z = "  ";

        List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {
            //  Log.e("frs", querylist);
            // pbbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {


            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getApplicationContext(),
                    R.layout.spinner, data_NomClient);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            spinClient.setAdapter(adapter);


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    PreparedStatement stmt, stmt2;
                    data_CodeClient = new ArrayList<String>();
                    data_NomClient = new ArrayList<String>();
                    stmt = con.prepareStatement("select  CodeClient,RaisonSociale from Client");
                    ResultSet rsss = stmt.executeQuery();
                    data_CodeClient.add("");
                    data_NomClient.add("Rechercher Client");
                    while (rsss.next()) {
                        String id = rsss.getString("CodeClient");
                        String designation = rsss.getString("RaisonSociale");
                        data_CodeClient.add(id);
                        data_NomClient.add(designation);

                    }


                }
            } catch (SQLException ex) {
                z = "list" + ex.toString();

            } catch (Exception e) {

            }
            return z;
        }
    }

    public class GetDataSpinnerReglement extends AsyncTask<String, String, String> {
        String z = "  ";


        @Override
        protected void onPreExecute() {
            //  Log.e("frs", querylist);
            // pbbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {


            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getApplicationContext(),
                    R.layout.spinner, data_NomReglement);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            spinReglement.setAdapter(adapter);


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    PreparedStatement stmt, stmt2;
                    data_CodeReglement = new ArrayList<String>();
                    data_NomReglement = new ArrayList<String>();
                    stmt = con.prepareStatement("select CodeModeReglement,Libelle from ModeReglement");
                    ResultSet rsss = stmt.executeQuery();
                    data_CodeReglement.add("");
                    data_NomReglement.add("choisir un mode");
                    while (rsss.next()) {
                        String id = rsss.getString("CodeModeReglement");
                        String designation = rsss.getString("Libelle");
                        data_CodeReglement.add(id);
                        data_NomReglement.add(designation);

                    }


                }
            } catch (SQLException ex) {
                z = "list" + ex.toString();

            } catch (Exception e) {

            }
            return z;
        }
    }

    public class GetDataSpinnerbanque extends AsyncTask<String, String, String> {
        String z = "  ";


        @Override
        protected void onPreExecute() {
            //  Log.e("frs", querylist);
            // pbbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {


            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getApplicationContext(),
                    R.layout.spinner, data_NomBanque);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            spinBanque.setAdapter(adapter);


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    PreparedStatement stmt, stmt2;
                    data_CodeBanque = new ArrayList<String>();
                    data_NomBanque = new ArrayList<String>();
                    stmt = con.prepareStatement("select CodeBanque,RaisonSociale from Banque");
                    ResultSet rsss = stmt.executeQuery();
                    data_CodeBanque.add("");
                    data_NomBanque.add("choisir un Banque");
                    while (rsss.next()) {
                        String id = rsss.getString("CodeBanque");
                        String designation = rsss.getString("RaisonSociale");
                        data_CodeBanque.add(id);
                        data_NomBanque.add(designation);

                    }


                }
            } catch (SQLException ex) {
                z = "list" + ex.toString();

            } catch (Exception e) {

            }
            return z;
        }
    }


    public class FillListDetailReglement extends AsyncTask<String, String, String> {
        String z = "";
        Boolean test = false;


        List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);


        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String r) {
            progressBar.setVisibility(View.GONE);


            String[] from = {"Reference", "Montant", "NbTranche", "Echeance", "CodeBanque", "CodeModeReglement"};
            int[] views = {R.id.txt_reference, R.id.txt_montant, R.id.txt_echeance, R.id.txt_code_banque, R.id.txt_mode_reg};
            final SimpleAdapter ADA = new SimpleAdapter(getApplicationContext(),
                    prolist, R.layout.item_detail_reglement_client, from,
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
                    //int[] views = {R.id.txt_reference, R.id.txt_montant, R.id.txt_echeance, R.id.txt_code_banque,
                    // R.id.txt_mode_reg};
                    //
                    convertView = layoutInflater.inflate(R.layout.item_detail_reglement_client, null);
                    final TextView txt_reference = (TextView) convertView.findViewById(R.id.txt_reference);
                    final TextView txt_montant = (TextView) convertView.findViewById(R.id.txt_montant);
                    final TextView txt_echeance = (TextView) convertView.findViewById(R.id.txt_echeance);
                    final TextView txt_code_banque = (TextView) convertView.findViewById(R.id.txt_code_banque);
                    final TextView txt_mode_reg = (TextView) convertView.findViewById(R.id.txt_mode_reg);


                    final HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(position);
                    //            String[] from = {"Reference", "Montant", "NbTranche", "Echeance", "CodeBanque","CodeModeReglement"};
                    String Reference = (String) obj.get("Reference");
                    String Montant = (String) obj.get("Montant");
                    String NbTranche = (String) obj.get("NbTranche");

                    String Echeance = (String) obj.get("Echeance");
                    String CodeBanque = (String) obj.get("CodeBanque");
                    String CodeModeReglement = (String) obj.get("CodeModeReglement");


                    txt_reference.setText(Reference);
                    txt_code_banque.setText(CodeBanque);
                    txt_mode_reg.setText(CodeModeReglement);
                    txt_echeance.setText(Echeance);
                    txt_montant.setText(Montant);


                    return convertView;
                }
            };


            grid_cheque.setAdapter(baseAdapter);


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {


                    PreparedStatement ps = con.prepareStatement(query_ligne_detail_reglemnt);
                    Log.e("query", query_ligne_detail_reglemnt);

                    ResultSet rs = ps.executeQuery();
                    z = "e";
                    Map<String, String> data = new HashMap<String, String>();
                    data.put("Reference", "Ref");
                    data.put("Montant", "Mnt");
                    data.put("NbTranche", "Nb");
                    data.put("Echeance", "Echeance");

                    data.put("CodeBanque", "Banque");
                    data.put("CodeModeReglement", "Mode");

                    prolist.add(data);
                    while (rs.next()) {

                        //    String Reference,CodeBaque,Echeance,CodeModeReglement,Montant;
                        DetailReglementClient detail = new DetailReglementClient(rs.getString("Reference"),
                                rs.getString("CodeBanque"), rs.getString("Echeance"), rs.getString("CodeModeReglement"),
                                rs.getString("Montant"), rs.getString("NbTranche"));
                        listDetailReglementClient.add(detail);


                        test = true;


                        z = "succees";
                    }

                    for (int i = 0; i < listDetailReglementClient.size(); i++) {
                        Map<String, String> alldata = new HashMap<String, String>();
                        alldata.put("Reference", listDetailReglementClient.get(i).getReference());
                        alldata.put("Montant", listDetailReglementClient.get(i).getMontant());
                        alldata.put("NbTranche", "" + i);
                        alldata.put("Echeance", listDetailReglementClient.get(i).getEcheance());

                        alldata.put("CodeBanque", listDetailReglementClient.get(i).getCodeBaque());
                        alldata.put("CodeModeReglement", listDetailReglementClient.get(i).getCodeModeReglement());

                        prolist.add(alldata);


                    }


                }
            } catch (SQLException ex) {
                z = "tablelist" + ex.toString();
                Log.e("erreur", z);


            }
            return z;
        }
    }

    /////////////***************************************************/////////////

    public class InsertDetail extends AsyncTask<String, String, String> {
        String z = "";
        String totalapaye = "";


        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            totalapaye = txt_total_a_paye.getText().toString();
        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), "Validé", Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);
            // helper.RemoveTransfert(NumTransfert);
            listReferences.clear();
            listDetailReglementClient.clear();
            Intent intent=new Intent(getApplicationContext(),MenuReglement.class);
            startActivity(intent);


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";

                } else {
// compteur
                    String query = "Select * from CompteurPiece  where NomPiecer='ReglementClient'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    String pre = "", NumeroReglementClient = "";
                    while (rs.next()) {
                        String anciencompteur = rs.getString("Compteur");

                        String annee = rs.getString("Annee");
                        String PrefixPiece = rs.getString("PrefixPiece");
                        pre = PrefixPiece + annee + anciencompteur;
                        NumeroReglementClient = pre;
                        Float comp = Float.parseFloat(anciencompteur);
                        comp++;

                        DecimalFormat numberFormat = new DecimalFormat("00000");
                        String st = numberFormat.format(comp);


                        String query2 = "update CompteurPiece  set Compteur='" + st + "' where NomPiecer='ReglementClient'";
                        PreparedStatement preparedStatement = con.prepareStatement(query2);


                        preparedStatement.executeUpdate();


                    }


                    //reglement
                    String q = "INSERT INTO   ReglementClient \n" +
                            "           ( NumeroReglementClient  \n" +
                            "           , DateReglement \n" +
                            "           , CodeClient \n" +
                            "           , TotalBrute \n" +
                            "           , TotalAvance \n" +
                            "           , TotalRetenu \n" +
                            "           , TotalNet \n" +
                            "           , TotalRecu \n" +
                            "           , NomUtilisateur \n" +
                            "           , DateCreation \n" +
                            "           , HeureCreation \n" +
                            "           , Observation \n" +
                            "           , TypeReglement \n" +
                            "           , Remise \n" +
                            "           , Virtuel \n" +
                            "           , RaisonSociale )\n" +
                            "     VALUES\n" +
                            "           ( '" + NumeroReglementClient + "', \n" +

                            "              getdate(),  \n" +
                            "             '" + CodeClient_select + "',  \n" +
                            "              " + totalapaye + ",  \n" +
                            "             0,  \n" +
                            "             0,  \n" +
                            "              " + totalapaye + ",  \n" +
                            "            0,  \n" +
                            "             '" + NomUtilisateur + "',  \n" +
                            "             convert(date,getdate(),103),  \n" +
                            "             getdate(),  \n" +
                            "            '',  \n" +
                            "             'P',  \n" +
                            "             0,  \n" +
                            "             0, \n" +
                            "             (select RaisonSociale from Client where CodeClient='" + CodeClient_select + "')  )\n" +
                            " ";
                    PreparedStatement preparedStatement_entete = con.prepareStatement(q);
                    Log.e("ReglementClient", q);

                    preparedStatement_entete.executeUpdate();


                         ///ligne  reglement

                    for (int i = 0; i < listReferences.size(); i++) {

                        String CodeClient = listReferences.get(i).getCodeClient();
                        String Reference = listReferences.get(i).getReference();
                        String Type = listReferences.get(i).getType();
                        String table = "", num = "";
                        if (Type.equals("BL")) {
                            table = "BonLivraisonVente";
                            num = "NumeroBonLivraisonVente";
                        } else {
                            table = "BonLivraisonVente";
                            num = "NumeroBonLivraisonVente";
                        }


                        String queryligne =
                                "INSERT INTO LigneReglementClient\n" +
                                        "           (NumeroReglementClient\n" +
                                        "           ,NumeroPiece\n" +
                                        "           ,TotalTVA\n" +
                                        "           ,MontantPieceTTC\n" +
                                        "           ,Observation\n" +
                                        "           ,TotalRecu\n" +
                                        "           ,TotalRestant\n" +
                                        "           ,TotalPayee)\n" +
                                        "     VALUES\n" +
                                        "           ( '" + NumeroReglementClient + "', \n" +
                                        "            '" + Reference + "', \n" +
                                        "            (select ISNULL(TotalTVA,0) from " + table + " where " + num + "='" + Reference + "'),  \n" +
                                        "            (select ISNULL(TotalTTC,0) from " + table + " where " + num + "='" + Reference + "'),  \n" +
                                        "             '', \n" +
                                        "            0,  \n" +
                                        "            0,  \n" +
                                        "            (select ISNULL(TotalTTC,0) from " + table + " where " + num + "='" + Reference + "')  \n" +
                                        " )\n" +
                                        "";
                        Log.e("qLigneReglementClient", queryligne);
                        PreparedStatement preparedStatementligne = con.prepareStatement(queryligne);


                        preparedStatementligne.executeUpdate();

                    }

                    for (int i = 0; i < listDetailReglementClient.size(); i++) {

                        String Reference = listDetailReglementClient.get(i).getReference();
                        String Montant = listDetailReglementClient.get(i).getMontant();
                        String NbTranch = "" + i;
                        String Echeance = listDetailReglementClient.get(i).getEcheance();

                        String CodeBanque = listDetailReglementClient.get(i).getCodeBaque();
                        String CodeModeReglement = listDetailReglementClient.get(i).getCodeModeReglement();

                        String queryligne =
                                " INSERT INTO   DetailReglementClient \n" +
                                        "           ( NumeroReglementClient \n" +
                                        "           , CodeModeReglement \n" +
                                        "           , Reference \n" +
                                        "           , Echeance \n" +
                                        "           , CodeBanque \n" +
                                        "           , MontantRecu \n" +
                                        "           , NumeroEtat \n" +
                                        "           , CodeCompte \n" +
                                        "           , MontantLettre \n" +
                                        "           , Imprimer \n" +
                                        "           , Porteur \n" +
                                        "           , RIB \n" +
                                        "           , Comptable \n" +
                                        "           , NumeroBordereau \n" +
                                        "           , MontantVerser \n" +
                                        "           , NumeroOperationRecu \n" +
                                        "           , NumeroCommution \n" +
                                        "           , TauxCommution \n" +
                                        "           , MontantCommution \n" +
                                        "           , MontantRestant )\n" +
                                        "     VALUES\n" +
                                        "           ( '" + NumeroReglementClient + "', \n" +

                                        "             '" + CodeModeReglement + "', \n" +
                                        "           '" + Reference + "',  \n" +
                                        "             convert(date,  '" + Echeance + "'),  \n" +
                                        "           '" + CodeBanque + "',  \n" +
                                        "             " + Montant + ",  \n" +
                                        "             'E13',  \n" +
                                        "              '',  \n" +
                                        "             0,  \n" +
                                        "             0, \n" +
                                        "             '',  \n" +
                                        "             '',  \n" +
                                        "             0, \n" +
                                        "              '',  \n" +
                                        "             0,  \n" +
                                        "              '',  \n" +
                                        "             '', \n" +
                                        "             0, \n" +
                                        "             0,  \n" +
                                        "             0  )\n" +
                                        "\n" +
                                        "\n";
                        Log.e("qDetail", queryligne);
                        PreparedStatement preparedStatementligne = con.prepareStatement(queryligne);


                        preparedStatementligne.executeUpdate();

                    }


                }
            } catch (SQLException ex) {
                z = "list" + ex.toString();
                Log.e("erreurquery", ex.toString());

            }
            return z;
        }
    }


}
