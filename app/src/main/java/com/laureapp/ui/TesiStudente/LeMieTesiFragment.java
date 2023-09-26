package com.laureapp.ui.TesiStudente;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.laureapp.R;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.StudenteTesiModelView;
import com.laureapp.ui.roomdb.viewModel.TesiModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

public class LeMieTesiFragment extends Fragment {
    Context context;
    UtenteModelView utenteView = new UtenteModelView(context); // Inizializza utenteView con un'istanza di UtenteModelView
    ;
    Long id_utente;
    Long id_studente;
    String email;

    Long id_tesi_studente;

    TesiModelView tesiView = new TesiModelView(context);
    StudenteModelView studenteView = new StudenteModelView(context);

    StudenteTesiModelView tesiStudenteView = new StudenteTesiModelView(context);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        View rootView = inflater.inflate(R.layout.fragment_lemietesi, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();

        email = getEmailFromSharedPreferences(); //chiamata al metodo per ottenere la mail
        if (email != null) { // se la mail non è nulla
            id_utente = utenteView.getIdUtente(email); //ottengo l'id dell'utente corrispondente a tale mail
            id_studente = studenteView.findStudente(id_utente); //ottengo l'id dello studente corrispondente all'id dell'utente
            id_tesi_studente = tesiStudenteView.getIdTesiFromIdStudente(id_studente);



        } else {
            // Email is not stored or is null
            Log.d("Email salvata:", "Non trovata");
        }

    }

    /**
     * Si utilizza questo metodo per prendere le preferenze salvate nel metodo presente in HomeFragment
     * Esso prende la cartella "preferenze" e ne ricava la mail o l'oggetto che ci serve.
     * @return
     */
    private String getEmailFromSharedPreferences() {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences("preferenze", Context.MODE_PRIVATE);
            return preferences.getString("email", null);
        }
        return null;
    }
}