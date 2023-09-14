package com.laureapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.laureapp.databinding.ActivityLoadingPageBinding;
import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteWithUtente;
import java.util.List;

public class LoadingPageActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoadingPageBinding binding;


        binding = ActivityLoadingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d("LoadingPage", "activity avviata");


        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        RoomDbSqlLite db = RoomDbSqlLite.getDatabase(getApplicationContext());

            //Qui leggo la collection degli utenti. L'id dell'utente collegato allo studente è corretto
            firestoreDB.collection("Utenti")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            //Per pulire la cache del db
                            db.utenteDao().deleteAll();

                            //Per salvare i dati in SQLite da Firestore
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            for (DocumentSnapshot document : documents) {
                                StudenteWithUtente studenteWithUtente = document.toObject(StudenteWithUtente.class);// Converte il documento in un oggetto Studente

                                assert studenteWithUtente != null;
                                if (studenteWithUtente.getUtente() != null) {
                                    db.utenteDao().insert(studenteWithUtente.getUtente());
                                } else {
                                    Exception exception = task.getException();
                                    if (exception != null) {
                                        Log.d("Firestore", "Errore nella lettura dei dati: " + exception.getMessage());
                                        exception.printStackTrace();
                                    }
                                }
                            }
                            Log.d("utenti", String.valueOf(db.utenteDao().getAllUtente()));


                        } else {
                            Log.d("Firestore", "Errore nella lettura dei dati: " + task.getException());
                        }
                    });

            //Qui leggo la collection degli studenti. L'id che collega lo studente all'utente è corretto e rimane invariato.
            // Ma ad ogni aggiornamento l'id dello studente viene incrementato.
            //TODO: Verificare in futuro che l'aggiornamento del db e quindi l'incremento dell'id dello studente ad ogni aggiornamento non causi problemi
            firestoreDB.collection("Utenti").document("Studenti").collection("Studenti")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            //Per pulire la cache del db
                            db.studenteDao().deleteAll();

                            //Per salvare i dati in SQLite da Firestore
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            for (DocumentSnapshot document : documents) {
                                Studente studente = document.toObject(Studente.class);
                                Log.d("studenti with utenti", String.valueOf(studente));

                                if (studente != null) {
                                    db.studenteDao().insert(studente); // Chiama il metodo per l'inserimento o l'aggiornamento
                                } else {
                                    Exception exception = task.getException();
                                    if (exception != null) {
                                        Log.d("Firestore", "Errore nella lettura dei dati: " + exception.getMessage());
                                        exception.printStackTrace();
                                    }
                                }
                            }
                            Log.d("studenti", String.valueOf(db.studenteDao().getAllStudente()));


                        } else {
                            Log.d("Firestore", "Errore nella lettura dei dati: " + task.getException());
                        }
                    });


            //Qui leggo la collection professori da Firebase
            firestoreDB.collection("Utenti").document("Professori").collection("Professori")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            //Per pulire la cache del db
                            db.professoreDao().deleteAll();

                            //Per salvare i dati in SQLite da Firestore
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            for (DocumentSnapshot document : documents) {
                                Professore professore = document.toObject(Professore.class);
                                Log.d("studenti with utenti", String.valueOf(professore));

                                if (professore != null) {
                                    db.professoreDao().insert(professore); // Chiama il metodo per l'inserimento o l'aggiornamento
                                } else {
                                    Exception exception = task.getException();
                                    if (exception != null) {
                                        Log.d("Firestore", "Errore nella lettura dei dati: " + exception.getMessage());
                                        exception.printStackTrace();
                                    }
                                }
                            }
                            Log.d("studenti", String.valueOf(db.professoreDao().getAllProfessore()));
                            Intent LoginActivity = new Intent(this, LoginActivity.class);
                            startActivity(LoginActivity);
                            finish(); // Chiudi questa attività in modo che non possa essere tornata indietro.

                        } else {
                            Log.d("Firestore", "Errore nella lettura dei dati: " + task.getException());
                        }
                    });

    }


}