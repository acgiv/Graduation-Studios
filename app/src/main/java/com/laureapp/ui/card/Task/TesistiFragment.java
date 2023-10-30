package com.laureapp.ui.card.Task;


import static com.laureapp.ui.controlli.ControlInput.showToast;
import static com.laureapp.ui.home.HomeFragment.getEmailFromSharedPreferences;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;


import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laureapp.R;
import com.laureapp.ui.MainActivity;
import com.laureapp.ui.card.Adapter.StudentAdapter;
import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteTesi;
import com.laureapp.ui.roomdb.entity.StudenteWithUtente;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.entity.TesiProfessore;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.repository.StudenteRepository;
import com.laureapp.ui.roomdb.repository.UtenteRepository;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;
import com.laureapp.ui.roomdb.viewModel.sharedDataModelView.SharedDataModelView;

import java.util.ArrayList;
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
    ListView listView;

    StudentAdapter adapter;
    String cognomeTesistaCercato;
    UtenteModelView utenteModelView;
    Long id_utente;
    SharedDataModelView sharedViewModel;




    private List<StudenteWithUtente> studentList = new ArrayList<>();
    private String tesistaCercato = "";



    public TesistiFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireContext();
        args = getArguments();
        if (args != null) {
            ruolo = args.getString("ruolo");
        }


        sharedViewModel = new ViewModelProvider(this).get(SharedDataModelView.class);

        utenteModelView = new UtenteModelView(context);

        String email = getEmailFromSharedPreferences(context); // Chiamata al metodo per ottenere la mail

        id_utente = utenteModelView.getIdUtente(email);



        loadProfessorForUserId(id_utente);

        mAuth = FirebaseAuth.getInstance();




    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tesisti, container, false);
        listView = view.findViewById(R.id.listView);
        adapter = new StudentAdapter(context, studentList);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mNav = Navigation.findNavController(view);


        listView.setAdapter(adapter);


        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // Recupera l'utente selezionato dalla lista
            StudenteWithUtente studenteWithUtente = studentList.get(position);

            // Crea un nuovo Bundle per passare i dati all'altro fragment




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
                        professore.setMatricola(doc.getLong("matricola"));




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
     * @param id_tesi_in_tesi_prof id della tesi nella tabella StudenteTesi
     * @return l'id della tesi presente nella tabella studente_tesi
     */
    private Task<Long> loadTesiByIdTesiInTesiProf(Long id_tesi_in_tesi_prof) {
        return FirebaseFirestore.getInstance()
                .collection("Tesi")
                .whereEqualTo("id_tesi", id_tesi_in_tesi_prof)
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
                    throw new NoSuchElementException("Utente non trovato con questa mail: " + id_tesi_in_tesi_prof);
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
                .collection("StudenteTesi").whereEqualTo("id_studente", id_studente)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        List<Long> studenteIds = new ArrayList<>();
                        for (QueryDocumentSnapshot studenteTesiDoc : task.getResult()) {
                            studenteIds.add(studenteTesiDoc.getLong("id_studente"));
                        }
                        return studenteIds;
                    } else {
                        throw new NoSuchElementException("Nessun risultato trovato con questo id_studente in StudenteTesi: " + id_studente);
                    }
                })
                .continueWithTask(studenteIdsTask -> {
                    List<Long> studenteIds = studenteIdsTask.getResult();

                    // Ottieni le informazioni degli studenti e degli utenti corrispondenti
                    List<Task<StudenteWithUtente>> tasks = new ArrayList<>();
                    for (Long studenteId : studenteIds) {
                        Task<StudenteWithUtente> task = getStudenteWithUtente(studenteId);
                        tasks.add(task);
                    }
                    Log.d("COSAEQUI", tasks.toString());


                    return Tasks.whenAllSuccess(tasks).continueWith(resultTask -> {
                        // Otteniamo una lista di StudenteWithUtente
                        List<StudenteWithUtente> studentiWithUtenti = new ArrayList<>();
                        for (Object studenteWithUtenteTask : resultTask.getResult()) {
                            studentiWithUtenti.add((StudenteWithUtente) studenteWithUtenteTask);
                        }
                        Log.d("MOMOMOM", studentiWithUtenti.toString());
                        return studentiWithUtenti;
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
                showToast(context, "Dati tesi professore 1 non caricati correttamente");

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
                showToast(context, "Dati tesi 2 professore non caricati correttamente");

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
                showToast(context, "Dati tesi professore 3 non caricati correttamente");

            }
        });
    }


    /**
     * Questo metodo permette di recuperare lo studente in base all'id dell'utente.
     * È il secondo metodo(2) utile per poter recuperare le tasks.
     *
     * @param id_studente è l'id dell'utente uguale a quello dello studente
     */
    private void loadStudForIdStudenteInStudenteTesi(final Long id_studente) {
        loadStudByIdStudenteInStudenteTesi(id_studente).addOnCompleteListener(studTask -> {
            if (studTask.isSuccessful()) {
                requireActivity().runOnUiThread(() -> addStudentsToList(studTask.getResult()));
            } else {
                showToast(context, "Dati tesi professore 4 non caricati correttamente");
            }
        });
    }

    /**
     * Questo metodo aggiunge le tasks alla lista delle task e aggiorna l'adapter permettendo
     * la visualizzazione delle tasks.
     * @param tasks è la lista delle tasks
     */
    private void addStudentsToList(final List<StudenteWithUtente> tasks) {
        requireActivity().runOnUiThread(() -> {
            studentList.addAll(tasks); // Aggiungi tutti gli studenti alla lista esistente.
            adapter.notifyDataSetChanged();
        });
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
        TaskCompletionSource<Studente> tcs = new TaskCompletionSource<>();

        // Implementa la logica per caricare uno studente per id da dove sono conservati i dati (database, API, ecc.)
        // Ad esempio:
        StudenteRepository stRep = new StudenteRepository(context);
        Studente studente = stRep.findAllById(studenteId);

        // Completa la task con il risultato
        tcs.setResult(studente);

        return tcs.getTask();
    }


    private Task<Utente> loadUtenteByStudente(Studente studente) {
        TaskCompletionSource<Utente> tcs = new TaskCompletionSource<>();

        // Implementa la logica per caricare l'utente associato a uno studente da dove sono conservati i dati
        // Ad esempio:
        UtenteRepository utRep = new UtenteRepository(context);
        Utente utente = utRep.findAllById(studente.getId_utente());

        // Completa la task con il risultato
        tcs.setResult(utente);

        return tcs.getTask();
    }







    }








