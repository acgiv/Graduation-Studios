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

import java.util.ArrayList;

public class RichiesteProfessoreAdapter extends ArrayAdapter<RichiesteTesi> {
    private Context mContext;
    private ArrayList<RichiesteTesi> mRichiesteTesiList;
    private ArrayList<String> mTitoliTesiList;

    public RichiesteProfessoreAdapter(Context context, ArrayList<RichiesteTesi> richiesteTesiList, ArrayList<String> titoliTesiList) {
        super(context, 0, richiesteTesiList);
        mContext = context;
        mRichiesteTesiList = richiesteTesiList;
        mTitoliTesiList = titoliTesiList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.lista_richieste_professore, parent, false);
        }


            final RichiesteTesi currentRichiesta = mRichiesteTesiList.get(position);
            String titoloTesi = mTitoliTesiList.get(position);

            TextView titoloTextView = listItemView.findViewById(R.id.titoloTesi);
            TextView idRichiestaTextView = listItemView.findViewById(R.id.RichiestaId);

            if (currentRichiesta != null) {
                Long idRichiestaTesi = currentRichiesta.getId_richiesta_tesi();

                if (idRichiestaTesi != null) {
                    titoloTextView.setText(titoloTesi);
                    idRichiestaTextView.setText(idRichiestaTesi.toString());
                } else {
                    titoloTextView.setText("Nessuna richiesta trovata");
                    idRichiestaTextView.setText("");
                }


            listItemView.setOnClickListener(v -> {
                // Handle the click action here
                Bundle args = new Bundle();
                // args.putSerializable("RichiestaTesi", currentRichiesta);
                // Navigation.findNavController(v).navigate(R.id.tua_azione_per_dettagli_richiesta, args);
            });
        }

        return listItemView;
    }
}