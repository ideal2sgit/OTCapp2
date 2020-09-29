package com.example.faten.testsql.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;

import   com.example.faten.testsql.R ;
import com.example.faten.testsql.task.ListClientTask;

public class RechercheClientCommande extends Fragment {



    SearchView searchViewClient  ;
    RecyclerView listClient ;
    ProgressBar pb;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView =  inflater.inflate(R.layout.fragment_recherche_client_cmd , container, false );

        listClient              = (RecyclerView) fragmentView.findViewById(R.id.list_select_client);
        searchViewClient        = (SearchView) fragmentView.findViewById(R.id.search_bar_client)  ;
        pb                      = (ProgressBar) fragmentView.findViewById(R.id.pb);



        pb.setVisibility(View.INVISIBLE);
        listClient.setHasFixedSize(true);
        listClient.setLayoutManager( new LinearLayoutManager(getActivity()));


        ListClientTask  listClientTask = new  ListClientTask(getActivity() ,listClient ,pb,searchViewClient  ) ;
        listClientTask.execute() ;




        return fragmentView ;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
         getActivity().setTitle ( "Selectionner un client" );
    }
}
