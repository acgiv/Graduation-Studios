package com.laureapp.ui.card.Task;

import static android.view.View.VISIBLE;

import static com.laureapp.ui.controlli.ControlInput.showToast;
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
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laureapp.R;
import com.laureapp.databinding.FragmentTaskBinding;
import com.laureapp.ui.card.Adapter.TaskTesiAdapter;
import com.laureapp.ui.roomdb.QueryFirestore;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteTesi;
import com.laureapp.ui.roomdb.entity.TaskTesi;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.entity.Utente;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
/**
 * Questo è un fragment per la gestione delle task relative alle tesi.
 */
public class TaskTesiFragment extends Fragment {

    private NavController mNav;

    static Context context;

    com.laureapp.databinding.FragmentTaskBinding binding;



    // Dichiarazione di una variabile di istanza per il dialog
    private AlertDialog alertDialog;
    private static TaskTesiAdapter adapter;
    TaskTesi selectedTask;

    static FirebaseFirestore db;
    Bundle args;
    Utente utente;

    private static List<TaskTesi> taskList = new ArrayList<>();

    /**
     * Costruttore predefinito del fragment.
     */
    public TaskTesiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inizializza il binding
        binding = FragmentTaskBinding.inflate(inflater, container, false);
        context = requireContext();
        args = getArguments();


