package com.laureapp.ui.card.TesiProfessore;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.laureapp.R;
import com.laureapp.databinding.FragmentInfoTesiProfessoreBinding;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.viewModel.TesiModelView;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

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

        /*context = requireContext();
        t_view = new TesiModelView(context);
        args = getArguments();*/

        return inflater.inflate(R.layout.fragment_info_tesi_professore, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("qualcosa","qualcosa");
        Bundle args = getArguments();
        Log.d("Parametri", String.valueOf(args));

        /*binding.inserisciTitolo.setText(tesi.getTitolo());
        binding.inserisciTipologia.setText(tesi.getTipologia());
        binding.inserisciDataPubblicazione.setText(tesi.getData_pubblicazione());
        binding.inserisciCicloCdl.setText(tesi.getCiclo_cdl());
        binding.inserisciAbstract.setText(tesi.getAbstract_tesi());*/


    }

    /*private void set_view() {
        binding.cardInfoTesi.setVisibility(View.GONE);
    }*/


}
