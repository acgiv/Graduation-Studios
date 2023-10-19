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

    FragmentInfoTesiProfessoreBinding binding;
    Bundle args;
    TesiModelView t_view;
    Context context;
    Tesi tesi;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {

        context = requireContext();
        //t_view = new TesiModelView(context);
        Log.d("Tesi passata", String.valueOf(getArguments()));
        args = getArguments();
        if(args != null) {
            tesi = args.getSerializable("Tesi",Tesi.class);
        }

        binding = FragmentInfoTesiProfessoreBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("qualcosa","qualcosa");
        //Bundle args = getArguments();
        //Log.d("Parametri", String.valueOf(args));

        /*binding.inserisciTitolo.setText(tesi.getTitolo());
        binding.inserisciTipologia.setText(tesi.getTipologia());
        binding.inserisciDataPubblicazione.setText(String.valueOf(tesi.getData_pubblicazione())); //Converto la data in Stringa con il casting
        binding.inserisciCicloCdl.setText(tesi.getCiclo_cdl());
        binding.inserisciAbstract.setText(tesi.getAbstract_tesi());*/

        /*TextView text = view.findViewById(R.id.inserisciTitolo);
        text.setText("DCNHASKC");*/ //alternativa del binding


    }

}
