package com.laureapp.ui.profilo;

import static com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE;

import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProfiloFragment extends Fragment {


    FragmentProfiloBinding binding;
    Bundle args;
    Context context;
    private Utente utente;
    private String email;
    UtenteModelView ut_view;
    StudenteModelView st_view;
    ProfessoreModelView pr_view;
    private String ruolo;
    private FirebaseUser user;
    private Studente studente;
    private Professore professore;
    private final int error_color = com.google.android.material.R.color.design_default_color_error;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = requireContext();
        ut_view = new UtenteModelView(context);
        st_view = new StudenteModelView(context);
        pr_view = new ProfessoreModelView(context);
        args = getArguments();
        if (args != null) {
            ruolo = args.getString("ruolo");
            email = args.getString("email");
        }
        user  = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("email", String.valueOf(utente));

        utente = ut_view.findAllById(ut_view.getIdUtente(email));
        Log.d("utente", String.valueOf(utente));
        if (StringUtils.equals(ruolo, getString(R.string.studente))) {
            studente = st_view.findAllById(st_view.findStudente(utente.getId_utente()));
        }else if(StringUtils.equals(ruolo, getString(R.string.professore))){
            professore = pr_view.findAllById(pr_view.findPorfessore(utente.getId_utente()));
        }
        binding = FragmentProfiloBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("utente", String.valueOf(utente));
        if (StringUtils.equals(ruolo, getString(R.string.professore))) {
            binding.textViewCorsi.setText("Corsi di lauera");
            binding.linearLayoutContainer.setVisibility(View.GONE);
            binding.modificaCorsi.setVisibility(View.VISIBLE);
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
            binding.componentInputVecchio.setHint("Nome Attuale");
            binding.componentInputNuovo.setHint("Nuovo Nome");
            confirm_modifiche("Nome", utente.getNome());
        });

        binding.modificaCognome.setOnClickListener(view1 -> {
            set_view();
            binding.componentInputVecchio.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.componentInputNuovo.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.componentInputVecchio.setHint("Cognome attuale");
            binding.componentInputNuovo.setHint("Nuovo Cognome");
            confirm_modifiche("Cognome", utente.getCognome());
        });

        binding.modificaPassword.setOnClickListener(view1 -> {
            set_view();
            binding.componentInputVecchio.setEndIconMode(END_ICON_PASSWORD_TOGGLE);
            binding.componentInputNuovo.setEndIconMode(END_ICON_PASSWORD_TOGGLE);
            binding.componentInputVecchio.setHint("Password attuale");
            binding.componentInputNuovo.setHint("Nuova password");
            confirm_modifiche("Password", utente.getPassword());
        });

        binding.modificaMatricola.setOnClickListener(v -> {
            set_view();
            binding.componentInputVecchio.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.componentInputNuovo.setEndIconMode(TextInputLayout.END_ICON_NONE);
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
            binding.Textnuovo.setInputType(TextInputEditText.AUTOFILL_TYPE_TEXT);
            binding.TextVecchio.setInputType(TextInputEditText.AUTOFILL_TYPE_TEXT);
            binding.componentInputVecchio.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.componentInputNuovo.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.componentInputVecchio.setHint("Media attuale");
            binding.componentInputNuovo.setHint("Nuova Media");
            confirm_modifiche("Media", String.valueOf(studente.getMedia()));
        });

        binding.modificaEsamiMancanti.setOnClickListener(v -> {
            set_view();
            binding.componentInputVecchio.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.componentInputNuovo.setEndIconMode(TextInputLayout.END_ICON_NONE);
            binding.componentInputVecchio.setHint("Esami Mancanti attualu");
            binding.componentInputNuovo.setHint("Nuovo valore esami mancanti.");
            confirm_modifiche("Esami Mancanti", String.valueOf(studente.getEsami_mancanti()));
        });
    }

    private void confirm_modifiche(String type, String campo) {

        binding.buttonRegister.setOnClickListener(view -> {

            if (!ControlInput.is_empty_string(binding.Textnuovo, binding.componentInputNuovo, Objects.requireNonNull(binding.componentInputNuovo.getHint()).toString(),context)
            || !ControlInput.is_empty_string(binding.TextVecchio, binding.componentInputVecchio, Objects.requireNonNull(binding.componentInputVecchio.getHint()).toString(),context)) {
                boolean control_nuovo;
                boolean control_attuale;
                String campo_nuovo_text = Objects.requireNonNull(binding.Textnuovo.getText()).toString();
                String message = "Il ".concat(type).concat(" coincide con il").concat(" precedente!");
                control_attuale =  is_control_campo_attuale(type, campo);
                control_nuovo = is_equal_campi("NUOVO", campo_nuovo_text, campo, message, Objects.requireNonNull(binding.componentInputNuovo));
                switch (type) {
                    case "Nome", "Cognome" -> {
                        if (ControlInput.is_correct_nome_cognome(binding.Textnuovo, binding.componentInputNuovo, context)) {
                            ControlInput.set_error(Objects.requireNonNull(binding.componentInputNuovo), false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
                                if (StringUtils.equals(type, "Nome") && (control_attuale && control_nuovo) ) {
                                    utente.setNome(campo_nuovo_text);
                                    ut_view.updateUtente(utente);
                                    binding.InsertNome.setText(utente.getNome());
                                    changeComponentFirestore("nome", "Utenti", utente.getNome());
                                    close_card_modifica();

                                } else if (StringUtils.equals(type, "Cognome")&& (control_attuale && control_nuovo)) {
                                    utente.setCognome(campo_nuovo_text);
                                    ut_view.updateUtente(utente);
                                    binding.InsertCongnome.setText(utente.getCognome());
                                    changeComponentFirestore("cognome", "Utenti", utente.getCognome());
                                    close_card_modifica();
                                }

                        }
                    }
                    case "Password" -> {
                        if (ControlInput.is_correct_password(binding.Textnuovo, binding.componentInputNuovo,context)){
                            if (control_attuale && control_nuovo) {
                                utente.setPassword(ControlInput.hashWith256(Objects.requireNonNull(binding.Textnuovo.getText()).toString()));
                                ut_view.updateUtente(utente);
                                updatePasswordAutentication();
                                changeComponentFirestore("password","Utenti" ,utente.getPassword());
                                close_card_modifica();
                            }
                        }
                    }
                    case "Matricola" ->{
                        if (ControlInput.is_correct_matricola(binding.Textnuovo, binding.componentInputNuovo, context, ruolo)) {
                            ControlInput.set_error(Objects.requireNonNull(binding.componentInputNuovo), false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
                            if (control_attuale && control_nuovo){
                                if (StringUtils.equals(ruolo, getString(R.string.studente))) {
                                    confirm_modifiche("Matricola", String.valueOf(studente.getMatricola()));
                                    studente.setMatricola(Long.valueOf(binding.Textnuovo.getText().toString()));
                                    st_view.updateStudente(studente);
                                    changeComponentFirestore("matricola","Utenti/Studenti/Studenti" ,String.valueOf(studente.getMatricola()));
                                }else{
                                    confirm_modifiche("Matricola", String.valueOf(professore.getMatricola()));
                                    professore.setMatricola((binding.Textnuovo.getText().toString()));
                                    changeComponentFirestore("matricola","Utenti/Professori/Professori" ,professore.getMatricola());
                                }

                                binding.InsertMatricola.setText(binding.Textnuovo.getText().toString());

                                close_card_modifica();
                            }
                        }
                    }
                    case "Media" -> {
                        if (ControlInput.is_correct_media(binding.Textnuovo, binding.componentInputNuovo, context)) {
                            ControlInput.set_error(Objects.requireNonNull(binding.componentInputNuovo), false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
                            if (control_attuale && control_nuovo) {
                                studente.setMedia(Integer.parseInt(binding.Textnuovo.getText().toString()));
                                st_view.updateStudente(studente);
                                binding.InsertMedia.setText(binding.Textnuovo.getText().toString());
                                changeComponentFirestore("media", "Utenti/Studenti/Studenti", String.valueOf(studente.getMedia()));
                                close_card_modifica();
                            }
                        }
                    }
                        case "Esami Mancanti" ->{
                            if (ControlInput.is_correct_esami_mancanti(binding.Textnuovo, binding.componentInputNuovo, context)) {
                                ControlInput.set_error(Objects.requireNonNull(binding.componentInputNuovo), false, "", R.color.color_primary, context, R.dimen.input_text_layout_height_error_email);
                                if (control_attuale && control_nuovo){
                                    studente.setEsami_mancanti(Integer.parseInt(binding.Textnuovo.getText().toString()));
                                    st_view.updateStudente(studente);
                                    binding.InsertEsamiMancanti.setText(binding.Textnuovo.getText().toString());
                                    changeComponentFirestore("esami_mancanti","Utenti/Studenti/Studenti" ,String.valueOf(studente.getEsami_mancanti()));
                                    close_card_modifica();
                                }
                            }
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
                Log.d("campo", campo1);
                Log.d("campo2", campo2);
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
            close_card_modifica();
        });
    }

    private void close_card_modifica() {
        binding.cardViewAnagrafica.setVisibility(View.VISIBLE);
        binding.cardViewCdl.setVisibility(View.VISIBLE);
        binding.modificaCampo.setVisibility(View.GONE);
        binding.Textnuovo.setText("");
        binding.TextVecchio.setText("");
        ControlInput.set_error(Objects.requireNonNull(binding.componentInputVecchio), false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
        ControlInput.set_error(Objects.requireNonNull(binding.componentInputNuovo), false, "", R.color.color_primary, context, R.dimen.input_text_layout_height);
    }



    private void updatePasswordAutentication(){
        user.updatePassword(utente.getPassword())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Password aggiornata con successo
                            Log.d("UPDATE PASSWORD", "Password aggiornata con successo");
                        } else {
                            // Si è verificato un errore durante l'aggiornamento della password
                            Log.e("UPDATE PASSWORD", "Errore nell'aggiornamento della password", task.getException());
                        }
                    }
                });
        close_card_modifica();
    }

    private void changeComponentFirestore(String key_component,String path,  String component) {
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        if (!StringUtils.isEmpty(currentUser.getUid())) {
            // Crea un oggetto con i dati da aggiornare nel documento
            Map<String, Object> updateData = new HashMap<>();
            updateData.put(key_component, component); // "nome" è il nome del campo da aggiornare
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

}