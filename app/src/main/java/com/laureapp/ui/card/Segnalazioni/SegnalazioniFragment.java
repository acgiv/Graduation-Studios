package com.laureapp.ui.card.Segnalazioni;

import static com.laureapp.ui.controlli.ControlInput.showToast;
import static com.laureapp.ui.home.HomeFragment.getEmailFromSharedPreferences;
import static com.laureapp.ui.roomdb.Converters.stringToTimestamp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laureapp.R;
import com.laureapp.databinding.FragmentTaskBinding;
import com.laureapp.ui.card.Adapter.SegnalazioniAdapter;
import com.laureapp.ui.roomdb.QueryFirestore;
import com.laureapp.ui.roomdb.entity.Segnalazione;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteTesi;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Questa classe rappresenta un fragment per la gestione delle segnalazioni. Mostra una lista di segnalazioni
 * e offre funzionalità per aggiungere nuove segnalazioni. La lista delle segnalazioni è associata all'utente
 * attualmente loggato e può variare in base al ruolo (Studente o Professore).
 */
public class SegnalazioniFragment extends Fragment {

    private NavController mNav;
    String ruolo;
    Context context;
    Bundle args;
    private FirebaseAuth mAuth;
    ListView listView;
    AlertDialog alertDialog;

    static SegnalazioniAdapter adapter;
    Utente utente;
    private static FirebaseFirestore db;

    private static List<Segnalazione> segnalazioniList = new ArrayList<>();
    ImageButton addButton;
    StudenteModelView studenteView = new StudenteModelView(context);
    UtenteModelView utenteView = new UtenteModelView(context);
    Long id_utente;
    String inputData;

    public SegnalazioniFragment() {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireContext();
        args = getArguments();

        if (args != null) {
            ruolo = args.getString("ruolo");
        }

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_segnalazioni, container, false);
        listView = view.findViewById(R.id.listSegnalazioniView);
        listView.setNestedScrollingEnabled(true);

