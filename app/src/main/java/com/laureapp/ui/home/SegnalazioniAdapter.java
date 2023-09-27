package com.laureapp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.laureapp.R;

import java.util.ArrayList;
import java.util.List;




public class SegnalazioniAdapter extends ArrayAdapter<String> {

    public SegnalazioniAdapter(Context context, List<String> segnalazioni) {
        super(context, 0, segnalazioni);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lista_segnalazioni, parent, false);
        }

        // Ottieni l'elemento corrente
        String segnalazione = getItem(position);

        // Imposta il testo nell'elemento principale
        TextView mainTextView = convertView.findViewById(R.id.main_text_view);
        mainTextView.setText(segnalazione);

        // Imposta il testo nell'elemento secondario (subitem)
        TextView subTextView = convertView.findViewById(R.id.sub_text_view);
        subTextView.setText("Sottotesto per " + segnalazione);

        return convertView;
    }
}

