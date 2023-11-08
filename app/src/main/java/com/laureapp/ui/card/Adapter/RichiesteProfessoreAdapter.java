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

/**
 * Questa classe rappresenta un adattatore personalizzato utilizzato per visualizzare una lista di richieste di tesi da parte degli studenti.
 */
public class RichiesteProfessoreAdapter extends ArrayAdapter<RichiesteTesi> {
    private Context mContext;
    private ArrayList<RichiesteTesi> mRichiesteTesiList;
    private ArrayList<Tesi> mTesiList;
    private String titolo;

    /**
     * Crea un nuovo adapter per la visualizzazione delle richieste di tesi.
     *
     * @param context           il contesto in cui viene utilizzato l'adapter.
     * @param richiesteTesiList la lista delle richieste di tesi da visualizzare.
     * @param tesiList          la lista delle tesi associate alle richieste.
     */
    public RichiesteProfessoreAdapter(Context context, ArrayList<RichiesteTesi> richiesteTesiList, ArrayList<Tesi> tesiList) {
        super(context, 0, richiesteTesiList);
        mContext = context;
        mRichiesteTesiList = richiesteTesiList;
        mTesiList = tesiList;
    }

    /**
     * Restituisce la vista che rappresenta un elemento della lista delle richieste di tesi
     * nella posizione specificata.
     *
     * @param position    la posizione dell'elemento nella lista.
     * @param convertView la vista precedentemente utilizzata per la riciclaggio, se disponibile.
     * @param parent      il layout padre a cui questa vista verrà eventualmente allegata.
     * @return            la vista che rappresenta l'elemento della lista nella posizione specificata.
     */
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
                Long idStudente = currentRichiesta.getId_studente();
                String stato = currentRichiesta.getStato();
                if (stato != null && (stato.equals("Rifiutata") || stato.equals("Accettata"))) {
                    // Se la richiesta è nello stato "Rifiutato" o "Accettato", restituisci una vista vuota
                    return new View(mContext);
                }

                boolean soddisfaRequisiti = currentRichiesta.isSoddisfa_requisiti();
                Log.d("vedi", String.valueOf(soddisfaRequisiti));
                if (idRichiestaTesi != null) {
                    // Cerca il titolo corrispondente utilizzando l'id_tesi
                    titolo = findTitoloByTesiId(currentRichiesta.getId_tesi());
                    if (titolo != null) {
                        titoloTextView.setText(titolo);
                        idRichiestaTextView.setText(idRichiestaTesi.toString());
                    } else {
                        titoloTextView.setText("Nessuna tesi trovata");
                        idRichiestaTextView.setText("");
                    }
                }

                //Qui gestisco quando l'utnte clicca una richiesta
                    listItemView.setOnClickListener(v -> {
                    Bundle args = new Bundle();
                    args.putSerializable("idRichiestaTesi",idRichiestaTesi);
                    args.putSerializable("Titolo",titolo);
                    args.putSerializable("idStudente",idStudente);
                    args.putSerializable("Soddisfa",soddisfaRequisiti);
                    Navigation.findNavController(v).navigate(R.id.action_richiesteProfessoreFragment_to_dettaglioRichiestaFragment,args);

                });
            }
        }

        return listItemView;
    }

    /**
     * Trova il titolo di una tesi corrispondente all'ID della tesi specificato.
     *
     * @param id_tesi l'ID della tesi da cercare.
     * @return il titolo della tesi corrispondente o null se non viene trovata alcuna corrispondenza.
     */
    private String findTitoloByTesiId(Long id_tesi) {
        for (Tesi tesi : mTesiList) {
            if (tesi.getId_tesi() != null && tesi.getId_tesi().equals(id_tesi)) {
                return tesi.getTitolo();
            }
        }
        return null; // Ritorna null se non trovi una corrispondenza
    }


}