        addButton = view.findViewById(R.id.add_segnalazioni_ImageButton);
        if(StringUtils.equals(ruolo,"Ospite")){
            addButton.setVisibility(View.GONE);
        }
        db = FirebaseFirestore.getInstance();


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mNav = Navigation.findNavController(view);
        if(args != null) {
            adapter = new SegnalazioniAdapter(context, segnalazioniList, mNav, args);
            if (StringUtils.equals("Professore", ruolo)) {
                String email = args.getString("emailStudente");

                id_utente = utenteView.getIdUtente(email);
                loadStudentForUserId(id_utente);
                addButton.setVisibility(View.VISIBLE);
                addButton.setOnClickListener(view1 ->
                        showInputDialog()
                );
                addButton.setVisibility(View.GONE);
            } else if (StringUtils.equals("Studente", ruolo)) {
                String email = getEmailFromSharedPreferences(context);

                id_utente = utenteView.getIdUtente(email);
                loadStudentForUserId(id_utente);
                addButton.setVisibility(View.VISIBLE);
                addButton.setOnClickListener(view1 ->
                        showInputDialog()
                );;

                id_utente = utenteView.getIdUtente(email);
                loadStudentForUserId(id_utente);
                addButton.setVisibility(View.VISIBLE);
                addButton.setOnClickListener(view1 ->
                        showInputDialog()
                );
            } else if (StringUtils.equals("Ospite", args.getString("ruolo"))) {
                String email = getEmailFromSharedPreferences(context);
                id_utente = utenteView.getIdUtente(email);
                loadStudentForUserId(id_utente);
            }
            listView.setAdapter(adapter);
        }

    }

    /**
     * Questo metodo mostra un dialog per inserire una nuova segnalazione. Il dialog include un campo di testo
     * in cui l'utente può inserire il titolo della segnalazione. Quando l'utente preme il pulsante "Ok" nel dialog,
     * il testo inserito viene recuperato e quindi aggiunto a Firestore come nuova segnalazione associata all'utente
     * attualmente loggato. Il dialog include anche un pulsante "Annulla" per chiudere il dialog senza salvare alcuna segnalazione.
     */
    public void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.nuovaSegnalazione);

        // Includi il layout XML personalizzato
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.add_segn_popup, null);
        builder.setView(view);

        EditText editTextTitolo = view.findViewById(R.id.editTextTitoloSegnalazione);


        // Aggiungi un ascoltatore personalizzato al pulsante OK
        builder.setPositiveButton("Ok", (dialog, which) -> {
            inputData = editTextTitolo.getText().toString();


            //Aggiungo la task a Firestore in base all'utente loggato
            addSegnalazioniToFirestoreLast(id_utente, inputData);
            //Log.d("id_utente_lista", utente.getId_utente().toString());
        });

        // Imposta un ascoltatore per il pulsante Annulla
        builder.setNegativeButton("Annulla", (dialog, which) -> {
            // Chiudi il dialog solo dopo che l'utente ha premuto Annulla
            dialog.dismiss();
        });


        // Crea il dialog
        alertDialog = builder.create();

        // Mostra il dialog
        alertDialog.show();

    }


    /**
     * Questo metodo mi permette di caricare da firestore gli studenti dando come parametro l'id dell'utente
     *
     * @param id_utente id dell'utente nella tabella Studente
     * @return l'id dello studente associati all' utente
     */
    private Task<Long> loadStudentByUserId(Long id_utente) {
        return FirebaseFirestore.getInstance()
                .collection("Utenti/Studenti/Studenti")
                .whereEqualTo("id_utente", id_utente)
                .get()
                .continueWith(task -> {


                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0); // Otteniamo il primo documento
                        Studente studente = new Studente();
                        studente.setId_studente(doc.getLong("id_studente"));
                        studente.setId_utente(doc.getLong("id_utente"));
                        studente.setMatricola(doc.getLong("matricola"));

                        Long mediaValue = doc.getLong("media");
                        if (mediaValue != null) {
                            studente.setMedia(mediaValue.intValue());
                        }

                        Long esamiMancantiValue = doc.getLong("esami_mancanti");
                        if (esamiMancantiValue != null) {
                            studente.setEsami_mancanti(esamiMancantiValue.intValue());
                        }




                        return studente.getId_studente();
                    }
                    throw new NoSuchElementException("Utente non trovato con questa mail: " + id_utente);
                });
    }

    /**
     * Questo metodo mi permette di caricare da firestore la tabella studente_tesi dando come parametro l'id dello studente
     *
     * @param id_studente id dello studente nella tabella Studente
     * @return l'id della tesi associata allo studente tesista
     */
    private Task<Long> loadStudenteTesiByStudenteId(Long id_studente) {
        return FirebaseFirestore.getInstance()
                .collection("StudenteTesi")
                .whereEqualTo("id_studente", id_studente)
                .get()
                .continueWith(task -> {


                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0); // Otteniamo il primo documento
                        StudenteTesi studenteTesi = new StudenteTesi();
                        studenteTesi.setId_studente(doc.getLong("id_studente"));
                        studenteTesi.setId_studente_tesi(doc.getLong("id_studente_tesi"));
                        studenteTesi.setId_tesi(doc.getLong("id_tesi"));
                        Log.d("id_studente_tesi", studenteTesi.getId_studente_tesi().toString());



                        return studenteTesi.getId_studente_tesi();
                    }
                    throw new NoSuchElementException("Utente non trovato con questa mail: " + id_studente);
                });
    }




    /**
     * Questo metodo mi permette di caricare da firestore le task dando come parametro l'id della tesi
     *
     * @param id_studente_tesi id della tesi nella tabella Tesi
     * @return la lista delle task in base all'id della tesi fornito
     */
    private Task<List<Segnalazione>> loadSegnalazioniById(Long id_studente_tesi) {
        return FirebaseFirestore.getInstance()
                .collection("Segnalazioni")
                .whereEqualTo("id_studente_tesi", id_studente_tesi)
                .get()
                .continueWith(task -> {
                    List<Segnalazione> segnalazioneList = new ArrayList<>();

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Segnalazione segnalazione = new Segnalazione();
                            segnalazione.setId_segnalazione(doc.getLong("id_segnalazione"));
                            segnalazione.setId_studente_tesi(doc.getLong("id_studente_tesi"));
                            segnalazione.setTitolo(doc.getString("titolo"));

                            segnalazioneList.add(segnalazione);
                        }

                        return segnalazioneList;
                    } else {
                        throw new NoSuchElementException("Studente non trovato con l'ID utente: " + id_studente_tesi);
                    }
                });
    }


    /*
        METODI PER LA LETTURA DEI DATI E LA LORO VISUALIZZAZIONE NELL'ADAPTER
     */



    /**
     * Questo metodo permette di recuperare lo studente in base all'id dell'utente.
     * È il secondo metodo(2) utile per poter recuperare le tasks.
     * @param id_utente è l'id dell'utente uguale a quello dello studente
     */
    private void loadStudentForUserId(Long id_utente) {
        loadStudentByUserId(id_utente).addOnCompleteListener(studentTask -> {
            if (studentTask.isSuccessful()) {
                Long id_studente = studentTask.getResult();
                loadStudenteTesiForStudenteId(id_studente);
            } else {
                segnalazioniList.clear();
                adapter.notifyDataSetChanged();
                showToast(context, "Dati studenti non caricati correttamente");

            }
        });
    }

    /**
     * Questo metodo permette di accedere alla tabella StudenteTesi dalla tabella studente in base all'id dello studente.
     * E' il terzo metodo(3) utile per poter recuperare le tasks.
     * @param id_studente è l'id dello studente nella tabella StudenteTesi
     */
    private void loadStudenteTesiForStudenteId(Long id_studente) {
        loadStudenteTesiByStudenteId(id_studente).addOnCompleteListener(studenteTesiTask -> {
            if (studenteTesiTask.isSuccessful()) {
                Long id_tesi_in_studente_tesi = studenteTesiTask.getResult();
                loadSegnForStudTesiId(id_tesi_in_studente_tesi);
            } else {
                showToast(context, "Dati StudenteTesi non caricati correttamente");
            }
        });
    }

    /**
     * Questo metodo permette di caricare le task in base all'id della tesi
     * @param id_stud_tesi_in_studente_tesi è l'id della tesi nelle tasks
     */
    private void loadSegnForStudTesiId(Long id_stud_tesi_in_studente_tesi) {
        loadSegnalazioniById(id_stud_tesi_in_studente_tesi).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                segnalazioniList.clear();
                addSegnalazioniToList(task.getResult());
            } else {
                segnalazioniList.clear();
                adapter.notifyDataSetChanged();
                showToast(context, "Dati task non caricati correttamente");
            }
        });
    }

    /**
     * Questo metodo aggiunge le tasks alla lista delle task e aggiorna l'adapter permettendo
     * la visualizzazione delle tasks.
     * @param tasks è la lista delle tasks
     */
    private static void addSegnalazioniToList(List<Segnalazione> tasks) {
        for (Segnalazione segnalazione : tasks) {
            if (segnalazione != null) {
                segnalazioniList.add(segnalazione);
                Log.d("id_studente_in_studente_tesi", segnalazione.getId_studente_tesi().toString());

            }
        }
        adapter.notifyDataSetChanged();
    }

