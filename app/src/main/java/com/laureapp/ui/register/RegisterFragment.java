    package com.laureapp.ui.register;
    import static com.laureapp.ui.controlli.ControlInput.hashWith256;
    import static com.laureapp.ui.controlli.ControlInput.isConnected;

    import android.content.Context;
    import android.content.Intent;
    import android.net.ConnectivityManager;
    import android.net.NetworkInfo;
    import android.os.Bundle;
    import android.text.Editable;
    import android.text.TextWatcher;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Toast;

    import com.google.android.material.textfield.TextInputLayout;


    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.navigation.NavController;
    import androidx.navigation.Navigation;

    import com.google.android.material.textfield.TextInputEditText;
    import com.google.firebase.auth.FirebaseAuth;

    import com.laureapp.R;
    import com.laureapp.databinding.FragmentRegisterBinding;

    import com.laureapp.ui.controlli.ControlInput;

    import com.laureapp.ui.roomdb.entity.Utente;


    import org.apache.commons.lang3.StringUtils;

    import java.io.Serializable;

    import java.util.HashMap;

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
            ConnectivityManager cm = (ConnectivityManager)getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            // Imposta l'azione del pulsante di registrazione
            binding.buttonRegister.setOnClickListener(view1 -> {
                AtomicInteger cont = null;
                if (isConnected(cm)) {
                    // Controlla i campi di input e stampa i risultati del controllo
                    cont = new AtomicInteger();
                    AtomicInteger finalCont = cont;
                    elem_text.forEach((key, values) -> {
                        if (Boolean.TRUE.equals(is_correct_form(values))) {
                            finalCont.addAndGet(1);
                        }
                    });
                    bundle = new Bundle();
                    //Gestione del Radio button dello studente quando viene premuto
                    if (cont.get() == 5) {
                        if (binding.studenteRegister.isChecked()){
                            bundle.putString("ruolo",binding.studenteRegister.getText().toString());
                        }else{
                            bundle.putString("ruolo",binding.professoreRegister.getText().toString());
                        }
                        // Ottenere l'oggetto "utente" che si desidera passare
                        Utente utente = get_utente();
                        bundle.putSerializable("utente", (Serializable) utente);
                        mNav.navigate(R.id.action_registerFragment_to_register2Fragment, bundle);

                       // createAccount();
                        //Studente st = new Studente();
                       // StudenteModelView st_db = new StudenteModelView(context);
                       // UtenteModelView ut_db = insert_utente_sqlLite();

                        //st.setMatricola(Long.valueOf(Objects.requireNonNull(binding.matricolaRegister.getText()).toString()));
                        //st.setId_utente(ut_db.getIdUtente(Objects.requireNonNull(binding.emailRegister.getText()).toString()));
                        //st_db.insertStudente(st);
                       // Log.d("tutti gli utenti", String.valueOf(ut_db.getAllUtente()));
                        //  Intent HomeActivity = new Intent(requireActivity(), MainActivity.class);
                        //bundle.putString("ruolo", "Studente");
                        //HomeActivity.putExtras(bundle);
                        //startActivity(HomeActivity);
                        //requireActivity().finish();
                    }

                    /*bundle = new Bundle();
                    //Gestione del Radio button dello studente quando viene premuto

                    if (binding.studenteRegister.isChecked() && cont.get() == 6) {
                        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
                        RoomDbSqlLite db = RoomDbSqlLite.getDatabase(requireActivity());
                        firestoreDB.collection("Utenti").document("Studenti").collection("Studenti")
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {

                                        //Per salavare i dati in authentication
                                        createAccount();

                                        //Per salvare i dati su Firestore
                                        saveStudenteToFirestore(firestoreDB);
                                        //Per pulire la cache del db
                                        db.studenteDao().deleteAll();
                                        //Per salvare i dati in SQLite da Firestore
                                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                                        for (DocumentSnapshot document : documents) {
                                            Studente studente = document.toObject(Studente.class); // Converte il documento in un oggetto Studente
                                            db.studenteDao().insert(studente); // Chiama il metodo per l'inserimento o l'aggiornamento
                                        }


                                        Log.d("studenti", String.valueOf(db.studenteDao().getAllStudente()));

                                    } else {
                                        Log.d("Firestore", "Errore nella lettura dei dati: " + task.getException());
                                    }
                                });*/

                }            //Gestione del Radio button del professore quando viene premuto

                  /*  FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
                    RoomDbSqlLite db = RoomDbSqlLite.getDatabase(requireActivity());
                    firestoreDB.collection("Utenti").document("Professori").collection("Professori")
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                                    //Per salavare i dati in authentication
                                    createAccount();

                                    //Per salvare i dati su Firestore
                                    //saveProfessoreToFirestore(firestoreDB);
                                    //Per pulire la chache del db, altrimenti vengono scritti dati non più esistenti
                                    db.studenteDao().deleteAll();
                                    //Per salvare i dati in SQLite da Firestore
                                    for (DocumentSnapshot document : documents) {
                                        Professore professore = document.toObject(Professore.class); // Converte il documento in un oggetto Studente
                                        db.professoreDao().insert(professore); // Chiama il metodo per l'inserimento o l'aggiornamento
                                    }
                                    Log.d("professori", String.valueOf(db.professoreDao().getAllProfessore()));

                                    mNav.navigate(R.id.action_register2Fragment_to_homeFragment);
                                } else {
                                    Log.d("Firestore", "Errore nella lettura dei dati: " + task.getException());
                                }
                            });*/
            else{
                Toast.makeText(requireContext(), "Connessione assente. Riprovare!", Toast.LENGTH_SHORT).show();}
            });
    }

        /*
        private void saveStudenteToFirestore(FirebaseFirestore firestoreDB) {

            String uid = mAuth.getCurrentUser().getUid();
            String password = hashWith256(binding.passwordRegister.getText().toString());

            Map<String, Object> datiMap = new HashMap<>();
            datiMap.put("nome", binding.nameRegister.getText().toString());
            datiMap.put("cognome",  binding.cognomeRegister.getText().toString());
            datiMap.put("email",  binding.emailRegister.getText().toString());
            datiMap.put("password", password);
            firestoreDB.collection("Utenti").document("Studenti").collection("Studenti").document(uid).set(datiMap)
                    .addOnSuccessListener(aVoid -> {
                        StudenteModelView st_db = new StudenteModelView(context);

                        // Imposta i dati dello studente dai campi di input
                        Studente studente = new Studente();
                        studente.setNome(Objects.requireNonNull(binding.nameRegister.getText()).toString());
                        studente.setCognome(Objects.requireNonNull(binding.cognomeRegister.getText()).toString());
                        studente.setEmail(Objects.requireNonNull(binding.emailRegister.getText()).toString());
                        studente.setPassword(password);
                        // Inserisce l'Utente nel database utilizzando il ViewModel

                        st_db.insertStudente(studente);

                        Log.d("studenti", String.valueOf(st_db.getAllStudente()));

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Error writing document");
                        }
                    });
            //Per reindirizzare l'utente nella home
            mNav.navigate(R.id.action_register2Fragment_to_homeFragment);
        }

        private void saveProfessoreToFirestore(FirebaseFirestore firestoreDB)
        {

            String password = hashWith256(binding.passwordRegister.getText().toString());

            String uid = mAuth.getCurrentUser().getUid();

            Map<String, Object> datiMap = new HashMap<>();
            datiMap.put("nome", binding.nameRegister.getText().toString());
            datiMap.put("cognome",  binding.cognomeRegister.getText().toString());
            datiMap.put("email",  binding.emailRegister.getText().toString());
            datiMap.put("password", password);
            firestoreDB.collection("Utenti").document("Professori").collection("Professori").document(uid).set(datiMap)
                    .addOnSuccessListener(aVoid -> {
                        ProfessoreModelView pr_db = new ProfessoreModelView(context);

                        // Imposta i dati del professore dai campi di input
                        Professore professore = new Professore();
                        professore.setNome(Objects.requireNonNull(binding.nameRegister.getText()).toString());
                        professore.setCognome(Objects.requireNonNull(binding.cognomeRegister.getText()).toString());
                        professore.setEmail(Objects.requireNonNull(binding.emailRegister.getText()).toString());
                        professore.setPassword(password);
                        // Inserisce l'Utente nel database utilizzando il ViewModel

                        pr_db.insertProfessore(professore);

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Error writing document");
                        }
                    });
        }
        */
        /**
         * Crea e inserisce un nuovo utente nel database.
         *
         * @return Il ViewModel relativo all'utente appena inserito.
         */
        private Utente get_utente() {
            // Crea un nuovo oggetto Utente
            Utente ut = new Utente();
            // Imposta i dati dell'Utente dai campi di input
            ut.setNome(Objects.requireNonNull(binding.nameRegister.getText()).toString());
            ut.setCognome(Objects.requireNonNull(binding.cognomeRegister.getText()).toString());
            ut.setEmail(Objects.requireNonNull(binding.emailRegister.getText()).toString());
            ut.setPassword(hashWith256(Objects.requireNonNull(binding.passwordRegister.getText()).toString()));
            return ut;
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
        }

        /**
         * Una classe personalizzata che implementa l'interfaccia TextWatcher per monitorare le modifiche del testo in un TextInputEditText.
         */
        private  class CustomTextWatcher implements TextWatcher {
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
            }
            return null;
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

