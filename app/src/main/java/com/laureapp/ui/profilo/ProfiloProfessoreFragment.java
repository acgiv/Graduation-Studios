package com.laureapp.ui.profilo;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.laureapp.R;
import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.dao.ProfessoreDao;
import com.laureapp.ui.roomdb.repository.ProfessoreRepository;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProfiloProfessoreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String RUOLO = "ruolo";
    private SQLiteDatabase database;

    // TODO: Rename and change types of parameters
    private String ruolo;
    private int columnIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle;
        bundle = getArguments();
        if (bundle != null) {
            ruolo = bundle.getString("ruolo"); // Ottengo il ruolo dal bundle se disponibile
            Log.d("ruolo",ruolo);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            View view = inflater.inflate(R.layout.fragment_profilo_professore, container, false);

            ProfessoreRepository professoreRepository = new ProfessoreRepository(getActivity());
            RoomDbSqlLite db = RoomDbSqlLite.getDatabase(requireActivity());


            try {
                db.professoreDao().getAllProfessore();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Cursor cursor = (Cursor) professoreRepository.findAllById(uid);
                Log.d("Id Utente", uid);

                if (cursor != null && cursor.moveToFirst()) {
                    @SuppressLint("Range") String nome = cursor.getString(cursor.getColumnIndex("nome"));
                    @SuppressLint("Range") String cognome = cursor.getString(cursor.getColumnIndex("cognome"));
                    @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
                    @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));

                    // Imposta i dati nelle view appropriate
                    EditText nomeTextView = view.findViewById(R.id.nome_prof);
                    EditText cognomeTextView = view.findViewById(R.id.cognome_profilo_prof);
                    EditText emailTextView = view.findViewById(R.id.email_profilo_prof);
                    EditText passwordTextView = view.findViewById(R.id.pass_profilo_prof);

                    nomeTextView.setTextColor(Color.BLACK);
                    nomeTextView.setText("Nome: " + nome);
                    cognomeTextView.setText("Cognome: " + cognome);
                    emailTextView.setText("Email: " + email);
                    passwordTextView.setText("Password: " + password);
                }

                if (cursor != null) {
                    cursor.close();
                } else {
                    Log.d("Cursore nullo", "Query senza risultato");
                }
            } catch (SQLException e) {
                // Gestisci eccezioni qui
            } finally {
                //database.close();
            }

            return view;
            // Ora la variabile 'uid' contiene l'UID dell'utente corrente
        } else {
            // L'utente non è attualmente loggato, gestisci di conseguenza
        }
        return inflater.inflate(R.layout.fragment_profilo_professore, container, false);
    }
}