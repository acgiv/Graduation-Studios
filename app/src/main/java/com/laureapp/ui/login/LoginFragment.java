package com.laureapp.ui.login;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import androidx.core.content.ContextCompat;
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
        int error_color = com.google.android.material.R.color.design_default_color_error;
        Resources resources = getResources();
        UtenteModelView utenteView = new UtenteModelView(context);
        //Pulsante di login
        btnLogin.setOnClickListener(view1 -> {
            if(email_layout != null && password_layout != null) {
                HashMap<String, Boolean> result = is_correct_email_password(error_color);
                if (Boolean.TRUE.equals(result.get("email")) && Boolean.TRUE.equals(result.get("password"))) {
                    //Se c'è connessione ad internet uso il db locale altrimenti uso quello in remoto
                    if (isConnected() == false) {
                        boolean result_query = utenteView.is_exist_email_password(String.valueOf(email_text.getText()), hashWith256(String.valueOf(password_text.getText())));
                        if (Boolean.FALSE.equals(result_query)) {
                            error_text.setVisibility(View.VISIBLE);
                        } else {
                            error_text.setVisibility(View.GONE);
                            Intent HomeActivity = new Intent(requireActivity(), MainActivity.class);
                            startActivity(HomeActivity);
                            requireActivity().finish();
                        }
                    } else if (isConnected() == true) {
                        loginUser(email_text.getText().toString(), password_text.getText().toString());
                    }
                }
            }
        });

        btnRegister = view.findViewById(R.id.registrati_login);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNav.navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        btnForgotPsw = view.findViewById(R.id.recupero_password_login);
        btnForgotPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Navigate to forgot psw fragment
            }
        });

        btnHostLogin = view.findViewById(R.id.ospite_login);
        btnHostLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeActivity = new Intent(requireActivity(), MainActivity.class);
                startActivity(HomeActivity);
                requireActivity().finish();
            }
        });

    }

    private HashMap<String,Boolean> is_correct_email_password(int error_color){
        HashMap<String,Boolean> result_error = new HashMap<String,Boolean>();
        if(StringUtils.isEmpty(String.valueOf(email_text.getText()))){
            String error_message = getString(R.string.errore_campo_vuoto).replace("{campo}", "Email");
            ControlInput.set_error(email_layout, true, error_message, error_color, context ,R.dimen.input_text_layout_height_error, getResources());
            email_text.requestFocus();
            result_error.put("Email", false);
        // controllo che il campo email non sia valido
        }else if (!ControlInput.isValidEmailFormat(String.valueOf(email_text.getText()))) {
            String error_message = getString(R.string.errore_email);
            ControlInput.set_error(email_layout, true, error_message, error_color, context ,R.dimen.input_text_layout_height_error, getResources());
            result_error.put("email", false);;
        }else{
            // cancello i messaggi di errore sul campo email
            ControlInput.set_error(email_layout, false, "", R.color.color_primary,context ,R.dimen.input_text_layout_height, getResources());
            result_error.put("email", true);
        }
        // controllo che il campo password non sia vuoto
        if( StringUtils.isEmpty(String.valueOf(password_text.getText()))){
            String error_message = getString(R.string.errore_campo_vuoto).replace("{campo}", "Password");
            ControlInput.set_error(password_layout, true, error_message, error_color,context ,R.dimen.input_text_layout_height_error, getResources());
            result_error.put("password", false);
            if (StringUtils.isNoneEmpty(String.valueOf(email_text.getText()))){
                password_text.requestFocus();
            }
        }else{
            // cancello i messaggi di errore sul campo password
            ControlInput.set_error(password_layout, false, "", R.color.color_primary,context ,R.dimen.input_text_layout_height, getResources());
            result_error.put("password", true);
        }

        return result_error;
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
         * @return un booleano che indica se la connessione è presente: true se c'è connessione altrimenti false
         */
        public boolean isConnected() {
            ConnectivityManager cm = (ConnectivityManager)getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                return true;
            } else {
                return false;
                // show an error message or do something else
            }
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
                            Toast.makeText(context.getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    });
        }

        /**
         * In questo metodo verifichiamo se l'utente è loggato e se risulta loggato, si andrà in MainActivity
         */
        private void updateUI(FirebaseUser user) {
            if (user != null) {
                String userId = user.getUid(); // Ottieni l'UID dell'utente autenticato

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://laureapp-21bff-default-rtdb.europe-west1.firebasedatabase.app");
                DatabaseReference myRef = database.getReference();

                DatabaseReference userRef = myRef.child("Utenti").child("Studenti").child(userId);

                userRef.child("Email").get();
                userRef.child("Password").get();

                redirectToStudenteHome();

            }else if (user != null) {
                String userId = user.getUid(); // Ottieni l'UID dell'utente autenticato

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://laureapp-21bff-default-rtdb.europe-west1.firebasedatabase.app");
                DatabaseReference myRef = database.getReference();

                DatabaseReference userRef = myRef.child("Utenti").child("Studenti").child(userId);

                userRef.child("Email").get();
                userRef.child("Password").get();

                redirectToProfessoreHome();
            }
        }

        private void redirectToStudenteHome() {
            Intent HomeActivity = new Intent(requireActivity(), MainActivity.class);
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
