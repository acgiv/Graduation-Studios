package com.laureapp.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.laureapp.R;

import org.apache.commons.lang3.StringUtils;


public class HomeFragment extends Fragment {

    CardView CardTesi;
    CardView CardTask;
    CardView CardTesisti;
    CardView CardSocial;
    //CardView CardTesisti;
    CardView CardMessages;
    String ruolo;
    Context context;
    Bundle args;
    private NavController mNav;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = requireContext();
        args = getArguments();
        if (args != null) {
            ruolo = args.getString("ruolo");
            Log.d("ruolo ", ruolo);
        }


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mNav = Navigation.findNavController(view);
        CardTesi =  view.findViewById(R.id.cardViewTesi);
        CardTask =  view.findViewById(R.id.cardViewTask);
        CardSocial =  view.findViewById(R.id.cardViewSocial);
        CardTesisti = view.findViewById(R.id.cardViewTesisti);



        CardTesi.setOnClickListener(view1 -> {
            if(StringUtils.equals("Studente", ruolo)){
                mNav.navigate(R.id.action_fragment_home_to_tesiStudenteFragment);
            }else if(StringUtils.equals("Professore", ruolo)){
                Log.d("Tesi", "cliccato tesi Professore");
            }else {
                mNav.navigate(R.id.action_fragment_home_to_tesiStudenteFragment);
            }
        });

        CardTask.setOnClickListener(view1 -> {
            if(StringUtils.equals("Studente", ruolo)){
                Log.d("Task", "cliccato Task studente");
            }else if(StringUtils.equals("Professore", ruolo)){
                Log.d("Task", "cliccato Task Professore");
            }else {
                Log.d("Task", "cliccato Task Ospite");
            }
        });


        CardTesisti =  view.findViewById(R.id.cardViewTesisti);
        CardTesisti.setOnClickListener(view1 -> {
            if(StringUtils.equals("Professore", ruolo)){
                Log.d("Task", "cliccato Task Professore");
                mNav.navigate(R.id.action_fragment_home_to_tesisti);
            }else {
                Log.d("Task", "cliccato Task Ospite");
                mNav.navigate(R.id.action_fragment_home_to_tesisti);
            }
        });

        CardTask.setOnClickListener(view1 -> {
            if(StringUtils.equals("Studente", ruolo)){

            }else if(StringUtils.equals("Professore", ruolo)){
                Log.d("Task", "cliccato Task Professore");
            }else {
                Log.d("Task", "cliccato Task Ospite");
            }
        });

        CardSocial.setOnClickListener(view1 -> {
            mNav.navigate(R.id.action_fragment_home_to_social_fragment);
        });

        CardMessages.setOnClickListener(view1 -> {
            mNav.navigate(R.id.action_fragment_home_to_messaggiFragment);
        });

        /**
         * Qui nascondo le card in base all'utente
         */
        if(!StringUtils.equals("Professore", ruolo)){ //se sono studente o ospite
           CardTesisti.setVisibility(View.GONE); //nascondo tesisti
        }


    }


}