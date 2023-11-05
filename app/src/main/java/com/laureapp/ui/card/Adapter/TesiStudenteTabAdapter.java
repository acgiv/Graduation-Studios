package com.laureapp.ui.card.Adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.laureapp.ui.card.TesiStudente.ElencoTesiFragment;
import com.laureapp.ui.card.TesiStudente.LeMieTesiFragment;

/**
 * Questa classe rappresenta un adattatore per gestire le schede di visualizzazione delle tesi in un'applicazione per studenti.
 */
public class TesiStudenteTabAdapter extends FragmentStateAdapter {
    private static final int NUM_TABS = 2; // Il numero di tab che hai
    Context context;
    String email;
    Bundle args;

    /**
     * Crea un nuovo adattatore per la gestione delle schede delle tesi studente.
     *
     * @param fragmentActivity l'oggetto FragmentActivity associato all'adattatore.
     */
    public TesiStudenteTabAdapter(@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);


    }

    /**
     * Restituisce il fragment associato a una determinata posizione di scheda.
     *
     * @param position la posizione della scheda per la quale ottenere il fragment associato.
     * @return il fragment associato alla posizione della scheda specificata.
     */
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

    /**
     * Restituisce il numero di schede gestite da questo adattatore.
     *
     * @return il numero di schede gestite dall'adattatore.
     */
    @Override
    public int getItemCount() {
        return NUM_TABS;
    }
}