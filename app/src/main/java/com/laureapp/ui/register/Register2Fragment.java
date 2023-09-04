package com.laureapp.ui.register;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.laureapp.R;
import com.laureapp.databinding.FragmentRegister2Binding;
import com.laureapp.ui.MainActivity;
import com.laureapp.ui.roomdb.entity.Utente;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Objects;

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
    private final HashMap<String, Object> elem_text = new HashMap<>();
    private FirebaseAuth mAuth;
    private Utente ut;
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
        bundle = getArguments();
        if (bundle != null) {
            String ruolo = bundle.getString("ruolo");
            Utente ut = bundle.getSerializable("utente", Utente.class);
        }
        mAuth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController mNav = Navigation.findNavController(view);
        String[] dipartimento = getResources().getStringArray(R.array.Dipartimento);
        String[] corsi = getResources().getStringArray(R.array.Corsi);
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, dipartimento);
        autoCompleteTextView.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, corsi);
        autoCompleteTextViewcorso.setAdapter(adapter2);

        //to get selected value add item click listener
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "" + autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });

        autoCompleteTextViewcorso.setOnItemClickListener((parent, view12, position, id) -> Toast.makeText(getContext(), "" + autoCompleteTextViewcorso.getText().toString(), Toast.LENGTH_SHORT).show());
        binding.buttonRegister.setOnClickListener(view1 -> {
            Intent HomeActivity = new Intent(requireActivity(), MainActivity.class);
            HomeActivity.putExtras(bundle);
            startActivity(HomeActivity);
            requireActivity().finish();
        });
           /*case "Matricola":
        if (!ControlInput.is_correct_matricola(Objects.requireNonNull(binding.matricolaRegister.getText()).toString())) {
            String error_message = getString(R.string.errore_matricola).replace("{campo}", getString(R.string.matricola));
            ControlInput.set_error(binding.matricolaInput, true, error_message, error_color, context, R.dimen.input_text_layout_height_error, getResources());
        } else {
            ControlInput.set_error(binding.matricolaInput, false, "", R.color.color_primary, context, R.dimen.input_text_layout_height, getResources());
            return true;
        }
        break;*/

        /**
         * Inizializza una mappa associativa tra nomi di elementi di testo e i corrispondenti campi di input del binding.
         * Questo metodo viene utilizzato per associare i nomi dei campi di input alle rispettive istanze di TextInputEditText nel binding.
         * Questo è utile per semplificare l'accesso e la gestione dei campi di input durante la verifica della correttezza.
         */


    }
    private void inizializzate_binding_text(){
        elem_text.put("matricola", binding.matricolaRegister);
        elem_text.put("mancanti", binding.esamiMancantiRegister);
        elem_text.put("media", binding.mediaRegister);
        elem_text.put("dipartimento", binding.dropdownCorso);
        elem_text.put("corsi", binding.filledExposedDropdown);
    }

    private Object getInputText(TextInputEditText editText){
        switch (Objects.requireNonNull(editText.getHint()).toString()){
            case "Matricola":
                return binding.matricolaInput;
            case "Media":
                return binding.mediaInput;
            case "Esami mancanti":
                return binding.esamiMancantiInput;
            case "Dipartimento":
                return binding.filledExposedDropdown;
            case "Corsi":
                return binding.dropdownCorso;
        }
        return null;
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
}