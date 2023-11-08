package com.laureapp.ui.register;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.laureapp.R;
import com.laureapp.databinding.FragmentRegister2Binding;
import com.laureapp.ui.MainActivity;
import com.laureapp.ui.controlli.ControlInput;
import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.viewModel.ProfessoreModelView;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Questa classe rappresenta un frammento utilizzato nella seconda fase del processo di registrazione di un nuovo utente nell'applicazione.
 * Gestisce le informazioni relative alla facoltà e ai corsi di studio selezionati durante la registrazione.
 */
public class Register2Fragment extends Fragment {


    private AutoCompleteTextView autoCompleteTextView;
    private AutoCompleteTextView autoCompleteTextViewcorso;
    private MultiAutoCompleteTextView professoreTextViewcorso;
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
        bundle = getArguments();
        if (bundle != null) {
            ruolo = bundle.getString("ruolo");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ut = bundle.getSerializable("utente", Utente.class);
            }
        }
        autoCompleteTextView = binding.filledExposedDropdown;
        autoCompleteTextViewcorso = binding.dropdownCorso;
        professoreTextViewcorso = binding.dropdownprofessoreCorso;
        corsi = getResources().getStringArray(R.array.Corsi);
        inizializzate_binding_text();

        // Configura gli ascoltatori per il cambiamento di testo negli elementi di input
        setupTextWatchers();


        mAuth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.facoltaInput.setVisibility(View.VISIBLE);
        facolta = getResources().getStringArray(R.array.Dipartimento);
        ArrayAdapter<String> adapterfacolta = new ArrayAdapter<>(context, R.layout.dropdown_item, facolta);
        autoCompleteTextView.setAdapter(adapterfacolta);

        if (StringUtils.equals(ruolo, getString(R.string.professore))){
            binding.mediaInput.setVisibility(View.GONE);
            binding.esamiMancantiInput.setVisibility(View.GONE);
            binding.corsoInput.setVisibility(View.GONE);
            binding.corsoprofessoreInput.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),  android.R.layout.simple_dropdown_item_1line, corsi);
            professoreTextViewcorso.setAdapter(adapter);
            professoreTextViewcorso.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            professoreTextViewcorso.setKeyListener(null);

        }else {
            corsi = getResources().getStringArray(R.array.Corsi);
            ArrayAdapter<String> adaptercorso = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, corsi);
            autoCompleteTextViewcorso.setAdapter(adaptercorso);
        }
        binding.buttonRegister.setOnClickListener(view1 -> {
            createAccount();
        });
    }

    /**
     * Questo metodo gestisce il processo di creazione di un account utente nell'applicazione.
     * Verifica se tutti i campi di input sono corretti e, in base al ruolo dell'utente, effettua la registrazione
     * utilizzando l'indirizzo email e la password forniti. Successivamente, memorizza le informazioni utente nel database Firestore
     * e reindirizza l'utente alla schermata principale dell'applicazione.
     */
    private  void createAccount()  {
        AtomicInteger cont = new AtomicInteger(0);
        elem_text.forEach((key, values) -> {
            if (Boolean.TRUE.equals(is_correct_form(values))) {
                cont.addAndGet(1);
            }
        });
        Log.d("cont", String.valueOf(cont.get()));
        if (StringUtils.equals(ruolo, getString(R.string.studente)) && cont.get()==5){
            mAuth.createUserWithEmailAndPassword(ut.getEmail(), ut.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
                            // Registrazione avvenuta con successo, puoi eseguire ulteriori azioni qui
                            ut.setId_utente(saveToFirestore( mAuth.getCurrentUser(), firestoreDB));
                            Intent HomeActivity = new Intent(requireActivity(), MainActivity.class);
                            bundle.putSerializable("email", ut.getEmail());
                            HomeActivity.putExtras(bundle);
                            startActivity(HomeActivity);
                            requireActivity().finish();

                        } else {
                            Exception exception = task.getException();
                            assert exception != null;
                            Toast.makeText(getContext(), exception.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }else if (StringUtils.equals(ruolo, getString(R.string.professore)) && cont.get()==3){
            mAuth.createUserWithEmailAndPassword(ut.getEmail(), ut.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
                            ut.setId_utente(saveToFirestore( mAuth.getCurrentUser(), firestoreDB));
                            Intent HomeActivity = new Intent(requireActivity(), MainActivity.class);
                            bundle.putSerializable("email", ut.getEmail());
                            HomeActivity.putExtras(bundle);
                            startActivity(HomeActivity);
                            requireActivity().finish();
                        } else {
                            Exception exception = task.getException();
                            assert exception != null;
                            Toast.makeText(getContext(), exception.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    /**
     * Questo metodo salva le informazioni dell'utente nel database Firestore dopo la creazione dell'account.
     *
     * @param currentUser L'utente corrente autenticato.
     * @param firestoreDB L'istanza del database Firestore.
     * @return L'ID dell'utente memorizzato nel database Firestore.
     */
    private Long saveToFirestore(FirebaseUser currentUser,  FirebaseFirestore firestoreDB)  {

        if (currentUser != null) {
            String uid = currentUser.getUid();
            UtenteModelView ut_vew = new UtenteModelView(context);
            ut.setFacolta(Objects.requireNonNull(binding.filledExposedDropdown.getText()).toString());
            if (StringUtils.equals(ruolo, getString(R.string.professore))){
                ut.setNome_cdl(binding.dropdownprofessoreCorso.getText().toString());
            }else if (StringUtils.equals(ruolo, getString(R.string.professore))){
                ut.setNome_cdl(binding.dropdownCorso.getText().toString());
            }

            ut_vew.insertUtente(ut);
            ut.setId_utente(ut_vew.getIdUtente(ut.getEmail()));
            firestoreDB.collection("Utenti").document(uid).set(ut.getUtenteMap())
                    .addOnSuccessListener(aVoid -> {
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Error writing document");
                        }
                    });
            if (StringUtils.equals(ruolo, getString(R.string.studente))){
                // Qui puoi creare e impostare il tuo oggetto Studente
                Studente studente = new Studente();
                studente.setId_utente(ut.getId_utente());
                studente.setMatricola(Long.valueOf(Objects.requireNonNull(binding.matricolaRegister.getText()).toString()));
                studente.setEsami_mancanti(Integer.parseInt(Objects.requireNonNull(binding.esamiMancantiRegister.getText()).toString()));
                studente.setMedia((Integer.parseInt(Objects.requireNonNull(binding.mediaRegister.getText()).toString())));
                StudenteModelView st_db = new StudenteModelView(context);
                st_db.insertStudente(studente);
                firestoreDB.collection("Utenti").document("Studenti").collection("Studenti").document(uid).set(studente.getStudenteMap())
                        .addOnSuccessListener(aVoid -> {
                        });
            }else if(StringUtils.equals(ruolo, getString(R.string.professore))){
                Professore professore = new Professore();
                professore.setId_utente(ut.getId_utente());
                professore.setMatricola(Long.valueOf(Objects.requireNonNull(binding.matricolaRegister.getText()).toString()));
                ProfessoreModelView pr_db = new ProfessoreModelView(context);
                pr_db.insertProfessore(professore);
                firestoreDB.collection("Utenti").document("Professori").collection("Professori")
                        .document(uid).set(professore.getProfessoreMap())
                        .addOnSuccessListener(aVoid -> {
                        });
            }
        }
        return ut.getId_utente();
    }

    /**
     * Questo metodo inizializza la mappa "elem_text" associando i campi di input ai rispettivi identificatori.
     * Questa mappa è utilizzata per gestire dinamicamente i campi di input in base al ruolo dell'utente.
     */
    private void inizializzate_binding_text(){
        elem_text.put("matricola", binding.matricolaRegister);
        elem_text.put("Dipartimento", binding.filledExposedDropdown);
        if(StringUtils.equals(ruolo, getString(R.string.professore))){
            elem_text.put("CorsiProfessore", binding.dropdownprofessoreCorso);
        }else if (StringUtils.equals(ruolo, getString(R.string.studente))){
            elem_text.put("mancanti", binding.esamiMancantiRegister);
            elem_text.put("media", binding.mediaRegister);
            elem_text.put("Corsi", binding.dropdownCorso);
        }
    }

    /**
     * Questo metodo configura i TextWatcher per gli elementi di input nella mappa "elem_text"
     * e imposta i listener di focus per gestire le modifiche nei campi di input.
     */
    private void setupTextWatchers(){
        // Configura i TextWatcher per tutti gli elementi nella mappa elem_text
        elem_text.forEach((key, value)->  {
            if (value instanceof TextInputEditText) {
                ((TextInputEditText) value).addTextChangedListener(textWatcher);
            } else if (value instanceof MaterialAutoCompleteTextView) {
                ((MaterialAutoCompleteTextView) value).addTextChangedListener(textWatcher);
            }else if(value instanceof  MultiAutoCompleteTextView){
                ((MultiAutoCompleteTextView) value).addTextChangedListener(textWatcher);
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
            if (value instanceof MultiAutoCompleteTextView) {
                ((MultiAutoCompleteTextView) value).setOnFocusChangeListener((view, hasFocus) -> {
                    if (hasFocus) {
                        textWatcher.setComponent(value);
                    }
                });
            }
        });
    }

    /**
     * Questo metodo verifica se il componente di input passato è compilato correttamente
     * in base al suo hint (etichetta) e al ruolo dell'utente.
     *
     * @param component Il componente di input da verificare.
     * @return true se il componente è compilato correttamente, altrimenti false.
     */
    private  boolean is_correct_form(Object component) {
        String hint = getHintText(component);
        if(!ControlInput.is_empty_string(component, getInputText(component),  hint, context)) {
            switch (hint) {
                case "Matricola" -> {
                    return ControlInput.is_correct_matricola(binding.matricolaRegister, binding.matricolaInput, context, ruolo);
                }
                case "Media" -> {
                    if (StringUtils.equals(ruolo, getString(R.string.studente))) {
                        return ControlInput.is_correct_media(binding.mediaRegister, binding.mediaInput, context);
                    } else {
                        return true;
                    }
                }
                case "Esami mancanti" -> {
                    if (StringUtils.equals(ruolo, getString(R.string.studente))) {
                        return ControlInput.is_correct_esami_mancanti(binding.esamiMancantiRegister, binding.esamiMancantiInput, context);
                    } else {
                        return true;
                    }
                }
                case "Facoltà" -> {
                    return ControlInput.is_correct_facolta(binding.filledExposedDropdown, binding.facoltaInput, context, facolta);
                }
                case "Corso di Laurea" -> {
                    if (StringUtils.equals(ruolo, getString(R.string.studente))) {
                        return ControlInput.is_correct_corso_di_laurea(binding.dropdownCorso, binding.corsoInput, context, corsi);
                    } else {
                        ControlInput.set_error( binding.corsoInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
                        return true;
                    }
                }
                default -> {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Questo metodo restituisce il testo dell'etichetta (hint) associato al componente di input passato.
     *
     * @param component Il componente di input da cui ottenere l'etichetta.
     * @return Il testo dell'etichetta se disponibile, altrimenti null.
     */
    private String getHintText(Object component) {
        String text = null;
        if (component instanceof TextInputEditText) {
            text = Objects.requireNonNull(((TextInputEditText) component).getHint()).toString();
        } else if (component instanceof MaterialAutoCompleteTextView) {
            text = Objects.requireNonNull(((MaterialAutoCompleteTextView) component).getHint()).toString();
        } else if (component instanceof MultiAutoCompleteTextView) {
            text = Objects.requireNonNull(binding.corsoprofessoreInput.getHint()).toString();
        }
        return text;
    }

    /**
     * Questo metodo restituisce l'elemento di input associato all'etichetta (hint) del componente passato.
     *
     * @param component Il componente di input da cui ottenere l'elemento di input associato all'etichetta.
     * @return L'elemento di input (TextInputLayout) associato all'etichetta, o null se l'etichetta non corrisponde a nessun elemento noto.
     */
    private TextInputLayout getInputText(Object component){
        switch (getHintText(component)) {
            case "Matricola" -> {
                return binding.matricolaInput;
            }
            case "Media" -> {
                return binding.mediaInput;
            }
            case "Esami mancanti" -> {
                return binding.esamiMancantiInput;
            }
            case "Facoltà" -> {
                return binding.facoltaInput;
            }
            case "Corso di Laurea" -> {
                if (StringUtils.equals(ruolo, "Studente")) {
                    return binding.corsoInput;
                } else {
                    return binding.corsoprofessoreInput;
                }
            }
        }
        return null;
    }


    /**
     * Una classe personalizzata che implementa l'interfaccia TextWatcher per monitorare le modifiche del testo in un TextInputEditText.
     */

    private  class CustomTextWatcher implements TextWatcher {
        Object component;
        private final Set<String> insertedCourses = new HashSet<>();
        private final ArrayList<String> validCourses = new ArrayList<>();
        private boolean programmaticTextChange = false;
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
            if (component != null){
                is_correct_form(component);
            }
        }

        /**
         * Chiamato dopo che il testo nel campo di input è stato modificato.
         *
         * @param editable Il testo modificato dopo la modifica.
         */
        @Override
        public void afterTextChanged(Editable editable) {
            // Nessuna azione richiesta dopo la modifica del testo.
            if (programmaticTextChange) {
                // L'evento è stato generato programmaticamente, non fare nulla.
                programmaticTextChange = false;
                return;
            }
            if (component instanceof MultiAutoCompleteTextView) {
                programmaticTextChange = true;
                String inputText = binding.dropdownprofessoreCorso.getText().toString();

                String[] enteredCourses = StringUtils.deleteWhitespace(inputText).split(",");
                if( !insertedCourses.contains(String.valueOf(enteredCourses[enteredCourses.length-1]))){
                    validCourses.add(enteredCourses[enteredCourses.length-1]);
                }else{
                    validCourses.remove(enteredCourses[enteredCourses.length-1]);
                }

                String updatedText = TextUtils.join(", ", validCourses);
                professoreTextViewcorso.setText(updatedText);
                professoreTextViewcorso.setSelection(updatedText.length());
                insertedCourses.clear(); // Rimuovi tutti i corsi inseriti
                insertedCourses.addAll(validCourses); // Aggiungi nuovamente i corsi validi
                is_correct_form(component);
                programmaticTextChange = false;
            }
        }
        /**
         * Imposta l'EditText associato a questa istanza di CustomTextWatcher.
         *
         */
        public void setComponent(Object component) {
            this.component = component;
        }

    }
}