package com.laureapp.ui.card.Task;

import static android.view.View.VISIBLE;

import static com.laureapp.ui.home.HomeFragment.getEmailFromSharedPreferences;
import static com.laureapp.ui.roomdb.Converters.stringToTimestamp;

import android.app.AlertDialog;
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
import com.laureapp.ui.card.Adapter.TaskTesiAdapter;
import com.laureapp.ui.controlli.ControlInput;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteTesi;
import com.laureapp.ui.roomdb.entity.TaskTesi;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.databinding.FragmentTaskBinding;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskTesiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class TaskTesiFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private NavController mNav;

    Context context;

    FragmentTaskBinding binding;

    // Dichiarazione di una variabile di istanza per il dialog
    private AlertDialog alertDialog;
    private TaskTesiAdapter adapter;

    FirebaseFirestore db;


    private List<TaskTesi> taskList = new ArrayList<>();


    public TaskTesiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskTesiFragment newInstance(String param1, String param2) {
        TaskTesiFragment fragment = new TaskTesiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

        // Inizializza il calendario

        // Altri codici del tuo fragment
        db = FirebaseFirestore.getInstance();

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageButton addButton = view.findViewById(R.id.add_task_ImageButton);

        mNav = Navigation.findNavController(view);
        ListView listTaskView = view.findViewById(R.id.listTaskView);

        adapter = new TaskTesiAdapter(context, taskList);
        String userEmail = getEmailFromSharedPreferences(requireContext());

        //Carico i dati delle task in base all'utente loggato
        loadDataForUser(userEmail);


        addButton.setOnClickListener(view1 ->
                showInputDialog()
        );


        listTaskView.setAdapter(adapter);

        listTaskView.setOnItemClickListener((parent, view1, position, id) -> {
            mNav.navigate(R.id.action_task_to_dettagli_task);
            Log.d("Tesi", "cliccato il tesista");
        });
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


        String userEmail = getEmailFromSharedPreferences(requireContext());

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

                    //Aggiungo la task a Firestore in base all'utente loggato
                    addTaskToFirestore(userEmail, inputData, startDate, dueDate);

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
     * Questo metodo mi permette di caricare da firestore gli utenti dando come parametro la mail dell'utente
     *
     * @param email email utente loggato
     * @return l'id dell'utente corrispondente alla mail dell'utente loggato
     */
    private Task<Long> loadUserByEmail(String email) {
        return FirebaseFirestore.getInstance()
                .collection("Utenti")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .continueWith(task -> {


                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0); // Otteniamo il primo documento
                        Utente utente = new Utente();
                        utente.setId_utente(doc.getLong("id_utente"));
                        utente.setNome(doc.getString("nome"));
                        utente.setCognome(doc.getString("cognome"));
                        utente.setEmail(doc.getString("email"));
                        utente.setPassword(doc.getString("password"));
                        utente.setFacolta(doc.getString("facolta"));
                        utente.setNome_cdl(doc.getString("nome_cdl"));



                        return utente.getId_utente();
                    }
                    throw new NoSuchElementException("Utente non trovato con questa mail: " + email);
                });
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
                            taskTesi.setStato("Non iniziato");
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
     * Questo metodo permette di recuperare l'utente in base alla sua mail.
     * È il primo metodo(1) utile per poter recuperare le tasks.
     * @param userEmail è la mail dell'utente loggato
     */
    private void loadDataForUser(String userEmail) {
        loadUserByEmail(userEmail).addOnCompleteListener(userTask -> {
            if (userTask.isSuccessful()) {
                Long id_utente = userTask.getResult();
                loadStudentForUserId(id_utente);
            } else {
                ControlInput.showToast(context, "Dati utenti non caricati correttamente");
            }
        });
    }

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
                ControlInput.showToast(context, "Dati studenti non caricati correttamente");

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
                ControlInput.showToast(context, "Dati StudenteTesi non caricati correttamente");
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
                ControlInput.showToast(context, "Dati tesi non caricati correttamente");
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
                ControlInput.showToast(context, "Dati task non caricati correttamente");
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
     * Metodo per l'aggiunta delle task a Firestore.
     * accetta la userEmail come parametro, quindi inizia caricando l'utente
     * tramite l'email e quindi procede a caricare lo studente e infine aggiunge la task a Firestore.
     *
     * @param userEmail mail utente loggato
     * @param inputData è il titolo della task
     * @param startDate è la data di inizio inserita
     * @param dueDate è la data di scadenza inserita
     */
    private void addTaskToFirestore(String userEmail, String inputData, Timestamp startDate, Timestamp dueDate) {
        loadUserByEmail(userEmail).addOnCompleteListener(userTask -> {
            if (userTask.isSuccessful()) {
                Long id_utente = userTask.getResult();
                loadStudentAndAddTask(id_utente, inputData, startDate, dueDate);
            } else {
                ControlInput.showToast(context, "Operazione completata con successo!");
            }
        });
    }

    /**
     * Metodo per il caricamento dello studente e l'aggiunta di una nuova task.
     * @param id_utente id dell'utente loggato
     * @param inputData titolo task
     * @param startDate data inizio inserita
     * @param dueDate data scadenza inserita
     */
    private void loadStudentAndAddTask(Long id_utente, String inputData, Timestamp startDate, Timestamp dueDate) {
        loadStudentByUserId(id_utente).addOnCompleteListener(studentTask -> {
            if (studentTask.isSuccessful()) {
                Long id_studente = studentTask.getResult();
                loadStudenteTesiAndAddTask(id_studente, inputData, startDate, dueDate);
            } else {
                ControlInput.showToast(context, "Lettura dati studenti e aggiunta task non avvenuta correttamente");
            }
        });
    }

    /**
     * Metodo per il caricamento dello studente e l'aggiunta di una nuova task.
     * @param id_studente id dello studente
     * @param inputData è il titolo della task
     * @param startDate data inizio inserita
     * @param dueDate data scadenza inserita
     */
    private void loadStudenteTesiAndAddTask(Long id_studente, String inputData, Timestamp startDate, Timestamp dueDate) {
        loadStudenteTesiByStudenteId(id_studente).addOnCompleteListener(studenteTesiTask -> {
            if (studenteTesiTask.isSuccessful()) {
                Long id_tesi_in_studente_tesi = studenteTesiTask.getResult();
                loadTesiAndAddTask(id_tesi_in_studente_tesi, inputData, startDate, dueDate);
            } else {
                ControlInput.showToast(context, "Dati StudenteTesi e aggiunta task non avvenuta correttamente");
            }
        });
    }

    /**
     * Metodo per il caricamento della tesi e l'aggiunta di una nuova task.
     * @param id_tesi_in_studente_tesi id tesi nella tabella StudenteTesi
     * @param inputData è il titolo della task
     * @param startDate data inizio inserita
     * @param dueDate data scadenza inserita
     */
    private void loadTesiAndAddTask(Long id_tesi_in_studente_tesi, String inputData, Timestamp startDate, Timestamp dueDate) {
        loadTesiByIdTesiInStudenteTesi(id_tesi_in_studente_tesi).addOnCompleteListener(tesiTask -> {
            if (tesiTask.isSuccessful()) {
                Long id_tesi = tesiTask.getResult();
                addTaskToFirestore(id_tesi, inputData, startDate, dueDate);
            } else {
                ControlInput.showToast(context, "Lettura dati tesi e aggiunta task non avvenuta correttamente");
            }
        });
    }

    /**
     * Metodo per l'aggiunta di una nuova task a Firestore.  accetta direttamente l'id_tesi come parametro e non richiede
     * il caricamento dell'utente o dello studente. Serve per aggiungere una task quando hai già l'id_tesi disponibile.
     *
     * @param id_tesi id tesi associata alla task
     * @param inputData è il titolo della task
     * @param startDate data inizio inserita
     * @param dueDate data scadenza inserita
     */
    private void addTaskToFirestore(Long id_tesi, String inputData, Timestamp startDate, Timestamp dueDate) {
        CollectionReference taskRef = db.collection("Task");
        Long id_task = 0L;
        Map<String, Object> taskData = new HashMap<>();
        taskData.put("id_task", id_task);
        taskData.put("titolo", inputData);
        taskData.put("data_inizio", startDate);
        taskData.put("data_scadenza", dueDate);
        taskData.put("stato", "Non iniziato");
        taskData.put("id_tesi", id_tesi);

        // Supponendo che 'taskRef' sia un oggetto valido di tipo CollectionReference
        taskRef.document().set(taskData).addOnSuccessListener(
                aVoid -> addTaskToList(id_task, inputData, startDate, dueDate, id_tesi)
        ).addOnFailureListener(e -> {
            // Gestisci l'errore
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
        taskTesi.setStato("Non iniziato");
        taskTesi.setId_tesi(id_tesi);

        // Aggiungi taskTesi alla lista
        taskList.add(taskTesi);
        adapter.notifyDataSetChanged();
    }











}
