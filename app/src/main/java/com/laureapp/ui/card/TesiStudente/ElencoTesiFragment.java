package com.laureapp.ui.card.TesiStudente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SearchView;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.laureapp.R;
import com.laureapp.ui.card.Adapter.ElencoTesiAdapter;
import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.entity.TesiProfessore;
import com.laureapp.ui.roomdb.viewModel.ProfessoreModelView;
import com.laureapp.ui.roomdb.viewModel.TesiProfessoreModelView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ElencoTesiFragment extends Fragment {

    Context context;
    private ElencoTesiAdapter adapter;
    private ListView listView;
    private String titoloTesiCercata = "";

    private AlertDialog filterDialog;
    ArrayList<Tesi> tesiList = new ArrayList<>();

    ArrayList<Long> idTesis = new ArrayList<>();
    final ArrayList<Long> idTesiProfessoreList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_elencotesi, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();

        SearchView searchView = view.findViewById(R.id.searchTesiView);
        listView = view.findViewById(R.id.listClassificaTesiView);

        //Definisco il bottone filtra
        Button filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(view1 -> {
            view.setAlpha(0.1f); // Imposta l'opacità desiderata (0.0-1.0)
            showFilterDialog(view,tesiList); //quando viene cliccato il pulsante filtra si apre il dialog
        });



        loadAllTesiData().addOnCompleteListener(tesiTask -> {
            if (tesiTask.isSuccessful()) {
                tesiList = tesiTask.getResult();
                adapter = new ElencoTesiAdapter(getContext(), tesiList);
                listView.setAdapter(adapter);
            } else {
                Log.e("Tesi Firestore Error", "Error getting Tesi data", tesiTask.getException());
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission (if needed)
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                titoloTesiCercata = newText;
                loadSearchedTesiData(titoloTesiCercata).addOnCompleteListener(tesiTask -> {
                    if (tesiTask.isSuccessful()) {
                        ArrayList<Tesi> searchedTesiList = tesiTask.getResult();
                    } else {
                        Log.e("Tesi Firestore Error", "Error getting searched Tesi data", tesiTask.getException());
                    }
                });
                return true;
            }
        });




    }

    /**
     * Questo metodo serve per caricare la lista di tutte le tesi
     * @return la lista delle tesi
     */
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

    /**
     * Questo metodo serve per prendere ed aggiornare la lista delle tesi da firestore filtrandole
     * in base al testo di ricerca
     * @param searchText rappresenta il titolo della tesi da ricercare
     * @return la lista delle tesi filtrata
     */
    private Task<ArrayList<Tesi>> loadSearchedTesiData(String searchText) {
        final ArrayList<Tesi> tesiList = new ArrayList<>();

        return FirebaseFirestore.getInstance()
                .collection("Tesi") //collection di firebase
                .orderBy("titolo") //ordino i risultati in base al titolo
                .startAt(searchText)
                .endAt(searchText + "\uf8ff")
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //se la ricerca ha successo, assegno i campi della tesi in un oggetto Tesi
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
                        // aggiorno l'adapter con i nuovi dati
                        if (adapter != null) {
                            adapter.clear();
                            adapter.addAll(tesiList);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    return tesiList;
                });
    }

    public void showFilterDialog(View rootView, ArrayList<Tesi> tesiList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_popup_filtra_tesi, null);
        builder.setView(view);

        /**
         * Definisco tutti gli input text
         */

        EditText editTextCognome = view.findViewById(R.id.cognomeRelatore);
        EditText editTextTipologia = view.findViewById(R.id.tipologia);
        EditText editTextCiclocdl = view.findViewById(R.id.ciclocdl);
        EditText editTextMedia = view.findViewById(R.id.media);
        EditText editTextEsami = view.findViewById(R.id.numeroEsamiMancanti);


        /**
         * Gestione del bottone conferma
         */
        Button confermaButton = view.findViewById(R.id.avviaRicerca);
        confermaButton.setOnClickListener(v -> {
            //prendo i valori degli editext
            String cognome = editTextCognome.getText().toString();
            String tipologia = editTextTipologia.getText().toString();
            String ciclocdl = editTextCiclocdl.getText().toString();
            String media = editTextMedia.getText().toString();
            String esami = editTextEsami.getText().toString();

            getTesiIds(); //chiamo il metodo per ottenere gli id delle tesi
            loadIdTesiProfessoreData();


        });

        /**
         * Gestione del bottone annulla
         */
        Button annullaButton = view.findViewById(R.id.annullaFiltra);
        annullaButton.setOnClickListener(v -> filterDialog.dismiss());

        filterDialog = builder.create(); //creo il dialog

        /**
         * Gestione del metodo on dismiss quando si clicca fuori o si clicca annulla
         */
        filterDialog.setOnDismissListener(dialog -> {
            rootView.setAlpha(1); //reimpose l'opacità di default

        });

        //mostro il filter dialog
        filterDialog.show();
    }


    /**
     * Metodo per ottenere gli id delle tesi dalla lista delle tesi
     * @return id delle tesi
     */
    private ArrayList<Long> getTesiIds() {
        for (Tesi tesi : tesiList) {
            Long tesiId = tesi.getId_tesi();
            idTesis.add(tesiId);
        }
        return idTesis;

    }

    /**
     * Questo metodo mi permette di caricare da firestore gli id delle tesi dando come parametro l'id dello studente
     *
     *
     * @return una lista di tipo Long contenente gli id delle tesi associate allo studente
     */
    private Task<ArrayList<Long>> loadIdTesiProfessoreData() {
        final ArrayList<Task<QuerySnapshot>> queryTasks = new ArrayList<>();
        final ArrayList<Long> idTesiProfessoreList = new ArrayList<>();

        // Itera su ciascun ID nella lista
        for (Long id_tesi : idTesis) {
            Log.d("jek1", String.valueOf(id_tesi));
            Task<QuerySnapshot> queryTask = FirebaseFirestore.getInstance()
                    .collection("TesiProfessore")
                    .whereEqualTo("id_tesi", id_tesi)
                    .get();
            queryTasks.add(queryTask);
        }

        return Tasks.whenAllSuccess(queryTasks)
                .continueWith(task -> {
                    for (Task<QuerySnapshot> queryResultTask : queryTasks) {
                        if (queryResultTask.isSuccessful()) {
                            QuerySnapshot querySnapshot = queryResultTask.getResult();
                            for (QueryDocumentSnapshot doc : querySnapshot) {
                                Long data = doc.getLong("id_tesi");
                                if (data != null) {
                                    idTesiProfessoreList.add(data);
                                    Log.d("jek", String.valueOf(idTesiProfessoreList));

                                }
                            }
                        }
                    }
                    return idTesiProfessoreList;
                });
    }



}