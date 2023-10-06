package com.laureapp.ui.card.TesiStudente;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Tesi;

import java.util.ArrayList;
import java.util.Date;

public class ElencoTesiFragment extends Fragment {

    Context context;
    private ElencoTesiAdapter adapter;
    private ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_elenco_tesi_studente,container,false);



    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();

        loadAllTesiData().addOnCompleteListener(tesiTask -> { //chiamo il metodo per ottenere le tesi in base alle id tesi ottenute
            if (tesiTask.isSuccessful()) {
                ArrayList<Tesi> tesiList = tesiTask.getResult();

                //mostro sulla listview tutte le tesi dello studente
                listView = view.findViewById(R.id.listAllTesiView);
                adapter = new ElencoTesiAdapter(getContext(), tesiList);
                listView.setAdapter(adapter);
            } else {
                Log.e("Tesi Firestore Error", "Error getting Tesi data", tesiTask.getException());
            }
        });

    }

    private Task<ArrayList<Tesi>> loadAllTesiData() {
        final ArrayList<Tesi> tesiList = new ArrayList<>();

        return FirebaseFirestore.getInstance()
                .collection("Tesi")
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Tesi tesi = new Tesi();
                            tesi.setId_tesi((Long) document.get("id_tesi"));
                            tesi.setId_vincolo((Long) document.get("id_vincolo"));

                            //codice che serve per convertire la data da firebase a sql
                            com.google.firebase.Timestamp firebaseTimestamp = (com.google.firebase.Timestamp) document.get("data_pubblicazione");
                            Date javaDate = firebaseTimestamp.toDate();
                            java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(javaDate.getTime());
                            tesi.setData_pubblicazione(sqlTimestamp);

                            tesi.setCiclo_cdl((String) document.get("ciclo_cdl"));
                            tesi.setAbstract_tesi((String) document.get("abstract_tesi"));
                            tesi.setTitolo((String) document.get("titolo"));
                            tesi.setTipologia((String) document.get("tipologia"));

                            if (tesi != null) {
                                tesiList.add(tesi);
                            }
                        }
                    }
                    return tesiList;
                });
    }

}