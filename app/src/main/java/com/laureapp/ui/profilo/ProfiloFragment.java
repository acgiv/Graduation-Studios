package com.laureapp.ui.profilo;

import static com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE;

import android.content.Context;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import java.util.Arrays;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;
import com.laureapp.R;
import com.laureapp.databinding.FragmentProfiloBinding;

import com.laureapp.ui.controlli.ControlInput;

import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.Utente;

import com.laureapp.ui.roomdb.viewModel.ProfessoreModelView;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProfiloFragment extends Fragment {


    FragmentProfiloBinding binding;
    Bundle args;
    Context context;

    UtenteModelView ut_view;
    StudenteModelView st_view;
    ProfessoreModelView pr_view;
    private FirebaseUser user;
    private ProfiloFragment.CustomTextWatcher textWatcher;
    private final HashMap<String, Object> elem_text = new HashMap<>();
    private String ruolo;
    private String email;
    private Studente studente;
    private Utente utente;
    private Professore professore;
    private String[] corsi;
    private  ArrayList<String> validCourses;
    private final Set<String> insertedCourses = new HashSet<>();
    private final int error_color = com.google.android.material.R.color.design_default_color_error;
    private boolean mod_corso = false;
    private boolean annulla_corso = false;
    private final Map<Class<?>, Object> component_pass = new HashMap<>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfiloBinding.inflate(inflater, container, false);
        context = requireContext();
        args = getArguments();
        if (args != null) {
            ruolo = args.getString("ruolo");
            email = args.getString("email");
        }
        user  = FirebaseAuth.getInstance().getCurrentUser();
        ut_view = new UtenteModelView(context);
        utente = ut_view.findAllById(ut_view.getIdUtente(email));

        st_view = new StudenteModelView(context);
        pr_view = new ProfessoreModelView(context);

        if (StringUtils.equals(ruolo, getString(R.string.studente))) {
            studente = st_view.findAllById(st_view.findStudente(utente.getId_utente()));
        }else if(StringUtils.equals(ruolo, getString(R.string.professore))){
            corsi = getResources().getStringArray(R.array.Corsi);
            professore = pr_view.findAllById(pr_view.findProfessore(utente.getId_utente()));
            textWatcher = new ProfiloFragment.CustomTextWatcher();
            inizializzate_binding_text();
            setupTextWatchers();
        }


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("utente", String.valueOf(utente));
        if (StringUtils.equals(ruolo, getString(R.string.professore))) {
            binding.textViewCorsi.setText("Corsi di Laurea");
            binding.linearLayoutContainer.setVisibility(View.GONE);
            binding.modificaCorsi.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context,  android.R.layout.simple_dropdown_item_1line, corsi);
            binding.dropdownprofessoreCorso.setAdapter(adapter);
            binding.dropdownprofessoreCorso.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            binding.dropdownprofessoreCorso.setKeyListener(null);
            binding.InsertMatricola.setText(String.valueOf(professore.getMatricola()));
            binding.InsertFacolta.setText(utente.getFacolta());
            binding.InsertCorso.setText(utente.getNome_cdl());
        }else{
            binding.modificaCorsi.setVisibility(View.GONE);
            binding.linearLayoutContainer.setVisibility(View.VISIBLE);
            binding.InsertCorso.setText(utente.getNome_cdl());
            binding.InsertFacolta.setText(utente.getFacolta());
            binding.InsertMatricola.setText(String.valueOf(studente.getMatricola()));
            binding.InsertMedia.setText(String.valueOf(studente.getMedia()));
            binding.InsertEsamiMancanti.setText(String.valueOf(studente.getEsami_mancanti()));
        }
        binding.InsertNome.setText(utente.getNome());
        binding.InsertCongnome.setText(utente.getCognome());
        binding.InsertEmail.setText(utente.getEmail());
        binding.InsertPassword.setText("********");

        binding.modificaNome.setOnClickListener(view1 -> {
            set_view();
            binding.componentInputVecchio.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.componentInputNuovo.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.TextVecchio.setTransformationMethod(null);
            binding.Textnuovo.setTransformationMethod(null);
            binding.componentInputVecchio.setHint("Nome Attuale");
            binding.componentInputNuovo.setHint("Nuovo Nome");
            confirm_modifiche("Nome", utente.getNome());
        });

        binding.modificaCognome.setOnClickListener(view1 -> {
            set_view();
            binding.componentInputVecchio.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.componentInputNuovo.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.TextVecchio.setTransformationMethod(null);
            binding.Textnuovo.setTransformationMethod(null);
            binding.componentInputVecchio.setHint("Cognome attuale");
            binding.componentInputNuovo.setHint("Nuovo Cognome");
            confirm_modifiche("Cognome", utente.getCognome());
        });

        binding.modificaPassword.setOnClickListener(view1 -> {
            set_view();
            binding.componentInputVecchio.setEndIconMode(END_ICON_PASSWORD_TOGGLE);
            binding.componentInputNuovo.setEndIconMode(END_ICON_PASSWORD_TOGGLE);
            binding.TextVecchio.setTransformationMethod(new PasswordTransformationMethod());
            binding.Textnuovo.setTransformationMethod(new PasswordTransformationMethod());
            binding.componentInputNuovo.setErrorIconDrawable(null);
            binding.componentInputVecchio.setErrorIconDrawable(null);
            binding.componentInputVecchio.setHint("Password attuale");
            binding.componentInputNuovo.setHint("Nuova password");
            confirm_modifiche("Password", utente.getPassword());
        });

        binding.modificaMatricola.setOnClickListener(v -> {
            set_view();
            binding.componentInputVecchio.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.componentInputNuovo.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.TextVecchio.setTransformationMethod(null);
            binding.Textnuovo.setTransformationMethod(null);
            binding.componentInputVecchio.setHint("Matricola attuale");
            binding.componentInputNuovo.setHint("Nuova Matricola");
            if (StringUtils.equals(ruolo, getString(R.string.studente))) {
                confirm_modifiche("Matricola", String.valueOf(studente.getMatricola()));
            }else{
                confirm_modifiche("Matricola", String.valueOf(professore.getMatricola()));
            }
        });

        binding.modificaMedia.setOnClickListener(v -> {
            set_view();
            binding.componentInputVecchio.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.componentInputNuovo.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.TextVecchio.setTransformationMethod(null);
            binding.Textnuovo.setTransformationMethod(null);
            binding.componentInputVecchio.setHint("Media attuale");
            binding.componentInputNuovo.setHint("Nuova Media");
            confirm_modifiche("Media", String.valueOf(studente.getMedia()));
        });

        binding.modificaEsamiMancanti.setOnClickListener(v -> {
            set_view();
            binding.componentInputVecchio.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.componentInputNuovo.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.TextVecchio.setTransformationMethod(null);
            binding.Textnuovo.setTransformationMethod(null);
            binding.componentInputVecchio.setHint("Esami Mancanti attualu");
            binding.componentInputNuovo.setHint("Nuovo valore esami mancanti.");
            confirm_modifiche("Esami Mancanti", String.valueOf(studente.getEsami_mancanti()));
        });

        binding.modificaCorsi.setOnClickListener(v -> {
            binding.componentInputNuovo.setVisibility(View.GONE);
            binding.corsoprofessoreInput.setVisibility(View.VISIBLE);
            binding.componentInputVecchio.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.componentInputNuovo.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.TextVecchio.setTransformationMethod(null);
            binding.TextVecchio.setEnabled(false);
            binding.TextVecchio.setText(utente.getNome_cdl());
            annulla_corso = true;
            if (!mod_corso) {
                String[] text = StringUtils.deleteWhitespace(utente.getNome_cdl()).split(",");
                String updatedText = TextUtils.join(", ", text);
                validCourses = new ArrayList<>(Arrays.asList(StringUtils.deleteWhitespace(utente.getNome_cdl()).split(",")));
                binding.dropdownprofessoreCorso.setText(updatedText);
                binding.dropdownprofessoreCorso.setSelection(updatedText.length());
            }

            insertedCourses.addAll(validCourses);
            binding.componentInputVecchio.setHint("Corsi di laurea attuali.");
            confirm_modifiche( "CorsiProfessore", binding.dropdownprofessoreCorso.getText().toString());
            set_view();
        });
    }

    private void confirm_modifiche(String type, String campo) {
        binding.buttonRegister.setOnClickListener(view -> {
            if(!StringUtils.equals(type, "CorsiProfessore") ){
                if (!ControlInput.is_empty_string(binding.Textnuovo, binding.componentInputNuovo, Objects.requireNonNull(binding.componentInputNuovo.getHint()).toString(),context)
                || !ControlInput.is_empty_string(binding.TextVecchio, binding.componentInputVecchio, Objects.requireNonNull(binding.componentInputVecchio.getHint()).toString(),context)) {
                    boolean control_nuovo;
                    boolean control_attuale;
                    String campo_nuovo_text = Objects.requireNonNull(binding.Textnuovo.getText()).toString();
                    String message = "Il ".concat(type).concat(" coincide con il").concat(" precedente!");
                    control_attuale =  is_control_campo_attuale(type, campo);
                    switch (type) {
                        case "Nome", "Cognome" -> {
                            if (ControlInput.is_correct_nome_cognome(binding.Textnuovo, binding.componentInputNuovo, context)) {
                                control_nuovo = is_equal_campi("NUOVO", campo_nuovo_text, campo, message, Objects.requireNonNull(binding.componentInputNuovo));
                                if (StringUtils.equals(type, "Nome") && (control_attuale && control_nuovo) ) {
                                        utente.setNome(campo_nuovo_text);
                                        ut_view.updateUtente(utente);
                                        binding.InsertNome.setText(utente.getNome());
                                        component_pass.put(String.class, utente.getNome());
                                        changeComponentFirestore("nome", "Utenti");
                                        close_card_modifica();
                                    } else if (StringUtils.equals(type, "Cognome")&& (control_attuale && control_nuovo)) {
                                        utente.setCognome(campo_nuovo_text);
                                        ut_view.updateUtente(utente);
                                        binding.InsertCongnome.setText(utente.getCognome());
                                        component_pass.put(String.class, utente.getCognome());
                                        changeComponentFirestore("cognome", "Utenti");
                                        close_card_modifica();
                                    }

                            }
                        }
                        case "Password" -> {
                            if (ControlInput.is_correct_password(binding.Textnuovo, binding.componentInputNuovo,context)){
                                control_nuovo = is_equal_campi("NUOVO", ControlInput.hashWith256(campo_nuovo_text), campo, message, Objects.requireNonNull(binding.componentInputNuovo));
                                if (control_attuale && control_nuovo) {
                                    utente.setPassword(ControlInput.hashWith256(Objects.requireNonNull(binding.Textnuovo.getText()).toString()));
                                    ut_view.updateUtente(utente);
                                    updatePasswordAutentication();
                                    component_pass.put(String.class, utente.getPassword());
                                    changeComponentFirestore("password","Utenti");
                                    close_card_modifica();
                                }
                            }
                        }
                        case "Matricola" ->{
                            if (ControlInput.is_correct_matricola(binding.Textnuovo, binding.componentInputNuovo, context, ruolo)) {
                                control_nuovo = is_equal_campi("NUOVO", campo_nuovo_text, campo, message, Objects.requireNonNull(binding.componentInputNuovo));
                                if (control_attuale && control_nuovo){
                                    if (StringUtils.equals(ruolo, getString(R.string.studente))) {
                                        confirm_modifiche("Matricola", String.valueOf(studente.getMatricola()));
                                        studente.setMatricola(Long.valueOf(binding.Textnuovo.getText().toString()));
                                        st_view.updateStudente(studente);
                                        component_pass.put(Long.class, studente.getMatricola());
                                        changeComponentFirestore("matricola","Utenti/Studenti/Studenti");
                                    }else{
                                        confirm_modifiche("Matricola", String.valueOf(professore.getMatricola()));
                                        professore.setMatricola(Long.valueOf(binding.Textnuovo.getText().toString()));
                                        component_pass.put(Long.class, professore.getMatricola());
                                        changeComponentFirestore("matricola","Utenti/Professori/Professori");
                                    }
                                    binding.InsertMatricola.setText(binding.Textnuovo.getText().toString());
                                    close_card_modifica();
                                }
                            }
                        }
                        case "Media" -> {
                            if (ControlInput.is_correct_media(binding.Textnuovo, binding.componentInputNuovo, context)) {
                                control_nuovo = is_equal_campi("NUOVO", campo_nuovo_text, campo, message, Objects.requireNonNull(binding.componentInputNuovo));
                                if (control_attuale && control_nuovo) {
                                    studente.setMedia(Integer.parseInt(binding.Textnuovo.getText().toString()));
                                    st_view.updateStudente(studente);
                                    binding.InsertMedia.setText(binding.Textnuovo.getText().toString());
                                    component_pass.put(Integer.class, studente.getMedia());
                                    changeComponentFirestore("media", "Utenti/Studenti/Studenti");
                                    close_card_modifica();
                                }
                            }
                        }
                            case "Esami Mancanti" ->{
                                if (ControlInput.is_correct_esami_mancanti(binding.Textnuovo, binding.componentInputNuovo, context)) {
                                    control_nuovo = is_equal_campi("NUOVO", campo_nuovo_text, campo, message, Objects.requireNonNull(binding.componentInputNuovo));
                                    if (control_attuale && control_nuovo){
                                        studente.setEsami_mancanti(Integer.parseInt(binding.Textnuovo.getText().toString()));
                                        st_view.updateStudente(studente);
                                        binding.InsertEsamiMancanti.setText(binding.Textnuovo.getText().toString());
                                        component_pass.put(Integer.class, studente.getEsami_mancanti());
                                        changeComponentFirestore("esami_mancanti","Utenti/Studenti/Studenti");
                                        close_card_modifica();
                                    }
                                }
                        }

                    }

                }
            }else{
                if(!ControlInput.is_empty_string( binding.dropdownprofessoreCorso, binding.corsoprofessoreInput,  Objects.requireNonNull(binding.corsoprofessoreInput.getHint()).toString(), context)){
                    if (StringUtils.equals(utente.getNome_cdl(), binding.dropdownprofessoreCorso.getText().toString())){
                        ControlInput.set_error(binding.corsoprofessoreInput, true, "I campi inseriti sono già presenti!", error_color, context, R.dimen.input_text_layout_height_error);
                    }else{
                        ControlInput.set_error(binding.corsoprofessoreInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
                        utente.setNome_cdl(binding.dropdownprofessoreCorso.getText().toString());
                        ut_view.updateUtente(utente);
                        component_pass.put(String.class, utente.getNome_cdl());
                        changeComponentFirestore("nome_cdl","Utenti");
                        close_card_modifica();
                        binding.corsoprofessoreInput.setVisibility(View.GONE);
                        binding.componentInputNuovo.setVisibility(View.VISIBLE);
                    }
                }
            }

        });
    }

    private boolean is_equal_campi(String tipo_controllo, String campo1, String campo2, String error_message, TextInputLayout layout) {
        if (StringUtils.equals(tipo_controllo, "VECCHIO")) {
            if (!StringUtils.equals(campo1, campo2)) {
                ControlInput.set_error(layout, true, error_message, error_color, context, R.dimen.input_text_layout_height_error);
            } else {
                ControlInput.set_error(layout, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
                return true;
            }
        } else if (StringUtils.equals(tipo_controllo, "NUOVO")) {
            if (StringUtils.equals(campo1, campo2)) {
                ControlInput.set_error(layout, true, error_message, error_color, context, R.dimen.input_text_layout_height_error);
            } else {
                ControlInput.set_error(layout, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
                return true;
            }
        }
        return false;
    }

    private boolean is_control_campo_attuale(String type, String campo) {
            switch (type) {
                case "Nome", "Cognome", "Media", "Matricola", "Esami Mancanti" -> {
                    return is_equal_campi("VECCHIO", Objects.requireNonNull(binding.TextVecchio.getText()).toString(),
                            campo, "Il ".concat(type).concat(" non coincide."),
                            Objects.requireNonNull(binding.componentInputVecchio));
                }
                case "Password" -> {
                    return is_equal_campi("VECCHIO", ControlInput.hashWith256(Objects.requireNonNull(binding.TextVecchio.getText()).toString()),
                            campo, "La password non coincide.",
                            Objects.requireNonNull(binding.componentInputVecchio));
                }
            }
            return false;
    }

    private void set_view() {
        binding.cardViewAnagrafica.setVisibility(View.GONE);
        binding.cardViewCdl.setVisibility(View.GONE);
        binding.modificaCampo.setVisibility(View.VISIBLE);
        binding.buttonAnnulla.setOnClickListener(view2 -> {
            binding.corsoprofessoreInput.setVisibility(View.GONE);
            binding.componentInputNuovo.setVisibility(View.VISIBLE);
            if (StringUtils.equals(ruolo, getString(R.string.professore))) {
                annulla_corso = false;
                String[] text = StringUtils.deleteWhitespace(utente.getNome_cdl()).split(",");
                String updatedText = TextUtils.join(", ", text);
                validCourses = new ArrayList<>(Arrays.asList(StringUtils.deleteWhitespace(utente.getNome_cdl()).split(",")));
                binding.dropdownprofessoreCorso.setText(updatedText);
                binding.dropdownprofessoreCorso.setSelection(updatedText.length());
            }
            close_card_modifica();
        });
    }

    private void close_card_modifica() {
        binding.cardViewAnagrafica.setVisibility(View.VISIBLE);
        binding.TextVecchio.setEnabled(true);
        binding.cardViewCdl.setVisibility(View.VISIBLE);
        binding.modificaCampo.setVisibility(View.GONE);
        binding.Textnuovo.setText("");
        binding.TextVecchio.setText("");
        ControlInput.set_error(Objects.requireNonNull(binding.componentInputVecchio), false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
        ControlInput.set_error(Objects.requireNonNull(binding.componentInputNuovo), false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
        if (StringUtils.equals(ruolo ,getString(R.string.professore))){
            mod_corso = true;
            binding.InsertCorso.setText(utente.getNome_cdl());
        }
        component_pass.clear();
    }



    private void updatePasswordAutentication(){
        user.updatePassword(utente.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Password aggiornata con successo
                        Log.d("UPDATE PASSWORD", "Password aggiornata con successo");
                    } else {
                        // Si è verificato un errore durante l'aggiornamento della password
                        Log.e("UPDATE PASSWORD", "Errore nell'aggiornamento della password", task.getException());
                    }
                });
        close_card_modifica();
    }

    private void changeComponentFirestore(String key_component,String path) {
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        if (!StringUtils.isEmpty(currentUser.getUid())) {
            // Crea un oggetto con i dati da aggiornare nel documento
            Map<String, Object> updateData = new HashMap<>();
            Map.Entry<Class<?>, Object> firstEntry = component_pass.entrySet().iterator().next();
            Object value = firstEntry.getValue();
            Class<?> type = firstEntry.getKey();
            if (type == String.class) {
                if (value instanceof String) {
                    String stringValue = (String) value;
                    updateData.put(key_component, stringValue);
                } else {
                    // Gestisci il caso in cui il valore non è una stringa.
                }
            } else if (type == Long.class) {
                if (value instanceof Long) {
                    Long longValue = (Long) value;
                    updateData.put(key_component, longValue);
                } else {
                    // Gestisci il caso in cui il valore non è un long.
                }
            } else if (type == Integer.class) {
                if (value instanceof Integer) {
                    Integer intValue = (Integer) value;
                    updateData.put(key_component, intValue);
                } else {
                    // Gestisci il caso in cui il valore non è un intero.
                }
            }
            // "nome" è il nome del campo da aggiornare
            // Esegui l'aggiornamento nel documento
            firestoreDB.collection(path)
                    .document(currentUser.getUid())
                    .update(updateData) // Aggiorna il campo "nome" con il nuovo nome
                    .addOnSuccessListener(aVoid -> {
                        // Successo nell'aggiornamento
                        // Puoi aggiungere qui le azioni da eseguire in caso di successo
                    })
                    .addOnFailureListener(e -> {
                        // Errore nell'aggiornamento
                        Log.e("ERRORE AGGIORNAMENTO", "Errore nell'aggiornamento della password in Firestore", e);
                    });
        } else {
            // Gestisci il caso in cui l'ID utente non sia valido o vuoto
            Log.e("USER_ERROR", "ERRORE UTENTE NON VALIDO");
        }


    }

    private  class CustomTextWatcher implements TextWatcher {
        Object component;
        private boolean programmaticTextChange = false;
        public CustomTextWatcher() {
        }

        /**
         * Chiamato prima che il testo nel campo di input cambi.
         *
         * @param charSequence Il testo attuale prima della modifica.
         * @param i            L'indice del carattere iniziale della modifica.
         * @param i1           Il numero di caratteri da rimuovere.
         * @param i2           Il numero di caratteri da inserire.
         */
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Nessuna azione richiesta prima della modifica del testo.
        }

        /**
         * Chiamato quando il testo nel campo di input sta cambiando.
         *
         * @param charSequence Il testo attuale dopo la modifica.
         * @param i            L'indice del carattere iniziale della modifica.
         * @param i1           Il numero di caratteri da rimuovere.
         * @param i2           Il numero di caratteri da inserire.
         */
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
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
            if (component instanceof MultiAutoCompleteTextView && annulla_corso) {
                programmaticTextChange = true;
                String inputText = binding.dropdownprofessoreCorso.getText().toString();
                String[] enteredCourses = StringUtils.deleteWhitespace(inputText).split(",");
                if (!insertedCourses.contains(String.valueOf(enteredCourses[enteredCourses.length - 1]))) {
                    validCourses.add(enteredCourses[enteredCourses.length - 1]);
                } else {
                    validCourses.remove(enteredCourses[enteredCourses.length - 1]);
                }
                String updatedText = TextUtils.join(", ", validCourses);
                binding.dropdownprofessoreCorso.setText(updatedText);
                binding.dropdownprofessoreCorso.setSelection(updatedText.length());
                insertedCourses.clear(); // Rimuovi tutti i corsi inseriti
                insertedCourses.addAll(validCourses); // Aggiungi nuovamente i corsi validi
                programmaticTextChange = false;

            }
        }
            public void setComponent(Object component) {
            this.component = component;
        }
    }


    private void inizializzate_binding_text(){
        if(StringUtils.equals(ruolo, getString(R.string.professore))){
            elem_text.put("CorsiProfessore", binding.dropdownprofessoreCorso);
        }
    }


    private void setupTextWatchers(){
        elem_text.forEach((key, value)->  {
        if(value instanceof  MultiAutoCompleteTextView){
            ((MultiAutoCompleteTextView) value).addTextChangedListener(textWatcher);
            ((MultiAutoCompleteTextView) value).setOnFocusChangeListener((view, hasFocus) -> {
                if (hasFocus) {
                    textWatcher.setComponent(value);
                }
            });
        }});
    }

}