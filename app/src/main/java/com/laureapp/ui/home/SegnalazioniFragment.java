package com.laureapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.List;



import com.laureapp.R;

public class SegnalazioniFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_segnalazioni_studente, container, false);

        // Ottieni il riferimento alla ListView
        ListView listView = rootView.findViewById(R.id.list_view);


        // Crea una lista di dati di esempio
        List<String> segnalazioniList = new ArrayList<>();
        segnalazioniList.add("Segnalazione 1");
        segnalazioniList.add("Segnalazione 2");
        segnalazioniList.add("Segnalazione 3");


        // Crea l'adapter personalizzato e imposta sulla ListView
        SegnalazioniAdapter adapter = new SegnalazioniAdapter(requireContext(), segnalazioniList);
        // Collega l'adapter alla ListView
        listView.setAdapter(adapter);

        // Imposta un listener per gli elementi della ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Ottieni il NavController dalla tua Activity principale
                NavController navController = NavHostFragment.findNavController(SegnalazioniFragment.this);

                // Esegui la navigazione verso DiscussioneFragment
                navController.navigate(R.id.action_segnalazioniFragment_to_discussioneFragment);
            }
        });


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){

    }


}