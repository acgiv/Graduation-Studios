package com.laureapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//.R serve per riprendere gli ID delle componenti grafiche che verranno utilizzate e che si trovano all'interno del file xml
import com.laureapp.R;



public class SettingsFragment extends Fragment {
    /*SharedPreferences shared;
    private RadioGroup radioBarGroup;
    private Button saveButton;
    private boolean barMode;
    private ImageView backArrow;


    // usato per bind di view del layout di riferimento con Java e l'implementazione dei listener quando la relativa view Ã¨ cliccata
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);

        radioBarGroup = v.findViewById(R.id.barRadioGroup);
        saveButton = v.findViewById(R.id.saveButton);
        backArrow = v.findViewById(R.id.back_pressed);

        //  Ã¨ premuta la freccia in alto a sinistra si torna indietro
        backArrow.setOnClickListener(v2 -> ((MainActivity) getActivity()).onBackPressed());

        // Ã¨ premuto il bottone Salva
        saveButton.setOnClickListener(v1 -> {
            switch (radioBarGroup.getCheckedRadioButtonId()) {
                case R.id.slideButton:
                    barMode = false;
                    break;
                case R.id.moveButton:
                    barMode = true;
                    break;
            }

            shared = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.putBoolean("accel", barMode);
            editor.apply();

            getActivity().onBackPressed();  // simula il 'back' del dispositivo -> va indietro (in pila)
        });


        return v;
    }


    //  per settare le impostazioni rispetto all'ultima modifica
    @Override
    public void onResume() {
        super.onResume();
    }
*/

}