        if(args != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                utente = args.getSerializable("Utente", Utente.class);
            }
            //Carico i dati delle task in base all'utente loggato
        }
        // Altri codici del tuo fragment
        db = FirebaseFirestore.getInstance();

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageButton addButton = view.findViewById(R.id.add_task_ImageButton);
        mNav = Navigation.findNavController(view);
        ListView listTaskView = view.findViewById(R.id.listTaskView);

        adapter = new TaskTesiAdapter(context, taskList,mNav);

        loadStudentForUserId(utente.getId_utente());

        addButton.setOnClickListener(view1 ->
                showInputDialog()
        );

        listTaskView.setAdapter(adapter);

    }



    /**
     * Metodo per mostrare il pop-up in un'attività o fragment
     */
    public void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Nuova task");

        // Includi il layout XML personalizzato
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.add_data_popup, null);
        builder.setView(view);

        EditText editTextTitolo = view.findViewById(R.id.editTextTitolo);

        CalendarView calendarStartView = view.findViewById(R.id.calendarAddStartTaskView);
        CalendarView calendarDueView = view.findViewById(R.id.calendarAddDueTaskView);

        //Button
        Button startDateButton = view.findViewById(R.id.inserisciDataInizioButton);
        Button dueDateButton = view.findViewById(R.id.inserisciDataScadenzaButton);

        //Setto la visibilità a GONE del calendario non appena viene visualizzato il fragment
        calendarStartView.setVisibility(View.GONE);
        calendarDueView.setVisibility(View.GONE);



        //Click sulla data di inizio task
        startDateButton.setOnClickListener(view1 -> {
            Log.d("MyApp", "Pulsante Data Inizio premuto");

            mostraCalendario(view,true);

            calendarStartView.setOnDateChangeListener((v, year, month, dayOfMonth) -> {
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                nascondiCalendario(view);
                startDateButton.setText(selectedDate);
                confronta_data_inizio_scadenza(view, startDateButton, dueDateButton);
            });
        });

        dueDateButton.setOnClickListener(view1 -> {
            Log.d("MyApp", "Pulsante Data Scadenza premuto");

            mostraCalendario(view,false);

            calendarDueView.setOnDateChangeListener((v, year, month, dayOfMonth) -> {
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                nascondiCalendario(view);
                dueDateButton.setText(selectedDate);
                confronta_data_inizio_scadenza(view, startDateButton, dueDateButton);
            });
        });






        // Aggiungi un ascoltatore personalizzato al pulsante OK
        builder.setPositiveButton("Ok", (dialog, which) -> {
                    String inputData = editTextTitolo.getText().toString();
                    //effettuo la conversione della stringa in data
                    Timestamp startDate;
                    Timestamp dueDate;

                    try {

                        startDate = stringToTimestamp(startDateButton.getText().toString());
                        dueDate = stringToTimestamp(dueDateButton.getText().toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                utente = args.getSerializable("Utente", Utente.class);
                addTaskToFirestoreLast(utente.getId_utente(), inputData, startDate, dueDate);

            }
            //Aggiungo la task a Firestore in base all'utente loggato
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
     * Con questo metodo mostro il calendario dopo che l'utente clicca su una bar
     * @param view corrisponde alla view originaria aggiornata
     */
    private void mostraCalendario(@NonNull View view, boolean isStartDate)
    {


        //Inizializzo le variabili
        //Button
        Button startDateButton = view.findViewById(R.id.inserisciDataInizioButton);
        Button dueDateButton = view.findViewById(R.id.inserisciDataScadenzaButton);


        //Calendario
        CalendarView calendarAddStartTaskView = view.findViewById(R.id.calendarAddStartTaskView);
        CalendarView calendarAddDueTaskView = view.findViewById(R.id.calendarAddDueTaskView);

        //TextView
        EditText barraTitoloTask = view.findViewById(R.id.editTextTitolo);

        //Messaggi di errore
        TextView errorDueDate = view.findViewById(R.id.errorDueDate);




        // Setto la visibilità - Calendario VISIBILE solo per la data corrispondente
        if (isStartDate) {
            calendarAddStartTaskView.setVisibility(VISIBLE);
            calendarAddDueTaskView.setVisibility(View.GONE);
        } else {
            calendarAddStartTaskView.setVisibility(View.GONE);
            calendarAddDueTaskView.setVisibility(VISIBLE);
        }
        //Button - Pulsanti INVISIBILI
        startDateButton.setVisibility(View.GONE);
        dueDateButton.setVisibility(View.GONE);


        //TextView - TextView INVISIBLI
        //autoCompleteTextView.setVisibility(View.GONE);
        barraTitoloTask.setVisibility(View.GONE);
        errorDueDate.setVisibility(View.GONE);
    }

    /**
     * Con questo metodo nascondo il calendario dopo che l'utente seleziona una data e mostro nuovamente le barre
     * @param view corrisponde alla view originaria aggiornata
     */
    private void nascondiCalendario(@NonNull View view)
    {
        //Inizializzo le variabili
        //Button
        Button startDateButton = view.findViewById(R.id.inserisciDataInizioButton);
        Button dueDateButton = view.findViewById(R.id.inserisciDataScadenzaButton);


        //Calendar
        CalendarView calendarAddStartTaskView = view.findViewById(R.id.calendarAddStartTaskView);
        CalendarView calendarAddDueTaskView = view.findViewById(R.id.calendarAddDueTaskView);


        EditText barraAddTitoloTask = view.findViewById(R.id.editTextTitolo);

        //Calendario - Calendario INVISIBILE
        calendarAddStartTaskView.setVisibility(View.GONE);
        calendarAddDueTaskView.setVisibility(View.GONE);

        //Button - Pulsanti VISIBILI
        startDateButton.setVisibility(VISIBLE);
        dueDateButton.setVisibility(VISIBLE);


        //TextView - TextView VISIBILI
        startDateButton.setVisibility(VISIBLE);
        dueDateButton.setVisibility(VISIBLE);
        barraAddTitoloTask.setVisibility(VISIBLE);
    }

    /**
     * Questo metodo permette di confrontare la data di inizio con la data di scadenza
     * @param view corrisponde alla view aggiornata nel caso di errore
     * @param startDateButton corrisponde al testo nella barra della data di inizio
     * @param dueDateButton corrisponde al testo nella barra della data di scadenza
     */
    private void confronta_data_inizio_scadenza(@NonNull View view, Button startDateButton,Button dueDateButton)
    {

        //Messaggi di errore
        TextView errorDueDate = view.findViewById(R.id.errorDueDate);


        // Quando l'utente seleziona le date, confrontale
        try {
            Timestamp startDate = stringToTimestamp(startDateButton.getText().toString());
            Timestamp dueDate = stringToTimestamp(dueDateButton.getText().toString());

            //se la start date viene dopo la data di scadenza e le due barre NON sono vuote
            if (startDate.after(dueDate)) {
                // La data di inizio è successiva alla data di scadenza, mostra l'errore
                errorDueDate.setVisibility(VISIBLE);
                if (alertDialog != null) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }

            }else {
                // Le date sono valide, nascondi gli errori
                errorDueDate.setVisibility(View.GONE);
                if (alertDialog != null) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }

            }
        } catch (ParseException e) {
            // Gestisci eccezione di parsing, ad esempio, quando il formato della data è errata
            e.printStackTrace();
            errorDueDate.setVisibility(VISIBLE);
            if (alertDialog != null) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        }
    }




    /**
     * Questo metodo mi permette di caricare da firestore gli studenti dando come parametro l'id dell'utente
     *
     * @param id_utente id dell'utente nella tabella Studente
     * @return l'id dello studente associati all' utente
     */
    private Task<Long> loadStudentByUserId(Long id_utente) {
        return FirebaseFirestore.getInstance()
                .collection("Utenti").document("Studenti").collection("Studenti")
                .whereEqualTo("id_utente", id_utente)
                .limit(1)
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
                .limit(1)
                .get()
                .continueWith(task -> {


                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0); // Otteniamo il primo documento
                        StudenteTesi studenteTesi = new StudenteTesi();
                        studenteTesi.setId_studente(doc.getLong("id_studente"));
                        studenteTesi.setId_studente_tesi(doc.getLong("id_studente_tesi"));
                        studenteTesi.setId_tesi(doc.getLong("id_tesi"));




                        return studenteTesi.getId_tesi();
                    }
                    throw new NoSuchElementException("Utente non trovato con questa mail: " + id_studente);
                });
    }

    /**
     * Questo metodo mi permette di caricare da firestore la tabella tesi dando come parametro l'id della tesi nella tabella studenteTesi
     *
     * @param id_tesi id della tesi nella tabella StudenteTesi
     * @return l'id della tesi presente nella tabella studente_tesi
     */
    private Task<Long> loadTesiByIdTesiInStudenteTesi(Long id_tesi) {
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
     * Questo metodo mi permette di caricare da firestore le task dando come parametro l'id della tesi
     *
     * @param id_tesi id della tesi nella tabella Tesi
     * @return la lista delle task in base all'id della tesi fornito
     */
    private Task<List<TaskTesi>> loadTaskById(Long id_tesi) {
        return FirebaseFirestore.getInstance()
                .collection("Task")
                .whereEqualTo("id_tesi", id_tesi)
                .get()
                .continueWith(task -> {
                    List<TaskTesi> taskTesiList = new ArrayList<>();

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            TaskTesi taskTesi = new TaskTesi();
                            taskTesi.setId_task(doc.getLong("id_task"));
                            taskTesi.setId_tesi(doc.getLong("id_tesi"));
                            taskTesi.setStato(doc.getString("stato"));
                            taskTesi.setTitolo(doc.getString("titolo"));
                            taskTesi.setData_inizio(Objects.requireNonNull(doc.getTimestamp("data_inizio")).toDate());
                            taskTesi.setData_scadenza(Objects.requireNonNull(doc.getTimestamp("data_scadenza")).toDate());

                            taskTesiList.add(taskTesi);
                        }

                        return taskTesiList;
                    } else {
                        throw new NoSuchElementException("Studente non trovato con l'ID utente: " + id_tesi);
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
                loadTesiForTesiIdInStudenteTesi(id_tesi_in_studente_tesi);
            } else {
                showToast(context, "Dati StudenteTesi non caricati correttamente");
            }
        });
    }

    /**
     * Questo metodo permette di accedere alle tesi dalla tabella StudenteTesi attraverso l'id della tesi in quest'ultima.
     * E' il quarto ed ultimo metodo(4) utile per poter recuperare le tasks.
     * @param id_tesi_in_studente_tesi è l'id della tesi nella tabella StudenteTesi
     */
    private void loadTesiForTesiIdInStudenteTesi(Long id_tesi_in_studente_tesi) {
        loadTesiByIdTesiInStudenteTesi(id_tesi_in_studente_tesi).addOnCompleteListener(tesiTask -> {
            if (tesiTask.isSuccessful()) {
                Long id_tesi = tesiTask.getResult();
                loadTasksForTesiId(id_tesi);
            } else {
                showToast(context, "Dati tesi non caricati correttamente");
            }
        });
    }

    /**
     * Questo metodo permette di caricare le task in base all'id della tesi
     * @param id_tesi è l'id della tesi nelle tasks
     */
    private void loadTasksForTesiId(Long id_tesi) {
        loadTaskById(id_tesi).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                taskList.clear();
                addTasksToList(task.getResult());
            } else {
                showToast(context, "Dati task non caricati correttamente");
            }
        });
    }

    /**
     * Questo metodo aggiunge le tasks alla lista delle task e aggiorna l'adapter permettendo
     * la visualizzazione delle tasks.
     * @param tasks è la lista delle tasks
     */
    private void addTasksToList(List<TaskTesi> tasks) {
        for (TaskTesi taskTesi : tasks) {
            if (taskTesi != null) {
                taskList.add(taskTesi);

            }
        }
        adapter.notifyDataSetChanged();
    }

