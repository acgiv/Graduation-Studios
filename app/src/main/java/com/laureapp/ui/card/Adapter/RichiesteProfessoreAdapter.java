package com.laureapp.ui.card.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.RichiesteTesi;
import com.laureapp.ui.roomdb.entity.Tesi;

import java.util.ArrayList;

public class RichiesteProfessoreAdapter extends ArrayAdapter<RichiesteTesi> {
    private Context mContext;
    private ArrayList<RichiesteTesi> mRichiesteTesiList;
    private ArrayList<Tesi> mTesiList;

    public RichiesteProfessoreAdapter(Context context, ArrayList<RichiesteTesi> richiesteTesiList, ArrayList<Tesi> tesiList) {
        super(context, 0, richiesteTesiList);
        mContext = context;
        mRichiesteTesiList = richiesteTesiList;
        mTesiList = tesiList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.lista_richieste_professore, parent, false);
        }

        if (position < mRichiesteTesiList.size()) {
            final RichiesteTesi currentRichiesta = mRichiesteTesiList.get(position);

            TextView titoloTextView = listItemView.findViewById(R.id.titoloTesi);
            TextView idRichiestaTextView = listItemView.findViewById(R.id.RichiestaId);

            if (currentRichiesta != null) {
                Long idRichiestaTesi = currentRichiesta.getId_richiesta_tesi();

                if (idRichiestaTesi != null) {
                    // Cerca il titolo corrispondente utilizzando l'id_tesi
                    String titolo = findTitoloByTesiId(currentRichiesta.getId_tesi());

                    if (titolo != null) {
                        titoloTextView.setText(titolo);
                        idRichiestaTextView.setText(idRichiestaTesi.toString());
                    } else {
                        titoloTextView.setText("Nessuna tesi trovata");
                        idRichiestaTextView.setText("");
                    }
                }

                //Qui gestisco quando l'utnte clicca una tesi
                    listItemView.setOnClickListener(v -> {
                    Bundle args = new Bundle();
                    Navigation.findNavController(v).navigate(R.id.action_richiesteProfessoreFragment_to_dettaglioRichiestaFragment,args);

                });
            }
        }

        return listItemView;
    }

    // Metodo per trovare il titolo della tesi in base all'id_tesi
    private String findTitoloByTesiId(Long id_tesi) {
        for (Tesi tesi : mTesiList) {
            if (tesi.getId_tesi() != null && tesi.getId_tesi().equals(id_tesi)) {
                return tesi.getTitolo();
            }
        }
        return null; // Ritorna null se non trovi una corrispondenza
    }
}