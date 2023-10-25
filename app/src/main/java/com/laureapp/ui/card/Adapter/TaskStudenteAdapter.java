package com.laureapp.ui.card.Adapter;


import static com.laureapp.ui.card.Task.TaskStudenteFragment.deleteTask;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;

import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.TaskStudente;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

public class TaskStudenteAdapter extends ArrayAdapter<TaskStudente> {

    private final NavController mNav;
    private final Context context;
    String ruolo;
    Bundle args;



    /**
     * @param context  si riferisce al contesto in cui viene utilizzato
     * @param taskList corrisponde alla lista di task da passare
     */
    public TaskStudenteAdapter(Context context, List<TaskStudente> taskList, NavController navController, Bundle args) {
        super(context,0,taskList);
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
            itemView = inflater.inflate(R.layout.lista_task, parent, false);
        }

        // Ora puoi ottenere le viste all'interno di 'lista_task.xml' e impostare i dati corrispondenti

        TextView taskTextView = itemView.findViewById(R.id.taskTextView);
        ImageButton deleteTaskImageButton = itemView.findViewById(R.id.delete_task_ImageButton);
        ImageView redDot = itemView.findViewById(R.id.red_status_dot);
        ImageView orangeDot = itemView.findViewById(R.id.orange_status_dot);
        ImageView greenDot = itemView.findViewById(R.id.green_status_dot);


        TaskStudente task = getItem(position);


        if (task != null) {
            taskTextView.setText(task.getTitolo());

            if(Objects.equals(task.getStato(), "Non iniziato")){
                redDot.setVisibility(View.VISIBLE);
                orangeDot.setVisibility(View.GONE);
                greenDot.setVisibility(View.GONE);
            } else if (Objects.equals(task.getStato(), "In corso")) {
                redDot.setVisibility(View.GONE);
                orangeDot.setVisibility(View.VISIBLE);
                greenDot.setVisibility(View.GONE);
            } else if (Objects.equals(task.getStato(), "Completato")) {
                redDot.setVisibility(View.GONE);
                orangeDot.setVisibility(View.GONE);
                greenDot.setVisibility(View.VISIBLE);
            }

            if(StringUtils.equals("Studente", args.getString("ruolo"))){
                Log.d("Task_studente_studente", "Cliccato la task");
                deleteTaskImageButton.setVisibility(View.GONE);
            }else if(StringUtils.equals("Professore", args.getString("ruolo"))){
                deleteTaskImageButton.setOnClickListener(view -> {
                    //Titolo della task
                    String titolo = task.getTitolo();
                    showConfirmationDialog(titolo, position);
                });
            }




        }
        // Gestisci il clic sull'elemento della lista
        itemView.setOnClickListener(v -> {
            TaskStudente selectedTask = getItem(position);
            if (selectedTask != null) {

                if(StringUtils.equals("Studente", args.getString("ruolo"))){
                    args.putSerializable("SelectedTask", selectedTask);
                    mNav.navigate(R.id.action_fragment_taskStudenteFragment_to_dettagli_task, args);

                }else if(StringUtils.equals("Professore", args.getString("ruolo"))) {
                    // Utilizza la NavHostController per navigare al dettaglio del task
                    args.putSerializable("SelectedTask", selectedTask);
                    mNav.navigate(R.id.action_task_to_dettagli_task, args);
                }
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

        builder.setPositiveButton("Conferma", (dialog, which) -> {
            // Elimina l'elemento
            deleteTask(position);
            dialog.dismiss(); // Chiudi il popup
        });

        builder.setNegativeButton("Annulla", (dialog, which) -> {
            dialog.dismiss(); // Chiudi il popup
        });

        // Mostra il popup
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