/*
    Metodi per l'aggiunta e visualizzazione di una nuova task
 */

    /**
     * Questo metodo carica le informazioni dello studente associato all'utente specificato e aggiunge la segnalazione alla tesi dello studente.
     * Richiede l'ID dell'utente attualmente loggato e il testo della segnalazione da aggiungere. Il testo della segnalazione viene associato come
     * dettaglio alla tesi dello studente.
     *
     * @param id_utente L'ID dell'utente attualmente loggato.
     * @param inputData Il testo della segnalazione da aggiungere.
     */
    private void addSegnalazioniToFirestoreLast(Long id_utente, String inputData) {

        // Ottieni l'ID della tesi in base all'ID utente fornito
        loadStudentAndAddSegnalazione(id_utente, inputData);
    }

    /**
     * Questo metodo carica le informazioni dello studente associato all'ID utente specificato e aggiunge la segnalazione alla tesi dello studente.
     * Richiede l'ID dell'utente attualmente loggato e il testo della segnalazione da aggiungere. Il metodo chiama il metodo `loadStudentByUserId`
     * per ottenere l'ID dello studente associato all'utente e successivamente chiama `loadStudenteTesiAndAddSegnalazione` per completare il processo
     * di aggiunta della segnalazione alla tesi dello studente.
     *
     * @param id_utente L'ID dell'utente attualmente loggato.
     * @param inputData Il testo della segnalazione da aggiungere.
     */
    private void loadStudentAndAddSegnalazione(Long id_utente, String inputData) {
        loadStudentByUserId(id_utente).addOnCompleteListener(studentTask -> {
            if (studentTask.isSuccessful()) {
                Long id_studente = studentTask.getResult();

                loadStudenteTesiAndAddSegnalazione(id_studente, inputData);
            } else {
                showToast(context, "Lettura dati studenti e aggiunta task non avvenuta correttamente");
            }
        });
    }

    /**
     * Questo metodo carica le informazioni della tesi dello studente associata all'ID dello studente specificato e aggiunge la segnalazione alla tesi.
     * Richiede l'ID dello studente e il testo della segnalazione da aggiungere. Il metodo chiama il metodo `loadStudenteTesiByStudenteId`
     * per ottenere l'ID della tesi dello studente associata al relativo studente e successivamente chiama `addSegnalazioneToFirestore` per completare
     * il processo di aggiunta della segnalazione alla tesi.
     *
     * @param id_studente L'ID dello studente a cui è associata la tesi.
     * @param inputData Il testo della segnalazione da aggiungere.
     */
    private void loadStudenteTesiAndAddSegnalazione(Long id_studente, String inputData) {
        loadStudenteTesiByStudenteId(id_studente).addOnCompleteListener(studenteTesiTask -> {
            if (studenteTesiTask.isSuccessful()) {
                Long id_studente_tesi_in_studente_tesi = studenteTesiTask.getResult();
                addSegnalazioneToFirestore(id_studente_tesi_in_studente_tesi, inputData);
            } else {
                showToast(context, "Dati StudenteTesi e aggiunta task non avvenuta correttamente");
            }
        });
    }

    /**
     * Questo metodo aggiunge una nuova segnalazione al Firestore. Richiede l'ID della tesi dello studente associata
     * alla segnalazione e il testo della segnalazione. Il metodo crea un nuovo documento nel Firestore con le informazioni
     * della segnalazione, tra cui un titolo e un ID univoco per la segnalazione.
     *
     * @param id_studente_tesi L'ID della tesi dello studente a cui è associata la segnalazione.
     * @param inputData Il testo della segnalazione da aggiungere.
     */
    private void addSegnalazioneToFirestore(Long id_studente_tesi, String inputData) {
        CollectionReference segnalazioniRef = db.collection("Segnalazioni");
        QueryFirestore queryFirestore = new QueryFirestore();
        Map<String, Object> segnalazioniData = new HashMap<>();
        queryFirestore.trovaIdSegnMax(context)
                .thenAccept(idMax -> {
                    idMax = idMax + 1L;
                    segnalazioniData.put("id_segnalazione", idMax);
                    segnalazioniData.put("titolo", inputData);
                    segnalazioniData.put("id_studente_tesi", id_studente_tesi);

                    // Supponendo che 'taskRef' sia un oggetto valido di tipo CollectionReference
                    Long finalIdMax = idMax;
                    segnalazioniRef.document().set(segnalazioniData).addOnSuccessListener(
                            aVoid -> addSegnalazioneToList(finalIdMax, inputData, id_studente_tesi)
                    ).addOnFailureListener(e -> {
                        // Gestisci l'errore
                    });
                });
    }

    /**
     * Metodo per l'aggiunta di una nuova task alla lista di partenza
     * @param id_segnalazione id della task da aggiungere alla lista
     * @param inputData è il titolo della task
     * @param id_studente_tesi tesi associata alla task
     */
    private void addSegnalazioneToList(Long id_segnalazione, String inputData, Long id_studente_tesi) {

        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setId_segnalazione(id_segnalazione);
        segnalazione.setTitolo(inputData);
        segnalazione.setId_studente_tesi(id_studente_tesi);

        // Aggiungi taskTesi alla lista
        segnalazioniList.add(segnalazione);
        adapter.notifyDataSetChanged();
    }

}