package com.laureapp.ui.card.TesiProfessore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.entity.Vincolo;

import java.util.HashMap;
import java.util.Map;

public class VincoliTesiFragment extends Fragment {

    Tesi tesi;
    Long id_vincolo;
    Vincolo vincolo;
    Long media;
    Long esamiMancanti;
    Long tempistiche;
    String skill;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        return inflater.inflate(R.layout.fragment_vincoli_tesi_professore, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Prendo gli argomenti del layout precedente
        Bundle args = getArguments();

        //Inizializzo
        TextView tempisticheProfessoreTextView = view.findViewById(R.id.insertTempisticheTesiProfessore);
        TextView mediaVotiProfessoreTextView = view.findViewById(R.id.insertMediaVotiTesiProfessore);
        TextView esamiMancantiProfessoreTextView = view.findViewById(R.id.insertEsamiMancantiTesiProfessore);
        TextView skillProfessoreTextView = view.findViewById(R.id.insertSkillTesiProfessore);

        ImageButton modificaTempisticheButton = view.findViewById(R.id.modificaTempisticheTesiProfessore);
        ImageButton modificaMediaVotiButton = view.findViewById(R.id.modificaMediaVotiTesiProfessore);
        ImageButton modificaEsamiMancantiButton = view.findViewById(R.id.modificaEsamiMancantiTesiProfessore);
        ImageButton modificaSkillButton = view.findViewById(R.id.modificaSkillTesiProfessore);

        if (args != null) { //Se non sono null

            tesi = (Tesi) args.getSerializable("Tesi"); //Prendo la tesi dagli args

            if(tesi != null) {

                id_vincolo = tesi.getId_vincolo(); //Ottengo l'id del vincolo

                loadVincoloData(id_vincolo).addOnCompleteListener(taskVincolo -> { //Carico i vincoli
                    if(taskVincolo.isSuccessful()) {
                        vincolo = taskVincolo.getResult();
                        media = vincolo.getMedia_voti();
                        esamiMancanti = vincolo.getEsami_mancanti_necessari();

                        tempisticheProfessoreTextView.setText(vincolo.getTempistiche().toString());
                        mediaVotiProfessoreTextView.setText(media.toString());
                        esamiMancantiProfessoreTextView.setText(esamiMancanti.toString());
                        skillProfessoreTextView.setText(vincolo.getSkill());

                    } else {
                        Log.e("Vincolo Firestore Error","Error getting vincolo data", taskVincolo.getException());
                    }
                });

                //Gestione del click sulla matita per le tempistiche
                modificaTempisticheButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //Mostra il popup e permetti la modifica
                        showEditPopup(tempisticheProfessoreTextView, String.valueOf(tempistiche));
                    }
                });

