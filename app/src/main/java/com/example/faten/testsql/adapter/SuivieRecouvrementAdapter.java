package com.example.faten.testsql.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.faten.testsql.R;
import com.example.faten.testsql.model.SuivieRecouvrement;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by fatima on 20/01/2017.
 */

public class SuivieRecouvrementAdapter extends ArrayAdapter<SuivieRecouvrement> {


    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    NumberFormat formatter = new DecimalFormat("0.000");

    private final Activity activity;
    private final ArrayList<SuivieRecouvrement> listSuivieRecouvrement;

    public SuivieRecouvrementAdapter(Activity activity, ArrayList<SuivieRecouvrement> listSuivieRecouvrement) {
        super(activity, R.layout.item_suivie_recouvrement, listSuivieRecouvrement);
        // TODO Auto-generated constructor stub

        this.activity = activity;
        this.listSuivieRecouvrement = listSuivieRecouvrement;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        Context context = parent.getContext();
        View rowView = inflater.inflate(R.layout.item_suivie_recouvrement, null, true);

        SuivieRecouvrement suivieRecouvrement = listSuivieRecouvrement.get(position)  ;

        TextView txt_raison_social = (TextView) rowView.findViewById(R.id.txt_raison_social_client);
        TextView txt_mode_reglement = (TextView) rowView.findViewById(R.id.txt_mode_reglement);
        TextView txt_montant = (TextView) rowView.findViewById(R.id.txt_montant);
        TextView txt_date_reglement = (TextView) rowView.findViewById(R.id.txt_date_reglement);


        txt_raison_social.setText(suivieRecouvrement.getRaisonSocial());


        String modeReg ="" ;
        if (suivieRecouvrement.getCodeModeReglement().equals("C"))
            modeReg ="Chèque " ;
        if (suivieRecouvrement.getCodeModeReglement().equals("T"))
            modeReg ="Traite " ;

        txt_mode_reglement.setText(modeReg+suivieRecouvrement.getRefrence());
        txt_montant.setText(formatter.format(suivieRecouvrement.getMontantRecu())+" DT");

        txt_date_reglement.setText(df.format(suivieRecouvrement.getDateReglement()));



        return rowView;

    }


}
