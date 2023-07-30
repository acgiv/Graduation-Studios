package com.laureapp.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

//.R serve per riprendere gli ID delle componenti grafiche che verranno utilizzate e che si trovano all'interno del file xml
import com.laureapp.R;


public class HomeFragment extends Fragment {
    private Button playButton;
    private Button settingsButton;


    // utilizzato per il bind delle view del layout di riferimento con il Java e l'implementazione dei listener
    // quando la relativa view viene cliccata
    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home, container, false);

        playButton = v.findViewById(R.id.playButton);
        settingsButton = v.findViewById(R.id.settingsButton);

        playButton.setOnClickListener(v14 -> {
            Intent i = new Intent(getActivity(), com.laureapp.MainGame.class); //Inserire il nome della classe di nostro interesse
            getActivity().startActivity(i);
        });

        settingsButton.setOnClickListener(v13 -> {
            AppCompatActivity activity = (MainActivity) getActivity();
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new SettingsFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
    }
*/

}

