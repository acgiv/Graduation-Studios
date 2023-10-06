package com.laureapp.ui;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.laureapp.R;
//.R serve per riprendere gli ID delle componenti grafiche che verranno utilizzate e che si trovano all'interno del file xml
import com.google.android.material.navigation.NavigationView;
import com.laureapp.databinding.ActivityMainBinding;
import com.laureapp.ui.login.LoginActivity;


public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    AppBarConfiguration mAppBarConfiguration;
    ActivityMainBinding binding;
    String ruolo;
    ImageView whatsapp;
    ImageView gmail;
    ImageView qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Gestione Social Fragment (DA AGGIUSTARE)

        /*
        whatsapp = findViewById(R.id.logoWhatsapp);
        gmail = findViewById(R.id.logoGmail);
        qrCode = findViewById(R.id.qrCode);

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.whatsapp.com/?lang=it_IT");
            }
        });

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.uniba.it/it/studenti/servizi-informatici/posta-elettronica-e-servizi-associati");
            }
        });

        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.uniba.it/it");
            }
        }); */

        // PASSO LE INFORMAZIONI ATTRAVERSO IL BUNDLE IN FRAGMENT_HOME
        Bundle bundle = getIntent().getExtras();
        ruolo = bundle.getString("ruolo");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        navController.navigate(R.id.fragment_home, bundle);

        // Imposta gli argomenti per il fragment
        navigationView = findViewById(R.id.navigation);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createAppBar();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.itemSettings) {
                //navController.navigate(R.id.home_fragment);
                Toast.makeText(getApplicationContext(), "Impostazioni", Toast.LENGTH_LONG).show();
            } else if (id == R.id.itemProfile) {
                Toast.makeText(getApplicationContext(), "Profilo", Toast.LENGTH_LONG).show();
            } else if (id == R.id.itemLogin) {
                Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_LONG).show();
            } else if (id == R.id.itemSignOut) {
                FirebaseAuth.getInstance().signOut();

                //faccio logout dell'utente se è loggato
                //in entrambi i casi cancello i dati dal locale
                   /* if(currentUser != null) {
                        String uid = currentUser.getUid();
                        db.userDao().deleteByUserId(uid);
                        FirebaseAuth.getInstance().signOut();
                    } else {
                        db.userDao().deleteByUserId("guest");
                    }
                    */
                //avvio l'activity di login
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });
        binding.bottomNavigationView.setOnItemSelectedListener(item1 -> {
            int id2 = item1.getItemId();
            NavController navController2 = Navigation.findNavController(this, R.id.nav_host_fragment_main);
            if (id2 == R.id.itemProfilo) {
                navController2.navigate(R.id.action_fragment_home_to_profilo, bundle);
            }
            if (id2 == R.id.itemHome) {
                navController2.navigate(R.id.fragment_home, bundle);
            }
            return true;
        });

    }

    //Reindirizzamento link Social Fragment (DA AGGIUSTARE)
    /*
    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    } */

    private void createAppBar() {
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
         mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.fragment_home, R.id.profilo_studente)
                    .setOpenableLayout(drawerLayout)
                    .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }






}

