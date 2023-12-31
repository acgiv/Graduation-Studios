package com.laureapp.ui.card.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.laureapp.R; // Replace with the correct package name
import com.laureapp.ui.roomdb.entity.Tesi;

import java.util.ArrayList;

/**
 * Questa classe rappresenta un adattatore personalizzato utilizzato per popolare una vista di elenco con una lista di tesi.
 */
public class LeMieTesiAdapter extends ArrayAdapter<Tesi> {
    private Context mContext;
    private ArrayList<Tesi> mTesiList;

    /**
     * Costruttore di LeMieTesiAdapter.
     *
     * Questo costruttore crea un'istanza della classe LeMieTesiAdapter, che è un adattatore personalizzato
     * utilizzato per popolare una vista di elenco con una lista di tesi.
     *
     * @param context
     * @param tesiList la lista di tesi da visualizzare nell'elenco.
     */
    public LeMieTesiAdapter(Context context, ArrayList<Tesi> tesiList) {
        super(context, 0, tesiList);
        mContext = context;
        mTesiList = tesiList;
    }

    /**
     * Restituisce la vista dell'elemento dell'adapter in base alla posizione specificata.
     *
     * @param position    la posizione dell'elemento nell'adapter.
     * @param convertView la vista riutilizzata da un elemento precedentemente visualizzato (se disponibile).
     * @param parent      il ViewGroup genitore a cui verrà eventualmente allegata la vista.
     * @return            la vista dell'elemento dell'adapter.
     */
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