package com.laureapp.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.laureapp.R;

public class DataEntryActivity extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextAddress;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);

        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextAddress = findViewById(R.id.editTextAddress);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviaDati();
            }
        });
    }

    private void inviaDati() {
        // Recupera i dati inseriti dall'utente
        String nome = editTextName.getText().toString();
        String eta = editTextAge.getText().toString();
        String indirizzo = editTextAddress.getText().toString();

        // Puoi elaborare i dati qui, ad esempio, mostrarli in una Toast
        String message = "Nome: " + nome + "\nEtà: " + eta + "\nIndirizzo: " + indirizzo;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // Puoi anche inserire qui del codice per salvare i dati in un database o fare altre azioni
    }
}
