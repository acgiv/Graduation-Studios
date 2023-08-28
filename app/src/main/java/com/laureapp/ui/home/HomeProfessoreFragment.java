package com.laureapp.ui.home;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laureapp.R;

public class HomeProfessoreFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return the View for the fragment's UI
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView;

        int currentOrientation = getResources().getConfiguration().orientation; //prendo l'orientamento corrente
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) { //se è in modalità orizzontale mostra il fragment orizzontale
            rootView = inflater.inflate(R.layout.fragment_home_studente_landscape, container, false);
        } else { //altrimenti mostra il fragment verticale
            rootView = inflater.inflate(R.layout.fragment_home_studente, container, false);
        }
        CardView cardViewTesi = rootView.findViewById(R.id.cardViewTesi);
        CardView cardViewMessaggi = rootView.findViewById(R.id.cardViewMessaggi);
        CardView cardViewClassifica = rootView.findViewById(R.id.cardViewClassifica);
        CardView cardViewSocial = rootView.findViewById(R.id.cardViewSocial);

        cardViewTesi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Azioni da eseguire quando la card Tesi viene cliccata
            }
        });

        cardViewMessaggi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Azioni da eseguire quando la card Messaggi viene cliccata
            }
        });

        cardViewClassifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Azioni da eseguire quando la card Classifica viene cliccata
            }
        });

        cardViewSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Azioni da eseguire quando la card Social viene cliccata
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}