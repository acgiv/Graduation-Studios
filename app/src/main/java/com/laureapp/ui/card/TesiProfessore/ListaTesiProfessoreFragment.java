package com.laureapp.ui.card.TesiProfessore;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laureapp.R;
import com.laureapp.ui.card.Adapter.ListaTesiProfessoreAdapter;
import com.laureapp.ui.card.TesiStudente.ConfermaRichiestaDialog;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.entity.Vincolo;
import com.laureapp.ui.roomdb.viewModel.ProfessoreModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ListaTesiProfessoreFragment extends Fragment {

    Context context;
    String email;
    Long id_utente;
    Long id_professore;
    private NavController mNav;
    private ListView listView;
    private ListaTesiProfessoreAdapter adapter;
    private static ListaTesiProfessoreAdapter adapterDue;
    ProfessoreModelView professoreView = new ProfessoreModelView(context);
    ArrayList<Long> idTesiList = new ArrayList<>();

    //Dichiarazioni di una variabile di istanza per il dialog
    private AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_tesi_professore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();
        email = getEmailFromSharedPreferences();
        ImageButton addButton = view.findViewById(R.id.addTesiProfessore);

        if (email != null) { // se la mail non è nulla

            UtenteModelView utenteView =  new UtenteModelView(context); //inizializza utenteView con un'istanza di UtenteModelView
            id_utente = utenteView.getIdUtente(email); //ottengo l'id dell'utente corrispondente a tale mail
            id_professore = professoreView.findProfessore(id_utente); //ottengo l'id del professore corrispondente all'id dell'utente

            //Carico l'elenco degli id delle tesi collegate con il professore
            loadIdTesiDataByProfessoreId(id_professore).addOnCompleteListener(task -> {
                if(task.isSuccessful()) { //se il task è completato con successo
                    idTesiList = task.getResult(); //assegno gli id delle tesi ad una lista di tipo Long
                    Log.d("Id Tesi", "Id Tesi " + idTesiList.toString());

                    //Verifica se idTesiList è null o vuoto
                    if (idTesiList != null && !idTesiList.isEmpty()) {
                        loadTesiData(idTesiList).addOnCompleteListener(tesiTask -> { //Chiamo il metodo per ottenere le tesi in base alle id tesi ottenute
                            if(tesiTask.isSuccessful()) {
                                ArrayList<Tesi> tesiList = tesiTask.getResult();
                                Log.d("Tesi", "Id Tesi " + tesiList.toString());

                                //Mostro sulla listview tutte le tesi dello studente associato al professore
                                listView = view.findViewById(R.id.listTesiProfessoreView);
                                adapter = new ListaTesiProfessoreAdapter(getContext(), tesiList);
                                listView.setAdapter(adapter);
                            } else {
                                Log.e("Tesi Firestore Error", "Error getting Tesi data", tesiTask.getException());
                            }
                        });

                    }


                    } else {
                    Log.e("Firestore Error","Error getting data", task.getException());
                    }
                });

            } else {
            Log.d("Email salvata: ", "Non trovata");
            }

            addButton.setOnClickListener(view1 -> {
                view.setAlpha(0.1f); // Imposta l'opacità desiderata (0.0-1.0)
                showTesiDialog(view); //quando viene cliccato il pulsante filtra si apre il dialog
            });

        }

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
     * Questo metodo mi consente di caricare da firebase tutte le informazioni relative alle tesi legate allo studente
     * @param idTesiList
     * @return una lista di tipo Tesi contenente le informazioni delle tesi
     */

    private Task<ArrayList<Tesi>> loadTesiData(ArrayList<Long> idTesiList) {
        final ArrayList<Tesi> tesiList = new ArrayList<>();

        // Create a Firestore query to fetch Tesi documents with matching IDs
        Query query = FirebaseFirestore.getInstance()
                .collection("Tesi")
                .whereIn("id_tesi", idTesiList);

        return query.get().continueWith(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Tesi tesi = new Tesi();
                    tesi.setId_tesi((Long) document.get("id_tesi"));
                    tesi.setId_vincolo((Long) document.get("id_vincolo"));

                    // Converto da Firebase timestamp a SQL timestamp
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

                // Aggiorna l'adapter con i nuovi dati
                if (adapter != null) {
                    adapter.clear();
                    adapter.addAll(tesiList);
                    adapter.notifyDataSetChanged();
                }
            }
            return tesiList;
        });
    }

    /**
     * Metodo per mostrare il pop-up in un'attività o fragment
     */

    public void showTesiDialog(View rootView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        // Include the custom XML layout
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.add_tesi_professore_popup, null);
        builder.setView(view);

        // EditText
        EditText editTextTitolo = view.findViewById(R.id.editTextTitoloTesiProfessore);
        EditText editTextTipologia = view.findViewById(R.id.editTextTipologiaTesiProfessore);

        //Gestisco il campo data di pubblicazione
        EditText editTextDataPubblicazione = view.findViewById(R.id.editTextDataPubblicazioneTesiProfessore);
        editTextDataPubblicazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(editTextDataPubblicazione);
            }
        });

        EditText editTextCicloCdl = view.findViewById(R.id.editTextCicloCdlTesiProfessore);
        EditText editTextAbstract = view.findViewById(R.id.editTextAbstractTesiProfessore);

        // Button
        Button avantiButton = view.findViewById(R.id.buttonAvantiTesiProfessore);
        Button annullaButton = view.findViewById(R.id.buttomAnnullaTesiProfessore);

        // Create the AlertDialog
        alertDialog = builder.create();

        // Show the dialog
        alertDialog.show();

        // Set a listener for the "Avanti" button
        avantiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //quando l'utente clicca avanti
                //definisco un oggetto di tipo tesi che conterrà i vari campi
                Tesi tesiNew = new Tesi();
                String titolo = editTextTitolo.getText().toString();
                String tipologia = editTextTipologia.getText().toString();
                String ciclocdl = editTextCicloCdl.getText().toString();
                String descrizione = editTextAbstract.getText().toString();
                String dataPubblicazione = editTextDataPubblicazione.getText().toString();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date dataConvertita = null;
                try {
                    dataConvertita = dateFormat.parse(dataPubblicazione);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                tesiNew.setTitolo(titolo);
                tesiNew.setAbstract_tesi(descrizione);
                tesiNew.setData_pubblicazione(dataConvertita);
                tesiNew.setTipologia(tipologia);
                tesiNew.setCiclo_cdl(ciclocdl);
                tesiNew.setVisualizzazioni(0L);


                alertDialog.dismiss();
                showVincoliDialog(view,tesiNew);
            }
        });

        annullaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Avanti" button click here

                // Close the dialog when you're done
                alertDialog.dismiss();
                rootView.setAlpha(1); //reimpose l'opacità di default

            }
        });

        /**
         * Gestione del metodo on dismiss quando si clicca fuori o si clicca annulla
         */
        alertDialog.setOnDismissListener(dialog -> {
            rootView.setAlpha(1); //reimpose l'opacità di default

        });

    }

    public void showVincoliDialog(View rootView,Tesi tesiNew) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        // Include the custom XML layout
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.add_vincolo_tesi_popup, null);
        builder.setView(view);

        // EditText
        EditText editTextMedia = view.findViewById(R.id.editTextMediaVincoloTesi);
        EditText editTextTempistiche = view.findViewById(R.id.editTextTempisticheVincoloTesi);
        EditText editTextEsamiMancanti= view.findViewById(R.id.editTextEsamiVincoloTesi);
        EditText editTextSkill = view.findViewById(R.id.editTextSkillVincoloTesi);

        // Button
        Button creaButton = view.findViewById(R.id.buttonConfermaVincoloTesi);
        Button annullaButton = view.findViewById(R.id.buttomAnnullaTesiProfessore);

        // Create the AlertDialog
        alertDialog = builder.create();

        // Show the dialog
        alertDialog.show();

        // Set a listener for the "Avanti" button
        creaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //quando l'utente clicca avanti
                //definisco un oggetto di tipo tesi che conterrà i vari campi
                Vincolo vincoloNew = new Vincolo();
                String media = editTextMedia.getText().toString();
                String tempistiche = editTextTempistiche.getText().toString();
                String esami = editTextEsamiMancanti.getText().toString();
                String skill = editTextSkill.getText().toString();

                vincoloNew.setMedia_voti(Long.valueOf(media));
                vincoloNew.setTempistiche(Long.valueOf(tempistiche));
                vincoloNew.setEsami_mancanti_necessari(Long.valueOf(esami));
                vincoloNew.setSkill(skill);

                createVincoloTesi(vincoloNew,tesiNew);
                alertDialog.dismiss();
            }
        });

        annullaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Avanti" button click here

                // Close the dialog when you're done
                alertDialog.dismiss();
                rootView.setAlpha(1); //reimpose l'opacità di default

            }
        });

        /**
         * Gestione del metodo on dismiss quando si clicca fuori o si clicca annulla
         */
        alertDialog.setOnDismissListener(dialog -> {
            rootView.setAlpha(1); //reimpose l'opacità di default

        });

    }

    /**
     * Metodo per mostrare il calendario quando l'utente clicca il campo data di pubblicazione
     * @param view
     */
    public void showDatePicker(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Called when the user selects a date from the DatePickerDialog
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                EditText editTextDataPubblicazione = alertDialog.findViewById(R.id.editTextDataPubblicazioneTesiProfessore);
                editTextDataPubblicazione.setText(selectedDate);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    private void createVincoloTesi(Vincolo vincoloNew,Tesi tesiNew){

        // Crea un oggetto per la nuova richiesta di tesi
        Map<String, Object> vincoloTesi = new HashMap<>();
        vincoloTesi.put("esami_mancanti_necessari", vincoloNew.getEsami_mancanti_necessari());
        vincoloTesi.put("media_voti",vincoloNew.getMedia_voti());
        vincoloTesi.put("skill",vincoloNew.getSkill());
        vincoloTesi.put("tempistiche",vincoloNew.getTempistiche());

        // Trova il massimo ID attuale e incrementalo di 1
        findMaxVincoloId(new ListaTesiProfessoreFragment.MaxRequestIdCallback() {
            @Override
            public void onCallback(Long maxRequestId) {
                vincoloTesi.put("id_vincolo", maxRequestId + 1);

                // Aggiungi la nuova richiesta di tesi a Firestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference richiesteTesiRef = db.collection("Vincolo");

                richiesteTesiRef.add(vincoloTesi)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                //se viene creato il vincolo
                                createTesi(tesiNew, (Long) vincoloTesi.get("id_vincolo")); //creo la tesi su firestore

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Si è verificato un errore nell'aggiunta della richiesta di tesi
                            }
                        });
            }
        });
    }

    private interface MaxRequestIdCallback {
        void onCallback(Long maxRequestId);
    }

    private void findMaxVincoloId(ListaTesiProfessoreFragment.MaxRequestIdCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference vincoloRef = db.collection("Vincolo");

        vincoloRef
                .orderBy("id_vincolo", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        Long maxRequestId = queryDocumentSnapshots.getDocuments().get(0).getLong("id_vincolo");
                        if (maxRequestId != null) {
                            callback.onCallback(maxRequestId);
                        } else {
                            // Nessun ID trovato, inizia da 1
                            callback.onCallback(1L);
                        }
                    } else {
                        // Nessun documento trovato, inizia da 1
                        callback.onCallback(1L);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore Error", "Error finding max request ID", e);
                });
    }

    private void createTesi(Tesi tesiNew,Long idVincolo){

        Long visualizzazioni = 0L;

        // Crea un oggetto per la nuova richiesta di tesi
        Map<String, Object> tesiMap = new HashMap<>();
        tesiMap.put("titolo", tesiNew.getTitolo());
        tesiMap.put("abstract_tesi",tesiNew.getAbstract_tesi());
        tesiMap.put("ciclo_cdl",tesiNew.getCiclo_cdl());
        tesiMap.put("data_pubblicazione",tesiNew.getData_pubblicazione());
        tesiMap.put("id_vincolo",idVincolo);
        tesiMap.put("visualizzazioni",visualizzazioni);

        // Trova il massimo ID attuale e incrementalo di 1
        findMaxTesiId(new ListaTesiProfessoreFragment.MaxRequestIdCallback() {
            @Override
            public void onCallback(Long maxRequestId) {
                tesiMap.put("id_tesi", maxRequestId + 1);

                // Aggiungi la nuova richiesta di tesi a Firestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference richiesteTesiRef = db.collection("Tesi");

                richiesteTesiRef.add(tesiMap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                //se viene creato la tesi


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Si è verificato un errore nell'aggiunta della richiesta di tesi
                            }
                        });
            }
        });
    }

    private void findMaxTesiId(ListaTesiProfessoreFragment.MaxRequestIdCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference richiesteTesiRef = db.collection("Tesi");

        richiesteTesiRef
                .orderBy("id_tesi", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        Long maxRequestId = queryDocumentSnapshots.getDocuments().get(0).getLong("id_tesi");
                        if (maxRequestId != null) {
                            callback.onCallback(maxRequestId);
                        } else {
                            // Nessun ID trovato, inizia da 1
                            callback.onCallback(1L);
                        }
                    } else {
                        // Nessun documento trovato, inizia da 1
                        callback.onCallback(1L);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore Error", "Error finding max request ID", e);
                });
    }





}