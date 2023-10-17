package com.laureapp.ui.card.TesiProfessore;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.laureapp.R;

public class InfoTesiFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        return inflater.inflate(R.layout.fragment_info_tesi_professore, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("qualcosa","qualcosa");
        Bundle args = getArguments();
        Log.d("skibida", String.valueOf(args));
    }


}
