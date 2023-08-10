package com.laureapp.ui;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.reflect.Field;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.laureapp.R;


public class LoginActivity extends AppCompatActivity {

    private static final Object TAG = "LoginActivity";
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button buttonRegister = findViewById(R.id.button_login);
        Drawable backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.button_background);
        int color = ContextCompat.getColor(this, R.color.color_secondary);
        assert backgroundDrawable != null;
        backgroundDrawable.setTint(color); // Imposta il colore desiderato
        buttonRegister.setBackground(backgroundDrawable);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Controlla se l'utente corrente esiste e aggiorna l'UI
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }




    public void OspiteClick(View view) {
    }

    public void loginClick(View view) {
        EditText mEmail = (EditText) findViewById(R.id.email_login);
        EditText mPassword = (EditText)findViewById(R.id.password_login);

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        
        loginUser(email, password);
    }

    private void loginUser(String email, String password) {
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.i((String) TAG, "signInEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i((String) TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
    }
    /**
     * In questo metodo verifichiamo se l'utente è loggato e se risulta loggato, si andrà in MainActivity
     * @param currentUser rappresenta l'utente corrente che sta cercando di loggarsi
     */
    private void updateUI(FirebaseUser currentUser) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            String email = user.getEmail();
            //TODO: In futuro cambiare nel fragment dello studente o del professore
            Intent mainIntent = new Intent(this, RegisterActivity.class);
            mainIntent.putExtra("msg", email);

            startActivity(mainIntent);
        }
    }

    public void recupera_password(View view) {
    }

    public void link_registrati(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    public void button_back_login(View view) {
    }
}