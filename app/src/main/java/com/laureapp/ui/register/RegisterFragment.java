package com.laureapp.ui.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.laureapp.R;
import com.laureapp.databinding.FragmentRegisterBinding;
import com.laureapp.ui.MainActivity;
import com.laureapp.ui.controlli.ControlInput;
import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.dao.UtenteDao;
import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.viewModel.ProfessoreModelView;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class RegisterFragment extends Fragment {

    private NavController mNav;
    private FragmentRegisterBinding binding;
    private Context context;
    private final CustomTextWatcher textWatcher = new CustomTextWatcher();
    private final HashMap<String,TextInputEditText> elem_text = new HashMap<>();
    private final int error_color = com.google.android.material.R.color.design_default_color_error;
    private FirebaseAuth mAuth;
    private Bundle bundle;

    /**
     * Chiamato quando il frammento viene creato per la prima volta.
     * In questo punto dovresti effettuare eventuali operazioni di setup e inizializzazione
     * specifiche per questo frammento.
     *
     * @param savedInstanceState Se il frammento viene ricreato da uno stato precedente,
     *                           questo è lo stato.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        // Esegui qui eventuali operazioni di setup e inizializzazione
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
    }
    /**
     * Chiamato quando il frammento sta per creare la vista associata.
     *
     * @param inflater Il LayoutInflater che può essere usato per creare la vista.
     * @param container Il contenitore padre in cui la vista deve essere inserita.
     * @param savedInstanceState Se non è null, questo frammento è stato ricostruito da uno stato salvato precedente.
     * @return La vista creata per il frammento.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        context = requireContext();

        // Infla la vista utilizzando il binding del frammento
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        // Inizializza il mapping degli elementi di testo associandoli al binding
        inizializzate_binding_text();

        // Configura gli ascoltatori per il cambiamento di testo negli elementi di input
        setupTextWatchers();

        // Restituisce la vista radice creata
        return binding.getRoot();
    }


    /**
     * Chiamato quando la vista associata al frammento è stata creata.
     *
     * @param view La vista creata dal metodo {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState Se non è null, questo frammento è stato ricostruito da uno stato salvato precedente.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNav = Navigation.findNavController(view);
        // Imposta l'azione del pulsante di registrazione
        binding.buttonRegister.setOnClickListener(view1 -> {
            // Controlla i campi di input e stampa i risultati del controllo
            AtomicInteger cont = new AtomicInteger();
            elem_text.forEach((key, values)-> {
                if (StringUtils.equals(key,"matricola")){
                    if (binding.studenteRegister.isChecked()){
                        if (Boolean.TRUE.equals(is_correct_form(values))) {
                             cont.addAndGet(1);
                        }
                    }
                }else{
                    if (Boolean.TRUE.equals(is_correct_form(values))) {
                        cont.addAndGet(1);
                    }
                }
            });
            bundle = new Bundle();
                if (binding.studenteRegister.isChecked() && cont.get() ==6){
                        createAccount();
                        Studente st = new Studente();
                        StudenteModelView st_db = new StudenteModelView(context);
                        UtenteModelView ut_db = insert_utente_sqlLite();

                        st.setMatricola(Long.valueOf(Objects.requireNonNull(binding.matricolaRegister.getText()).toString()));
                        st.setId_utente(ut_db.getIdUtente(Objects.requireNonNull(binding.emailRegister.getText()).toString()));
                        st_db.insertStudente(st);
                        Log.d("tutti gli utenti", String.valueOf(ut_db.getAllUtente()));
                        Intent HomeActivity = new Intent(requireActivity(), MainActivity.class);
                        bundle.putString("ruolo", "Studente");
                        HomeActivity.putExtras(bundle);
                        startActivity(HomeActivity);
                        requireActivity().finish();
                }
                else if (binding.professoreRegister.isChecked() && cont.get() ==5){
                    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
                    RoomDbSqlLite db = RoomDbSqlLite.getDatabase(requireActivity());
                    firestoreDB.collection("Utenti")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();

                            // Salva i dati in SQLite
                            for (DocumentSnapshot document : documents) {
                                Utente utente2 = document.toObject(Utente.class); // Converte il documento in un oggetto Utente
                                db.utenteDao().insert(utente2); // Chiama il metodo per l'inserimento o l'aggiornamento
                            }
                        } else {
                            Log.d("Firestore", "Errore nella lettura dei dati: " + task.getException());
                        }
                    });
                    Log.d("utenti", String.valueOf(db.utenteDao().getAllUtente()));
                    createAccount();
                    String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                    Map<String, Object> utente = new HashMap<>();
                    utente.put("nome", binding.nameRegister.getText().toString());
                    utente.put("cognome",  binding.cognomeRegister.getText().toString());
                    utente.put("email",  binding.emailRegister.getText().toString());
                    utente.put("password", binding.passwordRegister.getText().toString());
                    firestoreDB.collection("Utenti").document(uid).set(utente)
                            .addOnSuccessListener(aVoid -> {
                                UtenteModelView ut_db = new UtenteModelView(context);
                                // Imposta i dati dell'Utente dai campi di input
                                Utente utente1 = new Utente();
                                utente1.setNome(Objects.requireNonNull(binding.nameRegister.getText()).toString());
                                utente1.setCognome(Objects.requireNonNull(binding.cognomeRegister.getText()).toString());
                                utente1.setEmail(Objects.requireNonNull(binding.emailRegister.getText()).toString());
                                utente1.setPassword(hashWith256(Objects.requireNonNull(binding.passwordRegister.getText()).toString()));
                                // Inserisce l'Utente nel database utilizzando il ViewModel

                                ut_db.insertUtente(utente1);
                                Log.d("utenti", String.valueOf(ut_db.getAllUtente()));
                                //ProfessoreModelView pr_db = new ProfessoreModelView(context);
                                //Professore pr = new Professore();
                                //pr.setId_utente(ut_db.getIdUtente(Objects.requireNonNull(binding.emailRegister.getText()).toString()));
                                //pr_db.insertProfessore(pr);
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println("Error writing document");
                                }
                            });

                        Intent HomeActivity = new Intent(requireActivity(), MainActivity.class);
                        bundle.putString("ruolo", "Professore");
                        HomeActivity.putExtras(bundle);
                        startActivity(HomeActivity);
                        requireActivity().finish();

                }
        });

        // Imposta l'azione del pulsante "Studente"
        binding.studenteRegister.setOnClickListener(v -> {
            // Mostra il campo di input della matricola
            binding.matricolaInput.setVisibility(View.VISIBLE);
        });

        // Imposta l'azione del pulsante "Professore"
        binding.professoreRegister.setOnClickListener(v -> {
            // Nasconde il campo di input della matricola
            binding.matricolaInput.setVisibility(View.GONE);
        });
    }

    /**
     * Crea e inserisce un nuovo utente nel database.
     *
     * @return Il ViewModel relativo all'utente appena inserito.
     */
    private UtenteModelView insert_utente_sqlLite() {
        // Crea un nuovo oggetto Utente
        Utente ut = new Utente();

        // Crea un nuovo ViewModel per l'Utente
        UtenteModelView ut_db = new UtenteModelView(context);
        // Imposta i dati dell'Utente dai campi di input
        ut.setNome(Objects.requireNonNull(binding.nameRegister.getText()).toString());
        ut.setCognome(Objects.requireNonNull(binding.cognomeRegister.getText()).toString());
        ut.setEmail(Objects.requireNonNull(binding.emailRegister.getText()).toString());
        ut.setPassword(hashWith256(Objects.requireNonNull(binding.passwordRegister.getText()).toString()));
        // Inserisce l'Utente nel database utilizzando il ViewModel
        ut_db.insertUtente(ut);
        // Restituisce il ViewModel relativo all'Utente appena inserito
        return ut_db;
    }

        private  boolean is_correct_form(TextInputEditText editText) {
            boolean result_error = false;
            Log.d("fix_error","sono qui "+editText.getHint());
            if(!is_empty_string(editText ,getInputText(editText) , Objects.requireNonNull(editText.getHint()).toString())) {
                String campo = Objects.requireNonNull(editText.getHint()).toString();
                switch (campo){
                    case "Cognome":
                    case "Nome":
                        if (!StringUtils.isAlpha(Objects.requireNonNull(editText.getText()).toString())) {
                            String error_message = getString(R.string.errore_stringa).replace("{campo}", editText.getHint().toString());
                            ControlInput.set_error(Objects.requireNonNull(getInputText(editText)), true, error_message, error_color, context, R.dimen.input_text_layout_height_error, getResources());
                        } else {
                            ControlInput.set_error(Objects.requireNonNull(getInputText(editText)), false, "", R.color.color_primary, context, R.dimen.input_text_layout_height, getResources());
                             return true;
                        }
                        break;
                    case "Email":
                        if (!ControlInput.isValidEmailFormat(Objects.requireNonNull(binding.emailRegister.getText()).toString())) {
                            String error_message = getString(R.string.errore_email);
                            ControlInput.set_error(binding.emailInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error, getResources());
                        } else {
                            ControlInput.set_error(binding.emailInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height, getResources());
                            return true;
                        }
                        break;
                    case "Password":
                        if (!ControlInput.isPasswordSafe(Objects.requireNonNull(binding.passwordRegister.getText()).toString())) {
                            String error_message = getString(R.string.password_not_safe_error);
                            ControlInput.set_error(binding.passwordInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error, getResources());
                        } else {
                            ControlInput.set_error(binding.passwordInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height, getResources());
                            return control_confirm_password();
                        }
                        break;
                    case "Conferma Password":
                        return control_confirm_password();
                    case "Matricola":
                        if (!ControlInput.is_correct_matricola(Objects.requireNonNull(binding.matricolaRegister.getText()).toString())) {
                            String error_message = getString(R.string.errore_matricola).replace("{campo}", getString(R.string.matricola));
                            ControlInput.set_error(binding.matricolaInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error, getResources());
                        } else {
                            ControlInput.set_error(binding.matricolaInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height, getResources());
                            return true;
                        }
                        break;
                }
            }
            return result_error;
        }


    /**
     * Imposta i TextWatcher e i listener per il focus sugli elementi di input.
     * Questo metodo configura i TextWatcher per monitorare le modifiche nei campi di input e imposta i listener di focus
     * in modo che solo l'elemento attualmente in modifica venga identificato dal TextWatcher.
     * Gli elementi di input sono definiti nella mappa `elem_text` che associa i nomi dei campi alle istanze di TextInputEditText.
     */
    private void setupTextWatchers(){
        // Configura i TextWatcher per tutti gli elementi nella mappa elem_text
        elem_text.forEach((key, value)-> value.addTextChangedListener(textWatcher));

        // Imposta il listener di focus per ogni elemento di input
        elem_text.forEach((key, value)-> value.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                textWatcher.setEditText(value);
            }
        }));
    }

    /**
     * Inizializza una mappa associativa tra nomi di elementi di testo e i corrispondenti campi di input del binding.
     * Questo metodo viene utilizzato per associare i nomi dei campi di input alle rispettive istanze di TextInputEditText nel binding.
     * Questo è utile per semplificare l'accesso e la gestione dei campi di input durante la verifica della correttezza.
     */
    private void inizializzate_binding_text(){
        elem_text.put("nome", binding.nameRegister);
        elem_text.put("cognome", binding.cognomeRegister);
        elem_text.put("email", binding.emailRegister);
        elem_text.put("pass", binding.passwordRegister);
        elem_text.put("conf_password", binding.confermaPassword);
        elem_text.put("matricola", binding.matricolaRegister);
    }

    /**
     * Una classe personalizzata che implementa l'interfaccia TextWatcher per monitorare le modifiche del testo in un TextInputEditText.
     */
    private class CustomTextWatcher implements TextWatcher {
        private TextInputEditText editText;

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
            if (editText != null) {
                // Richiama la funzione che controlla il campo di input in focus in quel momento per verificare la correttezza.
                is_correct_form(editText);
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
        }

        /**
         * Imposta l'EditText associato a questa istanza di CustomTextWatcher.
         *
         * @param editText L'EditText da associare.
         */
        public void setEditText(TextInputEditText editText) {
            this.editText = editText;
        }
    }

    private TextInputLayout getInputText(TextInputEditText editText){
        switch (Objects.requireNonNull(editText.getHint()).toString()){
            case "Nome":
                return binding.nameInput;
            case "Cognome":
                return binding.cognomeInput;
            case "Email":
                return binding.emailInput;
            case "Password":
                return binding.passwordInput;
            case "Conferma Password":
                return binding.confermaPasswordInput;
            case "Matricola":
                return binding.matricolaInput;
        }
        return null;
    }

    private String hashWith256(String textToHash) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteOfTextToHash = textToHash.getBytes(StandardCharsets.UTF_8);
            byte[] hashedByetArray = digest.digest(byteOfTextToHash);
            return Base64.getEncoder().encodeToString(hashedByetArray);
        }catch (NoSuchAlgorithmException e){
            Log.e("LaureApp", "Error reading file", e);
        }
        return "";
    }

    /**
     * Crea l'account dell'utente su firebase

     */
    private  void createAccount()  {
        String email = Objects.requireNonNull(binding.emailRegister.getText()).toString();
        String password = Objects.requireNonNull(binding.passwordRegister.getText()).toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Registrazione avvenuta con successo, puoi eseguire ulteriori azioni qui
                        FirebaseUser user = mAuth.getCurrentUser();
                        //updateUI(user);
                        // Esempio: Aggiornare l'UI o inviare una verifica email
                    } else {
                        // La registrazione ha fallito, puoi gestire l'errore qui
                        Exception exception = task.getException();
                        assert exception != null;
                        Toast.makeText(getContext(), exception.toString(), Toast.LENGTH_SHORT).show();
                        // Esempio: Visualizzare un messaggio di errore o registrare l'errore
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        // Ottieni l'UID dell'utente autenticato
        if (user != null) {
            String userId = user.getUid();
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://laureapp-21bff-default-rtdb.europe-west1.firebasedatabase.app");
            DatabaseReference myRef = database.getReference();
            if (binding.studenteRegister.isChecked()) {
                DatabaseReference userRef = myRef.child("Utenti").child("Studenti").child(userId);
                user_reference(userRef);
                userRef.child("Matricola").setValue(Objects.requireNonNull(binding.matricolaRegister.getText()).toString());
            } else if (binding.professoreRegister.isChecked()) {
                DatabaseReference userRef = myRef.child("Utenti").child("Professori").child(userId);
                user_reference(userRef);
            }
        }
    }


    private void  user_reference(DatabaseReference userRef){
        userRef.child("Nome").setValue(Objects.requireNonNull(binding.nameRegister.getText()).toString());
        userRef.child("Cognome").setValue(Objects.requireNonNull(binding.cognomeRegister.getText()).toString());
        userRef.child("Email").setValue(Objects.requireNonNull(binding.emailRegister.getText()).toString());
        userRef.child("Password").setValue(hashWith256(Objects.requireNonNull(binding.passwordRegister.getText()).toString()));
    }



    private Boolean is_empty_string(TextInputEditText editText, TextInputLayout layout_input, String campo_error){
        boolean result = false;
        if(StringUtils.isEmpty(Objects.requireNonNull(editText.getText()).toString())){
            String error_message = getString(R.string.errore_campo_vuoto).replace("{campo}", campo_error);
            ControlInput.set_error(layout_input, true, error_message, error_color, context, R.dimen.input_text_layout_height_error, getResources());
            editText.requestFocus();
            result = true;
        }
        return result;
    }

    private boolean control_confirm_password(){
        boolean result_error = ControlInput.is_equal_password(error_color, Objects.requireNonNull(binding.confermaPassword.getText()).toString(),
                Objects.requireNonNull(binding.passwordRegister.getText()).toString(), binding.confermaPasswordInput,
                getString(R.string.non_equivalent_passwords), context, getResources());
        if(result_error)
            ControlInput.set_error(binding.confermaPasswordInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height, getResources());
        return result_error;

    }

}

