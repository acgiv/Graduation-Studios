package com.laureapp.ui.card.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.laureapp.ui.card.TesiProfessore.InfoTesiFragment;
import com.laureapp.ui.card.TesiProfessore.VincoliTesiFragment;

/**
 * Questa classe rappresenta un adattatore per gestire le schede di visualizzazione delle tesi in un'applicazione per professori.
 */
public class TesiTabProfessoreAdapter extends FragmentStateAdapter {

    private static final int NUM_TABS = 2; //Il numero di tab che hai
    Context context;
    String email;
    Bundle args;

    /**
     * Crea un nuovo adattatore per la gestione delle schede delle tesi per i professori.
     *
     * @param fragmentActivity l'oggetto FragmentActivity associato all'adattatore.
     * @param savedInstanceState un bundle di dati salvati da ripristinare (argomenti).
     */
    public TesiTabProfessoreAdapter(@NonNull FragmentActivity fragmentActivity, Bundle savedInstanceState) {
        super(fragmentActivity);
        this.args = savedInstanceState; // Imposta il bundle con gli argomenti

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

    /**
     * Restituisce il numero di schede gestite da questo adattatore.
     *
     * @return il numero di schede gestite dall'adattatore.
     */
    @Override
    public int getItemCount() { return NUM_TABS; }
}
