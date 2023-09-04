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
import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Studente;

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
                            db.studenteDao().insert(studente); // Chiama il metodo per l'inserimento o l'aggiornamento
                        }

                        for (DocumentSnapshot document : documents) {
                            Professore professore = document.toObject(Professore.class); // Converte il documento in un oggetto Studente
                            db.professoreDao().insert(professore); // Chiama il metodo per l'inserimento o l'aggiornamento
                        }


                        Log.d("studenti", String.valueOf(db.studenteDao().getAllStudente()));
                        Log.d("professori", String.valueOf(db.professoreDao().getAllProfessore()));

                        Intent loginIntent = new Intent(LoadingPageActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        finish(); // Chiudi questa attività in modo che non possa essere tornata indietro.
                    } else {
                        Log.d("Firestore", "Errore nella lettura dei dati: " + task.getException());
                    }
                });

    }

}