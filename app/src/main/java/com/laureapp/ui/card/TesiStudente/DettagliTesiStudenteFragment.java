package com.laureapp.ui.card.TesiStudente;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.entity.Vincolo;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DettagliTesiStudenteFragment} factory method to
 * create an instance of this fragment.
 */
public class DettagliTesiStudenteFragment extends Fragment {
    Tesi tesi;
    String titolo;
    String descrizione;
    Date dataPubblicazione;
    String tipologia;
    String cicloCdl;
    Vincolo vincolo;

    Long id_vincolo;
    Long id_tesi;
    Long id_utente;
    Long id_studente;

    Long visualizzazioni;
    Context context;
    String email;

    Long media;
    Long esamiMancanti;


    StudenteModelView studenteView = new StudenteModelView(context);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dettagli_tesi_studente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Prendo gli argomenti passatomi dal layout precedente
        Bundle args = getArguments();
        TextView titoloTextView = view.findViewById(R.id.insertTextViewTitolo);
        TextView abstractTextView = view.findViewById(R.id.insertTextViewAbstract);
        TextView tipologiaTextView = view.findViewById(R.id.insertTextViewTipologia);
        TextView dataTextView = view.findViewById(R.id.insertTextViewDataPubblicazione);
        TextView ciclocdlTextView = view.findViewById(R.id.insertTextViewCicloCDL);

        TextView tempisticheTextView = view.findViewById(R.id.insertTextViewTempistiche);
        TextView mediaTextView = view.findViewById(R.id.insertTextViewMedia);
        TextView esamiTextView = view.findViewById(R.id.insertTextViewEsamiMancanti);
        TextView skillTextView = view.findViewById(R.id.insertTextViewSkill);


