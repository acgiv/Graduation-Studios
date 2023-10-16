package com.laureapp.ui.card.Adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.laureapp.ui.card.TesiStudente.ElencoTesiFragment;
import com.laureapp.ui.card.TesiStudente.LeMieTesiFragment;

public class TesiStudenteTabAdapter extends FragmentStateAdapter {
    private static final int NUM_TABS = 2; // Il numero di tab che hai
    Context context;
    String email;
    Bundle args;


    public TesiStudenteTabAdapter(@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);


    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Restituisci il fragment associato a ciascun tab
        switch (position) {
            case 0:
                LeMieTesiFragment leMieTesiFragment = new LeMieTesiFragment();
                leMieTesiFragment.setArguments(args); // Passa gli argomenti

                return leMieTesiFragment;

            case 1:
                ElencoTesiFragment classificaTesiFragment = new ElencoTesiFragment();
                classificaTesiFragment.setArguments(args); // Passa gli argomenti


                return classificaTesiFragment;

            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUM_TABS;
    }
}