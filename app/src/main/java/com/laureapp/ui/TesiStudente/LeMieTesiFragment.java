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
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

public class LeMieTesiFragment extends Fragment {
    Context context;
    Bundle args;
    UtenteModelView utenteView;
    Long id_utente;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        View rootView = inflater.inflate(R.layout.fragment_lemietesi, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();

        String storedEmail = getEmailFromSharedPreferences(); //chiamata al metodo per ottenere la mail
        if (storedEmail != null) {
            // Use the stored email value
            Log.d("Email salvata:", storedEmail);
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