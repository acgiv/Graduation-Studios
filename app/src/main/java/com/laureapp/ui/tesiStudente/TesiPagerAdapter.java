package com.laureapp.ui.tesiStudente;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TesiPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_TABS = 3; // Il numero di tab che hai
    private Bundle args; // Aggiungi una variabile per gli argomenti

    public TesiPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.args = args;
    }

    @Override
    public Fragment getItem(int position) {
        // Restituisci il fragment associato a ciascun tab
        switch (position) {
            case 0:
                LeMieTesiFragment leMieTesiFragment = new LeMieTesiFragment();
                leMieTesiFragment.setArguments(args); // Passa gli argomenti
                return leMieTesiFragment;

            case 1:
                ClassificaTesiFragment classificaTesiFragment = new ClassificaTesiFragment();
                classificaTesiFragment.setArguments(args); // Passa gli argomenti
                return classificaTesiFragment;

            case 2:
                ElencoTesiFragment elencoTesiFragment = new ElencoTesiFragment();
                elencoTesiFragment.setArguments(args); // Passa gli argomenti
                return elencoTesiFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Restituisci il titolo per ciascun tab
        switch (position) {
            case 0:
                return "Le Mie Tesi";
            case 1:
                return "Classifica Tesi";
            case 2:
                return "Elenco Tesi";
            default:
                return null;
        }
    }
}