package com.laureapp.ui.card.Task;

import static com.laureapp.ui.roomdb.Converters.stringToTimestamp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.TaskTesi;
import com.laureapp.ui.roomdb.viewModel.TaskTesiModelView;
import com.laureapp.databinding.FragmentTaskBinding;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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
    private AutoCompleteTextView autoCompleteTextView;

    Context context;

    FragmentTaskBinding binding;
    // Dichiarazione di una variabile di istanza per il dialog
    private AlertDialog alertDialog;



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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inizializza il binding
        binding = FragmentTaskBinding.inflate(inflater, container, false);

        // Altri codici del tuo fragment

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        SearchView searchTaskView = view.findViewById(R.id.searchTaskView);
        ImageButton addButton = view.findViewById(R.id.add_task_ImageButton);

        CollectionReference taskRef = firestoreDB.collection("TaskTesi");
        mNav = Navigation.findNavController(view);
        ListView listTaskView = view.findViewById(R.id.listTaskView);

        taskRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    taskList.clear(); // Cancella la lista esistente
                    TaskTesiModelView taskTesiModelView = new TaskTesiModelView(context);
                    taskList = taskTesiModelView.getAllTaskTesi();
                    Log.d("Lista studenti-utenti", taskTesiModelView.getAllTaskTesi().toString());

                    Log.d("Lista Studenti Tesisti", taskList.toString());

                    TaskTesiAdapter adapter = new TaskTesiAdapter(requireContext(), taskList);
                    listTaskView.setAdapter(adapter);

                    listTaskView.setOnItemClickListener((parent, view1, position, id) -> {
                        mNav.navigate(R.id.action_fragment_tesisti_to_dettagli_tesista);
                        Log.d("Tesi", "cliccato il tesista");
                    });

                    // Aggiungi il TextWatcher per il campo di ricerca
                    searchTaskView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            // Quando il testo nel campo di ricerca cambia, filtra gli studenti
                            adapter.getFilter().filter(newText);
                            return true;
                        }
                    });
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Errore nel recupero degli studenti: " + e.getMessage()));


        addButton.setOnClickListener(view1 -> {
                showInputDialog();
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

        CalendarView calendarView = view.findViewById(R.id.calendarAddStartTaskView);

        //Button
        Button startDateButton = view.findViewById(R.id.inserisciDataInizioButton);
        Button dueDateButton = view.findViewById(R.id.inserisciDataScadenzaButton);

        //Setto la visibilità a GONE del calendario non appena viene visualizzato il fragment
        calendarView.setVisibility(View.GONE);

        //Click sulla data di inizio task
        startDateButton.setOnClickListener(view1 -> {
            Log.d("MyApp", "Pulsante Data Inizio premuto"); // Aggiungi questo log

            mostraCalendario(view);

            //L'utente seleziona la data dal calendario che gli appare
            calendarView.setOnDateChangeListener((v, year, month, dayOfMonth) -> {
                // La data selezionata dall'utente è disponibile qui
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;

                nascondiCalendario(view);
                startDateButton.setText(selectedDate);
                confronta_data_inizio_scadenza(view,startDateButton,dueDateButton);

                // Puoi fare qualcosa con la data selezionata
            });
        });

        //Click sulla data di scadenza task
        dueDateButton.setOnClickListener(view1 -> {
            Log.d("MyApp", "Pulsante Data Inizio premuto"); // Aggiungi questo log

            mostraCalendario(view);

            calendarView.setOnDateChangeListener((v, year, month, dayOfMonth) -> {
                // La data selezionata dall'utente è disponibile qui
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                nascondiCalendario(view);
                dueDateButton.setText(selectedDate);
                confronta_data_inizio_scadenza(view,startDateButton,dueDateButton);

                // Puoi fare qualcosa con la data selezionata
            });
        });


        // Imposta un ascoltatore per il pulsante OK
        builder.setPositiveButton("OK", (dialog, which) -> {
            String inputData = editTextTitolo.getText().toString();
            // Puoi fare qualcosa con il dato inserito

            // Chiudi il dialog solo dopo che l'utente ha premuto OK
            dialog.dismiss();
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
    private void mostraCalendario(@NonNull View view)
    {


        //Inizializzo le variabili
        //Button
        Button startDateButton = view.findViewById(R.id.startDateBar);
        Button dueDateButton = view.findViewById(R.id.dueDateBar);


        //Calendario
        CalendarView calendarView = view.findViewById(R.id.calendarStartTaskView);

        //TextView
        EditText barraTitoloTask = view.findViewById(R.id.editTextTitolo);

        //Messaggi di errore
        TextView errorDueDate = view.findViewById(R.id.errorDueDate);




        //Setto la visibilità - Calendario VISIBILE
        calendarView.setVisibility(View.VISIBLE);

        //Button - Pulsanti INVISIBILI
        startDateButton.setVisibility(View.GONE);
        dueDateButton.setVisibility(View.GONE);


        //TextView - TextView INVISIBLI
        autoCompleteTextView.setVisibility(View.GONE);
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
        Button startDateButton = view.findViewById(R.id.startDateBar);
        Button dueDateButton = view.findViewById(R.id.dueDateBar);


        //Calendar
        CalendarView calendarView = view.findViewById(R.id.calendarStartTaskView);


        EditText barraAddTitoloTask = view.findViewById(R.id.editTextTitolo);

        //Calendario - Calendario INVISIBILE
        calendarView.setVisibility(View.GONE);

        //Button - Pulsanti VISIBILI
        startDateButton.setVisibility(View.VISIBLE);
        dueDateButton.setVisibility(View.VISIBLE);

        autoCompleteTextView.setVisibility(View.VISIBLE);

        //TextView - TextView VISIBILI
        startDateButton.setVisibility(View.VISIBLE);
        dueDateButton.setVisibility(View.VISIBLE);
        barraAddTitoloTask.setVisibility(View.VISIBLE);
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
                errorDueDate.setVisibility(View.VISIBLE);

            }else {
                // Le date sono valide, nascondi gli errori
                errorDueDate.setVisibility(View.GONE);

            }
        } catch (ParseException e) {
            // Gestisci eccezione di parsing, ad esempio, quando il formato della data è errata
            e.printStackTrace();
            errorDueDate.setVisibility(View.VISIBLE);
        }
    }



    /**
     * Questo adapter fornisce l'accesso ai data Item. In questo caso,
     * ai ListItem della ListView e si occupare di settare graficamente
     * gli elementi della lista
     */
    class TaskTesiAdapter extends ArrayAdapter<TaskTesi> {
        private final LayoutInflater inflater;
        private final List<TaskTesi> taskList;
        private List<TaskTesi> filteredTaskList;


        /**
         * @param context  si riferisce al contesto in cui viene utilizzato
         * @param taskList corrisponde alla lista di task da passare
         */
        public TaskTesiAdapter(Context context, List<TaskTesi> taskList) {
            super(context, 0, taskList);
            inflater = LayoutInflater.from(context);
            this.taskList = taskList;
            this.filteredTaskList = new ArrayList<>(); // Initialize it as an empty ArrayList

            Log.d("Numero task adapter", "Numero di task nell'adapter: " + filteredTaskList.size());
        }


        /**
         * @param position    si riferisce alla posizione dell'item della lista
         * @param convertView si riferisce alla variabile che gestisce il cambiamento della view
         * @param parent      Interfaccia per le informazioni globali riguardo all'ambiente dell'applicazione.
         *                    usata per chiamare operazioni a livello applicazione launching activities, broadcasting e receiving intents
         * @return la view con la lista aggiornata
         */
        @NonNull
        @Override
        public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
            TaskTesi taskTesi = new TaskTesi();
            Long taskListView = getItem(position).getId_task();
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.lista_task, parent, false);
            }

            if (taskListView != null) {
                TextView taskTextView = convertView.findViewById(R.id.taskTextView);

                if (taskTextView != null) {
                    // Ottieni il nome e il cognome dall'oggetto Utente associato allo studente
                    String nomeTask = taskTesi.getTitolo();
                    Log.d("Lista Task", nomeTask);
                    taskTextView.setText(nomeTask);
                }
            }

            return convertView;
        }


        @NonNull
        public Filter getFilter() {
            return new Filter() {

                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults filterResults = new FilterResults();
                    String filterText = charSequence.toString().toLowerCase();

                    List<TaskTesi> filteredList = new ArrayList<>();

                    if (filterText.isEmpty()) {
                        // Se il testo di ricerca è vuoto, mostra l'intera lista originale
                        filteredList.addAll(taskList);

                    } else {
                        // Altrimenti, filtra gli studenti in base al testo di ricerca
                        for (TaskTesi task : taskList) {
                            String nomeTask = task.getTitolo();
                            if (nomeTask.toLowerCase().contains(filterText)) {
                                filteredList.add(task);
                            }
                        }
                    }

                    filterResults.values = filteredList;
                    filterResults.count = filteredList.size();
                    return filterResults;
                }


                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    List<TaskTesi> filteredList = (List<TaskTesi>) filterResults.values;
                    taskList.clear();
                    Log.d("Numero studenti adapter", "Numero di studenti nell'adapter: " + filteredTaskList.size());

                    if (filteredList != null) {

                        filteredTaskList.addAll(filteredList);
                    }
                    notifyDataSetChanged(); // Notifica all'adapter di aggiornare la vista
                }
            };
        }


    }
}
