package com.laureapp.ui.card.Adapter;

import android.content.Context;
import android.telephony.mbms.FileInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.laureapp.R;

import java.util.ArrayList;

public class InfoTesiProfessoreAdapter extends BaseAdapter {
    private ArrayList<String> nomiFile;
    private LayoutInflater inflater;

    public InfoTesiProfessoreAdapter(Context context, ArrayList<String> nomiFile) {
        this.nomiFile = nomiFile;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (nomiFile != null) {
            return nomiFile.size();
        } else {
            return 0;
        }
    }
    @Override
    public Object getItem(int position) {
        return nomiFile.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface DownloadButtonClickListener {
        void onDownloadButtonClick(int position);
    }

    private DownloadButtonClickListener downloadButtonClickListener;

    public void setDownloadButtonClickListener(DownloadButtonClickListener listener) {
        this.downloadButtonClickListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_file_tesi, parent, false);
        }

        // Ottieni l'oggetto FileInfo dalla lista
        String fileInfo = nomiFile.get(position);

        // Collega i dati dell'oggetto FileInfo alle viste nel layout del singolo elemento della ListView
        TextView fileNameTextView = convertView.findViewById(R.id.nomeFileTextView);
        fileNameTextView.setText(fileInfo.toString());

        // Aggiungi un gestore di clic per il download del file se necessario
        ImageButton downloadButton = convertView.findViewById(R.id.downloadMaterialeButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadButtonClickListener != null) {
                    downloadButtonClickListener.onDownloadButtonClick(position);
                }
            }
        });

        return convertView;
    }

}
