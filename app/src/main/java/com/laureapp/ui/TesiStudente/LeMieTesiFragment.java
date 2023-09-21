package com.laureapp.ui.TesiStudente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Utente;

public class LeMieTesiFragment extends Fragment {
    Utente utente = new Utente();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lemietesi, container, false);
    }

}
