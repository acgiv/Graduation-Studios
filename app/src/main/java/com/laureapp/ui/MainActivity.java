package com.laureapp.ui;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import com.laureapp.R;
//.R serve per riprendere gli ID delle componenti grafiche che verranno utilizzate e che si trovano all'interno del file xml
import com.google.android.material.navigation.NavigationView;
import com.laureapp.ui.home.HomeFragment;
import com.laureapp.ui.login.LoginActivity;


public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    NavController navController;
    AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // PASSO LE INFORMAZIONI ATTRAVERSO IL BUNDLE IN FRAGMENT_HOME
        Bundle bundle = getIntent().getExtras();
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

            //navController.navigate(R.id.downloadFragment);

                /*
                case R.id.itemTutorial:
                    //avvio l'activity di visualizzazione del tutorial, passando come extra
                    //la classe di provenienza, in modo da cambiare dinamicamente il testo
                    //dei tasti presenti nel tutorial
                    Intent startOnBoarding = new Intent(Main.this, OnBoarding.class);
                    startOnBoarding.putExtra("classFrom", Main.class.toString());
                    startActivity(startOnBoarding);
                    return true;

                case R.id.itemSettings:
                    closeDrawerIfOpen();
                    navController.navigate(R.id.settingsFragment);
                    return true;

                case R.id.itemAbout:
                    closeDrawerIfOpen();
                    navController.navigate(R.id.aboutUsFragment);
                    return true;
                */
            return false;
        });

    }

    private void createAppBar() {
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
         mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.fragment_home)
                    .setOpenableLayout(drawerLayout)
                    .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
}

