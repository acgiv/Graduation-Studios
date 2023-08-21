package com.laureapp.ui;


import com.google.android.material.textfield.TextInputLayout;
import java.lang.reflect.Field;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.laureapp.R;


public class LoginActivity extends AppCompatActivity {

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
    }

    public void OspiteClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void loginClick(View view) {
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