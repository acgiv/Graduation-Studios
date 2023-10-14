package com.laureapp.ui.card.Task;


import static com.laureapp.ui.controlli.ControlInput.showToast;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;


import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laureapp.R;
import com.laureapp.ui.card.Adapter.StudentAdapter;
import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteTesi;
import com.laureapp.ui.roomdb.entity.StudenteWithUtente;
import com.laureapp.ui.roomdb.entity.TaskTesi;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.entity.TesiProfessore;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.repository.StudenteRepository;
import com.laureapp.ui.roomdb.repository.UtenteRepository;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;


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
    SearchView searchView;
    ListView listView;

    StudentAdapter adapter;
    Utente utente;

    private List<StudenteWithUtente> studentList = new ArrayList<>();
    private List<StudenteWithUtente> filteredStudentList = new ArrayList<>();



    public TesistiFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireContext();
        args = getArguments();
        if (args != null) {
            ruolo = args.getString("ruolo");
            utente = Objects.requireNonNull(args.getSerializable("Utente", Utente.class));
            loadProfessorForUserId(utente.getId_utente());
        }
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tesisti, container, false);
        searchView = view.findViewById(R.id.searchTesistiView);
        listView = view.findViewById(R.id.listView);
        adapter = new StudentAdapter(context, studentList,filteredStudentList);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mNav = Navigation.findNavController(view);




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Quando il testo cambia, applica il filtro
                adapter.getFilter().filter(newText);
                return true;
            }
        });


        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // Recupera l'utente selezionato dalla lista
            StudenteWithUtente studenteWithUtente = studentList.get(position);

            // Crea un nuovo Bundle per passare i dati all'altro fragment


            // Aggiungi l'utente al Bundle
            args.putSerializable("Studente", studenteWithUtente.getStudente());
            args.putSerializable("Utente", studenteWithUtente.getUtente());

            // Passa il Bundle al fragment di destinazione
            mNav.navigate(R.id.action_fragment_tesisti_to_dettagli_tesista, args);
        });


    }



    private Task<Long> loadProfessorByUserId(Long id_utente) {
        return FirebaseFirestore.getInstance()
                .collection("Utenti/Professori/Professori")
                .whereEqualTo("id_utente", id_utente)
                .limit(1)
                .get()
                .continueWith(task -> {


                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0); // Otteniamo il primo documento
                        Professore professore = new Professore();
                        professore.setId_professore(doc.getLong("id_professore"));
                        professore.setId_utente(doc.getLong("id_utente"));
                        professore.setMatricola(doc.getString("matricola"));




                        return professore.getId_professore();
                    }
                    throw new NoSuchElementException("Utente non trovato con questa mail: " + id_utente);
                });
    }

    /**
     * Questo metodo mi permette di caricare da firestore la tabella studente_tesi dando come parametro l'id dello studente
     *
     * @param id_professore id dello studente nella tabella Studente
     * @return l'id della tesi associata allo studente tesista
     */
    private Task<Long> loadTesiProfessoreByProfessoreId(Long id_professore) {
        return FirebaseFirestore.getInstance()
                .collection("TesiProfessore")
                .whereEqualTo("id_professore", id_professore)
                .limit(1)
                .get()
                .continueWith(task -> {


                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0); // Otteniamo il primo documento
                        TesiProfessore tesiProfessore = new TesiProfessore();
                        tesiProfessore.setId_professore(doc.getLong("id_professore"));
                        tesiProfessore.setId_tesi_professore(doc.getLong("id_tesi_professore"));
                        tesiProfessore.setId_tesi(doc.getLong("id_tesi"));




                        return tesiProfessore.getId_tesi();
                    }
                    throw new NoSuchElementException("Utente non trovato con questa mail: " + id_professore);
                });
    }

    /**
     * Questo metodo mi permette di caricare da firestore la tabella tesi dando come parametro l'id della tesi nella tabella studenteTesi
     *
     * @param id_tesi id della tesi nella tabella StudenteTesi
     * @return l'id della tesi presente nella tabella studente_tesi
     */
    private Task<Long> loadTesiByIdTesiInTesiProf(Long id_tesi) {
        return FirebaseFirestore.getInstance()
                .collection("Tesi")
                .whereEqualTo("id_tesi", id_tesi)
                .limit(1)
                .get()
                .continueWith(task -> {


                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0); // Otteniamo il primo documento
                        Tesi tesi = new Tesi();
                        tesi.setId_tesi(doc.getLong("id_tesi"));
                        tesi.setId_vincolo(doc.getLong("id_vincolo"));
                        tesi.setAbstract_tesi(doc.getString("abstract_tesi"));
                        tesi.setTitolo(doc.getString("titolo"));
                        tesi.setTipologia(doc.getString("tipologia"));
                        tesi.setData_pubblicazione(Objects.requireNonNull(doc.getTimestamp("data_pubblicazione")).toDate());
                        tesi.setCiclo_cdl(doc.getString("ciclo_cdl"));




                        return tesi.getId_tesi();
                    }
                    throw new NoSuchElementException("Utente non trovato con questa mail: " + id_tesi);
                });
    }

    /**
     * Questo metodo mi permette di caricare da firestore la tabella tesi dando come parametro l'id della tesi nella tabella studenteTesi
     *
     * @param id_tesi id della tesi nella tabella StudenteTesi
     * @return l'id della tesi presente nella tabella studente_tesi
     */
    private Task<Long> loadTesiByIdTesiInTesiStud(Long id_tesi) {
        return FirebaseFirestore.getInstance()
                .collection("StudenteTesi")
                .whereEqualTo("id_tesi", id_tesi)
                .limit(1)
                .get()
                .continueWith(task -> {


                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0); // Otteniamo il primo documento
                        StudenteTesi studenteTesi = new StudenteTesi();
                        studenteTesi.setId_tesi(doc.getLong("id_tesi"));
                        studenteTesi.setId_studente(doc.getLong("id_studente"));
                        studenteTesi.setId_studente_tesi(doc.getLong("id_studente_tesi"));




                        return studenteTesi.getId_studente();
                    }
                    throw new NoSuchElementException("Utente non trovato con questa mail: " + id_tesi);
                });
    }

    /**
     * Questo metodo mi permette di caricare da firestore la tabella tesi dando come parametro l'id della tesi nella tabella studenteTesi
     *
     * @param id_studente id della tesi nella tabella StudenteTesi
     * @return l'id della tesi presente nella tabella studente_tesi
     */
    private Task<List<StudenteWithUtente>> loadStudByIdStudenteInStudenteTesi(Long id_studente) {
        // Ottieni gli id_studente dalla collezione "StudenteTesi"
        return FirebaseFirestore.getInstance()
                .collection("StudenteTesi")
                .whereEqualTo("id_studente", id_studente)
                .get()
                .continueWith(studenteTesiTask -> {
                    if (studenteTesiTask.isSuccessful() && !studenteTesiTask.getResult().isEmpty()) {
                        List<Long> studenteIds = new ArrayList<>();
                        for (QueryDocumentSnapshot studenteTesiDoc : studenteTesiTask.getResult()) {
                            studenteIds.add(studenteTesiDoc.getLong("id_studente"));
                        }
                        return studenteIds;
                    } else {
                        throw new NoSuchElementException("Nessun risultato trovato con questo id_studente in StudenteTesi: " + id_studente);
                    }
                })
                .continueWithTask(studenteIdsTask -> {
                    List<Long> studenteIds = studenteIdsTask.getResult();

                    // Ora ottieni le informazioni degli studenti e degli utenti corrispondenti
                    List<Task<StudenteWithUtente>> tasks = new ArrayList<>();
                    for (Long studenteId : studenteIds) {
                        Task<StudenteWithUtente> task = getStudenteWithUtente(studenteId);
                        tasks.add(task);
                    }

                    return Tasks.whenAll(tasks)
                            .continueWith(taskList -> {
                                if (taskList.isSuccessful()) {
                                    List<StudenteWithUtente> studenti = new ArrayList<>();
                                    for (Task<StudenteWithUtente> task : tasks) {
                                        if (task.isSuccessful()) {
                                            StudenteWithUtente studenteWithUtente = task.getResult();
                                            studenti.add(studenteWithUtente);
                                        }
                                    }
                                    return studenti;
                                } else {
                                    // Gestire l'errore se necessario
                                    Exception exception = taskList.getException();
                                    if (exception != null) {
                                        // Gestire l'eccezione
                                    }
                                    return new ArrayList<>(); // O un altro valore di fallback
                                }
                            });


                });
    }









    /**
     * Questo metodo permette di recuperare lo studente in base all'id dell'utente.
     * È il secondo metodo(2) utile per poter recuperare le tasks.
     * @param id_utente è l'id dell'utente uguale a quello dello studente
     */
    private void loadProfessorForUserId(Long id_utente) {
        loadProfessorByUserId(id_utente).addOnCompleteListener(professorTask -> {
            if (professorTask.isSuccessful()) {
                Long id_professore = professorTask.getResult();
                loadTesiProfessoreForProfessoreId(id_professore);
            } else {
                showToast(context, "Dati professore non caricati correttamente");

            }
        });
    }

    /**
     * Questo metodo permette di recuperare lo studente in base all'id dell'utente.
     * È il secondo metodo(2) utile per poter recuperare le tasks.
     * @param id_professore è l'id dell'utente uguale a quello dello studente
     */
    private void loadTesiProfessoreForProfessoreId(Long id_professore) {
        loadTesiProfessoreByProfessoreId(id_professore).addOnCompleteListener(tesiProfessoreTask -> {
            if (tesiProfessoreTask.isSuccessful()) {
                Long id_tesi_in_tesi_professore = tesiProfessoreTask.getResult();
                loadTesiForIdTesiInProfessoreTesi(id_tesi_in_tesi_professore);
            } else {
                showToast(context, "Dati tesi professore non caricati correttamente");

            }
        });
    }

    /**
     * Questo metodo permette di recuperare lo studente in base all'id dell'utente.
     * È il secondo metodo(2) utile per poter recuperare le tasks.
     * @param id_tesi_in_tesi_professore è l'id dell'utente uguale a quello dello studente
     */
    private void loadTesiForIdTesiInProfessoreTesi(Long id_tesi_in_tesi_professore) {
        loadTesiByIdTesiInTesiProf(id_tesi_in_tesi_professore).addOnCompleteListener(tesiTask -> {
            if (tesiTask.isSuccessful()) {
                Long id_tesi = tesiTask.getResult();
                loadStudTesiForIdTesi(id_tesi);
            } else {
                showToast(context, "Dati tesi professore non caricati correttamente");

            }
        });
    }

    /**
     * Questo metodo permette di recuperare lo studente in base all'id dell'utente.
     * È il secondo metodo(2) utile per poter recuperare le tasks.
     * @param id_tesi è l'id dell'utente uguale a quello dello studente
     */
    private void loadStudTesiForIdTesi(Long id_tesi) {
        loadTesiByIdTesiInTesiStud(id_tesi).addOnCompleteListener(studTesiTask -> {
            if (studTesiTask.isSuccessful()) {
                Long id_studente = studTesiTask.getResult();
                loadStudForIdStudenteInStudenteTesi(id_studente);
            } else {
                showToast(context, "Dati tesi professore non caricati correttamente");

            }
        });
    }

    /**
     * Questo metodo permette di recuperare lo studente in base all'id dell'utente.
     * È il secondo metodo(2) utile per poter recuperare le tasks.
     * @param id_studente è l'id dell'utente uguale a quello dello studente
     */
    private void loadStudForIdStudenteInStudenteTesi(Long id_studente) {
        loadStudByIdStudenteInStudenteTesi(id_studente).addOnCompleteListener(studTask -> {
            if (studTask.isSuccessful()) {
                studentList.clear();
                addStudentsToList(studTask.getResult());
            } else {
                showToast(context, "Dati tesi professore non caricati correttamente");

            }
        });
    }

    /**
     * Questo metodo aggiunge le tasks alla lista delle task e aggiorna l'adapter permettendo
     * la visualizzazione delle tasks.
     * @param tasks è la lista delle tasks
     */
    private void addStudentsToList(List<StudenteWithUtente> tasks) {
        for (StudenteWithUtente studenteWithUtenteTask : tasks) {
            if (studenteWithUtenteTask != null) {
                studentList.add(studenteWithUtenteTask);
            }
        }
        adapter.notifyDataSetChanged();
    }



    public Task<StudenteWithUtente> getStudenteWithUtente(Long studenteId) {
        Task<Studente> studenteTask = loadStudenteById(studenteId);

        return studenteTask.continueWithTask(task -> {
            if (task.isSuccessful()) {
                Studente studente = task.getResult();
                return loadUtenteByStudente(studente).continueWith(utenteTask -> {
                    if (utenteTask.isSuccessful()) {
                        Utente utente = utenteTask.getResult();
                        return new StudenteWithUtente(studente, utente);
                    } else {
                        throw new NoSuchElementException("Utente non trovato per lo studente con id: " + studenteId);
                    }
                });
            } else {
                throw new NoSuchElementException("Studente non trovato con id: " + studenteId);
            }
        });
    }


    private Task<Studente> loadStudenteById(Long studenteId) {
        // Implementa la logica per caricare uno studente per id da dove sono conservati i dati (database, API, ecc.)
        // Restituisci il risultato come un Task
        // Ad esempio:
        StudenteRepository stRep = new StudenteRepository(context);
        Studente studente = stRep.findAllById(studenteId);
        return Tasks.forResult(studente);
    }

    private Task<Utente> loadUtenteByStudente(Studente studente) {
        // Implementa la logica per caricare l'utente associato a uno studente da dove sono conservati i dati
        // Restituisci il risultato come un Task
        // Ad esempio:
        UtenteRepository utRep = new UtenteRepository(context);
        Utente utente = utRep.findAllById(studente.getId_utente());
        return Tasks.forResult(utente);
    }




}
