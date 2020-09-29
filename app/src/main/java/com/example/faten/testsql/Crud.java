package com.example.faten.testsql;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crud extends AppCompatActivity {
    String user, password, base, ip;
    ConnectionClass connectionClass;
    ListView lstpro;
    String proid;
    String zonederecherche;
    EditText textrecherche;
    String querylist;
    String DernierPrixAchatHT="",TauxRemise="",PrixAchatHT="",LiteTauxRemise="",Actif="",Stockable="",Destockage="",DernierPrixDevise="",prixTTC  ="";
    final Context co = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);
        connectionClass = new ConnectionClass();
        SharedPreferences prefe = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        user = prefe.getString("user", user);
        ip = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base = prefe.getString("base", base);

        lstpro = (ListView) findViewById(R.id.lstproducts);
        proid = "";
        querylist = "select  * from Article ";
        FillList list=new FillList();
        list.execute("");


        Button show= (Button)findViewById(R.id.show);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                querylist = "select  * from Article ";
                FillList fill=new FillList();
                fill.execute("");
            }
        });


        Button bb= (Button)findViewById(R.id.bt);
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textrecherche=(EditText)findViewById(R.id.zonederechercheART);
                zonederecherche =textrecherche.getText().toString();
                zonederecherche =textrecherche.getText().toString();
                if(zonederecherche.equals(""))
                { Toast.makeText(getApplicationContext(),zonederecherche,Toast.LENGTH_SHORT).show();
                    querylist = "select  * from Article   ";
                    FillList fill=new FillList();
                    fill.execute("");
                }else{
                    querylist = "select  * from Article where  CodeArticle like '%"+zonederecherche+"%' ";

                    FillList f=new FillList();
                    f.execute();
                }
            }









        });
        Button btajout= (Button)findViewById(R.id.btajout);
        btajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AddArticle.class);
                startActivity(intent);
            }
        });



    }

    /////////////********************Affichage list ***********************/////////////
    /////////////********************************************************/////////////
    /////////////********************************************************/////////////
    /////////////***************************************************/////////////

    public class FillList extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> prolist  = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String r) {


       Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();

            String[] from = { "A", "B", "C","D" ,"E","F","G","H","I","J","K","L","M","N","O"};
            int[] views = { R.id.codart, R.id.designation,R.id.prix };
            final SimpleAdapter ADA = new SimpleAdapter(getApplicationContext(),
                    prolist, R.layout.listcrud, from,
                    views);
            lstpro.setAdapter(ADA);

            lstpro.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    proid = (String) obj.get("A");
                    LayoutInflater li = LayoutInflater.from(co);
                          View px = li.inflate(R.layout.diagcrud, null);
                                  AlertDialog.Builder alt = new AlertDialog.Builder(co);
                    alt.setIcon(R.drawable.i2s);
                    alt.setTitle("Article");
                    alt.setView(px);
                    connectionClass = new ConnectionClass();

                    TextView codart=(TextView)px.findViewById(R.id.codart);
                    TextView marque=(TextView)px.findViewById(R.id.marque);
                    TextView famille=(TextView)px.findViewById(R.id.famille);
                    final  EditText prix=(EditText)px.findViewById(R.id.prixttc);
                    final EditText prixachatht=(EditText)px.findViewById(R.id.prixachatht);
                    final EditText tauxremise=(EditText)px.findViewById(R.id.tauxremise);
                    final  EditText Literemise=(EditText)px.findViewById(R.id.remise);
                    final EditText dernierprixdevise=(EditText)px.findViewById(R.id.prixdevise);
                    final EditText dernierprixht=(EditText)px.findViewById(R.id.dernierpaht);
                    final CheckBox actifcheck=(CheckBox)px.findViewById(R.id.actif);
                    final CheckBox stockchheck=(CheckBox)px.findViewById(R.id.stockable);
                    final  CheckBox destockchheck=(CheckBox)px.findViewById(R.id.destokage);

                    TextView des=(TextView)px.findViewById(R.id.designation);
                    TextView type=(TextView)px.findViewById(R.id.type);
                    codart.setText(proid);
                    marque.setText((String) obj.get("E"));

                    dernierprixht.setText((String) obj.get("I"));
                    Literemise.setText((String) obj.get("K"));

                    tauxremise.setText((String) obj.get("L"));

                    type.setText((String) obj.get("H"));
                    famille.setText((String) obj.get("F"));
                    prix.setText((String) obj.get("C"));
                    des.setText((String) obj.get("B"));
                    prixachatht.setText((String) obj.get("D"));

                    dernierprixdevise.setText((String) obj.get("J"));

                    if(actifcheck.isSelected())
                    {
                        Actif="1";
                    };
                    if(stockchheck.isSelected())
                    {
                     Stockable="1";
                    }
                    if(destockchheck.isSelected())
                    {
                        Destockage="1";

                    }




                    alt.setCancelable(false)
                            .setPositiveButton("Modifier",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface di, int i) {
                                            PrixAchatHT=prixachatht.getText().toString();
                                            DernierPrixAchatHT=dernierprixht.getText().toString();
                                            DernierPrixDevise=dernierprixdevise.getText().toString();
                                           TauxRemise=tauxremise.getText().toString();
                                           LiteTauxRemise=Literemise.getText().toString();
                                            prixTTC=prix.getText().toString();
                                            if(actifcheck.isChecked())
                                            {
                                                Actif="1";
                                            };
                                            if(stockchheck.isChecked())
                                            {
                                                Stockable="1";
                                            }
                                            if(destockchheck.isChecked())
                                            {
                                                Destockage="1";

                                            }
                                            update up=new update();
                                            up.execute("");

                                        }
                                    })
                            .setNegativeButton("Delete",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface di, int i) {
                                            delete d=new delete();
                                            d.execute("");

                                        }
                                    })
                            .setNeutralButton("Annuler",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface di, int i) {
                                            di.cancel();

                                        }
                                    })
                    ;
                    AlertDialog d = alt.create();
                    d.show();
                }
            });

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {


                    PreparedStatement ps = con.prepareStatement(querylist);
                    ResultSet rs = ps.executeQuery();

                    ArrayList data1 = new ArrayList();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", rs.getString("CodeArticle"));
                        datanum.put("B", rs.getString("Designation"));
                        String p= rs.getString("PrixVenteTTC");
                        datanum.put("C",p.substring(0,p.length()-2));
                        datanum.put("E", rs.getString("CodeMarque"));
                        datanum.put("F", rs.getString("CodeFamille"));
                        datanum.put("G", rs.getString("CodeSexe"));
                        datanum.put("H", rs.getString("CodeType"));
                        String paht= rs.getString("PrixAchatHT");
                        paht=paht.substring(0,paht.length()-2);
                        datanum.put("D", paht);

                        String dpaht =rs.getString("DernierPrixAchatHT");
                        dpaht=dpaht.substring(0,dpaht.length()-2);
                        datanum.put("I", dpaht);

                        String dpd= rs.getString("DernierPrixDevise");
                        dpd=paht.substring(0,dpd.length()-2);
                        datanum.put("J", dpd);


                        String t= rs.getString("LiteTauxRemise");
                        t=t.substring(0,t.length()-2);
                        datanum.put("K",t);

                        String dt =rs.getString("DernierTauxRemise");
                        dt=dt.substring(0,dt.length()-2);
                        datanum.put("L", dt);
                        datanum.put("M", rs.getString("Stockable"));
                        datanum.put("N", rs.getString("Actif"));
                        datanum.put("O", rs.getString("Destockage"));
                        prolist.add(datanum);
                        z = "Success";


                    }



                }
            } catch (SQLException ex) {
                z = ex.toString();

            }
            return z;
        }
    }


    /////////////******************** fin Affichage list ***********************/////////////
    /////////////********************************************************/////////////
    /////////////********************************************************/////////////
    /////////////***************************************************/////////////


    /////////////********************update ***********************/////////////
    /////////////********************************************************/////////////
    /////////////********************************************************/////////////
    /////////////***************************************************/////////////

    public class update extends AsyncTask<String, String, String> {
        String z = "";


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

   Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con =  connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                String query = "Update Article set DernierPrixAchatHT='"+DernierPrixAchatHT+"' , LiteTauxRemise='"+LiteTauxRemise+"', DernierPrixDevise ='"+DernierPrixDevise+"', Actif='"+Actif+"', Stockable ='"+Stockable+"',Destockage ='"+Destockage+" ',DernierTauxRemise='"+TauxRemise+" ',PrixVenteTTC='"+prixTTC+"',PrixAchatHT ='"+PrixAchatHT+"'where CodeArticle='"+proid+"'";

                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();
                    z = "Updated Successfully";

                }
            } catch (SQLException ex) {
                z = ex.toString();

            }
            return z;
        }
    }

//////////////////////////////////////////////////////
    public class  delete extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;



        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String r) {


            //  Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();

            if(isSuccess==true) {
                FillList fillList = new FillList();
                fillList.execute("");
            }


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con =  connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "Delete from Article where CodeArticle='"+proid+"' ";

                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();
                    z = "Deleted Successfully";

                    isSuccess = true;
                }
            } catch (SQLException ex) {
                z = ex.toString();

            }
            return z;
        }
    }


    /////////////******************** fin  ***********************/////////////
    /////////////********************************************************/////////////
    /////////////********************************************************/////////////



}
