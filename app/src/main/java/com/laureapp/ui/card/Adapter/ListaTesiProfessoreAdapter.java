package com.laureapp.ui.card.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Tesi;

import java.util.ArrayList;

public class ListaTesiProfessoreAdapter extends ArrayAdapter<Tesi> {

    private Context mContext;
    private ArrayList<Tesi> mTesiList;
    private NavController mNav;

    public ListaTesiProfessoreAdapter(Context context, ArrayList<Tesi> tesiList) {
        super(context,0,tesiList);
        mContext = context;
        mTesiList = tesiList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.lista_tesi_professore, parent, false);
        }

        final Tesi currentTesi = mTesiList.get(position);

        TextView titleTextView = listItemView.findViewById(R.id.titoloTesiProfessore);

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
            Navigation.findNavController(v).navigate(R.id.action_tesiProfessoreFragment_to_tesiTabProfessoreFragment,args);

        });

        return listItemView;
    }
}