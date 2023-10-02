package com.laureapp.ui.TesiStudente;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.laureapp.R;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

public class TesiStudenteFragment extends Fragment {
    Context context;
    String email;
    Bundle args;
    UtenteModelView utenteView =  new UtenteModelView(context);
    Long id_utente;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tesi_studente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();
        args = getArguments();
        TabLayout tabLayout = view.findViewById(R.id.TabLayoutTesi);
        ViewPager viewPager = view.findViewById(R.id.viewPagerTesi); // Add this line to get the ViewPager

        // Attach the ViewPager to the TabLayout
        tabLayout.setupWithViewPager(viewPager);

        // Create an instance of your TesiPagerAdapter and pass the arguments
        TesiPagerAdapter adapter = new TesiPagerAdapter(getChildFragmentManager(),args);

        // Set the adapter to the ViewPager
        viewPager.setAdapter(adapter);



    }




}