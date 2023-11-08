package com.laureapp.ui.card.Adapter;



import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;

import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Segnalazione;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class SegnalazioniAdapter extends ArrayAdapter<Segnalazione> {

    private final NavController mNav;
    private final Context context;
    String ruolo;
    Bundle args;



    /**
     * @param context  si riferisce al contesto in cui viene utilizzato
     * @param segnalazioneList corrisponde alla lista di task da passare
     */
    public SegnalazioniAdapter(Context context, List<Segnalazione> segnalazioneList, NavController navController, Bundle args) {
        super(context,0,segnalazioneList);
        this.context = context;
        this.mNav = navController;  // Inizializza il NavController
        this.args = args;
    }


    /**
     * @param position    si riferisce alla posizione dell'item della lista
     * @param convertView si riferisce alla variabile che gestisce il cambiamento della view
     * @param parent      Interfaccia per le informazioni globali riguardo all'ambiente dell'applicazione.
     *                    usata per chiamare operazioni a livello applicazione launching activities, broadcasting e receiving intents
     * @return la view con la lista aggiornata
     */
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            itemView = inflater.inflate(R.layout.lista_segnalazioni, parent, false);
        }

        // Ora puoi ottenere le viste all'interno di 'lista_task.xml' e impostare i dati corrispondenti

        TextView titoloTextView = itemView.findViewById(R.id.text_title);



        Segnalazione segnalazione = getItem(position);


        if (segnalazione != null) {
            titoloTextView.setText(segnalazione.getId_segnalazione().toString() + ". " + segnalazione.getTitolo());
        }
        // Gestisci il clic sull'elemento della lista
        itemView.setOnClickListener(v -> {
            if (segnalazione != null) {
                Bundle updatedArgs = new Bundle(args); // Crea una copia dei dati esistenti
                updatedArgs.putSerializable("SelectedSegnalazione", segnalazione);

                if (StringUtils.equals("Studente", args.getString("ruolo"))) {
                    updatedArgs.putString("ruolo", args.getString("ruolo"));
                }

                mNav.navigate(R.id.action_fragment_segnalazioni_to_chat_fragment, updatedArgs);
            }
        });


        return itemView;
    }


}
