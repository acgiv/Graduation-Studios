package com.laureapp.ui.card.Task;

import static com.laureapp.ui.controlli.ControlInput.showToast;
import static com.laureapp.ui.home.HomeFragment.getEmailFromSharedPreferences;

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

import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laureapp.R;
import com.laureapp.databinding.FragmentTaskBinding;
import com.laureapp.ui.card.Adapter.TaskStudenteAdapter;

import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteTesi;
import com.laureapp.ui.roomdb.entity.TaskStudente;

import com.laureapp.ui.roomdb.entity.Utente;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Questa classe rappresenta un frammento per la gestione delle task associate a uno studente.
 */
public class TaskHomeStudenteFragment extends Fragment {

    public TaskHomeStudenteFragment() {
        // Required empty public constructor
    }

    private NavController mNav;

    Context context;

    FragmentTaskBinding binding;


    // Dichiarazione di una variabile di istanza per il dialog
    static TaskStudenteAdapter adapter;

    String ruolo;

    private static final List<TaskStudente> taskList = new ArrayList<>();
    Bundle args;
    Utente utente;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inizializza il binding
        binding = com.laureapp.databinding.FragmentTaskBinding.inflate(inflater, container, false);
        context = requireContext();
        args = getArguments();


        if(args != null) {

                utente = (Utente)args.getSerializable("Utente");
                loadStudentForUserId(utente.getId_utente());
                Log.d("id_utenteTask", utente.getId_utente().toString());

                ruolo = args.getString("ruolo");


            //Carico i dati delle task in base all'utente loggato
        }
        // Altri codici del tuo fragment

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageButton addButton = view.findViewById(R.id.add_task_ImageButton);
        ListView listTaskView = view.findViewById(R.id.listTaskView);
        listTaskView.setNestedScrollingEnabled(true);

        mNav = Navigation.findNavController(view);
        adapter = new TaskStudenteAdapter(context, (ArrayList<TaskStudente>) taskList,mNav, args);

        addButton.setVisibility(View.GONE);

        listTaskView.setAdapter(adapter);

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
    private Task<List<TaskStudente>> loadTaskById(Long id_studente_tesi) {
        return FirebaseFirestore.getInstance()
                .collection("TaskStudente")
                .whereEqualTo("id_studente_tesi", id_studente_tesi)
                .get()
                .continueWith(task -> {
                    List<TaskStudente> taskStudenteList = new ArrayList<>();

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            TaskStudente taskStudente = new TaskStudente();
                            taskStudente.setId_task(doc.getLong("id_task"));
                            taskStudente.setId_studente_tesi(doc.getLong("id_studente_tesi"));
                            taskStudente.setStato(doc.getString("stato"));
                            taskStudente.setTitolo(doc.getString("titolo"));
                            taskStudente.setData_inizio(Objects.requireNonNull(doc.getTimestamp("data_inizio")).toDate());
                            taskStudente.setData_scadenza(Objects.requireNonNull(doc.getTimestamp("data_scadenza")).toDate());

                            taskStudenteList.add(taskStudente);
                        }

                        return taskStudenteList;
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
                taskList.clear();
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
                loadTaskForStudTesiId(id_tesi_in_studente_tesi);
            } else {
                showToast(context, "Dati StudenteTesi non caricati correttamente");
            }
        });
    }




    /**
     * Questo metodo permette di caricare le task in base all'id della tesi
     * @param id_stud_tesi_in_studente_tesi è l'id della tesi nelle tasks
     */
    private void loadTaskForStudTesiId(Long id_stud_tesi_in_studente_tesi) {
        loadTaskById(id_stud_tesi_in_studente_tesi).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                taskList.clear();
                //Mi stampa due volte l'id_studenteTesi, prima quello corretto e poi 4
                Log.d("id_studente_tesi", id_stud_tesi_in_studente_tesi.toString() + task.getResult());
                addTasksToList(task.getResult());
            } else {
                taskList.clear();
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
    private static void addTasksToList(List<TaskStudente> tasks) {
        for (TaskStudente taskStudente : tasks) {
            if (taskStudente != null) {
                taskList.add(taskStudente);
                Log.d("id_studente_in_studente_tesi", taskStudente.getId_studente_tesi().toString());

            }
        }
        adapter.notifyDataSetChanged();
    }



}