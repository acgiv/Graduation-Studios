package com.laureapp.ui.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.laureapp.R;
import com.laureapp.databinding.FragmentRegister2Binding;
import com.laureapp.ui.MainActivity;
import com.laureapp.ui.controlli.ControlInput;
import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Register2Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView autoCompleteTextViewcorso;
    private final int error_color = com.google.android.material.R.color.design_default_color_error;
    private final Register2Fragment.CustomTextWatcher textWatcher = new Register2Fragment.CustomTextWatcher();
    private final HashMap<String, Object> elem_text = new HashMap<>();
    private FirebaseAuth mAuth;
    private String[] facolta;
    private String[] corsi;
    private Utente ut;
    private String ruolo;
    FragmentRegister2Binding binding;
    private Context context;
    Bundle bundle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = requireContext();
        binding = FragmentRegister2Binding.inflate(inflater, container, false);
        autoCompleteTextView = binding.filledExposedDropdown;
        autoCompleteTextViewcorso = binding.dropdownCorso;
        // Inizializza il mapping degli elementi di testo associandoli al binding
        inizializzate_binding_text();

        // Configura gli ascoltatori per il cambiamento di testo negli elementi di input
        setupTextWatchers();


        bundle = getArguments();
        if (bundle != null) {
            ruolo = bundle.getString("ruolo");
            ut = bundle.getSerializable("utente", Utente.class);
        }
        mAuth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        facolta = getResources().getStringArray(R.array.Dipartimento);
        corsi = getResources().getStringArray(R.array.Corsi);
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        ArrayAdapter<String> adapterfacolta = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, facolta);
        autoCompleteTextView.setAdapter(adapterfacolta);
        ArrayAdapter<String> adaptercorso = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, corsi);
        autoCompleteTextViewcorso.setAdapter(adaptercorso);



       binding.buttonRegister.setOnClickListener(view1 -> {


               // Controlla i campi di input e stampa i risultati del controllo
                AtomicInteger cont = new AtomicInteger(0);
               elem_text.forEach((key, values) -> {
                   if (Boolean.TRUE.equals(is_correct_form(values))) {
                       cont.addAndGet(1);
                   }
               });
            FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
            if (StringUtils.equals(ruolo, "Studente") && cont.get()==5){

                //Per salavare i dati in authentication
                createAccount();
                saveStudenteToFirestore(firestoreDB);


                Intent HomeActivity = new Intent(requireActivity(), MainActivity.class);
                HomeActivity.putExtras(bundle);
                startActivity(HomeActivity);
                requireActivity().finish();
            }else{
                Intent HomeActivity = new Intent(requireActivity(), MainActivity.class);
                HomeActivity.putExtras(bundle);
                startActivity(HomeActivity);
                requireActivity().finish();
            }

        });
    }

    /**
     * Crea l'account dell'utente su firebase

     */
    private  void createAccount()  {
        mAuth.createUserWithEmailAndPassword(ut.getEmail(),ut.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Registrazione avvenuta con successo, puoi eseguire ulteriori azioni qui
                        mAuth.getCurrentUser();
                    } else {
                        // La registrazione ha fallito, puoi gestire l'errore qui
                        Exception exception = task.getException();
                        assert exception != null;
                        Toast.makeText(getContext(), exception.toString(), Toast.LENGTH_SHORT).show();
                        // Esempio: Visualizzare un messaggio di errore o registrare l'errore
                    }
                });
    }

    private void saveStudenteToFirestore(FirebaseFirestore firestoreDB)  {

        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        CompletableFuture<Long> future = new CompletableFuture<>();

        UtenteModelView ut_vew = new UtenteModelView(context);
        ut_vew.insertUtente(ut);
        ut.setId(ut_vew.getIdUtente(ut.getEmail()));

        firestoreDB.collection("Utenti").document(uid).set(ut.getUtenteMap())
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error writing document");
                    }
                });


            // Qui puoi creare e impostare il tuo oggetto Studente
            Studente studente = new Studente();
            studente.setId_utente(ut.getId());
            studente.setMatricola(Long.valueOf(Objects.requireNonNull(binding.matricolaRegister.getText()).toString()));
            studente.setFacolta(Objects.requireNonNull(binding.filledExposedDropdown.getText()).toString());
            studente.setEsami_mancati(Integer.parseInt(Objects.requireNonNull(binding.esamiMancantiRegister.getText()).toString()));
            studente.setCorso_laurea(binding.dropdownCorso.getText().toString());

            firestoreDB.collection("Utenti").document("Studenti").collection("Studenti").document(uid).set(studente.getStudenteMap())
                    .addOnSuccessListener(aVoid -> {
                        StudenteModelView st_db = new StudenteModelView(context);
                        st_db.insertStudente(studente);
                        Log.d("studenti", String.valueOf(st_db.getAllStudente()));
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Error writing document");
                        }
                    });

    }



    private void inizializzate_binding_text(){
        elem_text.put("matricola", binding.matricolaRegister);
        elem_text.put("mancanti", binding.esamiMancantiRegister);
        elem_text.put("media", binding.mediaRegister);
        elem_text.put("Corsi", binding.dropdownCorso);
        elem_text.put("Dipartimento", binding.filledExposedDropdown);
    }

    private void setupTextWatchers(){
        // Configura i TextWatcher per tutti gli elementi nella mappa elem_text
        elem_text.forEach((key, value)->  {
            if (value instanceof TextInputEditText) {
                ((TextInputEditText) value).addTextChangedListener(textWatcher);
            } else if (value instanceof MaterialAutoCompleteTextView) {
                ((MaterialAutoCompleteTextView) value).addTextChangedListener(textWatcher);
            }});

        // Imposta il listener di focus per ogni elemento di input
        elem_text.forEach((key, value)-> {
            if (value instanceof TextInputEditText) {
                ((TextInputEditText) value).setOnFocusChangeListener((view, hasFocus) -> {
                    if (hasFocus) {
                        textWatcher.setComponent(value);
                    }
                });
            }if (value instanceof MaterialAutoCompleteTextView) {
                ((MaterialAutoCompleteTextView) value).setOnFocusChangeListener((view, hasFocus) -> {
                    if (hasFocus) {
                        textWatcher.setComponent(value);
                    }
                });
            }
        });

    }

    private  boolean is_correct_form(Object component) {
        boolean result_error = false;
        String hint = getHintText(component);
        StudenteModelView st = new StudenteModelView(context);
        Log.d("fix_error","sono qui "+ getHintText(component));
        if(!is_empty_string(component, getInputText(component),  hint)) {
            switch (hint){
                case "Matricola":
                    if (!ControlInput.is_correct_matricola(Objects.requireNonNull(binding.matricolaRegister.getText()).toString())) {
                        String error_message = getString(R.string.errore_matricola).replace("{campo}", getString(R.string.matricola));
                        ControlInput.set_error(binding.matricolaInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error, getResources());
                    } else {
                        Long presente = st.findStudenteMatricola(Long.valueOf(Objects.requireNonNull(binding.matricolaRegister.getText()).toString())) ;
                        if (presente == null){
                            ControlInput.set_error(binding.matricolaInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height, getResources());
                            return true;
                        }else{
                            String error_message = getString(R.string.errore_matricola_duplicate);
                            ControlInput.set_error(binding.matricolaInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error, getResources());
                        }
                    }
                    break;
                case "Media":
                    String media =Objects.requireNonNull(binding.mediaRegister.getText()).toString();
                    if (StringUtils.isNumeric(media) && (Integer.parseInt(media)) >=0 && Integer.parseInt(media) <= 30) {
                        ControlInput.set_error(binding.mediaInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height, getResources());
                        return true;
                    } else {
                        String error_message = getString(R.string.errore_media).replace("{campo}", getString(R.string.media));
                        ControlInput.set_error(binding.mediaInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error_email, getResources());
                    }
                    break;
                case "Esami mancanti":
                    String esami =Objects.requireNonNull(binding.esamiMancantiRegister.getText()).toString();
                    if (StringUtils.isNumeric(esami) && (Integer.parseInt(esami)) >=0 && Integer.parseInt(esami) <= 40) {
                        ControlInput.set_error(binding.esamiMancantiInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height, getResources());
                        return true;
                    } else {
                        String error_message = getString(R.string.errore_media).replace("{campo}", getString(R.string.media)).replace("30", "40");
                        ControlInput.set_error(binding.esamiMancantiInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error_email, getResources());
                    }
                    break;
                case "Facoltà":
                    String facolta_text =Objects.requireNonNull(binding.filledExposedDropdown.getText()).toString();
                    boolean isFacoltaTextEqual = Arrays.stream(facolta)
                            .anyMatch(s -> StringUtils.equals(s, facolta_text));
                    if (isFacoltaTextEqual) {
                        ControlInput.set_error(binding.facoltaInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height, getResources());
                        return true;
                    } else {
                        String error_message = getString(R.string.errore_scelta);
                        ControlInput.set_error(binding.facoltaInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error_email, getResources());
                    }
                    break;
                case "Corso di Laurea":
                    String corso_text =Objects.requireNonNull(binding.dropdownCorso.getText()).toString();
                    boolean iscorsiTextEqual = Arrays.stream(corsi)
                            .anyMatch(s -> StringUtils.equals(s, corso_text));
                    if (iscorsiTextEqual) {
                        ControlInput.set_error(binding.corsoInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height, getResources());
                        return true;
                    } else {
                        String error_message = getString(R.string.errore_scelta);
                        ControlInput.set_error(binding.corsoInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error_email, getResources());
                    }
                    break;
            }
        }
        return result_error;
    }

    private String getHintText(Object component) {
        String text = null;
        if (component instanceof TextInputEditText) {
            text = Objects.requireNonNull(((TextInputEditText) component).getHint()).toString();
        } else if (component instanceof MaterialAutoCompleteTextView) {
            text = Objects.requireNonNull(((MaterialAutoCompleteTextView) component).getHint()).toString();
        }
        return text;
    }

    private TextInputLayout getInputText(Object component){

        switch (getHintText(component)){
            case "Matricola":
                return binding.matricolaInput;
            case "Media":
                return binding.mediaInput;
            case "Esami mancanti":
                return binding.esamiMancantiInput;
            case "Facoltà":
                return binding.facoltaInput;
            case "Corso di Laurea":
                return  binding.corsoInput;
        }
        return null;
    }




    /**
     * Una classe personalizzata che implementa l'interfaccia TextWatcher per monitorare le modifiche del testo in un TextInputEditText.
     */

        private  class CustomTextWatcher implements TextWatcher {
            Object component;

            public CustomTextWatcher(){

            }

            /**
             * Chiamato prima che il testo nel campo di input cambi.
             *
             * @param charSequence Il testo attuale prima della modifica.
             * @param i L'indice del carattere iniziale della modifica.
             * @param i1 Il numero di caratteri da rimuovere.
             * @param i2 Il numero di caratteri da inserire.
             */
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nessuna azione richiesta prima della modifica del testo.
            }

            /**
             * Chiamato quando il testo nel campo di input sta cambiando.
             *
             * @param charSequence Il testo attuale dopo la modifica.
             * @param i L'indice del carattere iniziale della modifica.
             * @param i1 Il numero di caratteri da rimuovere.
             * @param i2 Il numero di caratteri da inserire.
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (component != null)
                is_correct_form(component);
            }

            /**
             * Chiamato dopo che il testo nel campo di input è stato modificato.
             *
             * @param editable Il testo modificato dopo la modifica.
             */
            @Override
            public void afterTextChanged(Editable editable) {
                // Nessuna azione richiesta dopo la modifica del testo.
            }

            /**
             * Imposta l'EditText associato a questa istanza di CustomTextWatcher.
             *
             */
            public void setComponent(Object component) {
                this.component = component;
            }
        }



    private Boolean is_empty_string(Object component, TextInputLayout layout_input, String campo_error){
        String text = null;
        String error_message;

        if (component instanceof TextInputEditText){
            text= Objects.requireNonNull(((TextInputEditText) component).getText()).toString();
        }else if (component instanceof MaterialAutoCompleteTextView) {
            text= Objects.requireNonNull(((MaterialAutoCompleteTextView) component).getText()).toString();
        }

        if(StringUtils.isEmpty(text)){
            error_message = getString(R.string.errore_campo_vuoto).replace("{campo}", campo_error);
            ControlInput.set_error(layout_input, true, error_message, error_color, context, R.dimen.input_text_layout_height_error, getResources());
            if (component instanceof View) {
                ((View) component).requestFocus();
            }
           return true;
        }
        return false;
    }

}