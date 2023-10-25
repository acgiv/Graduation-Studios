package com.laureapp.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;



import com.laureapp.R;

public class DiscussioneFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_discussione, container, false);


        // Recupera i dati passati dall'adapter
        Bundle args = getArguments();
        if (args != null) {
            String segnalazione = args.getString("segnalazione");

            // Ora puoi utilizzare 'segnalazione' per personalizzare la visualizzazione del fragment
            // Ad esempio, impostare il titolo o caricare i dettagli della discussione
        }

        return rootView;
    }

}