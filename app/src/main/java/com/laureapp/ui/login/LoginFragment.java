package com.laureapp.ui.login;
import static android.content.ContentValues.TAG;

import static com.laureapp.ui.controlli.ControlInput.isConnected;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.laureapp.R;
import com.laureapp.ui.MainActivity;
import com.laureapp.ui.controlli.ControlInput;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;

public class LoginFragment extends Fragment {

    Button btnLogin;
    Button btnHostLogin;
    MaterialTextView btnForgotPsw;
    MaterialTextView btnRegister;
    TextInputLayout password_layout;
    TextInputLayout email_layout;
    TextInputEditText password_text;
    TextInputEditText email_text;
    MaterialTextView error_text;
    Context context ;
    private NavController mNav;
    private FirebaseAuth mAuth;
    private Bundle bundle;
    private  UtenteModelView utenteView ;
    private final int error_color = com.google.android.material.R.color.design_default_color_error;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        context = requireContext();
        utenteView = new UtenteModelView(context);
        return inflater.inflate(R.layout.fragment_login, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNav = Navigation.findNavController(view);

        btnLogin = view.findViewById(R.id.button_login);
        email_layout  =  view.findViewById(R.id.email_input);
        password_layout = view.findViewById(R.id.password_input);
        email_text = view.findViewById(R.id.email_register);
        password_text = view.findViewById(R.id.conferma_password);
        error_text = view.findViewById(R.id.error_text);

        Resources resources = getResources();
        ConnectivityManager cm = (ConnectivityManager)getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        //Pulsante di login
        btnLogin.setOnClickListener(view1 -> {
            if(email_layout != null && password_layout != null) {
                HashMap<String, Boolean> result = is_correct_email_password();
                if (Boolean.TRUE.equals(result.get("email")) && Boolean.TRUE.equals(result.get("password"))) {
                    //Se c'è connessione ad internet uso il db locale altrimenti uso quello in remoto
                    if (!isConnected(cm)) {
                        boolean result_query = utenteView.is_exist_email_password(String.valueOf(email_text.getText()), hashWith256(String.valueOf(password_text.getText())));

                        if (Boolean.FALSE.equals(result_query)) {
                            error_text.setVisibility(View.VISIBLE);
                        } else {
                            error_text.setVisibility(View.GONE);
                            redirectHome();
                        }
                    } else if (isConnected(cm)) {
                        Log.d("ciao",String.valueOf(utenteView.getAllUtente()));
                        loginUser(Objects.requireNonNull(email_text.getText()).toString(), Objects.requireNonNull(password_text.getText()).toString());
                    }
                }
            }
        });

        btnRegister = view.findViewById(R.id.registrati_login);
        btnRegister.setOnClickListener(view13 -> mNav.navigate(R.id.action_loginFragment_to_registerFragment));

        btnForgotPsw = view.findViewById(R.id.recupero_password_login);
        btnForgotPsw.setOnClickListener(view12 -> mNav.navigate(R.id.action_loginFragment_to_passwordRecoveryFragment));

        btnHostLogin = view.findViewById(R.id.ospite_login);
        btnHostLogin.setOnClickListener(view14 -> {
            Intent HomeActivity = new Intent(requireActivity(), MainActivity.class);
            bundle = new Bundle();
            bundle.putString("ruolo", "Ospite");
            HomeActivity.putExtras(bundle);
            startActivity(HomeActivity);
            requireActivity().finish();
        });

    }

    private HashMap<String,Boolean> is_correct_email_password(){
        HashMap<String,Boolean> result_error = new HashMap<String,Boolean>();
        if(is_empty_string(email_text , email_layout,"Email")){
            result_error.put("Email", false);
        // controllo che il campo email non sia valido
        }else if (!ControlInput.isValidEmailFormat(String.valueOf(email_text.getText()))) {
            String error_message = getString(R.string.errore_email);
            ControlInput.set_error(email_layout, true, error_message, error_color, context ,R.dimen.input_text_layout_height_error, getResources());
            result_error.put("email", false);;
        }else{
            // cancello i messaggi di errore sul campo email
            correct_text(email_layout, "email", result_error);
        }
        // controllo che il campo password non sia vuoto
        if(is_empty_string(password_text, password_layout, "Password")){
            if (StringUtils.isNoneEmpty(String.valueOf(email_text.getText()))){
                password_text.requestFocus();
            }
        }else{
            // cancello i messaggi di errore sul campo password
           correct_text(password_layout, "password", result_error);
        }

        return result_error;
    }


    private void correct_text(TextInputLayout inputLayout, String campo, HashMap<String, Boolean> error ){
        ControlInput.set_error(inputLayout, false, "", R.color.color_primary,context ,R.dimen.input_text_layout_height, getResources());
        error.put(campo, true);
    }

    private Boolean is_empty_string(TextInputEditText editText, TextInputLayout layout_input, String campo_error){
        boolean result = false;
        if(StringUtils.isEmpty(Objects.requireNonNull(editText.getText()).toString())) {
            String error_message = getString(R.string.errore_campo_vuoto).replace("{campo}", campo_error);
            ControlInput.set_error(layout_input, true, error_message, error_color, context, R.dimen.input_text_layout_height_error, getResources());
            editText.requestFocus();
            result = true;
        }
        return result;
    }

    private String hashWith256(String textToHash) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteOfTextToHash = textToHash.getBytes(StandardCharsets.UTF_8);
            byte[] hashedByetArray = digest.digest(byteOfTextToHash);
            return Base64.getEncoder().encodeToString(hashedByetArray);
        }catch (NoSuchAlgorithmException e){
            Log.e("LaureApp", "Error reading file", e);;
        }
        return "";
    }



        /**
         *
         * @param email rappresenta la mail inserita dall'utente che permette il login
         * @param password rappresenta la password inserita dall'utente per permettere il login
         */
        public void loginUser(String email, String password) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.i((String) TAG, "signInEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i((String) TAG, "signInWithEmail:failure", task.getException());
                            error_text.setVisibility(View.VISIBLE);
                        }
                    });
        }

        /**
         * In questo metodo verifichiamo se l'utente è loggato e se risulta loggato, si andrà in MainActivity
         */
        private void
        updateUI(FirebaseUser user) {
            if (user != null) {
                String userId = user.getUid(); // Ottieni l'UID dell'utente autenticato

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://laureapp-21bff-default-rtdb.europe-west1.firebasedatabase.app");
                DatabaseReference myRef = database.getReference();

                DatabaseReference userRef = myRef.child("Utenti").child("Studenti").child(userId);

                userRef.child("Email").get();
                userRef.child("Password").get();
                redirectHome();
            }
        }

        private void redirectHome() {
            Bundle bundle = new Bundle();
            Long id_utente = utenteView.getIdUtente(String.valueOf(email_text.getText()));
            StudenteModelView stud_view = new StudenteModelView(context);
            if( stud_view.findStudente(id_utente)!= null){
                bundle.putString("ruolo", "Studente");
            }else{
                bundle.putString("ruolo", "Professore");
            }
            Intent HomeActivity = new Intent(requireActivity(), MainActivity.class);
            HomeActivity.putExtras(bundle);
            startActivity(HomeActivity);
            requireActivity().finish();
        }

    private void redirectToProfessoreHome() {
        //TODO: Inserire il fragment o l'activity della home del professore
        Intent HomeActivity = new Intent(requireActivity(), MainActivity.class);
        startActivity(HomeActivity);
        requireActivity().finish();
    }
}
