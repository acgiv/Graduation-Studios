package com.laureapp.ui;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
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
import androidx.room.Database;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.laureapp.R;
//.R serve per riprendere gli ID delle componenti grafiche che verranno utilizzate e che si trovano all'interno del file xml
import com.google.android.material.navigation.NavigationView;
import com.laureapp.databinding.ActivityMainBinding;
import com.laureapp.ui.card.TesiStudente.DettagliTesiStudenteFragment;
import com.laureapp.ui.home.HomeFragment;
import com.laureapp.ui.login.LoginActivity;
import com.laureapp.ui.roomdb.entity.Tesi;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 *  Classe MainActivity
 */
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 3;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    AppBarConfiguration mAppBarConfiguration;
    ActivityMainBinding binding;
    String ruolo;
    NavController navController;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // PASSO LE INFORMAZIONI ATTRAVERSO IL BUNDLE IN FRAGMENT_HOME
        bundle = getIntent().getExtras();
        ruolo = bundle.getString("ruolo");
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        navController.navigate(R.id.fragment_home, bundle);



        // Imposta gli argomenti per il fragment
        navigationView = findViewById(R.id.navigation);





        createAppBar();




        navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        navigationView.bringToFront();

        NavController finalNavController = navController;

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.itemSettings) {

                finalNavController.navigate(R.id.action_fragment_home_to_impostazioniFragment);
                drawerLayout.closeDrawers(); // Chiude il menu


                Toast.makeText(getApplicationContext(), "Impostazioni", Toast.LENGTH_LONG).show();

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
                navController2.navigate(R.id.profilo_studente, bundle);
            }
            if (id2 == R.id.itemHome) {
                navController2.navigate(R.id.fragment_home, bundle);
            }
            if (id2 == R.id.itemScanQrCode) {
                scanQR();
            }

            return true;
        });

    }

    /**
     *  Barra superiore dell'applicazione
     */
    private void createAppBar() {
        // Creazione dell'AppBarConfiguration con le destinazioni desiderate
        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.fragment_home);
        drawerLayout = findViewById(R.id.drawer);

        // Aggiungi altre destinazioni principali se necessario
        mAppBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations)
                .setOpenableLayout(drawerLayout)
                .build();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        // Collega l'ActionBar o la Toolbar al NavController
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (navController.getCurrentDestination() != null &&
                    mAppBarConfiguration.getTopLevelDestinations().contains(navController.getCurrentDestination().getId())) {
                // Se l'utente è in una delle destinazioni principali, apri o chiudi il drawer
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }






    /**
     * Gestisce il pulsante di navigazione "Indietro" nella barra delle app.
     * @return true se la navigazione è stata gestita correttamente, false in caso contrario
     */
    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        navController.navigateUp();
        return super.onSupportNavigateUp();
   }


    /**
     * Metodo usato per lo scanQR, permette di mostrare la tesi trovata tramite QR
     * se l'utente conferma la richiesta di visualizzazione
     */
    private void scanQR(){
        if(CheckPermessiFotocamera()){
            ScanOptions options = new ScanOptions();
            options.setPrompt(getResources().getString(R.string.scanQrCode));
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            options.setCaptureActivity(CaptureAct.class);
            barLauncher.launch(options);
        }
    }

    /**
     * Avvia l'activity di scanning ddl QRCode
     */
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {

        if (result.getContents() != null) {

            String qrContent = result.getContents();
            Log.d("ContentsQrCode", qrContent);
            String[] elements = qrContent.split("\n"); // Supponendo che il separatore sia "/"

            if (elements.length >= 8) {
                String tesiId = elements[5].replaceAll("[^0-9]", ""); // Rimuove tutti i caratteri non numerici dalla stringa
                if (!tesiId.isEmpty()) {
                    loadTesiById(Long.valueOf(tesiId));
                    Log.d("id_tesiQr", tesiId);
                }
            } else {
                // Gestisci il caso in cui il contenuto del codice QR non contiene gli elementi previsti.
                // Puoi mostrare un messaggio di errore o gestire la situazione in un altro modo.
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.tesi);
            builder.setMessage("Errore qr code");
            builder.setOnDismissListener(DialogInterface::dismiss);
        }
    });






    /**
     * Il metodo verifica i permessi per l'utilizzo della fotocamera, se non sono ancora stati concessi li chiede all'utente
     * @return  restituisce true se i concessi sono già stati concessi, altrimenti false
     */
    private boolean CheckPermessiFotocamera() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                new AlertDialog.Builder(this)
                        .setTitle("Richiesta permessi fotocamera")
                        .setMessage("Il permesso è richiesto per l'utilizzo della fotocamera allo scopo della scannerizzazione dei QRCode")
                        .setPositiveButton(R.string.accetta, (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA))
                        .setNegativeButton(R.string.rifiuta, (dialog, which) -> dialog.dismiss()).create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            }
            return false;
        }
        return true;
    }

    /**
     * Override del metodo che gestisce la risposta alle richieste dei permessi.
     * @param requestCode Il codice della richiesta di permesso
     * @param permissions L'array di stringhe che rappresenta i permessi richiesti
     * @param grantResults L'array di interi che rappresenta i risultati delle richieste di permesso
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_CAMERA && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            scanQR();
        } else if(requestCode == REQUEST_CAMERA) {
            Toast.makeText(this, "Permesso negato", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Carica un oggetto Tesi dal database in base all'ID specificato.
     * @param id_tesi l'ID della tesi da caricare
     * @return un oggetto Task che rappresenta l'operazione di caricamento dell'oggetto Tesi dal database
     * @throws NoSuchElementException se non viene trovata alcuna tesi con l'ID specificato
     * @throws Exception se si verifica un errore durante l'operazione di caricamento dal database
     */
    private Task<Tesi> loadTesiById(Long id_tesi) {
        return FirebaseFirestore.getInstance()
                .collection("Tesi")
                .whereEqualTo("id_tesi", id_tesi)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();

                        if (!querySnapshot.isEmpty()) {
                            // Supponiamo che tu voglia gestire solo il primo documento trovato (se presente).
                            QueryDocumentSnapshot document = (QueryDocumentSnapshot) querySnapshot.getDocuments().get(0);

                            Tesi tesi = document.toObject(Tesi.class);

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Tesi", tesi);
                            navController.navigate(R.id.dettagliTesiStudente, bundle); // Naviga al fragment DettagliTesiStudente

                            return tesi;
                        } else {
                            throw new NoSuchElementException("Nessuna tesi trovata con l'ID: " + id_tesi);
                        }
                    } else {
                        throw task.getException();
                    }
                });
    }






}

