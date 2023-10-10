package com.laureapp.ui.card.Segnalazioni;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.List;

import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Segnalazione;
import com.laureapp.ui.roomdb.repository.SegnalazioneRepository;

public class SegnalazioniFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_segnalazioni_studente, container, false);

        //Bottone nuova segnalazione
        Button btnNS = rootView.findViewById(R.id.add_segn_ImageButton);
        btnNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_segnStudentiFragment_to_newSegnalazioni);

            }
        });

        // Ottieni l'ID della tesi desiderata (sostituiscilo con il valore effettivo)
        int idTesiDesiderata = 0; // Sostituiscilo con l'ID reale

        // Ottieni il riferimento alla ListView
        ListView listView = rootView.findViewById(R.id.segn_list_view);

        // Crea un'istanza del repository delle segnalazioni
        SegnalazioneRepository segnalazioneRepository = new SegnalazioneRepository(requireContext());

        // Ottieni le segnalazioni per la tesi desiderata dal repository
        List<Segnalazione> segnalazioniList = segnalazioneRepository.findSegnalazioniByTesiId(idTesiDesiderata);


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