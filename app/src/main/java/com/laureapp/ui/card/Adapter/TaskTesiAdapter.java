package com.laureapp.ui.card.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.TaskTesi;

import java.util.ArrayList;
import java.util.List;

/**
 * Questo adapter fornisce l'accesso ai data Item. In questo caso,
 * ai ListItem della ListView e si occupare di settare graficamente
 * gli elementi della lista
 */
public class TaskTesiAdapter extends ArrayAdapter<TaskTesi> {
    private final LayoutInflater inflater;
    private final List<TaskTesi> taskList;


    /**
     * @param context  si riferisce al contesto in cui viene utilizzato
     * @param taskList corrisponde alla lista di task da passare
     */
    public TaskTesiAdapter(Context context, List<TaskTesi> taskList) {
        super(context, 0, taskList);
        inflater = LayoutInflater.from(context);
        this.taskList = taskList;

    }


    /**
     * @param position    si riferisce alla posizione dell'item della lista
     * @param convertView si riferisce alla variabile che gestisce il cambiamento della view
     * @param parent      Interfaccia per le informazioni globali riguardo all'ambiente dell'applicazione.
     *                    usata per chiamare operazioni a livello applicazione launching activities, broadcasting e receiving intents
     * @return la view con la lista aggiornata
     */
    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lista_task, parent, false);
        }

        TextView taskTextView = convertView.findViewById(R.id.taskTextView);

        TaskTesi taskListView = getItem(position);


        if (taskListView != null) {


            if (taskTextView != null) {
                // Ottieni il nome e il cognome dall'oggetto Utente associato allo studente
                String nomeTask = taskListView.getTitolo();

                taskTextView.setText(nomeTask);
            }
        }

        return convertView;
    }

}



