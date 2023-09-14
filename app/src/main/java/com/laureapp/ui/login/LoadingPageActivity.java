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
import com.laureapp.ui.roomdb.entity.Utente;
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
        //Per pulire la cache del db
        Log.d("utenti", String.valueOf(db.utenteDao().getAllUtente()));

        Log.d("studenti", String.valueOf(db.studenteDao().getAllStudente()));
        Log.d("professori", String.valueOf(db.professoreDao().getAllProfessore()));

        firestoreDB.collection("Utenti").document("Studenti").collection("Studenti")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        db.studenteDao().deleteAll();

                        //Per salvare i dati in SQLite da Firestore
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        for (DocumentSnapshot document : documents) {
                            StudenteWithUtente studenteWithUtente = document.toObject(StudenteWithUtente.class);// Converte il documento in un oggetto Studente
                            Log.d("studenti with utenti", String.valueOf(studenteWithUtente.getStudente()));

                            if(studenteWithUtente.getUtente()!=null) {
                                db.utenteDao().insert(studenteWithUtente.getUtente());
                                db.studenteDao().insert(studenteWithUtente.getStudente()); // Chiama il metodo per l'inserimento o l'aggiornamento


                            }
                        }

                        Log.d("studenti", String.valueOf(db.studenteDao().getAllStudente()));
                        Log.d("utenti", String.valueOf(db.utenteDao().getAllUtente()));

                    }else {
                            Log.d("Firestore", "Errore nella lettura dei dati: " + task.getException());
                    }
        });

        // TODO: Bisogna creare la classe ProfessoreWithUtente e riprendere la stessa logica usata per lo studente
        firestoreDB.collection("Utenti").document("Professori").collection("Professori")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        db.professoreDao().deleteAll();
                        //Per salvare i dati in SQLite da Firestore
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        for (DocumentSnapshot document : documents) {
                            Professore professore = document.toObject(Professore.class); // Converte il documento in un oggetto Studente
                            db.professoreDao().insert(professore); // Chiama il metodo per l'inserimento o l'aggiornamento
                        }

                        Log.d("professori", String.valueOf(db.professoreDao().getAllProfessore()));
                    }else {
                        Log.d("Firestore", "Errore nella lettura dei dati: " + task.getException());
                    }
                });

        Intent LoginActivity = new Intent(this, LoginActivity.class);
        startActivity(LoginActivity);
        finish(); // Chiudi questa attività in modo che non possa essere tornata indietro.
    }

}