package com.laureapp.ui.TesiStudente;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

public class LeMieTesiFragment extends Fragment {

    Context context;
    String email;
    Bundle args;
    UtenteModelView utenteView =  new UtenteModelView(context);
    Long id_utente;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        View rootView = inflater.inflate(R.layout.fragment_lemietesi, container, false);

        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();
        args = getArguments();




        TextView textView = view.findViewById(R.id.leMieTesiTextView);
        textView.setText("Ciao");





        }



}
