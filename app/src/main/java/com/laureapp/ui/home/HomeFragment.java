package com.laureapp.ui.home;

import static com.laureapp.ui.controlli.ControlInput.showToast;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.laureapp.ui.roomdb.entity.Utente;

import org.apache.commons.lang3.StringUtils;


public class HomeFragment extends Fragment {

    CardView CardTesi;
    CardView CardTask;
    CardView CardTesisti;
    CardView CardSocial;
    CardView CardMessaggi;
    String ruolo;
    Context context;
    Bundle args;

    private NavController mNav;

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
            args.putSerializable("Utente", args.getSerializable("Utente", Utente.class));

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
        CardSocial =  view.findViewById(R.id.cardViewSocial);
        CardTesisti = view.findViewById(R.id.cardViewTesisti);



        CardTesi.setOnClickListener(view1 -> {
            if(StringUtils.equals("Studente", ruolo)){
                mNav.navigate(R.id.action_fragment_home_to_tesiStudenteFragment,args);
            }else if(StringUtils.equals("Professore", ruolo)){
                mNav.navigate(R.id.action_fragment_home_to_tesiProfessoreFragment);
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
                Log.d("Tesisti", "cliccato Tesisti Professore");
                mNav.navigate(R.id.action_fragment_home_to_tesisti,args);
            }else {
                Log.d("Tesisti", "cliccato Tesisti Ospite");
                mNav.navigate(R.id.action_fragment_home_to_tesisti);
            }
        });

        CardSocial.setOnClickListener(view1 -> {
            mNav.navigate(R.id.action_fragment_home_to_social_fragment);
        });

        CardMessaggi =  view.findViewById(R.id.cardViewMessaggi);
        CardMessaggi.setOnClickListener(view1 -> {
            if(StringUtils.equals("Professore", ruolo)){
                Log.d("Task", "cliccato Task Professore");
                mNav.navigate(R.id.action_fragment_home_to_messaggiFragment);
            }else {
                Log.d("Task", "cliccato Task Ospite");
                mNav.navigate(R.id.action_fragment_home_to_messaggiFragment);
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

    public static String getEmailFromSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("preferenze", Context.MODE_PRIVATE);
        return preferences.getString("email", null);
    }


}