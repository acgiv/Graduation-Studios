package com.laureapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.laureapp.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void registratiClick(View view) {
        Log.d("RegistrzioneActivity", "click");
    }

    public void button_back_register(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}