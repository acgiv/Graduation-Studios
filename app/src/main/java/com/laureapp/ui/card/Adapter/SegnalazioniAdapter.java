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

/**
 * Questa classe rappresenta un adattatore personalizzato utilizzato per visualizzare una lista di segnalazioni.
 */
public class SegnalazioniAdapter extends ArrayAdapter<Segnalazione> {

    private final NavController mNav;
    private final Context context;
    String ruolo;
    Bundle args;

    /**
     * Crea un nuovo adapter per la visualizzazione delle segnalazioni.
     *
     * @param context          il contesto dell'applicazione.
     * @param segnalazioneList la lista di segnalazioni da visualizzare.
     * @param navController    il NavController utilizzato per la navigazione tra le schermate.
     * @param args             un bundle di dati aggiuntivi da passare durante la navigazione.
     */
    public SegnalazioniAdapter(Context context, List<Segnalazione> segnalazioneList, NavController navController, Bundle args) {
        super(context,0,segnalazioneList);
        this.context = context;
        this.mNav = navController;  // Inizializza il NavController
        this.args = args;
    }


    /**
     * Restituisce la vista che rappresenta l'elemento nella posizione specificata nella lista delle segnalazioni.
     *
     * @param position    la posizione dell'elemento nella lista delle segnalazioni.
     * @param convertView la vista riutilizzata per visualizzare l'elemento (se disponibile).
     * @param parent      il genitore della vista (solitamente la ListView o la RecyclerView).
     * @return            La vista che rappresenta l'elemento nella posizione specificata.
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
