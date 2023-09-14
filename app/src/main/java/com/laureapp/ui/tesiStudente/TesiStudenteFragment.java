package com.laureapp.ui.tesiStudente;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.laureapp.R;
import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.repository.StudenteRepository;
import com.laureapp.ui.roomdb.repository.TesiRepository;
import com.laureapp.ui.roomdb.repository.UtenteRepository;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
             email = (String) args.getSerializable("email"); //prendo il valore relativo alla email
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