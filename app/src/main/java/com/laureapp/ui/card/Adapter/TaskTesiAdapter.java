package com.laureapp.ui.card.Adapter;


import static com.laureapp.ui.card.Task.TaskTesiFragment.deleteTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;

import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.TaskTesi;

import java.util.List;

/**
 * Questo adapter fornisce l'accesso ai data Item. In questo caso,
 * ai ListItem della ListView e si occupare di settare graficamente
 * gli elementi della lista
 */
public class TaskTesiAdapter extends ArrayAdapter<TaskTesi> {
    private final LayoutInflater inflater;
    private final List<TaskTesi> taskList;
    private NavController mNav;
    private Context context;



    /**
     * @param context  si riferisce al contesto in cui viene utilizzato
     * @param taskList corrisponde alla lista di task da passare
     */
    public TaskTesiAdapter(Context context, List<TaskTesi> taskList, NavController navController) {
        super(context, 0, taskList);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.taskList = taskList;
        this.mNav = navController;  // Inizializza il NavController

    }


    /**
     * @param position    si riferisce alla posizione dell'item della lista
     * @param convertView si riferisce alla variabile che gestisce il cambiamento della view
     * @param parent      Interfaccia per le informazioni globali riguardo all'ambiente dell'applicazione.
     *                    usata per chiamare operazioni a livello applicazione launching activities, broadcasting e receiving intents
     * @return la view con la lista aggiornata
     */
    /*@NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            itemView = inflater.inflate(R.layout.lista_task, parent, false);
        }

        // Ora puoi ottenere le viste all'interno di 'lista_task.xml' e impostare i dati corrispondenti

        TextView taskTextView = itemView.findViewById(R.id.taskTextView);
        ImageButton deleteTaskImageButton = itemView.findViewById(R.id.delete_task_ImageButton);

        TaskTesi task = getItem(position);

        if (task != null) {
            taskTextView.setText(task.getTitolo());

            deleteTaskImageButton.setOnClickListener(view -> {
                //Titolo della task
                String titolo = task.getTitolo();
                showConfirmationDialog(titolo, position);
            });


        }
        // Gestisci il clic sull'elemento della lista
        itemView.setOnClickListener(v -> {
            TaskTesi selectedTask = getItem(position);

            if (selectedTask != null) {
                Bundle args = new Bundle();
                args.putSerializable("SelectedTask", selectedTask);

                // Utilizza la NavHostController per navigare al dettaglio del task
                mNav.navigate(R.id.action_task_to_dettagli_task, args);
            }
        });


        return itemView;
    }

    private void showConfirmationDialog(String titolo, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Conferma eliminazione");

        String messageText = "Vuoi davvero eliminare la task <b>" + titolo + "</b>?";

        // Crea un oggetto SpannableString utilizzando Html.fromHtml per poter utilizzare la formattazione html
        //Il testo è ora in grassetto
        SpannableString message = new SpannableString(Html.fromHtml(messageText, Html.FROM_HTML_MODE_LEGACY));

        builder.setMessage(message);

        builder.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Elimina l'elemento
                deleteTask(position);
                dialog.dismiss(); // Chiudi il popup
            }
        });

        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Chiudi il popup
            }
        });

        // Mostra il popup
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

*/
}



