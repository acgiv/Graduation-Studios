package com.laureapp.ui.card.RichiesteProfessore;


import android.content.Context;
import android.content.SharedPreferences;
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
import com.laureapp.ui.card.Adapter.RichiesteProfessoreAdapter;
import com.laureapp.ui.roomdb.entity.RichiesteTesi;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.viewModel.ProfessoreModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class RichiesteProfessoreFragment extends Fragment {

    private RichiesteProfessoreAdapter adapter;

    Context context;
    String email;

    Long id_utente;
    Long id_professore;

    ArrayList<Tesi> tesiList = new ArrayList<>();
    ArrayList<Long> idTesiList = new ArrayList<>();
    ArrayList<Long> idStudenteTesiList = new ArrayList<>();

    ArrayList<String> titoliTesi = new ArrayList<>();

    ArrayList<RichiesteTesi> richiesteTesi = new ArrayList<>();
    Long idRichiestaTesi;
    Long idStudenteTesi;
    String stato;

    ArrayList<Long> idTesiListFromRichieste= new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        return inflater.inflate(R.layout.fragment_richieste_tesi_professore, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();
        email = getEmailFromSharedPreferences();
        ListView listView = view.findViewById(R.id.listRichiesteProfessoreView);
        listView.setNestedScrollingEnabled(true);


        if (email != null) {
            UtenteModelView utenteView = new UtenteModelView(context);
            ProfessoreModelView professoreModelView = new ProfessoreModelView(context);

            id_utente = utenteView.getIdUtente(email);
            id_professore = professoreModelView.findProfessore(id_utente);

            loadIdTesiDataByProfessoreId(id_professore).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    idTesiList = task.getResult();
                    if (idTesiList != null && !idTesiList.isEmpty()) {


                        loadRichiesteTesiByIdTesi(idTesiList).addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                richiesteTesi = task2.getResult();
                               idTesiListFromRichieste = extractIdTesiFromRichieste(richiesteTesi);

                                if (!idTesiListFromRichieste.isEmpty()) {
                                    loadTesiDataByIdTesiRichieste(idTesiListFromRichieste).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            tesiList = task1.getResult();

                                            adapter = new RichiesteProfessoreAdapter(context, richiesteTesi, tesiList);
                                            listView.setAdapter(adapter);
                                            Log.d("vedi", String.valueOf(richiesteTesi));
                                        }
                                    });
                                }
                            }
                        });

                    }


                } else {
                    Log.e("Firestore Error", "Error getting data", task.getException());
                }

            });
        }
    }


    /**
     * Si utilizza questo metodo per prendere le preferenze salvate nel metodo presente in HomeFragment
     * Esso prende la cartella "preferenze" e ne ricava la mail o l'oggetto che ci serve.
     * @return
     */
    private String getEmailFromSharedPreferences() {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences("preferenze", Context.MODE_PRIVATE);
            return preferences.getString("email", null);
        }
        return null;
    }

    /**
     * Questo metodo mi permette di caricare da firestore gli id delle tesi dando come parametro l'id del professore
     *
     * @param id_professore
     * @return una lista di tipo Long contenente gli id delle tesi associate allo studente
     */
    private Task<ArrayList<Long>> loadIdTesiDataByProfessoreId(Long id_professore) {
        final ArrayList<Long> idTesiList = new ArrayList<>();

        return FirebaseFirestore.getInstance()
                .collection("TesiProfessore")
                .whereEqualTo("id_professore", id_professore)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Long data = doc.getLong("id_tesi");
                            if (data != null) {
                                idTesiList.add(data);
                            }
                        }
                    }
                    return idTesiList;
                });
    }

    /**
     * Questo metodo mi permette di caricare da firestore le tesi in base al cognome del relatore
     *
     * @param idTesiListFromRichieste lista di tutti gli id delle tesi presa dalla tabella richieste
     * @return una lista di tipo Tesi contenente tutte le tesi filtrate per cognome relatore
     */
    private Task<ArrayList<Tesi>> loadTesiDataByIdTesiRichieste(ArrayList<Long> idTesiListFromRichieste) {
        final ArrayList<Tesi> tesiList = new ArrayList<>();

        return FirebaseFirestore.getInstance()
                .collection("Tesi")
                .whereIn("id_tesi",idTesiListFromRichieste)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Tesi tesi = new Tesi();
                            tesi.setId_tesi((Long) document.get("id_tesi"));
                            tesi.setId_vincolo((Long) document.get("id_vincolo"));

                            // Converto da firebase timestamp a sql timestamp
                            com.google.firebase.Timestamp firebaseTimestamp = (com.google.firebase.Timestamp) document.get("data_pubblicazione");
                            Date javaDate = firebaseTimestamp.toDate();
                            java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(javaDate.getTime());
                            tesi.setData_pubblicazione(sqlTimestamp);

                            tesi.setCiclo_cdl((String) document.get("ciclo_cdl"));
                            tesi.setAbstract_tesi((String) document.get("abstract_tesi"));
                            tesi.setTitolo((String) document.get("titolo"));
                            tesi.setTipologia((String) document.get("tipologia"));

                            tesiList.add(tesi);
                        }


                    }
                    return tesiList;
                });
    }

    private Task<ArrayList<RichiesteTesi>> loadRichiesteTesiByIdTesi(ArrayList<Long> idTesiList) {
        final ArrayList<RichiesteTesi> richiesteTesi = new ArrayList<>();

        return FirebaseFirestore.getInstance()
                .collection("RichiesteTesi")
                .whereIn("id_tesi", idTesiList)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Long idTesi = doc.getLong("id_tesi");
                            Long idRichiestaTesi = doc.getLong("id_richiesta_tesi");
                            String stato = doc.getString("stato");
                            Long idStudente = doc.getLong("id_studente");
                            boolean soddisfaRequisiti = doc.getBoolean("soddisfa_requisiti");
                            if (idTesi != null && idRichiestaTesi != null && stato != null) {
                                RichiesteTesi richiestaTesi = new RichiesteTesi();
                                richiestaTesi.setId_tesi(idTesi);
                                richiestaTesi.setId_richiesta_tesi(idRichiestaTesi);
                                richiestaTesi.setStato(stato);
                                richiestaTesi.setId_studente(idStudente);
                                richiestaTesi.setSoddisfa_requisiti(soddisfaRequisiti);
                                richiesteTesi.add(richiestaTesi);

                            }
                        }
                    }

                    return richiesteTesi;
                });


    }

    private ArrayList<Long> extractIdTesiFromRichieste(ArrayList<RichiesteTesi> richiesteList) {
        idTesiListFromRichieste = new ArrayList<>();

        for (RichiesteTesi richiesta : richiesteList) {
            idTesiListFromRichieste.add(richiesta.getId_tesi());
        }
        return idTesiListFromRichieste;
    }



}