package com.laureapp.ui.card.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.laureapp.R;
import com.laureapp.ui.card.Segnalazioni.SegnalazioniFragment;
import com.laureapp.ui.roomdb.entity.Segnalazione;
import com.laureapp.ui.roomdb.entity.TaskStudente;

import java.util.ArrayList;
import java.util.List;




public class SegnalazioniAdapter extends ArrayAdapter<Segnalazione> {

    private Context mContex;
    private List<Segnalazione> segnalazioniList;
    public SegnalazioniAdapter(Context context, List<Segnalazione> segnalazioni, Bundle args) {
        super(context, 0, segnalazioni);
        mContex = context;
        segnalazioniList = segnalazioni;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContex).inflate(R.layout.lista_segnalazioni, parent, false);
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.lista_segnalazioni, parent, false);

        // Ottieni l'oggetto Segnalazione corrente
        Segnalazione segnalazione = getItem(position);
        // Imposta il titolo dell'elemento principale
        TextView mainTextView = convertView.findViewById(R.id.text_title);
        // Imposta la richiesta nell'elemento secondario (subitem)
        TextView subTextView = convertView.findViewById(R.id.text_richiesta);

        assert segnalazione != null;
        if(segnalazione.getTitolo() != null) {
            mainTextView.setText(segnalazione.getTitolo());
        }

        if(segnalazione.getRichiesta() != null) {
            subTextView.setText(segnalazione.getRichiesta());
        }


        return listItem;
    }
}
