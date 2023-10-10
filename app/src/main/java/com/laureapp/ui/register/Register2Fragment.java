package com.laureapp.ui.register;

import android.content.Context;
import android.content.Intent;
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
import com.laureapp.ui.roomdb.QueryFirestore;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Register2Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private AutoCompleteTextView autoCompleteTextView;
    private AutoCompleteTextView autoCompleteTextViewcorso;
    private MultiAutoCompleteTextView professoreTextViewcorso;
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
        bundle = getArguments();
        if (bundle != null) {
            ruolo = bundle.getString("ruolo");
            ut = bundle.getSerializable("utente", Utente.class);
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
        ArrayAdapter<String> adapterfacolta = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, facolta);
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
     * Crea l'account dell'utente su firebase

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
                            try {
                                ut.setId_utente(saveToFirestore( mAuth.getCurrentUser(), firestoreDB));
                            } catch (ExecutionException | InterruptedException e) {
                                throw new RuntimeException(e);
                            }


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
                            try {
                                ut.setId_utente(saveToFirestore( mAuth.getCurrentUser(), firestoreDB));
                            } catch (ExecutionException | InterruptedException e) {
                                throw new RuntimeException(e);
                            }
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

    private Long saveToFirestore(FirebaseUser currentUser,  FirebaseFirestore firestoreDB) throws ExecutionException, InterruptedException {


        if (currentUser != null) {
            String uid = currentUser.getUid();
            UtenteModelView ut_vew = new UtenteModelView(context);
            ut.setFacolta(Objects.requireNonNull(binding.filledExposedDropdown.getText()).toString());
            if (StringUtils.equals(ruolo, getString(R.string.professore))){
                ut.setNome_cdl(binding.dropdownprofessoreCorso.getText().toString());
            }else if (StringUtils.equals(ruolo, getString(R.string.studente))){
                ut.setNome_cdl(binding.dropdownCorso.getText().toString());
            }

            QueryFirestore queryFirestore = new QueryFirestore();


            queryFirestore.trovaIdUtenteMax(context)
                    .thenAccept(idMax -> {
                        idMax = idMax+ 1L;
                        ut.setId_utente(idMax);
                        ut_vew.insertUtente(ut);
                        Log.d("ID_MAX", String.valueOf(idMax)); // Stampa il nuovo valore di idMax
                        firestoreDB.collection("Utenti").document(uid).set(ut.getUtenteMap())
                                .addOnSuccessListener(aVoid -> {
                                })
                                .addOnFailureListener(e -> System.out.println("Error writing document"));
                        if (StringUtils.equals(ruolo, getString(R.string.studente))){
                            // Qui puoi creare e impostare il tuo oggetto Studente
                            Studente studente = new Studente();
                            studente.setId_utente(idMax);

                            //Setto id_studente
                            queryFirestore.trovaIdStudenteMax(context)
                                    .thenAccept(idMaxStudente -> {


                                                studente.setId_studente(idMaxStudente + 1L);
                                                studente.setMatricola(Long.valueOf(Objects.requireNonNull(binding.matricolaRegister.getText()).toString()));
                                                studente.setEsami_mancanti(Integer.parseInt(Objects.requireNonNull(binding.esamiMancantiRegister.getText()).toString()));

                                                StudenteModelView st_db = new StudenteModelView(context);
                                                st_db.insertStudente(studente);
                                                firestoreDB.collection("Utenti").document("Studenti").collection("Studenti").document(uid).set(studente.getStudenteMap())
                                                        .addOnSuccessListener(aVoid -> {

                                                });

                            });
                        }else if(StringUtils.equals(ruolo, getString(R.string.professore))){
                            Professore professore = new Professore();
                            professore.setId_utente(idMax);

                            //Setto id_professore
                            queryFirestore.trovaIdProfessoreMax(context)
                                    .thenAccept(idMaxProfessore -> {

                                        professore.setId_professore(idMaxProfessore + 1L);
                                        professore.setMatricola(Objects.requireNonNull(binding.matricolaRegister.getText()).toString());
                                        ProfessoreModelView pr_db = new ProfessoreModelView(context);
                                        pr_db.insertProfessore(professore);
                                        firestoreDB.collection("Utenti").document("Professori").collection("Professori")
                                                .document(uid).set(professore.getProfessoreMap())
                                                .addOnSuccessListener(aVoid -> {
                                                });

                                    });


                        }
                    })
                    .exceptionally(e -> {
                        throw new RuntimeException(e);
                    });

        }
        return ut.getId_utente();
    }




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

    private  boolean is_correct_form(Object component) {
        boolean result_error = false;
        String hint = getHintText(component);
        StudenteModelView st = new StudenteModelView(context);
        ProfessoreModelView pr = new ProfessoreModelView(context);
        Log.d("fix_error","sono qui "+ getHintText(component));
        if(!is_empty_string(component, getInputText(component),  hint)) {
            switch (hint){
                case "Matricola":
                    if (!ControlInput.is_correct_matricola(Objects.requireNonNull(binding.matricolaRegister.getText()).toString())) {
                        String error_message = getString(R.string.errore_matricola).replace("{campo}", getString(R.string.matricola));
                        ControlInput.set_error(binding.matricolaInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error, getResources());
                    } else {
                        Long presentestud = st.findStudenteMatricola(Long.valueOf(Objects.requireNonNull(binding.matricolaRegister.getText()).toString())) ;
                        Long presenteprof = pr.findProfessoreMatricola(Long.valueOf(Objects.requireNonNull(binding.matricolaRegister.getText()).toString())) ;
                        if (presentestud == null && presenteprof == null) {
                            ControlInput.set_error(binding.matricolaInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height, getResources());
                            return true;
                        }else{
                            String error_message = getString(R.string.errore_matricola_duplicate).replace("{campo}", ruolo);
                            ControlInput.set_error(binding.matricolaInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error, getResources());
                        }
                    }
                    break;
                case "Media":
                    if (StringUtils.equals(ruolo, getString(R.string.studente))) {
                        String media = Objects.requireNonNull(binding.mediaRegister.getText()).toString();
                        if (StringUtils.isNumeric(media) && (Integer.parseInt(media)) >= 18 && Integer.parseInt(media) <= 30) {
                            ControlInput.set_error(binding.mediaInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height, getResources());
                            return true;
                        } else {
                            String error_message = getString(R.string.errore_media).replace("{campo}", getString(R.string.media));
                            ControlInput.set_error(binding.mediaInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error_email, getResources());
                        }
                    }else{
                        return true;
                    }
                    break;
                case "Esami mancanti":
                    if (StringUtils.equals(ruolo, getString(R.string.studente))) {
                        String esami = Objects.requireNonNull(binding.esamiMancantiRegister.getText()).toString();
                        if (StringUtils.isNumeric(esami) && (Integer.parseInt(esami)) >= 0 && Integer.parseInt(esami) <= 40) {
                            ControlInput.set_error(binding.esamiMancantiInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height, getResources());
                            return true;
                        } else {
                            String error_message = getString(R.string.errore_media).replace("{campo}", getString(R.string.media)).replace("30", "40");
                            ControlInput.set_error(binding.esamiMancantiInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error_email, getResources());
                        }
                    }else{
                        return true;
                    }
                    break;
                case "Facoltà":
                    String facolta_text = Objects.requireNonNull(binding.filledExposedDropdown.getText()).toString();
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
                    if (StringUtils.equals(ruolo, getString(R.string.studente))) {
                        String corso_text = Objects.requireNonNull(binding.dropdownCorso.getText()).toString();
                        boolean iscorsiTextEqual = Arrays.stream(corsi)
                                .anyMatch(s -> StringUtils.equals(s, corso_text));
                        if (iscorsiTextEqual) {
                            ControlInput.set_error(binding.corsoInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height, getResources());
                            return true;
                        } else {
                            String error_message = getString(R.string.errore_scelta);
                            ControlInput.set_error(binding.corsoInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error_email, getResources());
                        }
                    }else{
                        ControlInput.set_error(binding.corsoprofessoreInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height, getResources());
                        return true;
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
        } else if (component instanceof MultiAutoCompleteTextView) {
            text = Objects.requireNonNull(binding.corsoprofessoreInput.getHint()).toString();
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
                if (StringUtils.equals(ruolo, "Studente")){
                    return  binding.corsoInput;
                }else{
                    return binding.corsoprofessoreInput;
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
            if (programmaticTextChange) {
                // L'evento è stato generato programmaticamente, non fare nulla.
                programmaticTextChange = false;
                return;
            }
            if (component instanceof MultiAutoCompleteTextView) {
                programmaticTextChange = true;
                String inputText = binding.dropdownprofessoreCorso.getText().toString();

                String[] enteredCourses = StringUtils.deleteWhitespace(inputText).split(",");
                Log.d ("corsi", String.valueOf(enteredCourses.toString()));
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

        // Aggiungi anche un listener di testo modificato per gestire l'input dell'utente


    }



    private Boolean is_empty_string(Object component, TextInputLayout layout_input, String campo_error){
        String text = null;
        String error_message;
        if (component instanceof TextInputEditText){
            text= Objects.requireNonNull(((TextInputEditText) component).getText()).toString();
        }else if (component instanceof MaterialAutoCompleteTextView) {
            text= Objects.requireNonNull(((MaterialAutoCompleteTextView) component).getText()).toString();
        }else if (component instanceof MultiAutoCompleteTextView) {
            text= Objects.requireNonNull(((MultiAutoCompleteTextView) component).getText()).toString();
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