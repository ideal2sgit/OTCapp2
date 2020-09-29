package com.example.faten.testsql.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faten.testsql.ConnectionClass;
import com.example.faten.testsql.R;
import com.example.faten.testsql.adapter.FicheClientAdapter;
import com.example.faten.testsql.dialog.BonLivraisonDetail;
import com.example.faten.testsql.model.FicheClient;
import com.example.faten.testsql.pdf.FicheClientPdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FicheClientActivity extends AppCompatActivity {
    ConnectionClass connectionClass;


    private static final String IMAGE_DIRECTORY_NAME = "PDF_app";

    private static final String AUTHORITY = "com.example.faten.testsql";

    String user, password, base, ip;
    ListView lv_fiche_client;

    final Context co = this;

    double totalcredit = 0.0, totaldebit;

    double TotalSolde = 0.0;
    TextView txttotal;
    String datedebut = "", datefin = "";
    DatePicker datePicker;
    String codeClient = "", raisonSocial = "";
    TextView txtdebit, txtcredit;
    TextView txtdatedebut, txtdatefin, txtclient;


    public static ArrayList<FicheClient> listFicheClient = new ArrayList<>();
    private static final int REQUEST_CODE = 0x3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_client);

        connectionClass = new ConnectionClass();


        SharedPreferences prefe = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        user = prefe.getString("user", user);
        ip = prefe.getString("ip", ip);
        password = prefe.getString("password", password);
        base = prefe.getString("base", base);

        Intent intent = getIntent();
        codeClient = intent.getStringExtra("codeclient");
        raisonSocial = intent.getStringExtra("rsclient");
        Toast.makeText(getApplicationContext(), codeClient, Toast.LENGTH_SHORT).show();

        final Button btdatedebut = (Button) findViewById(R.id.btdatedebut);
        final Button btdatefin = (Button) findViewById(R.id.btdatefin);
        txtdatedebut = (TextView) findViewById(R.id.edtdatedebut);
        txtclient = (TextView) findViewById(R.id.rsclient);
        txtdatefin = (TextView) findViewById(R.id.edtdatefin);

        DefaultDates d = new DefaultDates();
        d.execute("");

        txtclient.setText(raisonSocial);
        // txtcredit=(TextView)px.findViewById(R.id.txtcredit);
        txttotal = (TextView) findViewById(R.id.total);
        lv_fiche_client = (ListView) findViewById(R.id.listfiche);
        final Button check = (Button) findViewById(R.id.btconsulter);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListFicheClientTask ficheClients = new ListFicheClientTask();
                ficheClients.execute("");
            }
        });


        btdatedebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                                txtdatedebut.setText(datedebut);

                            }
                        });

                AlertDialog dd = alt.create();
                dd.show();


            }
        });

        btdatefin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                                datefin = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH).format(d);
                                txtdatefin.setText(datefin);

                            }
                        });

                AlertDialog dd = alt.create();
                dd.show();

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fiche, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_download_dpf) {

            if (listFicheClient.size() != 0) {

                Toast.makeText(this, " PDF Button", Toast.LENGTH_LONG).show();
                try {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                    } else {

                        String filePath = getOutputMediaFile().getPath();
                        Log.e("filepath", "  " + filePath);
                        Toast.makeText(this, " filepath " + filePath, Toast.LENGTH_LONG).show();

                        FicheClientPdf ficheClientPdf = new FicheClientPdf();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                        Date datedu = sdf.parse(txtdatedebut.getText().toString());
                        Date dateau = sdf.parse(txtdatefin.getText().toString());
                        ficheClientPdf.executePDF(filePath, codeClient, raisonSocial, datedu, dateau, listFicheClient);

                        OpenPDFFile(filePath);

                    }

                } catch (Exception e) {
                    Log.e("Exeption", "" + e.getMessage());
                }

            } else {
                Toast.makeText(this, " Patientez SVP  ...  \n Chargement en cours du Fiche Client ... ", Toast.LENGTH_LONG).show();
            }
            return true;

        }

        return super.onOptionsItemSelected(item);

    }


    public class ListFicheClientTask extends AsyncTask<String, String, String> {
        String z = "";
        Boolean test = false;

        SimpleDateFormat dfSQL = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        //ArrayList<FicheClient> listFicheClient = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            totaldebit = 0;
            totalcredit = 0;

            TotalSolde = 0.0;

        }


        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String query = "\n" +
                            "DECLARE\t@return_value int\n" +
                            "\n" +
                            "EXEC\t@return_value = [dbo].[FicheClient]\n" +
                            "\t\t@CodeClient = N'" + codeClient + "',\n" +
                            "\t\t@DateDebut = '" + datedebut + "',\n" +
                            "\t\t@DateFin = '" + datefin + "'\n" +
                            "\n" +
                            "SELECT\t'Return Value' = @return_value";

                    Log.e("query", "" + query);
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    z = "e";
                    listFicheClient.clear();
                    TotalSolde = 0;
                    while (rs.next()) {

                        String CodeClient = rs.getString("Client");
                        String Libelle = rs.getString("Libelle");
                        String NumeroPiece = rs.getString("NumeroPiece");
                        Date datePiece = new Date();
                        Date dateCreation = new Date();
                        try {

                            datePiece = dfSQL.parse(rs.getString("DatePiece"));
                            dateCreation = dfSQL.parse(rs.getString("HeureCreation"));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        double debit = rs.getDouble("Debit");
                        double credit = rs.getDouble("Credit");
                        double solde = debit - credit;
                        TotalSolde = TotalSolde + solde;


                        FicheClient ficheClient = new FicheClient(CodeClient, datePiece, Libelle, NumeroPiece, debit, credit, solde, TotalSolde, dateCreation);
                        listFicheClient.add(ficheClient);


                        test = true;

                        z = "Chargement avec Success";
                    }

                }
            } catch (SQLException ex) {
                z = "etape" + ex.toString();

            }
            return z;
        }


        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();

            FicheClientAdapter ficheClientAdapter = new FicheClientAdapter(FicheClientActivity.this, listFicheClient);
            lv_fiche_client.setAdapter(ficheClientAdapter);

            lv_fiche_client.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (listFicheClient.get(i).getNumeroPiece().contains("BL")) {

                        final FragmentManager fm = getSupportFragmentManager();
                        BonLivraisonDetail dialog = new BonLivraisonDetail();
                        dialog.setNumeroBonLivraisonVente(listFicheClient.get(i).getNumeroPiece());
                        dialog.show(fm, "");

                    }
                }
            });

            DecimalFormat numberFormat = new DecimalFormat("#.###");
            txttotal.setText("Solde = " + numberFormat.format(TotalSolde));


        }
    }


    /////////////********************************************************/////////////
    /////////////********************************************************/////////////


    public class DefaultDates extends AsyncTask<String, String, String> {
        String z = "";
        String datefi = "";
        String datedeb = "";

        Date da;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {


            String anf = datefi.substring(0, 4);
            String moisf = datefi.substring(5, 7);
            String jourf = datefi.substring(8, 10);
            datefin = jourf + "/" + moisf + "/" + anf;

            String and = datedeb.substring(0, 4);
            String moisd = datedeb.substring(5, 7);
            String jourd = datedeb.substring(8, 10);
            datedebut = jourd + "/" + moisd + "/" + and;

            txtdatedebut.setText(datedebut);
            txtdatefin.setText(datefin);
            ListFicheClientTask f = new ListFicheClientTask();
            f.execute("");

            // Toast.makeText(getApplicationContext(),datefin+ r, Toast.LENGTH_SHORT).show();
        }


        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    Connection connect = connectionClass.CONN(ip, password, user, base);
                    PreparedStatement stmt;
                    String query = "select *  from ParametreDiver";
                    stmt = connect.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        datefi = rs.getString("DateFinExercice");
                        datedeb = rs.getString("DateDebutExercice");


                        z = "succee";
                    }
                    z = "e";


                }
            } catch (SQLException ex) {
                z = ex.toString();

            }
            return z;
        }
    }


    private static File getOutputMediaFile() {
        Log.e("**", "getOutputMediaFile");
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, " Oops! Failed create  "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name


        Log.e("path_file", mediaStorageDir.getPath() + File.separator + "FicheClient" + ".pdf");

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "FicheClient" + ".pdf");


        return mediaFile;
    }


    public void OpenPDFFile(String filePath) {

        // openFile(filePath);
        // /sdcard/Pictures/PDF_app/FicheClient.pdf
        // filePath = "/sdcard/Pictures/PDF_app/FicheClient.pdf" ;


        File pdfFile = new File(filePath);//File path

        if (pdfFile.exists()) {
            try {

                Uri path = Uri.fromFile(pdfFile);
                Intent objIntent = new Intent(Intent.ACTION_VIEW);
                objIntent.setDataAndType(path, "application/pdf");
                objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                objIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(objIntent);

            } catch (Exception e) {

                Toast.makeText(this, " ERROR " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                Log.e("txt_ERROR", e.getMessage().toString());

            }

        } else {
            Toast.makeText(this, "File inexistant !!! ", Toast.LENGTH_LONG).show();
        }
    }


    public void openFile(String filePath) {

        try {
            File pdfFile = new File(filePath);
            Uri photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + AUTHORITY, pdfFile);

            // pdfFile

            // FileProvider.getUriForFile(this, AUTHORITY, pdfFile)
            // photoURI
            Intent i = new Intent(Intent.ACTION_VIEW, FileProvider.getUriForFile(this, AUTHORITY, pdfFile));

            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(i);

        } catch (Exception ex) {
            Log.e("ERROR_openfile", ex.toString());
        }

    }



    static private void copy  ( InputStream in, File dst ) throws IOException {
        FileOutputStream out = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        int len;

        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        in.close();
        out.close();
    }


}
