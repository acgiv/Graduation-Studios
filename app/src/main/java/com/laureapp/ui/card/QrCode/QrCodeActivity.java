package com.laureapp.ui.card.QrCode;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Tesi;

public class QrCodeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_qr_code);

        ImageView qrCodeImageView = findViewById(R.id.qrCodeImageView);
        Bitmap qrCodeBitmap = getIntent().getParcelableExtra("qr_code_bitmap");
        qrCodeImageView.setImageBitmap(qrCodeBitmap);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // Chiudi l'activity del QR code quando l'utente preme il pulsante Indietro
    }
}

