package com.laureapp.ui.card.TesiStudente;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Tesi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DettagliTesiStudenteFragment} factory method to
 * create an instance of this fragment.
 */
public class DettagliTesiStudenteFragment extends Fragment {
    Tesi tesi;
    String titolo;
    String descrizione;
    Date dataPubblicazione;
    String tipologia;
    String cicloCdl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dettagli_tesi_studente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Prendo gli argomenti passatomi dal layout precedente
        Bundle args = getArguments();
        TextView titoloTextView = view.findViewById(R.id.insertTextViewTitolo);
        TextView abstractTextView = view.findViewById(R.id.insertTextViewAbstract);
        TextView tipologiaTextView = view.findViewById(R.id.insertTextViewTipologia);
        TextView dataTextView = view.findViewById(R.id.insertTextViewDataPubblicazione);
        TextView ciclocdlTextView = view.findViewById(R.id.insertTextViewCicloCDL);

        if (args != null) { //se non sono null

             tesi = (Tesi) args.getSerializable("Tesi"); //prendo la tesi dagli args
            if (tesi != null) {
                 titolo = tesi.getTitolo();
                 descrizione = tesi.getAbstract_tesi();
                 tipologia = tesi.getTipologia();
                 dataPubblicazione = tesi.getData_pubblicazione();
                 cicloCdl = tesi.getCiclo_cdl();
                 titoloTextView.setText(titolo);
                 abstractTextView.setText(descrizione);
                 tipologiaTextView.setText(tipologia);
                // formatto la data per convertirla da sql a java.date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = dateFormat.format(dataPubblicazione);
                dataTextView.setText(formattedDate);

                ciclocdlTextView.setText(cicloCdl);


            }
        }
    }
}