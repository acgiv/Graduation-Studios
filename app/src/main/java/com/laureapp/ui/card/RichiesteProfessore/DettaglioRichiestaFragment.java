package com.laureapp.ui.card.RichiesteProfessore;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.laureapp.R;
import com.laureapp.ui.card.TesiStudente.ConfermaRichiestaDialog;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Questa classe rappresenta un frammento per visualizzare i dettagli di una richiesta di tesi da uno studente.
 */
public class DettaglioRichiestaFragment extends Fragment {
    String titolo;
    Long idRichiestaTesi;

    Long idStudente;
    Context context;
    List<Studente> studenti;
    List<Utente> utenti;
    StudenteModelView studenteModelView = new StudenteModelView(context);
    UtenteModelView utenteView = new UtenteModelView(context);
    Long idTesi;

    boolean soddisfaRequisiti;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dettaglio_richieste_tesi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        TextView richiestaTextView = view.findViewById(R.id.insertTextViewIdRichiesta);
        TextView titoloTesiTextView = view.findViewById(R.id.insertTextViewTitoloTesi);
        TextView matricolaTextView = view.findViewById(R.id.insertTextViewMatricolaStudente);
        TextView nomeTextView = view.findViewById(R.id.insertTextViewNomeStudente);
        TextView cognomeTextView = view.findViewById(R.id.insertTextViewCognomeStudente);
        TextView emailTextView = view.findViewById(R.id.insertTextViewEmailStudente);
        TextView soddisfaTextView = view.findViewById(R.id.insertTextViewAvvisoVincoli);
        if (args != null) { //se non sono null

            titolo = (String) args.getSerializable("Titolo"); //prendo la tesi dagli args
            idRichiestaTesi = (Long) args.getSerializable("idRichiestaTesi");
            idStudente = (Long) args.getSerializable("idStudente");
            soddisfaRequisiti = (Boolean) args.getSerializable("Soddisfa");

            richiestaTextView.setText(idRichiestaTesi.toString());
            titoloTesiTextView.setText(titolo);

            context = getContext();

            studenti = studenteModelView.getAllStudente();
            utenti = utenteView.getAllUtente();

            for (Studente studente : studenti) {
                if (studente.getId_studente() == idStudente) {
                    matricolaTextView.setText(studente.getMatricola().toString());
                    Long id_utente = studente.getId_utente();
                    for(Utente utente: utenti){
                        if(utente.getId_utente() == id_utente){

                            nomeTextView.setText(utente.getNome());
                            cognomeTextView.setText(utente.getCognome());
                            emailTextView.setText(utente.getEmail());
                            if(soddisfaRequisiti == true){
                                soddisfaTextView.setText("Lo studente soddisfa tutti i vincoli della tesi");
                                soddisfaTextView.setTextColor(Color.parseColor("#006400"));


                            }else{
                                soddisfaTextView.setText("Lo studente non soddisfa tutti i vincoli della tesi");

                            }


                        }
                    }

                }

            }

            Button accettaButton = view.findViewById(R.id.AccettaRichiesta);
            accettaButton.setOnClickListener(view1 -> {

                createStudenteTesi(idStudente,titolo);
                changeStatoAccettata(idRichiestaTesi);
                Navigation.findNavController(view).navigate(R.id.action_dettaglioRichiestaFragment_to_richiesteProfessoreFragment);


            });

            Button rifiutaButton = view.findViewById(R.id.RifiutaRichiesta);
            rifiutaButton.setOnClickListener(view2 -> {

                changeStatoRifiutata(idRichiestaTesi);
                Navigation.findNavController(view).navigate(R.id.action_dettaglioRichiestaFragment_to_richiesteProfessoreFragment);
                //da implementare invio segnalazione allo studente


            });
        }

    }

    /**
     * Cambia lo stato di una richiesta di tesi in "Accettata" nel database Firestore.
     *
     * @param idRichiestaTesi l'ID della richiesta di tesi da accettare.
     */
    public void changeStatoAccettata(Long idRichiestaTesi){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference richiesteRef = db.collection("RichiesteTesi");

        Query query = richiesteRef.whereEqualTo("id_richiesta_tesi", idRichiestaTesi);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Ottieni il documento corrispondente (potrebbe esserci più di uno, ma ne usiamo il primo)
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                    // Estrai l'ID del documento
                    String documentId = documentSnapshot.getId();

                    // Incrementa le visualizzazioni utilizzando l'ID del documento
                    richiesteRef
                            .document(documentId)
                            .update("stato", "Accettata");
                }
            } else {
                Log.e("Firestore Error", "Error querying Tesi collection", task.getException());
            }
        });
    }

    /**
     * Cambia lo stato di una richiesta di tesi in "Rifiutata" nel database Firestore.
     *
     * @param idRichiestaTesi l'ID della richiesta di tesi da rifiutare.
     */
    public void changeStatoRifiutata(Long idRichiestaTesi){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference richiesteRef = db.collection("RichiesteTesi");

        Query query = richiesteRef.whereEqualTo("id_richiesta_tesi", idRichiestaTesi);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Ottieni il documento corrispondente (potrebbe esserci più di uno, ma ne usiamo il primo)
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                    // Estrai l'ID del documento
                    String documentId = documentSnapshot.getId();

                    // Incrementa le visualizzazioni utilizzando l'ID del documento
                    richiesteRef
                            .document(documentId)
                            .update("stato", "Rifiutata");
                }
            } else {
                Log.e("Firestore Error", "Error querying Tesi collection", task.getException());
            }
        });
    }

    /**
     * Cerca un'ID di tesi nel database Firestore in base al titolo specificato.
     *
     * @param titolo il titolo della tesi da cercare.
     * @return un oggetto di tipo Task<Long> che rappresenta un'operazione asincrona per trovare l'ID della tesi.
     */
    private Task<Long> findTesiIdByTitolo(String titolo) {
        final TaskCompletionSource<Long> tcs = new TaskCompletionSource<>();

        CollectionReference tesiCollection = FirebaseFirestore.getInstance().collection("Tesi");
        Query query = tesiCollection.whereEqualTo("titolo", titolo);

        query.get().continueWith(new Continuation<QuerySnapshot, Long>() {
            @Override
            public Long then(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot result = task.getResult();
                    if (result != null && !result.isEmpty()) {
                        for (QueryDocumentSnapshot doc : result) {
                            Long idTesi = doc.getLong("id_tesi");
                            tcs.setResult(idTesi);
                            return idTesi;
                        }
                    }
                }
                tcs.setResult(null); // Nessuna corrispondenza trovata
                return null;
            }
        });

        return tcs.getTask();
    }

    /**
     * Crea una relazione tra uno studente e una tesi nel database Firestore.
     *
     * @param idStudente l'ID dello studente associato alla tesi.
     * @param titolo il titolo della tesi da associare allo studente.
     */
    private void createStudenteTesi(Long idStudente, String titolo) {
        findTesiIdByTitolo(titolo).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Long idTesi = task.getResult();
                // Ora abbiamo l'idTesi, possiamo procedere a ottenere il massimo id_studente_tesi
                getMaxIdStudenteTesiAndCreateStudenteTesi(idStudente, idTesi);
            }
        });
    }

    /**
     * Ottiene il massimo ID di studente_tesi dal database Firestore e crea una nuova relazione studente-tesi.
     *
     * @param idStudente l'ID dello studente da associare alla tesi.
     * @param idTesi l'ID della tesi da associare allo studente.
     */
    private void getMaxIdStudenteTesiAndCreateStudenteTesi(Long idStudente, Long idTesi) {
        // Ottieni il massimo id_studente_tesi da Firestore
        findMaxIdStudenteTesi(new MaxStudenteTesiCallback() {
            @Override
            public void onCallback(Long maxIdStudenteTesi) {
                Long newIdStudenteTesi = maxIdStudenteTesi + 1; // Corrected variable name

                // Crea il documento "studenteTesi" con l'id incrementato
                Map<String, Object> studenteTesi = new HashMap<>();
                studenteTesi.put("id_tesi", idTesi);
                studenteTesi.put("id_studente", idStudente);
                studenteTesi.put("id_studente_tesi", newIdStudenteTesi);

                // Aggiungi il nuovo documento "studenteTesi" a Firestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference studenteTesiRef = db.collection("StudenteTesi");
                studenteTesiRef.add(studenteTesi)
                        .addOnSuccessListener(documentReference -> {
                            // La richiesta di tesi è stata aggiunta con successo
                        })
                        .addOnFailureListener(e -> {
                            // Si è verificato un errore nell'aggiunta del documento "studenteTesi"
                        });
            }
        });
    }

    /**
     * Interfaccia per il callback del massimo ID di studente_tesi.
     */
    private interface MaxStudenteTesiCallback {
        void onCallback(Long maxIdStudenteTesi);
    }

    /**
     * Trova il massimo ID di studente_tesi dal database Firestore.
     *
     * @param callback il callback per restituire il massimo ID di studente_tesi.
     */
    private void findMaxIdStudenteTesi(MaxStudenteTesiCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference studenteTesiRef = db.collection("StudenteTesi");

        studenteTesiRef
                .orderBy("id_studente_tesi", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        Long maxIdStudenteTesi = queryDocumentSnapshots.getDocuments().get(0).getLong("id_studente_tesi");
                        if (maxIdStudenteTesi != null) {
                            callback.onCallback(maxIdStudenteTesi);
                        } else {
                            // Nessun valore trovato, inizia da 1
                            callback.onCallback(1L);
                        }
                    } else {
                        // Nessun documento trovato, inizia da 1
                        callback.onCallback(1L);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore Error", "Error finding max id_studente_tesi", e);
                });
    }

}
