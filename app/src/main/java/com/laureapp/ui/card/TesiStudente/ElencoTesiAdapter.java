package com.laureapp.ui.card.TesiStudente;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.laureapp.R; // Replace with the correct package name
import com.laureapp.ui.roomdb.entity.Tesi;

import java.util.ArrayList;

public class ElencoTesiAdapter extends ArrayAdapter<Tesi> {
    private Context mContext;
    private ArrayList<Tesi> mTesiList;

    public ElencoTesiAdapter(Context context, ArrayList<Tesi> tesiList) {
        super(context, 0, tesiList);
        mContext = context;
        mTesiList = tesiList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.lista_tesi_studente, parent, false);
        }

        final Tesi currentTesi = mTesiList.get(position);

        TextView titleTextView = listItemView.findViewById(R.id.titoloTesi);

        if (currentTesi != null) {
            String titolo = currentTesi.getTitolo();

            if (titolo != null) {
                titleTextView.setText(titolo);
            } else {
                titleTextView.setText("Nessuna tesi trovata"); // Set an empty string or handle it as needed
            }
        }

        //Qui gestisco quando l'utnte clicca una tesi
        listItemView.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putSerializable("Tesi",currentTesi);
            Navigation.findNavController(v).navigate(R.id.action_fragment_tesistudenteFragment_to_dettagli_tesi_studente,args);



        });

        return listItemView;
    }

}