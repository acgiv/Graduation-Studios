package com.laureapp.ui.card.TesiProfessore;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.laureapp.R;
import com.laureapp.ui.card.Adapter.TesiTabProfessoreAdapter;

public class TesiTabProfessoreFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private TesiTabProfessoreAdapter viewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_tesi_professore, container, false);
        tabLayout = view.findViewById(R.id.TabLayoutTesiProfessore);
        viewPager2 = view.findViewById(R.id.viewPager2);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        savedInstanceState = getArguments();
        viewPagerAdapter = new TesiTabProfessoreAdapter((FragmentActivity) requireContext(),savedInstanceState); //Passo gli argomenti nei layout collegati
        viewPager2.setAdapter(viewPagerAdapter);

        Bundle args = getArguments();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }
}
