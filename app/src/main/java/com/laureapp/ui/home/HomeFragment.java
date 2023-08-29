package com.laureapp.ui.home;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

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

/**
 * Fragment per la schermata principale dell'applicazione.
 */

public class HomeFragment extends Fragment {

    CardView CardTesi;
    CardView CardTask;
    CardView CardSocial;
    CardView CardMessaggi ;
    CardView CardTesisti;
    String ruolo;
    Context context;
    Bundle bundle;
    private NavController mNav;

    /**
     * Chiamato quando l'attività sta per essere creata. Questo è il momento in cui
     * il frammento sta per essere inizializzato.
     *
     * @param savedInstanceState L'eventuale stato precedentemente salvato del frammento,
     *                           contenente dati che potrebbero essere necessari per il suo ripristino.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Chiamato per creare e restituire la vista del frammento.
     *
     * @param inflater L'oggetto inflater da utilizzare per creare la vista.
     * @param container Il contenitore in cui verrà inserita la vista.
     * @param savedInstanceState Se il frammento viene ricreato da uno stato precedente, questo è lo stato.
     * @return La vista creata per il frammento.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        int currentOrientation = getResources().getConfiguration().orientation; // Ottengo l'orientamento corrente
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) { // Se è in modalità orizzontale, mostro il fragment orizzontale
            rootView = inflater.inflate(R.layout.fragment_home_landscape, container, false);
        } else { // Altrimenti mostro il fragment verticale
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
        }

        context = requireContext();
        bundle = getArguments();
        if (bundle != null) {
            ruolo = bundle.getString("ruolo"); // Ottengo il ruolo dal bundle se disponibile
        }

        return rootView;
    }

    /**
     * Chiamato quando la vista del frammento è stata creata.
     *
     * @param view La vista creata.
     * @param savedInstanceState Se il frammento viene ricreato da uno stato precedente, questo è lo stato.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mNav = Navigation.findNavController(view);

        // Card per accedere alla sezione "Tesi"
        CardTesi =  view.findViewById(R.id.cardViewTesi);
        CardTesi.setOnClickListener(view1 -> {
            if(StringUtils.equals("Studente", ruolo)){
                Log.d("Tesi", "cliccato tesi studente");
            }else if(StringUtils.equals("Professore", ruolo)){
                Log.d("Tesi", "cliccato tesi Professore");
            }else {
                Log.d("Tesi", "cliccato tesi Ospite");
            }
        });

        // Card per accedere alla sezione "Task"
        CardTask =  view.findViewById(R.id.cardViewTask);
        CardTask.setOnClickListener(view2 -> {
            if(StringUtils.equals("Studente", ruolo)){
                Log.d("Task", "cliccato Task studente");
            }else if(StringUtils.equals("Professore", ruolo)){
                Log.d("Task", "cliccato Task Professore");
            }else {
                Log.d("Task", "cliccato Task Ospite");
            }
        });

        // Card per accedere alla sezione "Social"
        CardSocial = view.findViewById(R.id.cardViewSocial);
        CardSocial.setOnClickListener(view3 ->
                mNav.navigate(R.id.action_fragment_home_to_social_fragment)
        );

        // Card per accedere alla sezione "Messaggi"
        CardMessaggi =  view.findViewById(R.id.cardViewMessaggi);
        CardMessaggi.setOnClickListener(view4 -> {
            if(StringUtils.equals("Studente", ruolo)){
                Log.d("Messaggi", "cliccato Messaggi studente");
            }else if(StringUtils.equals("Professore", ruolo)){
                Log.d("Messaggi", "cliccato Messaggi Professore");
            }else {
                Log.d("Messaggi", "cliccato Messaggi Ospite");
            }
        });

        // Card per accedere alla sezione "Tesisti"
        CardTesisti =  view.findViewById(R.id.cardViewTesisti);
        CardTesisti.setOnClickListener(view5 -> {
            if(StringUtils.equals("Studente", ruolo)){
                Log.d("Tesisti", "cliccato Tesisti studente");
            }else if(StringUtils.equals("Professore", ruolo)){
                Log.d("Tesisti", "cliccato Tesisti Professore");
            }else {
                Log.d("Tesisti", "cliccato Tesisti Ospite");
            }
        });
    }

}