package com.laureapp.ui.card.Segnalazioni;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;

import com.laureapp.R;

import com.laureapp.ui.roomdb.entity.Segnalazione;
import com.laureapp.ui.roomdb.repository.SegnalazioneRepository;


public class NewSegnalazioniFragment extends Fragment {


    private TextInputEditText titoloEditText;
    private TextInputEditText richiestaEditText;
    private Button addButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_segn_popup, container, false);

        addButton = rootView.findViewById(R.id.button_add_segn);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Qui preleva i dati dai campi di input e fai qualcosa con essi
                String titolo = ((TextInputEditText) view.findViewById(R.id.titolo_register)).getText().toString();
                String richiesta = ((TextInputEditText) view.findViewById(R.id.richiesta_register)).getText().toString();


                // Crea un oggetto Segnalazione
                Segnalazione segnalazione = new Segnalazione();
                segnalazione.setTitolo(titolo);
                segnalazione.setRichiesta(richiesta);

                // Utilizza il repository per inserire la segnalazione nel database
                SegnalazioneRepository segnalazioneRepository = new SegnalazioneRepository(requireContext());
                segnalazioneRepository.insertSegnalazione(segnalazione);

                // Torna indietro al fragment precedente
                Navigation.findNavController(view).navigateUp();
            }
        });

        return rootView;
    }
}
