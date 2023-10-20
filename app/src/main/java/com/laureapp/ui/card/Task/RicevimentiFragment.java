package com.laureapp.ui.card.Task;

import static android.view.View.VISIBLE;
import static com.laureapp.ui.controlli.ControlInput.showToast;
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
import com.laureapp.databinding.FragmentRicevimentiBinding;
import com.laureapp.databinding.FragmentTaskBinding;
import com.laureapp.ui.card.Adapter.RicevimentiAdapter;
import com.laureapp.ui.card.Adapter.TaskStudenteAdapter;
import com.laureapp.ui.card.Adapter.TaskTesiAdapter;
import com.laureapp.ui.roomdb.QueryFirestore;
import com.laureapp.ui.roomdb.entity.Ricevimenti;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteTesi;
import com.laureapp.ui.roomdb.entity.TaskStudente;
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
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class RicevimentiFragment extends Fragment {



    public RicevimentiFragment() {
        // Required empty public constructor
    }

    private NavController mNav;

    static Context context;

    FragmentRicevimentiBinding binding;



    // Dichiarazione di una variabile di istanza per il dialog
    private AlertDialog alertDialog;
    private static RicevimentiAdapter adapter;

    private static FirebaseFirestore db;
    private Bundle args;
    private TaskStudente taskStudente;

    private static List<Ricevimenti> ricevimentiList = new ArrayList<>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inizializza il binding
        binding = com.laureapp.databinding.FragmentRicevimentiBinding.inflate(inflater, container, false);
        context = requireContext();
        args = getArguments();


        if(args != null) {

            taskStudente = args.getSerializable("SelectedTask", TaskStudente.class);
            //Carico i dati delle task in base all'utente loggato
        }
        // Altri codici del tuo fragment
        db = FirebaseFirestore.getInstance();

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageButton addButton = view.findViewById(R.id.add_ricevimento_ImageButton);
        mNav = Navigation.findNavController(view);
        ListView listRicevimentoView = view.findViewById(R.id.listRicevimentiView);
        TextView titleTask = view.findViewById(R.id.titleTextView);
        titleTask.setText(taskStudente.getTitolo());


        adapter = new RicevimentiAdapter(context, ricevimentiList,mNav);

        loadRicevimentiForTaskId(taskStudente.getId_task());

        addButton.setOnClickListener(view1 ->
                showInputDialog()
        );

        listRicevimentoView.setAdapter(adapter);

    }



    /**
     * Metodo per mostrare il pop-up in un'attività o fragment
     */
    public void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Nuovo ricevimento");

        // Includi il layout XML personalizzato
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.add_ricevimento_popup, null);
        builder.setView(view);

        EditText editTextArgomento = view.findViewById(R.id.editTextArgomento);

        CalendarView calendarDataView = view.findViewById(R.id.calendarAddRicevimentoView);

        //Button
        Button dataRicevimentoButton = view.findViewById(R.id.inserisciDataRicevimentoButton);

        //Setto la visibilità a GONE del calendario non appena viene visualizzato il fragment
        calendarDataView.setVisibility(View.GONE);



        //Click sulla data di inizio task
        dataRicevimentoButton.setOnClickListener(view1 -> {

            mostraCalendario(view);

            calendarDataView.setOnDateChangeListener((v, year, month, dayOfMonth) -> {
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                nascondiCalendario(view);
                dataRicevimentoButton.setText(selectedDate);
            });
        });




        // Aggiungi un ascoltatore personalizzato al pulsante OK
        builder.setPositiveButton("Ok", (dialog, which) -> {
            String argomento = editTextArgomento.getText().toString();
            //effettuo la conversione della stringa in data
            Timestamp dataRicevimento;

            try {

                dataRicevimento = stringToTimestamp(dataRicevimentoButton.getText().toString());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }


            //Aggiungo la task a Firestore in base all'utente loggato
            addRicevimentoToFirestore(taskStudente.getId_task(), argomento, dataRicevimento);
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
        Button dataRicevimentoButton = view.findViewById(R.id.inserisciDataRicevimentoButton);

        //Calendario
        CalendarView calendarAddRicevimentoView = view.findViewById(R.id.calendarAddRicevimentoView);

        //TextView
        EditText barraArgomentoTask = view.findViewById(R.id.editTextArgomento);


        //Button - Pulsanti INVISIBILI
        dataRicevimentoButton.setVisibility(View.GONE);


        //TextView - TextView INVISIBLI
        barraArgomentoTask.setVisibility(View.GONE);

        //Calendar - Calendario VISIBILE
        calendarAddRicevimentoView.setVisibility(VISIBLE);
    }

    /**
     * Con questo metodo nascondo il calendario dopo che l'utente seleziona una data e mostro nuovamente le barre
     * @param view corrisponde alla view originaria aggiornata
     */
    private void nascondiCalendario(@NonNull View view)
    {
        //Inizializzo le variabili
        //Button
        Button dataRicevimentoButton = view.findViewById(R.id.inserisciDataRicevimentoButton);

        //Calendario
        CalendarView calendarAddRicevimentoView = view.findViewById(R.id.calendarAddRicevimentoView);

        //TextView
        EditText barraArgomentoTask = view.findViewById(R.id.editTextArgomento);

        //Calendario - Calendario INVISIBILE
        calendarAddRicevimentoView.setVisibility(View.GONE);

        //Button - Pulsanti VISIBILI
        dataRicevimentoButton.setVisibility(VISIBLE);

        //TextView - TextView VISIBILI
        barraArgomentoTask.setVisibility(VISIBLE);

    }



    /**
     * Questo metodo mi permette di caricare da firestore le task dando come parametro l'id della tesi
     *
     * @param id_task id della tesi nella tabella Tesi
     * @return la lista delle task in base all'id della tesi fornito
     */
    private Task<List<Ricevimenti>> loadRicevimentiByTaskId(Long id_task) {
        return FirebaseFirestore.getInstance()
                .collection("Ricevimenti")
                .whereEqualTo("id_task", id_task)
                .get()
                .continueWith(task -> {
                    List<Ricevimenti> ricevimentiList = new ArrayList<>();

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Ricevimenti ricevimenti = new Ricevimenti();
                            ricevimenti.setId_task(doc.getLong("id_task"));
                            ricevimenti.setArgomento(doc.getString("argomento"));
                            ricevimenti.setId_ricevimento(doc.getLong("id_ricevimento"));
                            ricevimenti.setData_ricevimento(Objects.requireNonNull(doc.getTimestamp("data_ricevimento")).toDate());

                            ricevimentiList.add(ricevimenti);
                        }

                        return ricevimentiList;
                    } else {
                        throw new NoSuchElementException("Studente non trovato con l'ID utente: " + id_task);
                    }
                });
    }


    /*
        METODI PER LA LETTURA DEI DATI E LA LORO VISUALIZZAZIONE NELL'ADAPTER
     */

    /**
     * Questo metodo permette di caricare le task in base all'id della tesi
     * @param id_task è l'id della tesi nelle tasks
     */
    private void loadRicevimentiForTaskId(Long id_task) {
        loadRicevimentiByTaskId(id_task).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ricevimentiList.clear();
                addTasksToList(task.getResult());
            } else {
                showToast(context, "Dati task non caricati correttamente");
            }
        });
    }



    /**
     * Questo metodo aggiunge le tasks alla lista delle task e aggiorna l'adapter permettendo
     * la visualizzazione delle tasks.
     * @param ricevimenti è la lista dei ricevimenti
     */
    private static void addTasksToList(List<Ricevimenti> ricevimenti) {
        for (Ricevimenti ricevimento : ricevimenti) {
            if (ricevimento != null) {
                ricevimentiList.add(ricevimento);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /*
        Metodi per l'aggiunta e visualizzazione di una nuova task
    */


    private void addRicevimentoToFirestore(Long id_task, String argomento, Timestamp dataRicevimento) {
        CollectionReference ricevimentiRef = db.collection("Ricevimenti");
        QueryFirestore queryFirestore = new QueryFirestore();
        Map<String, Object> ricevimentiData = new HashMap<>();
        queryFirestore.trovaIdRicevimentiMax(context)
                .thenAccept(idMax -> {
                    idMax = idMax + 1L;
                    ricevimentiData.put("id_ricevimento", idMax);
                    ricevimentiData.put("argomento", argomento);
                    ricevimentiData.put("data_ricevimento", dataRicevimento);
                    ricevimentiData.put("id_task", id_task);

                    // Supponendo che 'taskRef' sia un oggetto valido di tipo CollectionReference
                    Long finalIdMax = idMax;
                    ricevimentiRef.document().set(ricevimentiData).addOnSuccessListener(
                            aVoid -> addTaskToList(finalIdMax, argomento, dataRicevimento, id_task)
                    ).addOnFailureListener(e -> {
                        // Gestisci l'errore
                    });
                });
    }

    /**
     * Metodo per l'aggiunta di una nuova task alla lista di partenza
     * @param id_ricevimento id della task da aggiungere alla lista
     * @param argomento è il titolo della task
     * @param dataRicevimento data inizio inserita
     * @param id_task data scadenza inserita
     */
    private void addTaskToList(Long id_ricevimento, String argomento, Timestamp dataRicevimento, Long id_task) {

        Ricevimenti ricevimenti = new Ricevimenti();
        ricevimenti.setId_ricevimento(id_ricevimento);
        ricevimenti.setArgomento(argomento);
        ricevimenti.setData_ricevimento(dataRicevimento);
        ricevimenti.setId_task(id_task);


        // Aggiungi taskTesi alla lista
        ricevimentiList.add(ricevimenti);
        adapter.notifyDataSetChanged();
    }

    public static void deleteRicevimento(int position) {
        if (position >= 0 && position < ricevimentiList.size()) {
            Ricevimenti ricevimentoToDelete = ricevimentiList.get(position);

            // Rimuovi la task da Firestore
            deleteRicevimentoFromFirestore(ricevimentoToDelete.getId_ricevimento());

            // Rimuovi la task dalla lista locale
            ricevimentiList.remove(position);
            adapter.notifyDataSetChanged();
        } else {
            showToast(context, "Posizione non valida o elemento non trovato");
        }
    }


    private static void deleteRicevimentoFromFirestore(Long id_ricevimento) {
        // Ottieni il riferimento al documento della task da eliminare
        CollectionReference ricevimentiRef = db.collection("Ricevimenti");
        ricevimentiRef.whereEqualTo("id_ricevimento", id_ricevimento).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                doc.getReference().delete().addOnSuccessListener(aVoid -> {
                    showToast(context, "Ricevimento eliminato con successo");
                }).addOnFailureListener(e -> {
                    showToast(context, "Errore durante l'eliminazione del ricevimento");
                });
            } else {
                showToast(context, "Ricevimento non trovato nel database");
            }
        });
    }


}