package com.laureapp.ui;

//import android.app.FragmentManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

//.R serve per riprendere gli ID delle componenti grafiche che verranno utilizzate e che si trovano all'interno del file xml
import com.laureapp.R;


public class MainActivity extends AppCompatActivity {
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, new HomeFragment());
        ft.addToBackStack(null);
        ft.commit();

    }*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Avvia DataEntryActivity quando viene eseguita MainActivity
        Intent intent = new Intent(this, DataEntryActivity.class);
        startActivity(intent);
    }

}

