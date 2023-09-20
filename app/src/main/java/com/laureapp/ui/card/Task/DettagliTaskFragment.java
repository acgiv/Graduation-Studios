package com.laureapp.ui.card.Task;

import static com.laureapp.ui.roomdb.Converters.stringToTimestamp;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.laureapp.R;
import com.laureapp.databinding.FragmentDettagliTaskBinding;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DettagliTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DettagliTaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private NavController mNav;
    private AutoCompleteTextView autoCompleteTextView;
    FragmentDettagliTaskBinding binding;
    Bundle bundle;
    private final HashMap<String, Object> elem_text = new HashMap<>();


    public DettagliTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DettagliTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DettagliTaskFragment newInstance(String param1, String param2) {
        DettagliTaskFragment fragment = new DettagliTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context context = requireContext();
        binding = FragmentDettagliTaskBinding.inflate(inflater, container, false);
        autoCompleteTextView = binding.filledExposedDropdown;



        /*// Trova il tuo MaterialAutoCompleteTextView e TextInputLayout
        TextInputLayout textInputLayout = binding.getRoot().findViewById(R.id.dropdownStatoTask);

        // Nascondi la freccia del dropdown menu
        Drawable transparentDrawable = new ColorDrawable(android.graphics.Color.TRANSPARENT);
        autoCompleteTextView.setDropDownBackgroundDrawable(transparentDrawable);
        textInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
        */

        // Inizializza il mapping degli elementi di testo associandoli al binding
        inizializzate_binding_text();

        // Configura gli ascoltatori per il cambiamento di testo negli elementi di input


        bundle = getArguments();

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        mNav = Navigation.findNavController(view);
        //Inizializzazione delle variabili
        Button startDateButton = view.findViewById(R.id.startDateBar);
        Button dueDateButton = view.findViewById(R.id.dueDateBar);
        Button ricevimentiButton = view.findViewById(R.id.button_visualizza_ricevimenti_task);
        Button salvaButton = view.findViewById(R.id.button_salva_modifiche_task);

        CalendarView calendarView = view.findViewById(R.id.calendarStartTaskView);

        //Setto la visibilità a GONE del calendario non appena viene visualizzato il fragment
        calendarView.setVisibility(View.GONE);

        //salvo in questa variabile la lista del dropdown menu dello stato
        String[] stato = getResources().getStringArray(R.array.StatoTask);



         // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        ArrayAdapter<String> adapterStato = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, stato);
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
            mNav.navigate(R.id.action_dettagli_task_to_ricevimenti_fragment);
            Log.d("Click ricevimenti","cliccato ricevimenti");
        });

        //TODO: Implementare il salvataggio sul db delle modifiche con un'apposita finestra di dialogo
        salvaButton.setOnClickListener(view1 -> {
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
        Button startDateButton = view.findViewById(R.id.startDateBar);
        Button dueDateButton = view.findViewById(R.id.dueDateBar);
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

}