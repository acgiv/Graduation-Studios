    package com.laureapp.ui.login;


    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.Toolbar;
    import androidx.navigation.NavController;
    import androidx.navigation.Navigation;
    import androidx.navigation.ui.NavigationUI;

    import android.content.SharedPreferences;
    import android.content.res.Configuration;
    import android.content.res.Resources;
    import android.os.Bundle;
    import android.util.DisplayMetrics;

    import com.laureapp.R;
    import com.laureapp.ui.roomdb.viewModel.StudenteModelView;

    import java.util.Locale;

    /**
     * Questa è l'activity principale per il login degli utenti.
     */
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

            SharedPreferences prefsLanguage = getSharedPreferences("LanguagePrefs", 0);
            String language = prefsLanguage.getString("Language", "it");
            setLocal(language);

        }

        /**
         * Metodo chiamato quando viene premuto il pulsante "Indietro" nella barra delle azioni.
         * Naviga all'indietro nell'interfaccia utente.
         *
         * @return Restituisce true se la navigazione all'indietro è stata gestita con successo.
         */
        @Override
        public boolean onSupportNavigateUp() {
            navController.navigateUp();
            return super.onSupportNavigateUp();
        }

        /**
         * Imposta la lingua dell'applicazione in base alla lingua specificata.
         *
         * @param lingua La lingua da impostare (ad esempio, "it" per l'italiano).
         */
        public void setLocal(String lingua){
            Resources resources = getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            Configuration config = resources.getConfiguration();
            config.setLocale(new Locale(lingua));
            resources.updateConfiguration(config, resources.getDisplayMetrics());
            onConfigurationChanged(config);

        }

    }

