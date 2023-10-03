package com.laureapp.ui.card.Task;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.laureapp.R;
import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteWithUtente;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Questa classe gestisce la lista degli studenti mostrata dopo aver cliccato la card
 * Tesisti, lato professore. Questa lista viene filtrata in base al testo inserito dall'utente.
 *
 */
public class TesistiFragment extends Fragment {

    private NavController mNav;
    String ruolo;
    Context context;
    Bundle args;
    private FirebaseAuth mAuth;

    private List<StudenteWithUtente> studentList = new ArrayList<>();

    public TesistiFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = requireContext();
        args = getArguments();
        if (args != null) {
            ruolo = args.getString("ruolo");
            Log.d("ruolo ", ruolo);
        }
        View view = inflater.inflate(R.layout.fragment_tesisti, container, false);

        AutoCompleteTextView editText = view.findViewById(R.id.searchTextView);
        StudentAdapter adapter = new StudentAdapter(context, studentList);
        editText.setAdapter(adapter);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        CollectionReference usersRef = firestoreDB.collection("Utenti");
        mNav = Navigation.findNavController(view);
        ListView listView = view.findViewById(R.id.listView);
        StudentAdapter adapter = new StudentAdapter(context, studentList);


        usersRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                            studentList.clear(); // Cancella la lista esistente

                            // Qui ottieni gli studenti con utenti associati
                            for (DocumentSnapshot userSnapshot : queryDocumentSnapshots) {
                                Utente utente = userSnapshot.toObject(Utente.class); // Ottieni lo studente da Firestore

                                if (utente != null) {
                                    // Crea un oggetto StudenteWithUtente e imposta lo studente
                                    StudenteWithUtente studenteWithUtente = new StudenteWithUtente();
                                    studenteWithUtente.setUtente(utente);

                                    loadStudenteById(utente.getId_utente()).addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Studente studente = task.getResult();

                                            if (studente != null) {
                                                // Imposta lo studente ottenuto nell'oggetto StudenteWithUtente
                                                studenteWithUtente.setStudente(studente);

                                                // Aggiungi studenteWithUtente alla lista
                                                studentList.add(studenteWithUtente);
                                                adapter.notifyDataSetChanged();

                                                Log.d("Lista Studenti Tesisti", studenteWithUtente.getUtente().toString());
                                                Log.d("Lista StudentiWithUtenti", studenteWithUtente.toString());
                                            } else {
                                                // Gestisci il caso in cui lo studente sia null
                                            }
                                        } else {
                                            // Gestisci l'errore se la promessa non è stata completata con successo
                                            Exception exception = task.getException();
                                            if (exception != null) {
                                                // Log o gestione dell'errore
                                            }
                                        }
                                    });
                                }
                            }
                        });

                            listView.setAdapter(adapter);


        listView.setOnItemClickListener((parent, view1, position, id) -> {
            mNav.navigate(R.id.action_fragment_tesisti_to_dettagli_tesista);
            Log.d("Tesi", "cliccato il tesista");
        });

    }


    /**
     * Questo metodo mi permette di caricare da firestore gli id delle tesi dando come parametro l'id dello studente
     *
     * @param id_utente
     * @return una lista di tipo Long contenente gli id delle tesi associate allo studente
     */
    private Task<Studente> loadStudenteById(Long id_utente) {
        return FirebaseFirestore.getInstance()
                .collection("Utenti").document("Studenti").collection("Studenti")
                .whereEqualTo("id_utente", id_utente)
                .limit(1)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0); // Otteniamo il primo documento
                        Studente studente = new Studente();
                        studente.setId_utente(doc.getLong("id_utente"));
                        studente.setId_studente(doc.getLong("id_studente"));
                        studente.setMatricola(doc.getLong("matricola"));
                        studente.setEsami_mancanti(Math.toIntExact(doc.getLong("esami_mancanti")));
                        studente.setMedia(Math.toIntExact(doc.getLong("media")));
                        return studente;
                    }
                    throw new NoSuchElementException("Studente non trovato con l'ID utente: " + id_utente);
                });
    }



}





