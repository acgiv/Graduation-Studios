package com.laureapp.ui.card.TesiProfessore;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.entity.Vincolo;

public class VincoliTesiFragment extends Fragment {

    Tesi tesi;
    Long id_vincolo;
    Vincolo vincolo;
    Long media;
    Long esamiMancanti;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        return inflater.inflate(R.layout.fragment_vincoli_tesi_professore, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Prendo gli argomenti del layout precedente
        Bundle args = getArguments();

        //Inizializzo
        TextView tempisticheProfessoreTextView = view.findViewById(R.id.insertTempisticheTesiProfessore);
        TextView mediaVotiProfessoreTextView = view.findViewById(R.id.insertMediaVotiTesiProfessore);
        TextView esamiMancantiProfessoreTextView = view.findViewById(R.id.insertEsamiMancantiTesiProfessore);
        TextView skillProfessoreTextView = view.findViewById(R.id.insertSkillTesiProfessore);

        if (args != null) { //Se non sono null

            tesi = (Tesi) args.getSerializable("Tesi"); //Prendo la tesi dagli args

            if(tesi != null) {

                id_vincolo = tesi.getId_vincolo(); //Ottengo l'id del vincolo

                loadVincoloData(id_vincolo).addOnCompleteListener(taskVincolo -> { //Carico i vincoli
                    if(taskVincolo.isSuccessful()) {
                        vincolo = taskVincolo.getResult();
                        media = vincolo.getMedia_voti();
                        esamiMancanti = vincolo.getEsami_mancanti_necessari();

                        tempisticheProfessoreTextView.setText(vincolo.getTempistiche().toString());
                        mediaVotiProfessoreTextView.setText(media.toString());
                        esamiMancantiProfessoreTextView.setText(esamiMancanti.toString());
                        skillProfessoreTextView.setText(vincolo.getSkill());

                    } else {
                        Log.e("Vincolo Firestore Error","Error getting vincolo data", taskVincolo.getException());
                    }
                });

            }
        }

    }

    /**
     * Questo metodo consente di ottenere i dati dei vincoli delle tesi da firestore e riempire la entity Vincolo
     *
     * @param id_vincolo id del vincolo legata alla tesi
     * @return entity Vincolo
     */

    private Task<Vincolo> loadVincoloData(Long id_vincolo) {
        final Vincolo vincolo = new Vincolo();

        //Crea una Firestore query per prendere Tesi documents abbinando gli ID
        Query query = FirebaseFirestore.getInstance()
                .collection("Vincolo")
                .whereEqualTo("id_vincolo", id_vincolo);
        return query.get().continueWith(task -> {
            if (task.isSuccessful()) {
                for(QueryDocumentSnapshot document : task.getResult()) {
                    vincolo.setId_vincolo((Long) document.get("id_vincolo"));
                    vincolo.setTempistiche((Long) document.get("tempistiche"));
                    vincolo.setMedia_voti((Long) document.get("media_voti"));
                    vincolo.setEsami_mancanti_necessari((Long) document.get("esami_mancanti_necessari"));
                    vincolo.setSkill((String) document.get("skill"));
                }
            }
            return vincolo;
        });

    }

}
