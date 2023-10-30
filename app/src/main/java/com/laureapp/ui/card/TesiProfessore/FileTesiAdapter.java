package com.laureapp.ui.card.TesiProfessore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.FileTesi;

import java.util.List;

public class FileTesiAdapter extends RecyclerView.Adapter<FileTesiAdapter.FileTesiViewHolder> {
    private List<FileTesi> fileTesiList;

    public FileTesiAdapter(List<FileTesi> fileTesiList) {
        this.fileTesiList = fileTesiList;
    }

    @NonNull
    @Override
    public FileTesiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_tesi, parent, false);

        return new FileTesiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileTesiViewHolder holder, int position) {
        FileTesi fileTesi = fileTesiList.get(position);

        // Imposta il nome del file
        holder.nomeFileTextView.setText(fileTesi.getNomeFile());

        // Gestisci il clic sul pulsante di download
        holder.downloadMaterialeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aggiungi il codice per il download del file
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileTesiList.size();
    }

    public class FileTesiViewHolder extends RecyclerView.ViewHolder {
        TextView nomeFileTextView;
        ImageButton downloadMaterialeButton;

        public FileTesiViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeFileTextView = itemView.findViewById(R.id.nomeFileTextView);
            downloadMaterialeButton = itemView.findViewById(R.id.downloadMaterialeTesiProfessore);
        }
    }
}
