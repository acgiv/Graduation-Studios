package com.laureapp.ui.card.Segnalazioni;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;

import com.laureapp.R;
import com.laureapp.ui.card.Adapter.SegnalazioniAdapter;
import com.laureapp.ui.roomdb.QueryFirestore;
import com.laureapp.ui.roomdb.entity.Segnalazione;
import com.laureapp.ui.roomdb.repository.SegnalazioneRepository;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.StudenteTesiModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

public class SegnalazioniFragment extends Fragment {

    Bundle args;
    Context context;
    String ruolo;
    String email;
    Long id_utente;
    Long id_studente;
    Long idTesiDesiderata;
    ArrayList<Long> idTesiList = new ArrayList<>();
    private AlertDialog alertDialog;
    private static List<Segnalazione> segnalazioniList;
    private static SegnalazioniAdapter adapter;
    private static final String TAG = "FragmentSegnalazione"; // Definizione della variabile TAG

    //FragmentSegnBuilding binding;

    UtenteModelView utenteView = new UtenteModelView(context); // Inizializza utenteView con un'istanza di UtenteModelView
    StudenteModelView studenteView = new StudenteModelView(context);
    StudenteTesiModelView studenteTesiView = new StudenteTesiModelView(context);

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Riferimento alla collezione
    private CollectionReference segnalazioniRef = db.collection("Segnalazioni");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        context = requireContext();
        args= getArguments();
        if (args != null) {
            ruolo = args.getString("ruolo");
            Log.d("Segn ruolo ", ruolo);
        }

        View rootView = inflater.inflate(R.layout.fragment_segnalazioni_studente, container, false);;

