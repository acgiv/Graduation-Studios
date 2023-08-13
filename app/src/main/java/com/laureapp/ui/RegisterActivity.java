package com.laureapp.ui;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.laureapp.R;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth; //Permette di gestire l'autenticazione tramite Firebase
    //Variabili per effettuare il controllo sull'input durante l'autenticazione
    EditText mConfermaPassword;
    EditText mEmail;
    EditText mPassword;
    EditText mNome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUI();
        mAuth = FirebaseAuth.getInstance();
    }

    private void initUI() {
        mEmail = (EditText)findViewById(R.id.email_login);
        mPassword = (EditText)findViewById(R.id.password_register);
        mConfermaPassword = (EditText)findViewById(R.id.password_login);
        mNome = (EditText)findViewById(R.id.name_register);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Controlla se l'utente corrente esiste e aggiorna l'UI
        //TO-DO: scrivere una funziona che aggiorni l'interfaccia utente
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Toast.makeText( this, "utente già loggato", Toast.LENGTH_SHORT);
        // La funziona dovrà essere richiamata in questo modo: updateUI(currentUser);
    }

    private void createFirebaseUser(String email, String password)
    {


        //Questo metodo restituisce un oggetto di tipo Task
        mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LaureappRegistration", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Authentication Successful.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LaureappRegistration", "createUserWithEmail:failure", task.getException());
                            //In futuro al posto di Toast bisognerà inserire il layout del profilo dell'utente
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
    public void registratiClick(View view) {
        Log.d("RegistrazioneActivity", "pulsante di registrazione cliccato");
        //Validazione dati
        String nome = mNome.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        /*
        if(!nomeValido(nome))
            Toast.makeText(getApplicationContext(), "Nome non valido", Toast.LENGTH_SHORT).show();
        else if(( !emailValida(email) ) ){
                Toast.makeText(getApplicationContext(), "Email non valida", Toast.LENGTH_SHORT).show();
        } else if ( !passwordValida(password) ) {
            Toast.makeText(getApplicationContext(), "Password non valida", Toast.LENGTH_SHORT).show();
        }else {*/
            createFirebaseUser(email, password);
        //}
    }

    public void button_back_register(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}