/*
    Metodi per l'aggiunta e visualizzazione di una nuova task
 */

    /**
     * Aggiunge una nuova task al database Firestore in base all'ID dell'utente, al titolo della task,
     * alla data di inizio e alla data di scadenza forniti.
     *
     * @param id_utente   l'ID dell'utente associato alla task.
     * @param inputData   il titolo della nuova task da aggiungere.
     * @param startDate   la data di inizio della task.
     * @param dueDate     la data di scadenza della task.
     */
    private void addTaskToFirestoreLast(Long id_utente, String inputData, Timestamp startDate, Timestamp dueDate) {

        // Ottieni l'ID della tesi in base all'ID utente fornito
        loadStudentAndAddTask(id_utente, inputData, startDate, dueDate);
    }

    /**
     * Carica i dati dello studente in base all'ID dell'utente fornito e aggiunge una nuova task al database Firestore.
     * Questo metodo è utilizzato per creare una catena di chiamate per caricare i dati necessari e aggiungere una task.
     *
     * @param id_utente   l'ID dell'utente associato alla task.
     * @param inputData   il titolo della nuova task da aggiungere.
     * @param startDate   la data di inizio della task.
     * @param dueDate     la data di scadenza della task.
     */
    private void loadStudentAndAddTask(Long id_utente, String inputData, Timestamp startDate, Timestamp dueDate) {
        loadStudentByUserId(id_utente).addOnCompleteListener(studentTask -> {
            if (studentTask.isSuccessful()) {
                Long id_studente = studentTask.getResult();
                loadStudenteTesiAndAddTask(id_studente, inputData, startDate, dueDate);
            } else {
                showToast(context, "Lettura dati studenti e aggiunta task non avvenuta correttamente");
            }
        });
    }

    /**
     * Carica i dati dello studente tesi in base all'ID dello studente fornito e aggiunge una nuova task al database Firestore.
     * Questo metodo è utilizzato per creare una catena di chiamate per caricare i dati necessari e aggiungere una task.
     *
     * @param id_studente   l'ID dello studente tesi associato alla task.
     * @param inputData     il titolo della nuova task da aggiungere.
     * @param startDate     la data di inizio della task.
     * @param dueDate       la data di scadenza della task.
     */
    private void loadStudenteTesiAndAddTask(Long id_studente, String inputData, Timestamp startDate, Timestamp dueDate) {
        loadStudenteTesiByStudenteId(id_studente).addOnCompleteListener(studenteTesiTask -> {
            if (studenteTesiTask.isSuccessful()) {
                Long id_tesi_in_studente_tesi = studenteTesiTask.getResult();
                loadTesiAndAddTask(id_tesi_in_studente_tesi, inputData, startDate, dueDate);
            } else {
                showToast(context, "Dati StudenteTesi e aggiunta task non avvenuta correttamente");
            }
        });
    }

    /**
     * Carica i dati della tesi in base all'ID della tesi associata allo studente tesi e aggiunge una nuova task al database Firestore.
     * Questo metodo è utilizzato per creare una catena di chiamate per caricare i dati necessari e aggiungere una task.
     *
     * @param id_tesi_in_studente_tesi    l'ID della tesi associata allo studente tesi.
     * @param inputData                   il titolo della nuova task da aggiungere.
     * @param startDate                   la data di inizio della task.
     * @param dueDate                     la data di scadenza della task.
     */
    private void loadTesiAndAddTask(Long id_tesi_in_studente_tesi, String inputData, Timestamp startDate, Timestamp dueDate) {
        loadTesiByIdTesiInStudenteTesi(id_tesi_in_studente_tesi).addOnCompleteListener(tesiTask -> {
            if (tesiTask.isSuccessful()) {
                Long id_tesi = tesiTask.getResult();
                addTaskToFirestore(id_tesi, inputData, startDate, dueDate);
            } else {
                showToast(context, "Lettura dati tesi e aggiunta task non avvenuta correttamente");
            }
        });
    }

    /**
     * Aggiunge una nuova task al database Firestore con i dati forniti.
     *
     * @param id_tesi      l'ID della tesi associata alla nuova task.
     * @param inputData    il titolo della nuova task.
     * @param startDate    la data di inizio della task.
     * @param dueDate      la data di scadenza della task.
     */
    private void addTaskToFirestore(Long id_tesi, String inputData, Timestamp startDate, Timestamp dueDate) {
        CollectionReference taskRef = db.collection("Task");
        QueryFirestore queryFirestore = new QueryFirestore();
        Map<String, Object> taskData = new HashMap<>();
        queryFirestore.trovaIdTaskMax(context)
                .thenAccept(idMax -> {
                    idMax = idMax + 1L;
                    taskData.put("id_task", idMax);
                    taskData.put("titolo", inputData);
                    taskData.put("data_inizio", startDate);
                    taskData.put("data_scadenza", dueDate);
                    taskData.put("stato", R.string.default_stato_task);
                    taskData.put("id_tesi", id_tesi);

                    // Supponendo che 'taskRef' sia un oggetto valido di tipo CollectionReference
                    Long finalIdMax = idMax;
                    taskRef.document().set(taskData).addOnSuccessListener(
                            aVoid -> addTaskToList(finalIdMax, inputData, startDate, dueDate, id_tesi)
                    ).addOnFailureListener(e -> {
                        // Gestisci l'errore
                    });
                });
    }

    /**
     * Metodo per l'aggiunta di una nuova task alla lista di partenza
     * @param id_task id della task da aggiungere alla lista
     * @param inputData è il titolo della task
     * @param startDate data inizio inserita
     * @param dueDate data scadenza inserita
     * @param id_tesi tesi associata alla task
     */
    private void addTaskToList(Long id_task, String inputData, Timestamp startDate, Timestamp dueDate, Long id_tesi) {

        TaskTesi taskTesi = new TaskTesi();
        taskTesi.setId_task(id_task);
        taskTesi.setTitolo(inputData);
        taskTesi.setData_inizio(startDate);
        taskTesi.setData_scadenza(dueDate);
        taskTesi.setStato(String.valueOf(R.string.default_stato_task));
        taskTesi.setId_tesi(id_tesi);

        // Aggiungi taskTesi alla lista
        taskList.add(taskTesi);
        adapter.notifyDataSetChanged();
    }

    /**
     * Rimuove una task dalla lista e dal database Firestore in base alla posizione specificata.
     *
     * @param position la posizione della task da rimuovere nella lista.
     */
    public static void deleteTask(int position) {
        if (position >= 0 && position < taskList.size()) {
            TaskTesi taskToDelete = taskList.get(position);

            // Rimuovi la task da Firestore
            deleteTaskFromFirestore(taskToDelete.getId_task());

            // Rimuovi la task dalla lista locale
            taskList.remove(position);
            adapter.notifyDataSetChanged();
        } else {
            showToast(context, "Posizione non valida o elemento non trovato");
        }
    }

    /**
     * Rimuove una task dal database Firestore in base all'ID della task specificato.
     *
     * @param id_task l'ID della task da rimuovere dal database Firestore.
     */
    private static void deleteTaskFromFirestore(Long id_task) {
        // Ottieni il riferimento al documento della task da eliminare
        CollectionReference taskRef = db.collection("Task");
        taskRef.whereEqualTo("id_task", id_task).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                doc.getReference().delete().addOnSuccessListener(aVoid -> {
                    showToast(context, "Task eliminata con successo");
                }).addOnFailureListener(e -> {
                    showToast(context, "Errore durante l'eliminazione della task");
                });
            } else {
                showToast(context, "Task non trovata nel database");
            }
        });
    }

}