        email = getEmailFromSharedPreferences(context);
        Log.d("segnM", email);
        if(email != null) {
            id_utente = utenteView.getIdUtente(email); //ottengo l'id dell'utente corrispondente a tale mail
            Log.d("idUtente", String.valueOf(id_utente));
            id_studente = studenteView.findStudente(id_utente); //ottengo l'id dello studente corrispondente all'id dell'utente
            Log.d("idStudente", String.valueOf(id_studente));
            //id_stedente_tesi = studenteTesiView.findIdTesiByIdTesi(id_studente);


            //Ricerca Tesi
            db.collection("Segnalazioni")
                    .whereEqualTo("id_studente", id_studente)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Ottieni il valore id_tesi dal documento
                                String idTesi = document.getString("id_tesi");
                                // Ottieni l'ID della tesi desiderata
                                idTesiDesiderata = Long.parseLong(idTesi);
                                Log.d("tesiStudente", String.valueOf(idTesiDesiderata));
                            }
                        }else {
                            Log.d("Firestore", "Errore durante la ricerca: ", task.getException());
                        }
                    });

        }

        //Recupera i dati dalla collezione "Segnalazioni"
        segnalazioniRef.get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()) {
                       for(QueryDocumentSnapshot document : task.getResult()){
                           Segnalazione segnalazione = document.toObject(Segnalazione.class);
                           segnalazioniList.add(segnalazione);
                       }
                   }else {
                       Log.d(TAG, "Error getting documents: " + task.getException().getMessage());
                   }
                });

        // Ottieni il riferimento alla ListView
        ListView listView = rootView.findViewById(R.id.segn_list_view);

        // Crea un'istanza del repository delle segnalazioni
        SegnalazioneRepository segnalazioneRepository = new SegnalazioneRepository(requireContext());

        // Ottieni le segnalazioni per la tesi desiderata dal repository
        segnalazioniList = segnalazioneRepository.findSegnalazioniByTesiId(idTesiDesiderata);


        // Crea l'adapter personalizzato e imposta sulla ListView
        adapter = new SegnalazioniAdapter(requireContext(), segnalazioniList);
        // Collega l'adapter alla ListView
        listView.setAdapter(adapter);

        // Imposta un listener per gli elementi della ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Ottieni il NavController dalla tua Activity principale
                NavController navController = NavHostFragment.findNavController(SegnalazioniFragment.this);

                // Esegui la navigazione verso DiscussioneFragment
                navController.navigate(R.id.action_segnalazioniFragment_to_discussioneFragment);
            }
        });

        //Bottone nuova segnalazione
        ImageButton btnNS = rootView.findViewById(R.id.add_segn_ImageButton);
        btnNS.setOnClickListener(view1 ->
                showInputDialog()
        );

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){

    }

    public static String getEmailFromSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("preferenze", Context.MODE_PRIVATE);
        return preferences.getString("email", null);
    }

    public void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Nuova Segnalazione");

        View view = LayoutInflater.from(requireContext()).inflate(R.layout.add_segn_popup, null);
        builder.setView(view);

        EditText editTextTitolo = view.findViewById(R.id.titolo_register);
        EditText editTextRichiesta = view.findViewById(R.id.richiesta_register);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String inputTitolo = editTextTitolo.getText().toString();
                String inputRichiesta = editTextRichiesta.getText().toString();

                //Controllo tesi associata
                Log.d("tesiStudente", String.valueOf(idTesiDesiderata));
                if(idTesiDesiderata != null){
                    addSegnToFirestore(inputTitolo,inputRichiesta,idTesiDesiderata);
                }else {
                    showErrorPopup();
                }

                //Gestione dati
                if (alertDialog != null) {
                    alertDialog.dismiss(); // Chiude l'AlertDialog
                }
            }
        });


        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });

        // Crea e Mostra il dialog

        alertDialog = builder.create();
        alertDialog.show();


    }

    private void addSegnToFirestore (String titolo, String richiesta, Long id_tesi) {

        CollectionReference segnRef = db.collection("Segnalazioni");
        QueryFirestore queryFirestore = new QueryFirestore();

        // Creazione di un nuovo oggetto mappa con i dati da inserire
        Map<String, Object> segnMap = new HashMap<>();

        queryFirestore.trovaIdSegnMax(context)
                        .thenAccept(idMax -> {
                            idMax = idMax + 1L;
                            segnMap.put("id_segn", idMax);
                            segnMap.put("id_tesi", id_tesi);
                            segnMap.put("Titolo", titolo);
                            segnMap.put("Richiesta", richiesta);

                            // Supponendo che 'taskRef' sia un oggetto valido di tipo CollectionReference
                            Long finalIdMax = idMax;
                            segnRef.document().set(segnMap).addOnSuccessListener(
                                    aVoid -> addSegnToList(finalIdMax, titolo, richiesta, id_tesi)
                            ).addOnFailureListener(e -> {
                                // Gestisci l'errore
                            });
                        });
/*
        // Aggiungi i dati nella collezione "Segnalazioni"
        db.collection("Segnalazioni")
                .add(segnMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("FBSegn", "Dati aggiunti con successo");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("FBSegn", "Errore durante l'aggiunta dei dati: "+ e);
                    }
                });

 */

    }

    /**
     * Metodo per l'aggiunta di una nuova segnalazione su SQLite
     * @param id_segn id della segn da aggiungere alla lista
     * @param titolo è il titolo della segnalazione
     * @param richiesta della segnalazione
     * @param id_tesi tesi associata alla segnalazione
     */

    private void addSegnToList (Long id_segn, String titolo, String richiesta, Long id_tesi) {

        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setIdSegnalazione(id_segn);
        segnalazione.setIdTesi(id_tesi);
        segnalazione.setTitolo(titolo);
        segnalazione.setRichiesta(richiesta);

        // Aggiungi Segnalazione alla lista
        segnalazioniList.add(segnalazione);
        adapter.notifyDataSetChanged();;


    }

    private void showErrorPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Errore");
        builder.setMessage("Si è verificato un errore." +
                "Nessuna Tesi trovata");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }




}