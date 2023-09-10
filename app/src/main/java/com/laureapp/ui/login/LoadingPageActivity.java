package com.laureapp.ui.login;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.laureapp.databinding.ActivityLoadingPageBinding;

import com.laureapp.R;
import com.laureapp.ui.MainActivity;
import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.Utente;

import java.util.List;

public class LoadingPageActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityLoadingPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityLoadingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d("LoadingPage", "activity avviata");


        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

        RoomDbSqlLite db = RoomDbSqlLite.getDatabase(getApplicationContext());
        firestoreDB.collection("Utenti").get()
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        db.utenteDao().deleteAll();
                        List<DocumentSnapshot> documents2 = task1.getResult().getDocuments();
                        for (DocumentSnapshot document : documents2) {
                            Utente utente = document.toObject(Utente.class);
                            if (utente != null && utente.getNome() != null && utente.getCognome() != null && utente.getEmail() != null && utente.getPassword() != null) {
                                Log.d("utente", String.valueOf(utente));
                                db.utenteDao().insert(utente);
                            } else {
                                Log.d("utente", "Utente non valido: " + utente);
                            }
                        }
                    }  else {
                        Log.d("Firestore", "Errore nella lettura dei dati: " + task1.getException());
                    }
                });
        firestoreDB.collection("Utenti").document("Studenti").collection("Studenti")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        //Per pulire la cache del db
                        db.studenteDao().deleteAll();
                        db.professoreDao().deleteAll();
                        //Per salvare i dati in SQLite da Firestore
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        for (DocumentSnapshot document : documents) {
                            Studente studente = document.toObject(Studente.class); // Converte il documento in un oggetto Studente
                            Log.d("studente", String.valueOf(studente));
                            if(studente.getId_utente()!= null)
                            db.studenteDao().insert(studente);
                        }

                        for (DocumentSnapshot document : documents) {
                            Professore professore = document.toObject(Professore.class);
                            // Converte il documento in un oggetto Studente
                            assert professore != null;
                            if(professore.getId()!= null) {
                                db.professoreDao().insert(professore); // Chiama il metodo per l'inserimento o l'aggiornamento
                            }
                        }

                        Log.d("utenti", String.valueOf(db.utenteDao().getAllUtente()));
                        Log.d("studenti", String.valueOf(db.studenteDao().getAllStudente()));
                        Log.d("professori", String.valueOf(db.professoreDao().getAllProfessore()));

                        Intent LoginActivity = new Intent(this, LoginActivity.class);
                        startActivity(LoginActivity);
                        finish(); // Chiudi questa attività in modo che non possa essere tornata indietro.
                    } else {
                        Log.d("Firestore", "Errore nella lettura dei dati: " + task.getException());
                    }
                });

    }

}