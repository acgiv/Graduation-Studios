package com.laureapp.ui;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.laureapp.R;
//.R serve per riprendere gli ID delle componenti grafiche che verranno utilizzate e che si trovano all'interno del file xml
import com.google.android.material.navigation.NavigationView;
import com.laureapp.databinding.ActivityMainBinding;
import com.laureapp.ui.home.HomeFragment;
import com.laureapp.ui.login.LoginActivity;
import com.laureapp.ui.roomdb.viewModel.sharedDataModelView.SharedDataModelView;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    AppBarConfiguration mAppBarConfiguration;
    ActivityMainBinding binding;
    String ruolo;
    SharedDataModelView sharedViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // PASSO LE INFORMAZIONI ATTRAVERSO IL BUNDLE IN FRAGMENT_HOME
        Bundle bundle = getIntent().getExtras();
        ruolo = bundle.getString("ruolo");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        navController.navigate(R.id.fragment_home, bundle);

        sharedViewModel = new ViewModelProvider(this).get(SharedDataModelView.class);


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

        NavController finalNavController = navController;
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.itemSettings) {

                finalNavController.navigate(R.id.action_fragment_home_to_impostazioniFragment);
                drawerLayout.closeDrawers(); // Chiude il menu

                Toast.makeText(getApplicationContext(), "Impostazioni", Toast.LENGTH_LONG).show();
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
                navController2.navigate(R.id.action_to_profilo, bundle);
            }
            if (id2 == R.id.itemHome) {
                navController2.navigate(R.id.fragment_home, bundle);
            }
            return true;
        });

    }

    private void createAppBar() {
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // Passa il tuo R.id.fragment_home come destinazione principale
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.fragment_home)
                .setOpenableLayout(drawerLayout)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
   }



}

