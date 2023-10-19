package com.laureapp.ui.card.TesiProfessore;

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

import com.laureapp.R;
import com.laureapp.databinding.FragmentInfoTesiProfessoreBinding;
import com.laureapp.databinding.FragmentProfiloBinding;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.viewModel.TesiModelView;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;

public class InfoTesiFragment extends Fragment {

    Tesi tesi;

    String titolo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_info_tesi_professore, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        TextView titoloTesiProfessoreTextView = view.findViewById(R.id.insertTitoloTesiProfesore);
        Log.d("ciao", String.valueOf(args));

        if (args != null) { //se non sono null

            tesi = (Tesi) args.getSerializable("Tesi"); //prendo la tesi dagli args
            titolo = tesi.getTitolo();

            titoloTesiProfessoreTextView.setText(titolo);
            Log.d("ciao",titolo);
        }

    }

}