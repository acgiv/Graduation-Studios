package com.laureapp.ui.card.Adapter;

import android.content.Context;
import android.media.Image;
import android.telephony.mbms.FileInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.laureapp.R;

import java.util.ArrayList;

/**
 * Questa classe è un adattatore personalizzato utilizzato per visualizzare una lista di nomi di file
 * associati a una tesi e fornire interazioni per il download e l'eliminazione dei file.
 */
public class InfoTesiProfessoreAdapter extends BaseAdapter {
    private ArrayList<String> nomiFile;
    private LayoutInflater inflater;

    /**
     * Costruttore di InfoTesiProfessoreAdapter.
     *
     * Questo costruttore crea un'istanza della classe InfoTesiProfessoreAdapter, che è un adattatore personalizzato
     * utilizzato per popolare una vista di elenco con una lista di nomi di file associati a una tesi.
     *
     * @param context
     * @param nomiFile la lista di nomi di file associati a una tesi.
     */
    public InfoTesiProfessoreAdapter(Context context, ArrayList<String> nomiFile) {
        this.nomiFile = nomiFile;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (nomiFile != null) {
            return nomiFile.size();
        } else {
            return 0;
        }
    }
    @Override
    public Object getItem(int position) {
        return nomiFile.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Interfaccia per la gestione del clic sul pulsante di eliminazione in un adapter.
     */
    public interface DeleteButtonClickListener {
        void onDeleteButtonClick(int position);
    }

    private DeleteButtonClickListener deleteButtonClickListener;

    /**
     * Imposta un listener per il clic sul pulsante di eliminazione.
     *
     * @param listener
     */
    public void setDeleteButtonClickListener(DeleteButtonClickListener listener) {
        this.deleteButtonClickListener = listener;
    }

    /**
     * Questa interfaccia definisce un listener per il clic sul pulsante di download in un elemento dell'adapter.
     */
    public interface DownloadButtonClickListener {
        void onDownloadButtonClick(int position);
    }

    private DownloadButtonClickListener downloadButtonClickListener;

    /**
     * Imposta un listener per il clic sul pulsante di download nell'adapter.
     *
     * @param listener
     */
    public void setDownloadButtonClickListener(DownloadButtonClickListener listener) {
        this.downloadButtonClickListener = listener;
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_file_tesi, parent, false);
        }

        // Ottieni l'oggetto FileInfo dalla lista
        String fileInfo = nomiFile.get(position);

        // Collega i dati dell'oggetto FileInfo alle viste nel layout del singolo elemento della ListView
        TextView fileNameTextView = convertView.findViewById(R.id.nomeFileTextView);
        fileNameTextView.setText(fileInfo.toString());

        // Aggiungi un gestore di clic per il download del file se necessario
        ImageButton downloadButton = convertView.findViewById(R.id.downloadMaterialeButton);
        ImageButton deleteButton = convertView.findViewById(R.id.deleteMaterialeButton);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadButtonClickListener != null) {
                    downloadButtonClickListener.onDownloadButtonClick(position);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteButtonClickListener != null) {
                    deleteButtonClickListener.onDeleteButtonClick(position);
                }
            }
        });

        return convertView;
    }

}
