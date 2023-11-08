package com.laureapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.laureapp.R;
import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.viewModel.ProfessoreModelView;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoadingPageActivity extends AppCompatActivity {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private FirebaseFirestore db_firestore;
    private OnUsersInsertedListener onUsersInsertedListener;
    private int insertStudentiCompleted = 0;
    private int insertProfessoriCompleted = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);

        RoomDbSqlLite db = RoomDbSqlLite.getDatabase(getApplicationContext());
        executor.execute(db::clearAllTables);

        db_firestore = FirebaseFirestore.getInstance();

        // Inizializza il callback
        onUsersInsertedListener = new OnUsersInsertedListener() {
            @Override
            public void onUsersInserted() {
                // Chiamato quando l'inserimento degli utenti è completato
                insertStudenti();
                insertProfessori();
            }
        };

        insertUtenti();
    }

    // Metodo per inserire gli utenti
    private void insertUtenti() {
        UtenteModelView ut_view = new UtenteModelView(this);
        CollectionReference utentiRef = db_firestore.collection("Utenti");

        utentiRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Itera attraverso i documenti e elimina ciascun documento
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference();
                        Map<String, Object> userData = document.getData();
                        if (userData.containsKey("nome") && userData.containsKey("cognome") && userData.containsKey("email") &&
                                userData.containsKey("password") && userData.containsKey("id_utente") && userData.containsKey("nome_cdl") &&
                                userData.containsKey("facolta")) {
                            Utente utente = new Utente(
                                    Long.valueOf(String.valueOf(userData.get("id_utente"))),
                                    String.valueOf(userData.get("nome")),
                                    String.valueOf(userData.get("cognome")),
                                    String.valueOf(userData.get("email")),
                                    String.valueOf(userData.get("password")),
                                    String.valueOf(userData.get("nome_cdl")),
                                    String.valueOf(userData.get("facolta"))
                            );
                            ut_view.insertUtente(utente);
                        }
                    }
                    // Chiamare il callback quando l'inserimento degli utenti è completato
                    onUsersInsertedListener.onUsersInserted();
                });
    }

    // Metodo per inserire gli studenti
    private void insertStudenti() {
        StudenteModelView st_view = new StudenteModelView(this);
        CollectionReference studentiRef = db_firestore.collection("Utenti/Studenti/Studenti");

        studentiRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Verifica se il documento contiene dati
                        if (document.exists()) {
                            // Ottenere i dati del documento come mappa
                            Map<String, Object> userData = document.getData();
                            if (userData.containsKey("id_studente") && userData.containsKey("esami_mancanti") && userData.containsKey("matricola") &&
                                    userData.containsKey("media") && userData.containsKey("id_utente")) {
                                Studente studente = new Studente(
                                        Long.valueOf(String.valueOf(userData.get("id_studente"))),
                                        Long.valueOf(String.valueOf(userData.get("id_utente"))),
                                        Long.valueOf(String.valueOf(userData.get("matricola"))),
                                        Integer.parseInt(String.valueOf(userData.get("media"))),
                                        Integer.parseInt(String.valueOf(userData.get("esami_mancanti")))
                                );

                                st_view.insertStudente(studente);
                            }
                        }
                    }
                    insertStudentiCompleted++;
                    checkInsertionsCompleted();
                });
    }

    private void insertProfessori() {
        ProfessoreModelView pr_view = new ProfessoreModelView(this);
        CollectionReference professoreRef = db_firestore.collection("Utenti/Professori/Professori");

        professoreRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Verifica se il documento contiene dati
                        if (document.exists()) {
                            // Ottenere i dati del documento come mappa
                            Map<String, Object> userData = document.getData();
                            if (userData.containsKey("id_professore") && userData.containsKey("id_utente") && userData.containsKey("matricola")){
                                Professore professore = new Professore(
                                        Long.valueOf(String.valueOf(userData.get("id_professore"))),
                                        Long.valueOf(String.valueOf(userData.get("id_utente"))),
                                        Long.valueOf(String.valueOf(userData.get("matricola")))
                                );
                               pr_view.insertProfessore(professore);

                            }
                        }
                    }
                    insertProfessoriCompleted++;
                    checkInsertionsCompleted();
                });
    }

    // Controlla se entrambi gli inserimenti sono completati e avvia l'activity successiva se lo sono
    private void checkInsertionsCompleted() {
        if (insertStudentiCompleted == 1 && insertProfessoriCompleted == 1) {
            // Avvia l'activity successiva qui
            Intent intent = new Intent(LoadingPageActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Chiudi questa activity dopo il passaggio all'activity successiva
        }
    }

}
