package com.example.faten.testsql.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.faten.testsql.Choixclient;
import com.example.faten.testsql.Clients;
import com.example.faten.testsql.activity.HistoriqueBonCommande;
import com.example.faten.testsql.R;
import com.example.faten.testsql.SuivieMensuelActivity;
import com.example.faten.testsql.activity.InventaireActivity;
import com.example.faten.testsql.activity.PassationDeReglementActivity;
import com.example.faten.testsql.activity.SuivieRecouvrementActivity;


//import android.app.DialogFragment;
//import android.app.FragmentManager;


public class PrincipalFragment extends Fragment {

    //private BoomMenuButton bmb;

    CardView  btn_client,btn_article ,
           // btn_bon_commande,
            btn_bon_commande_avec_terminal,
            btn_suivie_recouvrement,
            btn_passation_de_reglement  ,
            btn_suivie_mensuel  ,
            btn_historique_bon_commande,
            btn_inventaire ,
            btn_mes_demande_et_demande_des_autres;





    String CodeLivreur, CodeDepot, NomPrenom ,NomUtilisateur;

    TextView txt_nom_prenom_livreur;


    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_principal, container, false);

        txt_nom_prenom_livreur = (TextView) fragmentView.findViewById(R.id.txt_nom_prenom_livreur);



        btn_client = (CardView) fragmentView.findViewById(R.id.btn_client);
        btn_article  = (CardView)  fragmentView.findViewById(R.id.btn_article);
       // btn_bon_commande  = (CardView) fragmentView.findViewById(R.id.btn_bon_commande) ;
        btn_bon_commande_avec_terminal = (CardView)  fragmentView.findViewById(R.id.btn_cmd_avec_terminal) ;
        btn_suivie_recouvrement = (CardView) fragmentView.findViewById(R.id.btn_suivie_recouvrement);
        btn_suivie_mensuel = (CardView) fragmentView.findViewById(R.id.btn_suivie_recouvrement);

        btn_passation_de_reglement  = (CardView) fragmentView.findViewById(R.id.btn_passation_de_reglement);


        btn_historique_bon_commande= (CardView) fragmentView.findViewById(R.id.btn_historique_bc);
        btn_inventaire= (CardView) fragmentView.findViewById(R.id.btn_inventaire);
        btn_mes_demande_et_demande_des_autres= (CardView) fragmentView.findViewById(R.id.btn_mes_demande_et_demande_des_autre);



        SharedPreferences pref = getActivity().getSharedPreferences("usersession", Context.MODE_PRIVATE);
        CodeLivreur = pref.getString("CodeRepresentant", CodeLivreur);
        CodeDepot = pref.getString("CodeDepot", CodeDepot);
        NomPrenom = pref.getString("NomPrenom", NomPrenom);
        NomUtilisateur = pref.getString("NomUtilisateur", NomUtilisateur);

        txt_nom_prenom_livreur.setText(NomPrenom);





        btn_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), Clients.class);
                startActivity(in);

            }
        });

        btn_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


      /*  btn_bon_commande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getActivity(), Choixclient.class);
                in.putExtra("NomUtilisateur", NomUtilisateur);
                startActivity(in);
            }
        });
        */



        btn_bon_commande_avec_terminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = null;
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                fragment = new RechercheClientCommande();

                //replacing the fragment
                if (fragment != null) {

                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();

                }

            }
        });

        btn_suivie_recouvrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), SuivieRecouvrementActivity.class);
                startActivity(in);

            }
        });


        btn_suivie_mensuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), SuivieMensuelActivity.class);
                startActivity(in);
            }
        });



        btn_historique_bon_commande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), HistoriqueBonCommande.class);
                in.putExtra("NomUtilisateur", NomUtilisateur);
                in.putExtra("CodeLivreur", CodeLivreur);
                startActivity(in);
            }
        });



        btn_inventaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), InventaireActivity.class);
                startActivity(in);


            }
        });


        btn_mes_demande_et_demande_des_autres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Fragment fragment = null;
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                fragment = new EtatMesDemandeEtDemandeDesAutre();

                //replacing the fragment
                if (fragment != null) {

                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();

                }

            }
        });



        btn_passation_de_reglement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent  toPassationReglement  = new Intent(getActivity()  , PassationDeReglementActivity.class) ;
                startActivity(toPassationReglement);

            }
        });
        return fragmentView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Principal");
    }


}
