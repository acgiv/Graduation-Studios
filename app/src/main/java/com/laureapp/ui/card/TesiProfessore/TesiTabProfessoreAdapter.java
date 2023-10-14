package com.laureapp.ui.card.TesiProfessore;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TesiTabProfessoreAdapter extends FragmentStateAdapter {

    private static final int NUM_TABS = 2; //Il numero di tab che hai
    Context context;
    String email;
    Bundle args;

    public TesiTabProfessoreAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        //Restituisci il fragment associato a ciascun tab
        switch (position) {
            case 0:
                InfoTesiFragment infoTesiFragment = new InfoTesiFragment();
                infoTesiFragment.setArguments(args); //Passa gli argomenti

                return infoTesiFragment;

            case 1:
                VincoliTesiFragment vincoliTesiFragment = new VincoliTesiFragment();
                vincoliTesiFragment.setArguments(args); //Passa gli argomenti

                return vincoliTesiFragment;

            default:
                return null;
        }
    }

    @Override
    public int getItemCount() { return NUM_TABS; }
}
