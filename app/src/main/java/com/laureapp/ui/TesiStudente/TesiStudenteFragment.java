package com.laureapp.ui.TesiStudente;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.laureapp.R;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

public class TesiStudenteFragment extends Fragment {
    Context context;
    String email;
    Bundle args;
    UtenteModelView utenteView =  new UtenteModelView(context);
    Long id_utente;

    /**
     * Metodo che mi serve per ottenere l'id dell'utente ottenuto
     * dalla mail passata come argomento in LoginFragment nel passaggio dalla pagina di login alla home
     * @param savedInstanceState
     *
     * @return l'email dello studente
     */
    private Long getIdUtente(Bundle savedInstanceState){

        args = getArguments(); //prendo gli argomenti passati dalla home (nel caso del login: login -> home -> tesi)
        if (args != null) {

             email = utenteView.getEmail(); //prendo il valore relativo alla email
             Log.d("ide", String.valueOf(args));
             Log.d("idde", String.valueOf(email));

            id_utente = utenteView.getIdUtente(email); //ne ricavo l'id dell'utente
        }

        return id_utente;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tesi_studente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();
        TabLayout tabLayout = view.findViewById(R.id.TabLayoutTesi);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position){
                    case 0:

                        id_utente = getIdUtente(args);
                        Log.d("id", String.valueOf(id_utente));
                        break;

                    case 1:
                        Log.d("Tag", "Selected Tab Position 1 ");
                        break;

                    case 2:
                        Log.d("Tag", "Selected Tab Position 2 ");
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // This method will be called when a tab is unselected.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // This method will be called when a tab is reselected (if applicable).
            }
        });


    }




}