package com.example.faten.testsql.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.faten.testsql.R;
import com.example.faten.testsql.activity.BonCommandeVenteAvecTerminalActivity;
import com.example.faten.testsql.dialog.DialogChoixAutomatiqueORParChoix;
import com.example.faten.testsql.dialog.DialogChoixEditORDeleteChoix;
import com.example.faten.testsql.model.Client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by fatima on 20/01/2017.
 */


public class ClientSelectAdapterRV extends RecyclerView.Adapter<ClientSelectAdapterRV.ViewHolder> {


    private final Activity activity;
    private final ArrayList<Client> listClient;//= new ArrayList<>();


    public static String login;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    DateFormat HeureF = new SimpleDateFormat("HH:mm");


    public ClientSelectAdapterRV(Activity activity, ArrayList<Client> listClient ) {

        this.activity = activity;
        this.listClient = listClient;


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client_select, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Client client = listClient.get(position);
        holder.raison_social_client.setText(client.getRaisonSociale());

        if (client.getNbrClick() == 0) {
            holder.cb_client.setChecked(false);
        } else if (client.getNbrClick() == 1) {
            holder.cb_client.setChecked(true);
        }





        holder.card_item_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.cb_client.setChecked(true);
                client.setNbrClick(1);

                for (Client c : listClient) {
                    if (c.getCodeClient() != client.getCodeClient()) {
                        holder.cb_client.setChecked(false);
                        c.setNbrClick(0);

                    }
                }


                final FragmentManager fm = activity.getFragmentManager();
                DialogChoixAutomatiqueORParChoix dialog = new DialogChoixAutomatiqueORParChoix();
                dialog.setActivity(activity);
                dialog.setCodeClient(client.getCodeClient());
                dialog.setRaisonSociale(client.getRaisonSociale());
                dialog.setActivity(activity);

                dialog.show(fm, "");


                notifyDataSetChanged();

            }
        });


    }


    @Override
    public int getItemCount() {
        Log.e("size", "" + listClient.size());
        return listClient.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView card_item_client;
        public TextView raison_social_client;
        public CheckBox cb_client;



        public ViewHolder(View itemView) {
            super(itemView);

            card_item_client = (CardView) itemView.findViewById(R.id.card_item_client);
            raison_social_client = (TextView) itemView.findViewById(R.id.txt_raison_social_client);
            cb_client = (CheckBox) itemView.findViewById(R.id.cb_select_client);




        }

    }

}
