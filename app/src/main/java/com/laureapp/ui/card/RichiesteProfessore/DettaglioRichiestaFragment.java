package com.laureapp.ui.card.RichiesteProfessore;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DettaglioRichiestaFragment extends Fragment {
    String titolo;
    Long idRichiestaTesi;

    Long idStudente;
    Context context;
    List<Studente> studenti;
    List<Utente> utenti;
    StudenteModelView studenteModelView = new StudenteModelView(context);
    UtenteModelView utenteView = new UtenteModelView(context);

    boolean soddisfaRequisiti;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dettaglio_richieste_tesi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        TextView richiestaTextView = view.findViewById(R.id.insertTextViewIdRichiesta);
        TextView titoloTesiTextView = view.findViewById(R.id.insertTextViewTitoloTesi);
        TextView matricolaTextView = view.findViewById(R.id.insertTextViewMatricolaStudente);
        TextView nomeTextView = view.findViewById(R.id.insertTextViewNomeStudente);
        TextView cognomeTextView = view.findViewById(R.id.insertTextViewCognomeStudente);
        TextView emailTextView = view.findViewById(R.id.insertTextViewEmailStudente);
        TextView soddisfaTextView = view.findViewById(R.id.insertTextViewAvvisoVincoli);
        if (args != null) { //se non sono null

            titolo = (String) args.getSerializable("Titolo"); //prendo la tesi dagli args
            idRichiestaTesi = (Long) args.getSerializable("idRichiestaTesi");
            idStudente = (Long) args.getSerializable("idStudente");
            soddisfaRequisiti = (Boolean) args.getSerializable("Soddisfa");

            richiestaTextView.setText(idRichiestaTesi.toString());
            titoloTesiTextView.setText(titolo);

            context = getContext();

            studenti = studenteModelView.getAllStudente();
            utenti = utenteView.getAllUtente();

            for (Studente studente : studenti) {
                if (studente.getId_studente() == idStudente) {
                    matricolaTextView.setText(studente.getMatricola().toString());
                    Long id_utente = studente.getId_utente();
                    for(Utente utente: utenti){
                        if(utente.getId_utente() == id_utente){

                            nomeTextView.setText(utente.getNome());
                            cognomeTextView.setText(utente.getCognome());
                            emailTextView.setText(utente.getEmail());
                            if(soddisfaRequisiti == true){
                                soddisfaTextView.setText("Lo studente soddisfa tutti i vincoli della tesi");
                                soddisfaTextView.setTextColor(Color.parseColor("#006400"));


                            }else{
                                soddisfaTextView.setText("Lo studente non soddisfa tutti i vincoli della tesi");

                            }


                        }
                    }

                }

            }
        }


    }
}
