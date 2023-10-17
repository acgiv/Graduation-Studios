package com.laureapp.ui.card.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Tesi;

import java.util.ArrayList;

public class ListaTesiProfessoreAdapter extends ArrayAdapter<Tesi> {

    private Context mContext;
    private ArrayList<Tesi> mTesiList;

    public ListaTesiProfessoreAdapter(Context context, ArrayList<Tesi> tesiList) {
        super(context,0,tesiList);
        mContext = context;
        mTesiList = tesiList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.lista_tesi_studente, parent, false);
        }

        final Tesi currentTesi = mTesiList.get(position);

        TextView titleTextView = listItemView.findViewById(R.id.titoloTesi);

        if (currentTesi != null) {
            String titolo = currentTesi.getTitolo();

            if (titolo != null) {
                titleTextView.setText(titolo);
            } else {
                titleTextView.setText("Nessuna tesi trovata"); // Set an empty string or handle it as needed
            }
        }

        //Qui gestisco quando l'utnte clicca una tesi
        listItemView.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putSerializable("Tesi",currentTesi);
            Navigation.findNavController(v).navigate(R.id.action_tesiProfessoreFragment_to_tesiTabProfessoreFragment);

        });

        return listItemView;
    }
}