                //Gestione del click sulla matita per la media voti
                modificaMediaVotiButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Mostra il popup e permetti la modifica
                        showEditPopup(mediaVotiProfessoreTextView, String.valueOf(media));
                    }
                });

                //Gestione del click sulla matita per gli esami mancanti
                modificaEsamiMancantiButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Mostra il popup e permetti la modifica
                        showEditPopup(esamiMancantiProfessoreTextView, String.valueOf(esamiMancanti));
                    }
                });

                //Gestione del click sulla matita per le skill
                modificaSkillButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    //Mostra il popup e permetti la modifica
                    public void onClick(View v) {
                        showEditPopup(skillProfessoreTextView, skill);
                    }
                });

            }
        }
    }

    /**
     * Questo metodo consente di ottenere i dati dei vincoli delle tesi da firestore e riempire la entity Vincolo
     *
     * @param id_vincolo id del vincolo legata alla tesi
     * @return entity Vincolo
     */

    private Task<Vincolo> loadVincoloData(Long id_vincolo) {
        final Vincolo vincolo = new Vincolo();

        //Crea una Firestore query per prendere Tesi documents abbinando gli ID
        Query query = FirebaseFirestore.getInstance()
                .collection("Vincolo")
                .whereEqualTo("id_vincolo", id_vincolo);
        return query.get().continueWith(task -> {
            if (task.isSuccessful()) {
                for(QueryDocumentSnapshot document : task.getResult()) {
                    vincolo.setId_vincolo((Long) document.get("id_vincolo"));
                    vincolo.setTempistiche((Long) document.get("tempistiche"));
                    vincolo.setMedia_voti((Long) document.get("media_voti"));
                    vincolo.setEsami_mancanti_necessari((Long) document.get("esami_mancanti_necessari"));
                    vincolo.setSkill((String) document.get("skill"));
                }
            }
            return vincolo;
        });

    }

    /**
     * Questo metodo serve a mostrare un popup generico dove posso modificare i campi di tipo String (anche tramite casting)
     * In base all'id del textview verrà utilizzato il set opportuno per modificare il campo all'interno dell'oggetto Vincolo
     * @param textView
     * @param currentText
     */

    private void showEditPopup(final TextView textView, String currentText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View popupView = getLayoutInflater().inflate(R.layout.fragment_popup_modifica_campi_tesi, null);
        final EditText editText = popupView.findViewById(R.id.editText);

        //Imposta il testo corrente nell'EditText
        editText.setText(currentText);

        builder.setView(popupView);

        //Salva
        builder.setPositiveButton("Salva", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Ottieni il nuovo testo dalla casella di testo
                String nuovoTesto = editText.getText().toString();

                //Aggiorna il cammpo corrispondente (Tempistiche, media voti, ecc...)
                textView.setText(nuovoTesto);

                //Aggiorna l'oggetto Tesi con i nuovi valori
                if (textView.getId() == R.id.insertTempisticheTesiProfessore) {
                    vincolo.setTempistiche(Long.valueOf(nuovoTesto));
                } else if (textView.getId() == R.id.insertMediaVotiTesiProfessore) {
                    vincolo.setMedia_voti(Long.valueOf(nuovoTesto));
                } else if (textView.getId() == R.id.insertEsamiMancantiTesiProfessore) {
                    vincolo.setEsami_mancanti_necessari(Long.valueOf(nuovoTesto));
                } else if (textView.getId() == R.id.insertSkillTesiProfessore) {
                    vincolo.setSkill(nuovoTesto);
                }

                //Aggiungi altre condizioni per gli altri campi
                Log.d("vedi6", vincolo.toString());
                updateVincoloData(vincolo); //Chiamata al metodo di aggiornamento dei vincoli su firestore
                dialog.dismiss();
            }
        });

        //Annulla
        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });

        builder.create().show();

    }

    /**
     * Metodo per aggiornare i vincoli modificati su firestore
     * @param vincolo
     */

    private void updateVincoloData (Vincolo vincolo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference vincoloRef = db.collection("Vincolo");

        Query query = vincoloRef.whereEqualTo("id_vincolo", vincolo.getId_vincolo()); //cerco la tesi con l'id tesi corrispondente

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Ottieni il documento corrispondente (potrebbe esserci più di uno, ma ne usiamo il primo)
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                    // Estrai l'ID del documento
                    String documentId = documentSnapshot.getId();

                    //Crea un oggetto mappa con i nuovi valori per tutti i campi
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("tempistiche", vincolo.getTempistiche());
                    updateData.put("media_voti", vincolo.getMedia_voti());
                    updateData.put("esami_mancanti_necessari", vincolo.getEsami_mancanti_necessari());
                    updateData.put("skill", vincolo.getSkill());

                    //Esegui l'aggiornamento
                    vincoloRef.document(documentId)
                            .update(updateData)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firestore Success", "Dati della tesi aggiornati con successo.");
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firestore Error", "Errore durante l'aggiornamento dei dati della tesi", e);
                            });
                }
            } else {
                Log.e("Firestore Error", "Error querying Tesi collection", task.getException());
            }

        });

    }

}
