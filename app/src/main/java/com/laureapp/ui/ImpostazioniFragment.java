package com.laureapp.ui;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.laureapp.R;

import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ImpostazioniFragment extends Fragment {

    //Variabili e Oggetti
    private Spinner spinner;
    private String[] languages;

    public ImpostazioniFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_impostazioni, container, false);

        spinner = view.findViewById(R.id.lingua);

        impostaLingua();


        return view;
    }

    private void impostaLingua() {
        languages = new String[]{getResources().getString(R.string.impostaLingua), getResources().getString(R.string.italiano), getResources().getString(R.string.inglese)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.requireContext(), android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLang = adapterView.getItemAtPosition(i).toString();
                if(selectedLang.equals(getResources().getString(R.string.italiano))){
                    setLocal("it");
                    saveLangPref("it");
                    Intent intent = requireActivity().getIntent();
                    getActivity().finish();
                    startActivity(intent);
                }else if(selectedLang.equals(getResources().getString(R.string.inglese))){
                    setLocal("en");
                    saveLangPref("en");
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void saveLangPref(String lingua) {
        // Saving the language preference
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LanguagePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Language", lingua);
        editor.apply();
    }

    public void setLocal(String lingua){
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.setLocale(new Locale(lingua));
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        onConfigurationChanged(config);
    }

}