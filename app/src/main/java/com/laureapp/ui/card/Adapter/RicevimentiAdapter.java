package com.laureapp.ui.card.Adapter;



import static com.laureapp.ui.card.Task.RicevimentiFragment.deleteRicevimento;
import static com.laureapp.ui.card.Task.TaskStudenteFragment.deleteTask;

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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.navigation.NavController;

import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Ricevimenti;
import com.laureapp.ui.roomdb.entity.TaskStudente;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class RicevimentiAdapter extends ArrayAdapter<Ricevimenti> {

    private final LayoutInflater inflater;
    private final List<Ricevimenti> ricevimentiList;
    private NavController mNav;
    private Context context;



    /**
     * @param context  si riferisce al contesto in cui viene utilizzato
     * @param ricevimentiList corrisponde alla lista di task da passare
     */
    public RicevimentiAdapter(Context context, List<Ricevimenti> ricevimentiList, NavController navController) {
        super(context,0,ricevimentiList);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.ricevimentiList = ricevimentiList;
        this.mNav = navController;  // Inizializza il NavController

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
            itemView = inflater.inflate(R.layout.lista_ricevimenti, parent, false);
        }

        // Ora puoi ottenere le viste all'interno di 'lista_task.xml' e impostare i dati corrispondenti

        TextView id_ricevimentoTextView = itemView.findViewById(R.id.ricevimentiTextView);
        TextView arg_ricevimentoTextView = itemView.findViewById(R.id.argomentoRicevimentoTextView);
        TextView data_ricevimentoTextView = itemView.findViewById(R.id.dataRicevimentoTextView);

        ImageButton deleteRicevimentoImageButton = itemView.findViewById(R.id.delete_ricevimento_ImageButton);

        Ricevimenti ricevimento = getItem(position);

        if (ricevimento != null) {

            String ricevimentoText = "<b>Ricevimento " + ricevimento.getId_ricevimento().toString() + "</b>";

            //Il testo è ora in grassetto
            SpannableString ricevimentoTextBold = new SpannableString(Html.fromHtml(ricevimentoText, Html.FROM_HTML_MODE_LEGACY));
            id_ricevimentoTextView.setText(ricevimentoTextBold);
            arg_ricevimentoTextView.setText(ricevimento.getArgomento());

            //Per non utilizzare il metodo deprecated getDate
            Date dataRicevimento = ricevimento.getData_ricevimento();
            if (dataRicevimento != null) {
                LocalDate localDate = dataRicevimento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String formattedDate = localDate.toString(); // Formatta la data come preferisci

                data_ricevimentoTextView.setText(formattedDate);
            } else {
                data_ricevimentoTextView.setText("Data non disponibile");
            }


            deleteRicevimentoImageButton.setOnClickListener(view -> {
                //Titolo della task
                String data_ricevimento = ricevimento.getData_ricevimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
                showConfirmationDialog(data_ricevimento, position);
            });


        }



        return itemView;
    }

    private void showConfirmationDialog(String data_ricevimento, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Conferma eliminazione");

        String messageText = "Vuoi davvero eliminare il ricevimento in data <b>" + data_ricevimento + "</b>?";

        // Crea un oggetto SpannableString utilizzando Html.fromHtml per poter utilizzare la formattazione html
        //Il testo è ora in grassetto
        SpannableString message = new SpannableString(Html.fromHtml(messageText, Html.FROM_HTML_MODE_LEGACY));

        builder.setMessage(message);

        builder.setPositiveButton("Conferma", (dialog, which) -> {
            // Elimina l'elemento
            deleteRicevimento(position);
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
