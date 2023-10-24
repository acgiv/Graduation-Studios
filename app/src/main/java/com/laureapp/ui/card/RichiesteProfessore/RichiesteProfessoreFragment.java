package com.laureapp.ui.card.RichiesteProfessore;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laureapp.R;
import com.laureapp.ui.card.Adapter.ElencoTesiAdapter;
import com.laureapp.ui.card.Adapter.RichiesteProfessoreAdapter;
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

    String titoloTesi;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        return inflater.inflate(R.layout.fragment_richieste_tesi_professore, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();
        email = getEmailFromSharedPreferences(); //chiamata al metodo per ottenere la mail

        if (email != null) { // se la mail non è nulla

            UtenteModelView utenteView = new UtenteModelView(context); // Inizializza utenteView con un'istanza di UtenteModelView
            ProfessoreModelView professoreModelView = new ProfessoreModelView(context); // Inizializza utenteView con un'istanza di UtenteModelView


            id_utente = utenteView.getIdUtente(email);
            id_professore = professoreModelView.findProfessore(id_utente);

            loadIdTesiDataByProfessoreId(id_professore).addOnCompleteListener(task -> {
                if (task.isSuccessful()) { //se il task è completato con successo
                    idTesiList = task.getResult(); //assegno gli id delle tesi ad una lista di tipo Long
                    if(idTesiList != null && !idTesiList.isEmpty()) { //se ci sono tesi associate al professore
                        loadTesiDataByIdTesi(idTesiList).addOnCompleteListener(task1 -> { //carico tutte le tesi associate
                            if (task1.isSuccessful()) {
                                tesiList = task1.getResult();


                            }

                        });

                        loadIdTesiStudenteByIdTesi(idTesiList).addOnCompleteListener(task2->{
                            if(task2.isSuccessful()){
                                idStudenteTesiList = task2.getResult();
                                Log.d("vedi", String.valueOf(idStudenteTesiList));
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
     * @param idTesiList lista di tutti gli id delle tesi
     * @return una lista di tipo Tesi contenente tutte le tesi filtrate per cognome relatore
     */
    private Task<ArrayList<Tesi>> loadTesiDataByIdTesi(ArrayList<Long> idTesiList) {
        final ArrayList<Tesi> tesiList = new ArrayList<>();
        final ArrayList<Tesi> tesiByIdProfessore = new ArrayList<>();

        return FirebaseFirestore.getInstance()
                .collection("Tesi")
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

                        for (Long idTesi : idTesiList) {
                            for (Tesi tesi : tesiList) {
                                if (idTesi.equals(tesi.getId_tesi())) {
                                    tesiByIdProfessore.add(tesi);
                                    break; // Esci dal ciclo interno dopo aver trovato una corrispondenza
                                }
                            }
                        }

                    }
                    return tesiByIdProfessore;
                });
    }

    /**
     * Metodo utilizzato per ottenere l'idStudenteTesi partendo dall'id della tesi associata al professore
     * Cerco all'interno della tabella StudenteTesi l'id_studente_tesi
     * @return idStudenteTesiList lista contenente tutti gli id tesi legate allo studente
     *
     */

    private Task<ArrayList<Long>> loadIdTesiStudenteByIdTesi(ArrayList<Long> idTesiList) {
        final ArrayList<Long> idStudenteTesiList = new ArrayList<>();

        return FirebaseFirestore.getInstance()
                .collection("StudenteTesi")
                .whereIn("id_tesi", idTesiList)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Long data = doc.getLong("id_studente_tesi");
                            if (data != null) {
                                idStudenteTesiList.add(data);
                            }
                        }
                    }
                    return idStudenteTesiList;
                });
    }




}