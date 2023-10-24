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

    // Constructor to pass the message
    public ConfermaRichiestaDialog(String message,Long idTesi,Long idStudente,boolean soddisfaRequisiti ) {
        this.idTesi = idTesi;
        this.idStudente = idStudente;
        this.message = message;
        this.soddisfaRequisiti = soddisfaRequisiti;
    }

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

    private interface MaxRequestIdCallback {
        void onCallback(Long maxRequestId);
    }

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

