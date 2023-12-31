package com.laureapp.ui.home;

import static com.laureapp.ui.controlli.ControlInput.showToast;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Utente;

import org.apache.commons.lang3.StringUtils;

/**
 * Fragment principale dell'applicazione che mostra le opzioni disponibili in base al ruolo dell'utente.
 */
public class HomeFragment extends Fragment {

    CardView CardTesi;
    CardView CardTask;
    CardView CardTesisti;
    CardView CardMessaggi;

    CardView CardSegnalazioni;
    String ruolo;
    Context context;
    Bundle args;

    private NavController mNav;

    /**
     * Questo metodo viene chiamato quando il fragment è stato creato.
     * Inizializza le variabili di istanza del fragment e salva l'argomento "ruolo" e "email" ottenuti dai dati del bundle.
     *
     * @param savedInstanceState i dati precedenti sull'istanza del fragment (opzionale).
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireContext();
        args = getArguments();
        if (args != null) {
            ruolo = args.getString("ruolo");
            Log.d("ruolo ", ruolo);
            //questo è null quando fai login
            saveEmailToSharedPreferences(args.getString("email"));

            //args.putSerializable("UtenteLoggato", (Utente) args.getSerializable("Utente"));


        }

    }

    /**
     * Questo metodo viene chiamato quando la configurazione del dispositivo cambia, ad esempio da modalità paesaggio a ritratto o viceversa.
     * Puoi utilizzarlo per aggiornare l'interfaccia utente in base alla nuova configurazione, se necessario.
     *
     * @param newConfig l'oggetto Configuration che rappresenta la nuova configurazione del dispositivo.
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Il dispositivo è in modalità landscape
            // Puoi aggiornare l'interfaccia utente qui se necessario
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Il dispositivo è in modalità ritratto
            // Puoi aggiornare l'interfaccia utente qui se necessario
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mNav = Navigation.findNavController(view);
        CardTesi =  view.findViewById(R.id.cardViewTesi);
        CardTask =  view.findViewById(R.id.cardViewTask);
        CardTesisti = view.findViewById(R.id.cardViewTesisti);

        TextView taskTextView = view.findViewById(R.id.taskTextView);

        if (StringUtils.equals("Professore", ruolo)) {
            taskTextView.setText(R.string.richieste);
        }

        CardTesi.setOnClickListener(view1 -> {
            if(StringUtils.equals("Studente", ruolo)){
                mNav.navigate(R.id.action_fragment_home_to_tesiStudenteFragment,args);
            }else if(StringUtils.equals("Professore", ruolo)){
                mNav.navigate(R.id.action_fragment_home_to_tesiProfessoreFragment,args);
                Log.d("Tesi", "cliccato tesi Professore");
            }else {
                mNav.navigate(R.id.action_fragment_home_to_tesiStudenteFragment);
            }
        });

        CardTask.setOnClickListener(view1 -> {
            if(StringUtils.equals("Studente", ruolo)){
                mNav.navigate(R.id.action_fragment_home_to_taskStudenteFragment,args);
                Log.d("Task", "cliccato Task studente");
            } else if(StringUtils.equals("Professore", ruolo)){
                mNav.navigate(R.id.action_fragment_home_to_richiesteProfessoreFragment,args);
            }else if(StringUtils.equals("Ospite", ruolo)) {
                Log.d("Task", "cliccato Task Ospite" +ruolo);

                mNav.navigate(R.id.action_fragment_home_to_taskStudenteFragment,args);
                           }

        });


        CardTesisti =  view.findViewById(R.id.cardViewTesisti);
        CardTesisti.setOnClickListener(view1 -> {
            if(StringUtils.equals("Professore", ruolo)){
                Log.d("Tesisti", "cliccato Tesisti Professore");
                mNav.navigate(R.id.action_fragment_home_to_tesisti, args);
            }else {
                Log.d("Tesisti", "cliccato Tesisti Ospite");
                mNav.navigate(R.id.action_fragment_home_to_tesisti);
            }
        });

        CardSegnalazioni =  view.findViewById(R.id.cardViewMessaggi);
        CardSegnalazioni.setOnClickListener(view1 -> {
            if(StringUtils.equals("Professore", ruolo)){
                Log.d("Segn", "cliccato Segnalazione Professore"  + ruolo);
                mNav.navigate(R.id.action_fragment_home_to_tesisti_segnalazione_fragment, args);
            }else if(StringUtils.equals("Studente", ruolo)){
                Log.d("Segn", "cliccato Segnalazione Studente"  + ruolo);
                mNav.navigate(R.id.action_fragment_home_to_segnalazione_fragment, args);
            }else if(StringUtils.equals("Ospite", ruolo)) {
                Log.d("Segn", "cliccato Segnalazione Ospite" + ruolo);
                mNav.navigate(R.id.action_fragment_home_to_segnalazione_fragment, args);
            }
        });

        /**
         * Qui nascondo le card in base all'utente
         */
        if(!StringUtils.equals("Professore", ruolo)){ //se sono studente o ospite
           CardTesisti.setVisibility(View.GONE); //nascondo tesisti
        }


    }

    /**
     * Salvo la mail ottenuta dal login e dalla registrazine passata mediante i Bundle
     * Il metodo crea un "file" di preferenze denominato preferenze ed inserisce mediante
     * l'editor la mail.
     * Si potrebbero passare tutti i dati ma SharedPreferences è pensato per passare piccole quantità di dati
     *
     * @param email
     */
    private void saveEmailToSharedPreferences(String email) {
        SharedPreferences preferences = context.getSharedPreferences("preferenze", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.apply();
    }

    /**
     * Ottiene l'indirizzo email memorizzato nelle preferenze condivise (SharedPreferences).
     *
     * @param context il contesto in cui è richiesta l'operazione. È necessario passare un oggetto di tipo Context.
     * @return l'indirizzo email memorizzato nelle preferenze condivise o null se non è presente.
     */
    public static String getEmailFromSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("preferenze", Context.MODE_PRIVATE);
        return preferences.getString("email", null);
    }


}