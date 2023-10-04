package com.laureapp.ui.TesiProfessore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ListaTesiProfessoreAdapter extends FragmentStateAdapter {

    public ListaTesiProfessoreAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ListaTesiInfo();
            case 1:
                return new ListaTesiTesisti();
            case 2:
                return new ListaTesiVincoli();
            default:
                return new ListaTesiInfo();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
