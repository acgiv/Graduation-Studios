package com.laureapp.ui.card.TesiStudente;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class ConfermaRichiestaDialog extends DialogFragment {

    private String message;
    private Long idTesi;
    private Long idStudente;
    private boolean soddisfaRequisiti;

    /**
     * Crea un nuovo oggetto di dialogo per la conferma di una richiesta con i parametri specificati.
     *
     * @param message il messaggio da visualizzare nel dialogo di conferma.
     * @param idTesi l'ID della tesi associata alla richiesta.
     * @param idStudente l'ID dello studente che ha inviato la richiesta.
     * @param soddisfaRequisiti un flag che indica se la richiesta soddisfa i requisiti.
     */
    public ConfermaRichiestaDialog(String message,Long idTesi,Long idStudente,boolean soddisfaRequisiti ) {
        this.idTesi = idTesi;
        this.idStudente = idStudente;
        this.message = message;
        this.soddisfaRequisiti = soddisfaRequisiti;
    }

    /**
     * Crea e restituisce una finestra di dialogo di conferma per l'invio di una richiesta di tesi.
     *
     * @param savedInstanceState un oggetto Bundle che rappresenta lo stato precedente dell'istanza.
     * @return una finestra di dialogo di conferma per l'invio di una richiesta di tesi.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Conferma Richiesta")
                .setMessage(message)
                .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        createRichiestaTesi();

                    }
                })
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle the cancel action here
                    }
                });
        return builder.create();
    }

    /**
     * Crea una nuova richiesta di tesi in Firestore con i parametri specificati.
     * La richiesta di tesi include l'ID della tesi, l'ID dello studente, lo stato, e se soddisfa i requisiti.
     */

    private void createRichiestaTesi(){
        String stato = "In Attesa";
        // Crea un oggetto per la nuova richiesta di tesi
        Map<String, Object> richiestaTesi = new HashMap<>();
        richiestaTesi.put("id_tesi", idTesi);
        richiestaTesi.put("id_studente",idStudente);
        if(soddisfaRequisiti == true){
            richiestaTesi.put("soddisfa_requisiti",true);

        }else{
            richiestaTesi.put("soddisfa_requisiti",false);
        }
        // Trova il massimo ID attuale e incrementalo di 1
        findMaxRequestId(new MaxRequestIdCallback() {
            @Override
            public void onCallback(Long maxRequestId) {
                richiestaTesi.put("id_richiesta_tesi", maxRequestId + 1);
                richiestaTesi.put("stato", stato);

                // Aggiungi la nuova richiesta di tesi a Firestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference richiesteTesiRef = db.collection("RichiesteTesi");

                richiesteTesiRef.add(richiestaTesi)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // La richiesta di tesi è stata aggiunta con successo
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Si è verificato un errore nell'aggiunta della richiesta di tesi
                            }
                        });
            }
        });
    }

    /**
     * Questa interfaccia fornisce un callback per ottenere il valore massimo di un identificatore.
     * È utilizzata per restituire il valore massimo di un identificatore, ad esempio, un ID,
     * utilizzato all'interno di un'operazione asincrona.
     */

    private interface MaxRequestIdCallback {
        void onCallback(Long maxRequestId);
    }

    /**
     * Metodo utilizzato per trovare l'id massimo presente nella tabella richiesteTesi
     * @param callback
     */
    private void findMaxRequestId(MaxRequestIdCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference richiesteTesiRef = db.collection("RichiesteTesi");

        richiesteTesiRef
                .orderBy("id_richiesta_tesi", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        Long maxRequestId = queryDocumentSnapshots.getDocuments().get(0).getLong("id_richiesta_tesi");
                        if (maxRequestId != null) {
                            callback.onCallback(maxRequestId);
                        } else {
                            // Nessun ID trovato, inizia da 1
                            callback.onCallback(1L);
                        }
                    } else {
                        // Nessun documento trovato, inizia da 1
                        callback.onCallback(1L);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore Error", "Error finding max request ID", e);
                });
    }

}

