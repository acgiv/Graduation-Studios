package com.laureapp.ui.card.Task;

import static androidx.databinding.DataBindingUtil.setContentView;
import static com.laureapp.ui.controlli.ControlInput.showToast;
import static com.laureapp.ui.roomdb.Converters.stringToTimestamp;

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
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laureapp.R;
import com.laureapp.databinding.FragmentDettagliTaskBinding;
import com.laureapp.ui.roomdb.entity.TaskStudente;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class DettagliTaskFragment extends Fragment {


    private NavController mNav;
    private AutoCompleteTextView autoCompleteTextView;
    private FragmentDettagliTaskBinding binding;
    private FirebaseFirestore db;
    private Bundle args;
    private TaskStudente taskStudente;
    private Context context;
    private Button startDateButton;
    private Button dueDateButton;

    private final HashMap<String, Object> elem_text = new HashMap<>();


    public DettagliTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = requireContext();
        args = getArguments();
        if(args != null) {
            taskStudente = new TaskStudente();
            taskStudente = args.getSerializable("SelectedTask", TaskStudente.class);
            //Carico i dati delle task in base all'utente loggato
        }



        // Altri codici del tuo fragment
        db = FirebaseFirestore.getInstance();



        // Inizializza il mapping degli elementi di testo associandoli al binding
        //inizializzate_binding_text();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDettagliTaskBinding.inflate(inflater, container, false);
        // Inizializza le variabili nel metodo onCreate
        startDateButton = binding.startDateBar;
        dueDateButton = binding.dueDateBar;
        autoCompleteTextView = binding.filledExposedDropdown;


        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mNav = Navigation.findNavController(view);
        //Inizializzazione delle variabili
        EditText titoloTask = binding.titoloTaskBar;
        Button ricevimentiButton = binding.buttonVisualizzaRicevimentiTask;
        Button salvaButton = binding.buttonSalvaModificheTask;
        CalendarView calendarView = binding.calendarStartTaskView;


        //Setto i valori della task cliccata
        titoloTask.setText(taskStudente.getTitolo());

        //Setto i valori del dropdown dello stato task
        autoCompleteTextView.setText(taskStudente.getStato());
        Log.d("statoTask", taskStudente.getStato());

        Date dataScadenza = taskStudente.getData_scadenza();
        if (dataScadenza != null) {
            LocalDate localDate = dataScadenza.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String formattedDate = localDate.toString(); // Formatta la data come preferisci

            dueDateButton.setText(formattedDate);
        } else {
            dueDateButton.setText("Data non disponibile");
        }

        Date dataInizio = taskStudente.getData_inizio();
        if (dataInizio != null) {
            LocalDate localDateInizio = dataInizio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String formattedDateInizio = localDateInizio.toString(); // Formatta la data come preferisci

            startDateButton.setText(formattedDateInizio);
        } else {
            startDateButton.setText("Data di inizio non disponibile");
        }





        //Setto la visibilità a GONE del calendario non appena viene visualizzato il fragment
        calendarView.setVisibility(View.GONE);



        String[] stato = getResources().getStringArray(R.array.StatoTask);

         // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        ArrayAdapter<String> adapterStato = new ArrayAdapter<>(context, R.layout.dropdown_item, stato);
        autoCompleteTextView.setAdapter(adapterStato);



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

        //Click sul tasto dei ricevimenti
        ricevimentiButton.setOnClickListener(view1 -> {
            args.putSerializable("Task", taskStudente);
            mNav.navigate(R.id.action_dettagli_task_to_ricevimenti_fragment,args);
            Log.d("Click ricevimenti","cliccato ricevimenti");
        });

        salvaButton.setOnClickListener(view1 -> {
            // Crea un AlertDialog per la conferma del salvataggio
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Conferma salvataggio");
            builder.setMessage("Sei sicuro di voler salvare le modifiche?");

            // Aggiungi un pulsante "Conferma" che effettuerà il salvataggio
            builder.setPositiveButton("Conferma", (dialog, which) -> {
                modificaDatiTask(); // Esegui il salvataggio
                dialog.dismiss(); // Chiudi il popup
            });

            // Aggiungi un pulsante "Annulla" che chiuderà il popup senza effettuare il salvataggio
            builder.setNegativeButton("Annulla", (dialog, which) -> {
                dialog.dismiss(); // Chiudi il popup
            });

            // Mostra il popup
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });



    }
    private void inizializzate_binding_text(){
        elem_text.put("titolo", binding.titoloTaskBar);
        elem_text.put("stato", binding.dropdownStatoTask);
        elem_text.put("data_inizio", binding.startDateBar);
        elem_text.put("data_scadenza", binding.dueDateBar);
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
        Button ricevimentiButton = view.findViewById(R.id.button_visualizza_ricevimenti_task);
        Button salvaButton = view.findViewById(R.id.button_salva_modifiche_task);

        //Calendario
        CalendarView calendarView = view.findViewById(R.id.calendarStartTaskView);

        //TextView
        TextView titoloTaskText = view.findViewById(R.id.titoloTaskTextView);
        TextView inizioTaskText = view.findViewById(R.id.startDateLabel);
        TextView scadenzaTaskText = view.findViewById(R.id.dueDateLabel);
        EditText barraTitoloTask = view.findViewById(R.id.titoloTaskBar);
        //Messaggi di errore
        TextView errorDueDate = view.findViewById(R.id.errorDueDate);
        //Dropdown
        TextInputLayout dropdownStatoTask = view.findViewById(R.id.dropdownStatoTask);



        //Setto la visibilità - Calendario VISIBILE
        calendarView.setVisibility(View.VISIBLE);

        //Button - Pulsanti INVISIBILI
        startDateButton.setVisibility(View.GONE);
        dueDateButton.setVisibility(View.GONE);
        ricevimentiButton.setVisibility(View.GONE);
        salvaButton.setVisibility(View.GONE);

        //TextView - TextView INVISIBLI
        autoCompleteTextView.setVisibility(View.GONE);
        inizioTaskText.setVisibility(View.GONE);
        scadenzaTaskText.setVisibility(View.GONE);
        titoloTaskText.setVisibility(View.GONE);
        barraTitoloTask.setVisibility(View.GONE);
        errorDueDate.setVisibility(View.GONE);
        dropdownStatoTask.setHint("");
    }

    /**
     * Con questo metodo nascondo il calendario dopo che l'utente seleziona una data e mostro nuovamente le barre
     * @param view corrisponde alla view originaria aggiornata
     */
    private void nascondiCalendario(@NonNull View view)
    {
        //Inizializzo le variabili
        //Button
        startDateButton = view.findViewById(R.id.startDateBar);
        dueDateButton = view.findViewById(R.id.dueDateBar);
        Button ricevimentiButton = view.findViewById(R.id.button_visualizza_ricevimenti_task);
        Button salvaButton = view.findViewById(R.id.button_salva_modifiche_task);

        //Calendar
        CalendarView calendarView = view.findViewById(R.id.calendarStartTaskView);

        //TextView
        TextView inizioTaskText = view.findViewById(R.id.startDateLabel);
        TextView scadenzaTaskText = view.findViewById(R.id.dueDateLabel);
        TextView titoloTaskText = view.findViewById(R.id.titoloTaskTextView);
        EditText barraTitoloTask = view.findViewById(R.id.titoloTaskBar);
        TextInputLayout dropdownStatoTask = view.findViewById(R.id.dropdownStatoTask);

        //Calendario - Calendario INVISIBILE
        calendarView.setVisibility(View.GONE);

        //Button - Pulsanti VISIBILI
        startDateButton.setVisibility(View.VISIBLE);
        dueDateButton.setVisibility(View.VISIBLE);
        ricevimentiButton.setVisibility(View.VISIBLE);
        salvaButton.setVisibility(View.VISIBLE);
        autoCompleteTextView.setVisibility(View.VISIBLE);
        dropdownStatoTask.setHint("Stato");

        //TextView - TextView INVISIBILI
        inizioTaskText.setVisibility(View.VISIBLE);
        scadenzaTaskText.setVisibility(View.VISIBLE);
        titoloTaskText.setVisibility(View.VISIBLE);
        barraTitoloTask.setVisibility(View.VISIBLE);
    }

    /**
     * Questo metodo permette di confrontare la data di inizio con la data di scadenza
     * @param view corrisponde alla view aggiornata nel caso di errore
     * @param startDateButton corrisponde al testo nella barra della data di inizio
     * @param dueDateButton corrisponde al testo nella barra della data di scadenza
     */
    private void confronta_data_inizio_scadenza(@NonNull View view, Button startDateButton,Button dueDateButton)
    {

        Button salvaButton = view.findViewById(R.id.button_salva_modifiche_task);


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
                salvaButton.setEnabled(false);
                salvaButton.setBackgroundResource(R.drawable.button_deactivated);

            }else {
                // Le date sono valide, nascondi gli errori
                errorDueDate.setVisibility(View.GONE);
                salvaButton.setEnabled(true);
                // Imposta nuovamente il Drawable personalizzato come background
                salvaButton.setBackgroundResource(R.drawable.button_blu);
            }
        } catch (ParseException e) {
            // Gestisci eccezione di parsing, ad esempio, quando il formato della data è errata
            e.printStackTrace();
            errorDueDate.setVisibility(View.VISIBLE);
        }
    }


    // Questo metodo viene chiamato quando l'utente vuole salvare le modifiche
    private void modificaDatiTask() {
        // Esegui il recupero dei dati inseriti dall'utente
        String nuovoTitolo = binding.titoloTaskBar.getText().toString();
        String nuovoStato = autoCompleteTextView.getText().toString();
        String nuovaDataInizio = startDateButton.getText().toString();
        String nuovaDataScadenza = dueDateButton.getText().toString();

        if (nuovoTitolo.isEmpty()) {
            // Se il titolo è vuoto, mostra un messaggio di errore
            showToast(context, "Il titolo non può essere vuoto");
        } else {
            // Aggiorna gli oggetti taskStudente con i nuovi dati
            taskStudente.setTitolo(nuovoTitolo);
            taskStudente.setStato(nuovoStato);

            try {
                taskStudente.setData_inizio(stringToTimestamp(nuovaDataInizio));
                taskStudente.setData_scadenza(stringToTimestamp(nuovaDataScadenza));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Salva i dati nel database
            salvaDatiTaskStudente(taskStudente);

            // Puoi anche aggiungere un messaggio di successo o altre azioni qui.
            showToast(context, "Modifiche effettuate con successo");

        }
    }


    private void salvaDatiTaskStudente(TaskStudente taskStudente) {
        // Ottieni un riferimento alla collezione "Task" nel tuo database Firestore
        CollectionReference taskRef = db.collection("TaskStudente");

        // Recupera un riferimento al documento in base all'ID della task
        Long taskId = taskStudente.getId_task(); // Assumi che taskStudente abbia un campo "id_task" per identificare la task
        Query query = taskRef.whereEqualTo("id_task", taskId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Ottieni l'ID del documento trovato
                    String documentId = document.getId();

                    // Crea un oggetto con i dati da salvare
                    Map<String, Object> datiTask = new HashMap<>();
                    datiTask.put("titolo", taskStudente.getTitolo());
                    datiTask.put("stato", taskStudente.getStato());
                    datiTask.put("data_inizio", taskStudente.getData_inizio());
                    datiTask.put("data_scadenza", taskStudente.getData_scadenza());

                    // Esegui l'operazione di aggiornamento del documento
                    db.collection("TaskStudente").document(documentId)
                            .update(datiTask)
                            .addOnSuccessListener(aVoid -> {
                                // Aggiornamento avvenuto con successo
                                Log.d("Firestore", "Dati aggiornati con successo.");
                            })
                            .addOnFailureListener(e -> {
                                // Gestione degli errori in caso di fallimento dell'aggiornamento
                                Log.e("Firestore", "Errore nell'aggiornamento dei dati: " + e.getMessage());
                            });
                }
            } else {
                Log.e("Firestore", "Errore nella ricerca del documento: " + task.getException());
            }
        });
    }


}