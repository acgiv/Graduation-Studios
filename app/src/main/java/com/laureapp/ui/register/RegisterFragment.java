package com.laureapp.ui.register;

import static com.laureapp.ui.controlli.ControlInput.hashWith256;
import static com.laureapp.ui.controlli.ControlInput.isConnected;
import static com.laureapp.ui.controlli.ControlInput.is_correct_password;

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

/**
 * Questa classe rappresenta un frammento utilizzato per la registrazione di un nuovo utente nell'applicazione.
 * Gestisce le informazioni e le azioni relative al processo di registrazione.
 */
public class RegisterFragment extends Fragment {

    private NavController mNav;
    private FragmentRegisterBinding binding;
    private Context context;
    private final CustomTextWatcher textWatcher = new CustomTextWatcher();
    private final HashMap<String,TextInputEditText> elem_text = new HashMap<>();
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

                }
            }

            else{
                Toast.makeText(requireContext(), "Connessione assente. Riprovare!", Toast.LENGTH_SHORT).show();}
        });
    }

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

    /**
     * Questo metodo verifica se il campo di input specificato contiene dati corretti in base al tipo di campo specificato.
     * Restituisce un valore booleano che indica se il campo è valido o contiene errori.
     *
     * @param editText Il campo di input da verificare.
     * @return true se il campo contiene dati corretti, altrimenti false.
     */
    private  boolean is_correct_form(TextInputEditText editText) {
        boolean result_error = false;
        if(!ControlInput.is_empty_string(editText ,getInputText(editText) , Objects.requireNonNull(editText.getHint()).toString(), context)) {
            String campo = Objects.requireNonNull(editText.getHint()).toString();
            switch (campo) {
                case "Cognome", "Nome" -> {
                    return ControlInput.is_correct_nome_cognome(editText, getInputText(editText), context);
                }
                case "Email" -> {
                    return ControlInput.is_correct_email(editText, getInputText(editText), context);
                }
                case "Password" -> {
                   if (is_correct_password(editText, getInputText(editText), context)){
                       return ControlInput.is_correct_confirm_password(editText, binding.confermaPassword ,binding.confermaPasswordInput, context);
                   }
                }
                case "Conferma Password" -> {
                    return ControlInput.is_correct_confirm_password(binding.passwordRegister, editText ,getInputText(editText), context);
                }
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

    /**
     * Questo metodo restituisce il TextInputLayout associato al campo di input specificato.
     *
     * @param editText Il campo di input di cui si desidera ottenere il TextInputLayout.
     * @return Il TextInputLayout associato al campo di input specificato, o null se il campo non è stato trovato.
     */
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

}