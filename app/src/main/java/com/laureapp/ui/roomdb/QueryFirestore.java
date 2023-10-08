package com.laureapp.ui.roomdb;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.laureapp.ui.controlli.ControlInput;
import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.Utente;

import java.util.concurrent.CompletableFuture;

public class QueryFirestore {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference utentiRef = db.collection("Utenti");
    CollectionReference studentiRef = db.collection("Utenti/Studenti/Studenti");
    CollectionReference professoriRef = db.collection("Utenti/Professori/Professori");


    public CompletableFuture<Long> trovaIdUtenteMax(Context context) {
        CompletableFuture<Long> future = new CompletableFuture<>();

        utentiRef
                .orderBy("id_utente", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        Utente utente = document.toObject(Utente.class);
                        if (utente != null) {
                            Long idMax = utente.getId_utente();
                            future.complete(idMax);
                        }
                    } else {
                        future.complete(0L); // Completa con una stringa vuota in caso di nessun utente
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Errore durante la ricerca dell'ID più grande", e);
                    ControlInput.showToast(context, "Errore durante la ricerca dell'ID più grande");
                    future.completeExceptionally(e); // Completa con l'eccezione in caso di errore
                });

        return future;
    }

    public CompletableFuture<Long> trovaIdStudenteMax(Context context) {
        CompletableFuture<Long> future = new CompletableFuture<>();

        studentiRef
                .orderBy("id_studente", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        Studente studente = document.toObject(Studente.class);
                        if (studente != null) {
                            Long idMax = studente.getId_studente();
                            future.complete(idMax);
                        }
                    } else {
                        future.complete(0L); // Completa con 0L in caso di nessun studente
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Errore durante la ricerca dell'ID più grande degli studenti", e);
                    ControlInput.showToast(context, "Errore durante la ricerca dell'ID più grande degli studenti");
                    future.completeExceptionally(e); // Completa con l'eccezione in caso di errore
                });

        return future;
    }

    public CompletableFuture<Long> trovaIdProfessoreMax(Context context) {
        CompletableFuture<Long> future = new CompletableFuture<>();

        professoriRef
                .orderBy("id_professore", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        Professore professore = document.toObject(Professore.class);
                        if (professore != null) {
                            Long idMax = professore.getId_professore();
                            future.complete(idMax);
                        }
                    } else {
                        future.complete(0L); // Completa con 0L in caso di nessun studente
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Errore durante la ricerca dell'ID più grande degli studenti", e);
                    ControlInput.showToast(context, "Errore durante la ricerca dell'ID più grande degli studenti");
                    future.completeExceptionally(e); // Completa con l'eccezione in caso di errore
                });

        return future;
    }

}
