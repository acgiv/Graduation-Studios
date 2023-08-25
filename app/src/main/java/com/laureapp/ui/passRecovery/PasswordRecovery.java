package com.laureapp.ui.passRecovery;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.laureapp.R;


public class PasswordRecovery extends AppCompatActivity {

    private Button btnSend;
    private EditText mailBox;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_recovery_pass);

        initViews();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to do
            }
        });

    }

    private void initViews(){
        btnSend = findViewById(R.id.btnSendEmail);
        mailBox = findViewById(R.id.email_rec);
    }

}
