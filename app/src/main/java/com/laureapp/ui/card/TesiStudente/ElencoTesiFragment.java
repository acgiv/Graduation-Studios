package com.laureapp.ui.card.TesiStudente;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SearchView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laureapp.R;
import com.laureapp.ui.card.Adapter.ElencoTesiAdapter;
import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.entity.Vincolo;
import com.laureapp.ui.roomdb.viewModel.ProfessoreModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

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

    List<Utente> utentiList = new ArrayList<>();
    List<Professore> professoriList = new ArrayList<>();

    ArrayList<Long> idTesiList = new ArrayList<>();

    ArrayList<Vincolo> vincoliList = new ArrayList<>();

    Long media;
    Long esami;
    Long tempistiche;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_elencotesi, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();

        //Ottengo la lista degli utenti che mi servirà nel filtra
        UtenteModelView utenteModelView = new UtenteModelView(context);
        utentiList = utenteModelView.getAllUtente();

        //ottengo la lista dei professori che mi servirà nel filtra
        ProfessoreModelView professoreModelView = new ProfessoreModelView(context);
        professoriList = professoreModelView.getAllProfessore();


        SearchView searchView = view.findViewById(R.id.searchTesiView);
        listView = view.findViewById(R.id.listClassificaTesiView);

        //Definisco il bottone filtra
        Button filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(view1 -> {
            view.setAlpha(0.1f); // Imposta l'opacità desiderata (0.0-1.0)
            showFilterDialog(view,tesiList,utentiList); //quando viene cliccato il pulsante filtra si apre il dialog
        });


        //carico i dati delle tesi
        loadAllTesiData().addOnCompleteListener(tesiTask -> {
            if (tesiTask.isSuccessful()) {
                tesiList = tesiTask.getResult();
                adapter = new ElencoTesiAdapter(getContext(), tesiList);
                listView.setAdapter(adapter);
            } else {
                Log.e("Tesi Firestore Error", "Error getting Tesi data", tesiTask.getException());
            }
        });

        //carico i dati dei vincoli
        loadVincoloData().addOnCompleteListener(taskVincolo->{
            if(taskVincolo.isSuccessful()){
                vincoliList = taskVincolo.getResult();
            }else{
                Log.e("vincolo Firestore Error", "Error getting vincolo data", taskVincolo.getException());

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

    public void showFilterDialog(View rootView, ArrayList<Tesi> tesiList, List<Utente> utentiList) {
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
        EditText editTextTempistiche = view.findViewById(R.id.tempiticheTesi);

        /**
         * Gestione del bottone conferma
         */
        Button confermaButton = view.findViewById(R.id.avviaRicerca);
        confermaButton.setOnClickListener(v -> {
            //prendo i valori degli editext
            String cognome = editTextCognome.getText().toString();
            String tipologia = editTextTipologia.getText().toString();
            String ciclocdl = editTextCiclocdl.getText().toString();
            String mediaString = editTextMedia.getText().toString();
            String esamiString = editTextEsami.getText().toString();
            String tempisticheString = editTextTempistiche.getText().toString();

            //converto in long da string
            if(!mediaString.isEmpty() ){
                 media = Long.parseLong(mediaString);

            }
            if(!esamiString.isEmpty()){
                 esami = Long.parseLong(esamiString);

            }
            if(!tempisticheString.isEmpty()){
                 tempistiche = Long.parseLong(tempisticheString);

            }



            //filtro in base al cognome del relatore
           Long id_utente =  getIdUtenteByCognome(cognome);
           if(id_utente != null) {

               Long id_professore = getIdProfessorebyIdUtente(id_utente);
                //chiamo il metodo per caricare gli id  delle tesi in base all'id del professore
               loadIdTesiDataByProfessoreId(id_professore).addOnCompleteListener(task -> {
                   if (task.isSuccessful()) { //se il task è completato con successo
                       idTesiList = task.getResult(); //assegno gli id delle tesi ad una lista di tipo Long
                       loadTesiDataByCognomeRelatore(idTesiList);
                       filterDialog.dismiss();
                   } else {
                       Log.e("Firestore Error", "Error getting data", task.getException());
                   }
               });

           }
           //filtro in base alla tipologia e/o al ciclo di laurea
            filterTesiByTipologiaCdl(tipologia,ciclocdl);

           //filtro in base alla media - esami - tempistiche
            filterTesiByMediaEsTempistiche(media,esami,tempistiche);

           //chiamo questo metodo se non ci sono risultati
           listIsBlank(context);
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
     * Metodo per ottenere gli id degli utenti cercandoli in base al cognome
     * @return id delle tesi
     */
    private Long getIdUtenteByCognome(String cognome) {

        Long id_utente = null;
        for (Utente utente : utentiList) { //ciclo gli utenti

            if (utente.getCognome().equalsIgnoreCase(cognome)) { //se il cognome dell'utente corrisponde
                return id_utente = utente.getId_utente(); //ritorno il suo id

            }

        }
        return id_utente;
    }

    /**
     * Metodo per ottenre l'id del professore, qualora lo sia, in base all'id utente
     * @param id_utente
     * @return id del professore
     */
    private Long getIdProfessorebyIdUtente(Long id_utente){
        Long id_professore = null;

        for(Professore professore : professoriList){
            if(professore.getId_utente().equals(id_utente)){
                return id_professore = professore.getId_professore();
            }
        }

        return id_professore;
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

    private Task<ArrayList<Tesi>> loadTesiDataByCognomeRelatore(ArrayList<Long> idTesiList) {
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

                        // Itera attraverso gli idTesiList
                        ArrayList<Tesi> tesiByCognomeRelatore = new ArrayList<>();
                        for (Long idTesi : idTesiList) {
                            for (Tesi tesi : tesiList) {
                                if (idTesi.equals(tesi.getId_tesi())) {
                                    tesiByCognomeRelatore.add(tesi);
                                    break; // Esci dal ciclo interno dopo aver trovato una corrispondenza
                                }
                            }
                        }

                        // Aggiorna l'adapter con i nuovi dati
                        if (adapter != null) {
                            adapter.clear();
                            adapter.addAll(tesiByCognomeRelatore);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    return tesiList;
                });
    }

    /**
     * Questo metodo mi consente di filtrare le tesi in base alla tipologia e al cicloCdl
     * poiché questi due campi fanno parte della entity Tesi,posso lavorare con l'array tesiList
     * senza utilizzare firestore
     *
     * @param tipologia
     * @param cicloCdl
     */
    private void filterTesiByTipologiaCdl(String tipologia, String cicloCdl) {
        ArrayList<Tesi> filteredTesiList = new ArrayList<>();

        for (Tesi tesi : tesiList) {
            if (TextUtils.isEmpty(tipologia) && TextUtils.isEmpty(cicloCdl)) {
                // Se entrambi i campi di filtro sono vuoti, aggiungi tutte le tesi
                filteredTesiList.add(tesi);
            } else if (TextUtils.isEmpty(tipologia) && !TextUtils.isEmpty(cicloCdl)) {
                // Se il campo tipologia è vuoto ma c'è un filtro per il cicloCdl
                if (tesi.getCiclo_cdl().equals(cicloCdl)) {
                    filteredTesiList.add(tesi);
                }
            } else if (!TextUtils.isEmpty(tipologia) && TextUtils.isEmpty(cicloCdl)) {
                // Se il campo cicloCdl è vuoto ma c'è un filtro per la tipologia
                if (tesi.getTipologia().equals(tipologia)) {
                    filteredTesiList.add(tesi);
                }
            } else {
                // Se ci sono filtri per entrambi i campi
                if (tesi.getCiclo_cdl().equals(cicloCdl) && tesi.getTipologia().equals(tipologia)) {
                    filteredTesiList.add(tesi);
                }
            }
        }
        //chiudo il filtro dopo la ricerca
        if (filterDialog != null && filterDialog.isShowing()) {
            filterDialog.dismiss();
        }
        // aggiorno  l'adapter con la lista filtrata
        if (adapter != null) {
            adapter.clear();
            adapter.addAll(filteredTesiList);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Questo metodo serve per far comparire un messaggio quando non ci sono tesi dopo aver filtrato
     * @param context
     */
    private void listIsBlank(Context context){
        if(tesiList.isEmpty()){
            Toast toast = Toast.makeText(context, "Nessun risultato", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0); // Imposta il Toast in alto
            toast.show();
        }
    }

    /**
     * Questo metodo consente di ottenere i dati dei vincoli delle tesi da firestore e riempire la entity Vincolo
     *
     * @return entity Vincolo
     */
    private Task<ArrayList<Vincolo>> loadVincoloData() {
        final ArrayList<Vincolo> vincoliList = new ArrayList<>();

        // Create a Firestore query to fetch Tesi documents with matching IDs
        Query query = FirebaseFirestore.getInstance()
                .collection("Vincolo");

        return query.get().continueWith(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Vincolo vincolo = new Vincolo();
                    vincolo.setId_vincolo((Long) document.get("id_vincolo"));
                    vincolo.setEsami_mancanti_necessari((Long) document.get("esami_mancanti_necessari"));
                    vincolo.setSkill((String) document.get("skill"));
                    vincolo.setTempistiche((Long) document.get("tempistiche"));
                    vincolo.setMedia_voti((Long) document.get("media_voti"));

                    vincoliList.add(vincolo);
                }
            }
            return vincoliList;
        });
    }

    private void filterTesiByMediaEsTempistiche(Long media, Long numero_esami_mancanti, Long tempistiche) {
        ArrayList<Tesi> filteredTesiList = new ArrayList<>();

        for (Tesi tesi : tesiList) {
            Long id_vincolo = tesi.getId_vincolo();

            // Cerca il vincolo corrispondente nell'elenco dei vincoli
            Vincolo vincolo = findVincoloById(id_vincolo);
            Log.d("cogn9", String.valueOf(vincolo));
            Log.d("cogn10",String.valueOf(vincolo.getId_vincolo()));
            if (vincolo != null) {
                // Ora puoi applicare i filtri sui campi di vincolo
                if ((media == null || vincolo.getMedia_voti() <= media)
                        && (numero_esami_mancanti == null || vincolo.getEsami_mancanti_necessari() <= numero_esami_mancanti)
                        && (tempistiche == null || vincolo.getTempistiche() <= tempistiche)) {
                    filteredTesiList.add(tesi);
                }
            }
        }

        // Ora puoi aggiornare l'adapter con la lista filtrata
        if (adapter != null) {
            adapter.clear();
            adapter.addAll(filteredTesiList);
            adapter.notifyDataSetChanged();
        }
    }



    private Vincolo findVincoloById(Long id) {
        for (Vincolo vincolo : vincoliList) {
            if (vincolo.getId_vincolo().equals(id)) {
                return vincolo;
            }
        }
        return null; // Restituisci null se non viene trovato alcun vincolo corrispondente
    }


}