        if (args != null) { //se non sono null

            tesi = (Tesi) args.getSerializable("Tesi"); //prendo la tesi dagli args
            if (tesi != null) {
                //mi passo tutti i parametri di una Tesi
                titolo = tesi.getTitolo();
                descrizione = tesi.getAbstract_tesi();
                tipologia = tesi.getTipologia();
                dataPubblicazione = tesi.getData_pubblicazione();
                cicloCdl = tesi.getCiclo_cdl();
                id_vincolo = tesi.getId_vincolo();
                id_tesi = tesi.getId_tesi();
                visualizzazioni = tesi.getVisualizzazioni();

                incrementaVisualizzazioni(titolo); //incremento le visualizzazioni della tesi che sto visualizzando

                titoloTextView.setText(titolo);
                abstractTextView.setText(descrizione);
                tipologiaTextView.setText(tipologia);
                // formatto la data per convertirla da sql a java.date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = dateFormat.format(dataPubblicazione);
                dataTextView.setText(formattedDate);

                ciclocdlTextView.setText(cicloCdl);

                loadVincoloData(id_vincolo).addOnCompleteListener(taskVincolo -> {
                    if (taskVincolo.isSuccessful()) {
                        vincolo = taskVincolo.getResult();
                        media = vincolo.getMedia_voti();
                        esamiMancanti = vincolo.getEsami_mancanti_necessari();

                        tempisticheTextView.setText(vincolo.getTempistiche().toString());
                        esamiTextView.setText(esamiMancanti.toString());
                        mediaTextView.setText(media.toString());
                        skillTextView.setText(vincolo.getSkill());

                        //chiamo il metodo che verifica se lo studente rispetti i vincoli per richiedere la tesi
                        StudenteMatchesVincoli(view,media,esamiMancanti);

                    } else {
                        Log.e("vincolo Firestore Error", "Error getting vincolo data", taskVincolo.getException());

                    }
                });

                StudenteHasATesi(id_tesi,view);

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


        // Create a Firestore query to fetch Tesi documents with matching IDs
        Query query = FirebaseFirestore.getInstance()
                .collection("Vincolo")
                .whereEqualTo("id_vincolo", id_vincolo);
        return query.get().continueWith(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    vincolo.setId_vincolo((Long) document.get("id_vincolo"));
                    vincolo.setEsami_mancanti_necessari((Long) document.get("esami_mancanti_necessari"));
                    vincolo.setSkill((String) document.get("skill"));
                    vincolo.setTempistiche((Long) document.get("tempistiche"));
                    vincolo.setMedia_voti((Long) document.get("media_voti"));

                }
            }
            return vincolo;
        });
    }

    /**
     * Si utilizza questo metodo per prendere le preferenze salvate nel metodo presente in HomeFragment
     * Esso prende la cartella "preferenze" e ne ricava la mail o l'oggetto che ci serve.
     * @return
     */
    private String getEmailFromSharedPreferences(Context context) {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences("preferenze", Context.MODE_PRIVATE);
            return preferences.getString("email", null);
        }
        return null;
    }

    /**
     * Metodo utilizzato per nascondere il pulsante Richiedi Tesi qualora abbia già la tesi che si sta visualizzando
     * @param id_tesi
     * @param view
     */
    private void StudenteHasATesi(Long id_tesi, View view) {
        context = getContext();
        email = getEmailFromSharedPreferences(context); // Chiamata al metodo per ottenere la mail

        UtenteModelView utenteView = new UtenteModelView(context);
        id_utente = utenteView.getIdUtente(email);
        id_studente = studenteView.findStudente(id_utente);

        if (email != null && id_utente != null && id_studente != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference studenteTesiRef = db.collection("StudenteTesi");

            // Crea una clausola composta con and tra le condizioni
            Query query = studenteTesiRef
                    .whereEqualTo("id_studente", id_studente)
                    .whereEqualTo("id_tesi", id_tesi);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // Ci sono occorrenze
                        Button filterButton = view.findViewById(R.id.richiediTesi);
                        filterButton.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Log.e("Firestore Error", "Error querying StudenteTesi collection", task.getException());
                }
            });
        }else{ //se i tre valori sono null e quindi sono ospite nascondo il bottone
            Button filterButton = view.findViewById(R.id.richiediTesi);
            filterButton.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Metodo utilizzato per verificare se lo studente rispetti i vincoli in modo tale da poter richiedere la tesi
     * @param view
     * @param media
     * @param esamiMancanti
     */
    private void StudenteMatchesVincoli(View view,Long media,Long esamiMancanti) {
        List<Studente> studenti;
        context = getContext();
        email = getEmailFromSharedPreferences(context); // Chiamata al metodo per ottenere la mail

        UtenteModelView utenteView = new UtenteModelView(context);
        id_utente = utenteView.getIdUtente(email);
        id_studente = studenteView.findStudente(id_utente);
        studenti = studenteView.getAllStudente();



        for (Studente studente : studenti) {
            if (studente.getId_studente() == id_studente) {

                Long mediaStudente = (long) studente.getMedia();
                Long esamiStudente = (long) studente.getEsami_mancanti();
                if(mediaStudente < media && esamiStudente > esamiMancanti ){
                    Button richiediTesiButton = view.findViewById(R.id.richiediTesi);
                    richiediTesiButton.setOnClickListener(view1 -> {
                        Toast toast = Toast.makeText(context, "Impossibile richiede la tesi. Non soddisfi tutti i vincoli", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0); // Imposta il Toast in alto
                        toast.show();

                    });
                }
            }
        }
    }

    /**
     * Metodo utilizzato per incrementare le visualizzazioni di una tesi. Quando si visualizza nel dettaglio una tesi il valore viene incrementato di uno al fine di
     * stilare una classifica.
     * @param titoloTesi
     */
    private void incrementaVisualizzazioni(String titoloTesi) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tesiRef = db.collection("Tesi");

        Query query = tesiRef.whereEqualTo("titolo", titoloTesi);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Ottieni il documento corrispondente (potrebbe esserci più di uno, ma ne usiamo il primo)
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                    // Estrai l'ID del documento
                    String documentId = documentSnapshot.getId();

                    // Incrementa le visualizzazioni utilizzando l'ID del documento
                    tesiRef
                            .document(documentId)
                            .update("visualizzazioni", visualizzazioni + 1)
                            .addOnSuccessListener(aVoid -> {
                                // L'incremento è riuscito, puoi fare qualcosa in caso di successo
                                visualizzazioni++; // Aggiorna anche il valore locale
                                // Puoi anche aggiornare la visualizzazione nell'UI, se necessario
                            })
                            .addOnFailureListener(e -> {
                                // Si è verificato un errore durante l'incremento
                                Log.e("Firestore Error", "Error incrementing visualizzazioni", e);
                            });
                }
            } else {
                Log.e("Firestore Error", "Error querying Tesi collection", task.getException());
            }
        });
    }


}