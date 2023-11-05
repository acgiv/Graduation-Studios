package com.laureapp.ui.card.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Tesi;

import java.util.ArrayList;

/**
 * Questa classe rappresenta un adattatore personalizzato utilizzato per visualizzare una lista di tesi associate a un professore.
 */
public class ListaTesiProfessoreAdapter extends ArrayAdapter<Tesi> {

    private Context mContext;
    private ArrayList<Tesi> mTesiList;
    private NavController mNav;

    /**
     * Interfaccia DeleteButtonClickListener.
     *
     * Questa interfaccia definisce un contratto per gestire gli eventi di clic sul pulsante di eliminazione
     * all'interno di un adapter personalizzato.
     */
    public interface DeleteButtonClickListener {
        void onDeleteButtonClick(int position);
    }

    private ListaTesiProfessoreAdapter.DeleteButtonClickListener deleteButtonClickListener;

    /**
     * Imposta un listener per il clic sul pulsante di eliminazione all'interno dell'adapter.
     *
     * @param listener
     */
    public void setDeleteButtonClickListener(ListaTesiProfessoreAdapter.DeleteButtonClickListener listener) {
        this.deleteButtonClickListener = listener;
    }

    /**
     * Crea un nuovo adapter per la visualizzazione di una lista di tesi associate a un professore.
     *
     * @param context
     * @param tesiList La lista di oggetti Tesi da visualizzare.
     */
    public ListaTesiProfessoreAdapter(Context context, ArrayList<Tesi> tesiList) {
        super(context,0,tesiList);
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
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.lista_tesi_professore, parent, false);
        }

        ImageButton deleteButton = listItemView.findViewById(R.id.deleteTesiButton);

        final Tesi currentTesi = mTesiList.get(position);

        TextView titleTextView = listItemView.findViewById(R.id.titoloTesiProfessore);

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
            Navigation.findNavController(v).navigate(R.id.action_tesiProfessoreFragment_to_tesiTabProfessoreFragment,args);

        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteButtonClickListener != null) {
                    deleteButtonClickListener.onDeleteButtonClick(position);
                }
            }
        });

        return listItemView;
    }
}