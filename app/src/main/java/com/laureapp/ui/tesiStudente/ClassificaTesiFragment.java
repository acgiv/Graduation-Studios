package com.laureapp.ui.tesiStudente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Utente;

import java.util.ArrayList;

public class ClassificaTesiFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_classificatesi, container, false);
    }

}
