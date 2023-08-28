    package com.laureapp.ui.login;


    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.Toolbar;
    import androidx.navigation.NavController;
    import androidx.navigation.Navigation;
    import androidx.navigation.ui.NavigationUI;

    import android.os.Bundle;
    import com.laureapp.R;


    public class LoginActivity extends AppCompatActivity {
        Toolbar toolbar;
        NavController navController;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
            NavigationUI.setupActionBarWithNavController(this, navController);
        }

        @Override
        public boolean onSupportNavigateUp() {
            navController.navigateUp();
            return super.onSupportNavigateUp();
        }


    }