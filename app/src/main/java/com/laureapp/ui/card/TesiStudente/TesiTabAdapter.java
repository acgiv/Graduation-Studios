package com.laureapp.ui.card.TesiStudente;

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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Tesi;

public class TesiTabAdapter extends FragmentStateAdapter {
    private static final int NUM_TABS = 3; // Il numero di tab che hai
    Context context;
    String email;
    Bundle args;


    public TesiTabAdapter(@NonNull FragmentActivity fragmentActivity){
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
    public int getItemCount() {
        return NUM_TABS;
    }
}