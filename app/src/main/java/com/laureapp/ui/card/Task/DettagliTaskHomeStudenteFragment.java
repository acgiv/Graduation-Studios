package com.laureapp.ui.card.Task;

import static androidx.databinding.DataBindingUtil.setContentView;
import static com.laureapp.ui.controlli.ControlInput.showToast;
import static com.laureapp.ui.roomdb.Converters.stringToTimestamp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laureapp.R;
import com.laureapp.databinding.FragmentDettagliTaskBinding;
import com.laureapp.databinding.FragmentDettagliTaskHomeBinding;
import com.laureapp.ui.roomdb.entity.TaskStudente;

import org.apache.commons.lang3.StringUtils;

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
public class DettagliTaskHomeStudenteFragment extends Fragment {


    private NavController mNav;
    private AutoCompleteTextView autoCompleteTextView;
    private FragmentDettagliTaskHomeBinding binding;
    private FirebaseFirestore db;
    private Bundle args;
    private TaskStudente taskStudente;
    private Context context;
    private Button startDateButton;
    private Button dueDateButton;
    String ruolo;

    private final HashMap<String, Object> elem_text = new HashMap<>();


    public DettagliTaskHomeStudenteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
        if(args != null) {

            taskStudente = (TaskStudente)args.getSerializable("SelectedTask");

            ruolo = args.getString("ruolo");
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

        binding = FragmentDettagliTaskHomeBinding.inflate(inflater, container, false);
        // Inizializza le variabili nel metodo onCreate
        startDateButton = binding.startDateBar;
        dueDateButton = binding.dueDateBar;
        autoCompleteTextView = binding.filledExposedDropdown;
        context = requireContext();

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mNav = Navigation.findNavController(view);
        //Inizializzazione delle variabili
        EditText titoloTask = binding.titoloTaskBar;
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


        //Se è uno studente disattivo tutti i campi tranne lo stato e rendo invisibile il pulsante ricevimenti
            // Setta il titolo della task non modificabile
            titoloTask.setFocusable(false);
            titoloTask.setFocusableInTouchMode(false);
            titoloTask.setClickable(false);


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