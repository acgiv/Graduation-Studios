package com.laureapp.ui.card.TesiProfessore;

import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.Locale;
import java.util.StringJoiner;

public class InfoTesiFragment extends Fragment {

    Tesi tesi;
    String titolo;
    String descrizione;
    String tipologia;
    Date dataPubblicazione;
    String cicloCdl;

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
        TextView tipologiaTesiProfessoreTextView = view.findViewById(R.id.insertTipologiaTesiProfessore);
        TextView dataPubblicazioneProfessoreTextView = view.findViewById(R.id.insertDataPubblicazioneTesiProfessore);
        TextView cicloCdlProfessoreTextView = view.findViewById(R.id.insertCicloCdlTesiProfessore);
        TextView abstractProfessoreTextView = view.findViewById(R.id.insertAbstractTesiProfessore);

        Log.d("ValoriInfo", String.valueOf(args));

        if (args != null) { //se non sono null

            tesi = (Tesi) args.getSerializable("Tesi"); //prendo la tesi dagli args
            if (tesi != null) {
                //mi passo tutti i parametri di una tesi
                titolo = tesi.getTitolo();
                tipologia = tesi.getTipologia();
                dataPubblicazione = tesi.getData_pubblicazione();
                cicloCdl = tesi.getCiclo_cdl();
                descrizione = tesi.getAbstract_tesi();

                titoloTesiProfessoreTextView.setText(titolo);
                Log.d("Titolo",titolo);
                tipologiaTesiProfessoreTextView.setText(tipologia);
                Log.d("Tipologia",tipologia);

                //Formatto la data per convertirla da SQL a java.date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = dateFormat.format(dataPubblicazione);
                dataPubblicazioneProfessoreTextView.setText(formattedDate);

                cicloCdlProfessoreTextView.setText(cicloCdl);
                abstractProfessoreTextView.setText(descrizione);

            }

        }

    }

}