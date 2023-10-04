package com.laureapp.ui.card.TesiStudente;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.laureapp.R; // Replace with the correct package name
import com.laureapp.ui.roomdb.entity.Tesi;

import java.util.ArrayList;

public class LeMieTesiAdapter extends ArrayAdapter<Tesi> {
    private Context mContext;
    private ArrayList<Tesi> mTesiList;

    public LeMieTesiAdapter(Context context, ArrayList<Tesi> tesiList) {
        super(context, 0, tesiList);
        mContext = context;
        mTesiList = tesiList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.lista_tesi_studente, parent, false);
        }

        Tesi currentTesi = mTesiList.get(position);

        TextView titleTextView = listItemView.findViewById(R.id.titoloTesi);

        // Check for null values before setting text
        if (currentTesi != null) {
            String title = currentTesi.getTitolo();

            if (title != null) {
                titleTextView.setText(title);
            } else {
                titleTextView.setText("Nessuna tesi trovata"); // Set an empty string or handle it as needed
            }

        }

        return listItemView;
    }
}