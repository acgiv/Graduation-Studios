package com.laureapp.ui;

//import android.app.FragmentManager;
import android.database.sqlite.SQLiteDatabase;
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
        setContentView(R.layout.activity_login);
        // Avvia DataEntryActivity quando viene eseguita MainActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        SQLiteDatabase database = this.openOrCreateDatabase("Laureapp", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS"
                .concat(" professore ( ")
                .concat("ID_PROFESSORE").concat(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .concat("NOME").concat(" VARCHAR, ")
                .concat("COGNOME").concat(" VARCHAR, ")
                .concat("FOREIGN KEY(ID_TESI) REFERENCES tesi(ID_TESI)")
                .concat(")")
        );

        database.execSQL("CREATE TABLE IF NOT EXISTS"
                .concat("studente ( ")
                .concat("ID_STUDENTE").concat(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .concat("MATRICOLA").concat(" INT(10), ")
                .concat("DATA_IMMATRICOLAZIONE").concat(" DATETIME ")
                .concat(")")
        );

        database.execSQL("CREATE TABLE IF NOT EXISTS"
                .concat("esame ( ")
                .concat("ID_ESAME").concat(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .concat("ESAME").concat(" VARCHAR, ")
                .concat("DATA_CONSEGUMENTO").concat(" DATETIME ")
                .concat(")")
        );

        database.execSQL("CREATE TABLE IF NOT EXISTS"
                .concat("vincolo ( ")
                .concat("ID_VINCOLO").concat(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .concat("TEMPISTICHE").concat(" VARCHAR, ")
                .concat("MEDIA_VOTI").concat(" INT(10), ")
                .concat("ESAMI_NECESSARI").concat(" VARCHAR, ")
                .concat("SKILL").concat(" VARCHAR")
                .concat(")")
        );

        database.execSQL("CREATE TABLE IF NOT EXISTS"
                .concat("tesi (")
                .concat("ID_TESI").concat(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .concat("FOREIGN KEY(id_ruolo) REFERENCES ruolo(id_ruolo), ")
                .concat("FOREIGN KEY(ID_VINCOLO) REFERENCES vincolo(ID_VINCOLO), ")
                .concat("TITOLO").concat(" VARCHAR, ")
                .concat("TIPOLOGIA").concat(" VARCHAR, ")
                .concat("ABSTRACT").concat(" VARCHAR, ")
                .concat("DATA_PUBBLICAZIONE").concat(" DATETIME")
                .concat(")")
        );

        database.execSQL("CREATE TABLE IF NOT EXISTS"
                .concat("tesi_professore ( ")
                .concat("ID_tesi_professore").concat(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .concat("RUOLO_PROFESSORE").concat(" VARCHAR, ")
                .concat("FOREIGN KEY(ID_STUDENTE) REFERENCES studente(ID_STUDENTE), ")
                .concat("FOREIGN KEY(ID_PROFESSORE) REFERENCES professore(ID_PROFESSORE) ")
                .concat(")")
        );

    }

}

