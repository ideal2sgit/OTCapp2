package com.example.faten.testsql.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.R;
import com.example.faten.testsql.adapter.LigneBLAdapter;
import com.example.faten.testsql.model.BonLivraisonVente;
import com.example.faten.testsql.model.LigneBonLivraisonVente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


@SuppressLint("ValidFragment")
public class BonLivraisonDetail extends DialogFragment {
    ConnectionClass connectionClass;
    String user, password, base, ip;
    private String NumeroBonLivraisonVente;

    public void setNumeroBonLivraisonVente(String numeroBonLivraisonVente) {
        NumeroBonLivraisonVente = numeroBonLivraisonVente;
    }


    TextView txt_numero_piece, txt_date_piece, txt_etat, txt_prix_ttc, txt_livreur;
    ListView lv_ligne_bl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_bon_livraison_detail, container);
        txt_numero_piece = rootView.findViewById(R.id.txt_num_piece);
        txt_date_piece = rootView.findViewById(R.id.txt_date_piece);
        txt_etat = rootView.findViewById(R.id.txt_etat);
        txt_prix_ttc = rootView.findViewById(R.id.txt_total_ttc);
        txt_livreur = rootView.findViewById(R.id.txt_livreur);
        lv_ligne_bl = rootView.findViewById(R.id.lv_ligne_bl);

        connectionClass = new ConnectionClass();
        SharedPreferences prefe = getActivity().getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        user = prefe.getString("user", user);
        ip = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base = prefe.getString("base", base);

        BonLivraisonTask bonLivraisonTask = new BonLivraisonTask(NumeroBonLivraisonVente);
        bonLivraisonTask.execute();

        return rootView;
    }


    public class BonLivraisonTask extends AsyncTask<String, String, String> {
        String z = "";
        Boolean test = false;

        SimpleDateFormat dfSQL = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String NumeroBonLivraisonVente;

        public BonLivraisonTask(String numeroBonLivraisonVente) {
            NumeroBonLivraisonVente = numeroBonLivraisonVente;
        }

        DecimalFormat df = new DecimalFormat("0.000");
        BonLivraisonVente blv;
        ArrayList<LigneBonLivraisonVente> list_ligne_blv = new ArrayList<>();


        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String query = " SELECT [NumeroBonLivraisonVente]\n" +
                            "      ,[DateBonLivraisonVente]\n" +
                            "      ,[NumeroBonCommandeVente]\n" +
                            "      ,[TotalHT]\n" +
                            "      ,[TotalRemise]\n" +
                            "      ,[TotalNetHT]\n" +
                            "      ,[TotalTVA]\n" +
                            "      ,[TotalTTC]\n" +
                            "      ,Etat.[NumeroEtat]\n" +
                            "      ,Etat.[Libelle]\n" +
                            "      ,[NumeroFactureVente]\n" +
                            "      ,Livreur.[CodeLivreur]\n" +
                            "      ,Livreur.[NomPrenom]\n" +
                            "      \n" +
                            "  FROM  [BonLivraisonVente]\n" +
                            "  INNER  JOIN Etat  ON Etat.NumeroEtat = [BonLivraisonVente].NumeroEtat\n" +
                            "  INNER  JOIN Livreur  ON Livreur.CodeLivreur = [BonLivraisonVente].CodeLivreur\n" +
                            "  where [NumeroBonLivraisonVente] = '" + NumeroBonLivraisonVente + "' ";

                    Log.e("query", "" + query);
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    z = "e";

                    while (rs.next()) {

                        String NumeroBonLivraisonVente = rs.getString("NumeroBonLivraisonVente");

                        Date DateBonLivraisonVente = new Date();

                        try {
                            DateBonLivraisonVente = dfSQL.parse(rs.getString("DateBonLivraisonVente"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String NumeroBonCommandeVente = rs.getString("NumeroBonCommandeVente");
                        double TotalHT = rs.getDouble("TotalHT");

                        double TotalRemise = rs.getDouble("TotalRemise");
                        double TotalNetHT = rs.getDouble("TotalNetHT");
                        double TotalTVA = rs.getDouble("TotalTVA");
                        double TotalTTC = rs.getDouble("TotalTTC");

                        String NumeroEtat = rs.getString("NumeroEtat");
                        String Libelle = rs.getString("Libelle");

                        String NumeroFactureVente = rs.getString("NumeroFactureVente");
                        String CodeLivreur = rs.getString("CodeLivreur");
                        String NomPrenom = rs.getString("NomPrenom");

                        test = true;

                        blv = new BonLivraisonVente(NumeroBonLivraisonVente, DateBonLivraisonVente, NumeroBonCommandeVente, TotalHT, TotalRemise, TotalNetHT, TotalTVA, TotalTTC, NumeroEtat, Libelle, NumeroFactureVente, CodeLivreur, NomPrenom);
                        Log.e("BonLivraisonVente", blv.toString());
                        z = "Chargement avec Success";
                    }


                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                    String queryLBLV = " SELECT  [NumeroBonLivraisonVente]\n" +
                            "      ,[CodeArticle]\n" +
                            "      ,[DesignationArticle]\n" +
                            "      ,[NumeroOrdre]\n" +
                            "      ,[PrixVenteHT]\n" +
                            "      ,[Quantite]\n" +
                            "      ,[MontantHT]\n" +
                            "      ,[TauxRemise]\n" +
                            "      ,[MontantRemise]\n" +
                            "      ,[NetHT]\n" +
                            "      ,[TauxTVA]\n" +
                            "      ,[MontantTVA]\n" +
                            "      ,[MontantTTC]\n" +
                            "      ,[PrixAchatNet]\n" +
                            "      ,[NumeroBonCommandeVente]\n" +
                            "  \n" +
                            "  FROM  [LigneBonLivraisonVente]\n" +
                            "  where [NumeroBonLivraisonVente] = '"+NumeroBonLivraisonVente+"'\n" +
                            "  ORDER  BY [NumeroOrdre] ";

                    Log.e("query", "" + queryLBLV);
                    PreparedStatement ps2 = con.prepareStatement(queryLBLV);
                    ResultSet rs2 = ps2.executeQuery();
                    z = "e";

                    while (rs2.next()) {

                        //CodeArticle
                        String NumeroBonLivraisonVente = rs2.getString("NumeroBonLivraisonVente");
                        String CodeArticle = rs2.getString("CodeArticle");
                        String DesignationArticle = rs2.getString("DesignationArticle");

                        int NumeroOrdre = rs2.getInt("NumeroOrdre");
                        double PrixVenteHT = rs2.getDouble("PrixVenteHT");
                        int Quantite = rs2.getInt("Quantite");

                        double MontantHT = rs2.getDouble("MontantHT");
                        double TauxRemise = rs2.getDouble("TauxRemise");
                        double MontantRemise = rs2.getDouble("MontantRemise");
                        double NetHT = rs2.getDouble("NetHT");
                        double TauxTVA = rs2.getDouble("TauxTVA");


                        double MontantTVA = rs2.getDouble("MontantTVA");
                        double MontantTTC = rs2.getDouble("MontantTTC");
                        double PrixAchatNet = rs2.getDouble("PrixAchatNet");
                        String NumeroBonCommandeVente = rs2.getString("NumeroBonCommandeVente");


                        LigneBonLivraisonVente l_blv = new LigneBonLivraisonVente(NumeroBonLivraisonVente, CodeArticle, DesignationArticle, NumeroOrdre, PrixVenteHT, Quantite, MontantHT, TauxRemise, MontantRemise, NetHT, TauxTVA, MontantTVA, MontantTTC, PrixAchatNet, NumeroBonCommandeVente);
                        Log.e("LigneBonLivraisonVente", l_blv.toString());
                        list_ligne_blv.add(l_blv);

                        test = true;

                        z = "Chargement avec Success";
                    }


                }
            } catch (Exception ex) {
                Log.e("ERROR", ex.toString());
                z = "etape" + ex.toString();
            }
            return z;
        }


        @Override
        protected void onPreExecute() {


            /*totaldebit = 0;
            totalcredit = 0;
            TotalSolde = 0.0;*/

        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String r) {
            txt_numero_piece.setText(blv.getNumeroBonLivraisonVente());
            txt_date_piece.setText(sdf.format(blv.getDateBonLivraisonVente()));
            txt_prix_ttc.setText(df.format(blv.getTotalTTC()) + " DT");
            txt_etat.setText(blv.getEtat());
            txt_livreur.setText("Livré par :" + blv.getLivreur());

            LigneBLAdapter ligneBLAdapter  = new LigneBLAdapter(getActivity(),list_ligne_blv);
            lv_ligne_bl.setAdapter(ligneBLAdapter);


        }

    }

}
