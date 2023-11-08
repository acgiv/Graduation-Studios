package com.laureapp.ui.card.TesiStudente;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.laureapp.R;
import com.laureapp.ui.card.Adapter.InfoTesiProfessoreAdapter;
import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.entity.TesiProfessore;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.entity.Vincolo;
import com.laureapp.ui.roomdb.viewModel.ProfessoreModelView;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    Long id_professore;
    Long visualizzazioni;
    Context context;
    String email;
    String nomeCognome;

    String nomeRelatore = "";
    String nomeCorelatore = "";
    Long media;
    Long esamiMancanti;
    ImageButton share;
    ArrayList<String> nomiFile = new ArrayList<>(); // FileInfo rappresenta le informazioni sui file da visualizzare

    List<TesiProfessore> tesiProfessoreList = new ArrayList<>();

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

        TextView relatoreTextView = view.findViewById(R.id.insertTextViewRelatore);
        TextView corelatoreTextView = view.findViewById(R.id.insertTextViewCoRelatore);


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


                share = view.findViewById(R.id.shareImageButton);


                share.setOnClickListener(view1 -> {
                    // Genera il QR code
                    Bitmap qrCodeBitmap = QRGenerator(tesi);




                    // Avvia un'Activity per condividere il QR code
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/*");
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] qrCodeBytes = byteArrayOutputStream.toByteArray();
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), qrCodeBitmap, "QR Code", null)));
                    startActivity(Intent.createChooser(shareIntent, "Condividi QR Code tramite"));
                });

                //carico i dati dei vincoli
                loadVincoloData(id_vincolo).addOnCompleteListener(taskVincolo -> {
                    if (taskVincolo.isSuccessful()) {
                        vincolo = taskVincolo.getResult();
                        media = vincolo.getMedia_voti();
                        esamiMancanti = vincolo.getEsami_mancanti_necessari();

                        tempisticheTextView.setText(vincolo.getTempistiche().toString());
                        esamiTextView.setText(esamiMancanti.toString());
                        mediaTextView.setText(media.toString());
                        skillTextView.setText(vincolo.getSkill());

                        Button richiediTesiButton = view.findViewById(R.id.richiediTesi);
                        richiediTesiButton.setOnClickListener(view1 -> {

                            //chiamo il metodo che verifica se lo studente rispetti i vincoli per richiedere la tesi

                            if (StudenteMatchesVincoli( media, esamiMancanti)) {
                                boolean soddisfaRequisiti = true;
                                Log.d("vedi2", String.valueOf(soddisfaRequisiti));

                                showConfirmationDialog("Vuoi richiedere questa tesi?", id_tesi, id_studente, soddisfaRequisiti);
                            } else {
                                boolean soddisfaRequisiti = false;

                                showConfirmationDialog("Attenzione: Non soddisfi tutti i requisiti per richiedere questa tesi. Vuoi richiederla comunque?", id_tesi, id_studente, soddisfaRequisiti);
                            }

                        });




                    } else {
                        Log.e("vincolo Firestore Error", "Error getting vincolo data", taskVincolo.getException());

                    }
                });
                //chiamata al metodo per controllare se lo studente ha già una tesi
                StudenteHasATesi(id_tesi,view);

                loadTesiProfessoreData(id_tesi).addOnCompleteListener(taskTesiProfessore -> {
                    if(taskTesiProfessore.isSuccessful()){
                        tesiProfessoreList = taskTesiProfessore.getResult();
                        getRoleTesiProfessoreList(tesiProfessoreList,context,relatoreTextView,corelatoreTextView);


                    }


                });


            }
        }
    }

    /**
     * Questo metodo consente di ottenere i dati dei vincoli delle tesi da firestore e riempire la entity Vincolo
     *
     * @param id_tesi id tesi da cercare legata alla tesi
     * @return entity Vincolo
     */
    private Task<List<TesiProfessore>> loadTesiProfessoreData(Long id_tesi) {
        // Create a Firestore query to fetch Tesi documents with matching IDs
        Query query = FirebaseFirestore.getInstance()
                .collection("TesiProfessore")
                .whereEqualTo("id_tesi", id_tesi);

        return query.get().continueWith(task -> {
            List<TesiProfessore> tesiProfessoreList = new ArrayList<>();

            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    TesiProfessore tesiProfessore = new TesiProfessore();
                    tesiProfessore.setId_professore((Long) document.get("id_professore"));
                    tesiProfessore.setId_tesi((Long) document.get("id_tesi"));
                    tesiProfessore.setRuolo_professore((String) document.get("ruolo_professore"));
                    tesiProfessoreList.add(tesiProfessore);
                }
            }

            return tesiProfessoreList;
        });
    }

    /**
     * Metodo utilizzato per iterare la lista dei professori e capire se sono relatori e/o corelatori
     *
     */
    private void getRoleTesiProfessoreList(List<TesiProfessore> tesiProfessoreList, Context context, TextView relatoreTextView, TextView corelatoreTextView) {


        for (TesiProfessore tesiProfessore : tesiProfessoreList) {
            Long idProfessore = tesiProfessore.getId_professore();
            String ruoloProfessore = tesiProfessore.getRuolo_professore();

            if (ruoloProfessore.equals("Relatore")) {
                getIdUtenteByIdProfessore(context, idProfessore, true);
            } else if (ruoloProfessore.equals("Co-Relatore")) {
                getIdUtenteByIdProfessore(context, idProfessore, false);
            }
        }

        if (!nomeRelatore.isEmpty()) {
            relatoreTextView.setText(nomeRelatore);
        } else {
            relatoreTextView.setText(""); // Lascia vuoto se non c'è un relatore
        }

        if (!nomeCorelatore.isEmpty()) {
            corelatoreTextView.setText(nomeCorelatore);
        } else {
            corelatoreTextView.setText(""); // Lascia vuoto se non c'è un co-relatore
        }
    }


    /**
     * Metodo utilizzato per prendere il nome e il cognome del professore in base al suo id
     */
    private void getIdUtenteByIdProfessore(Context context, Long idProfessore, boolean isRelatore) {
        ProfessoreModelView professoreModelView = new ProfessoreModelView(context);
        List<Professore> professoreList = professoreModelView.getAllProfessore();
        Long idUtente = null;

        for (Professore professore : professoreList) {
            if (professore.getId_professore().equals(idProfessore)) {
                idUtente = professore.getId_utente();
                break;
            }
        }

        if (idUtente != null) {
            getNomeCognomeProfessoreById(context, idUtente, isRelatore);
        }
    }

    /**
     * Metodo utilizzato per prendere il nome e il cognome del professore in base all'id utente
     */
    private void getNomeCognomeProfessoreById(Context context, Long idUtente, boolean isRelatore) {
        UtenteModelView utenteModelView = new UtenteModelView(context);
        List<Utente> utenteList = utenteModelView.getAllUtente();

        String nomeCognome = null;

        for (Utente utente : utenteList) {
            if (utente.getId_utente().equals(idUtente)) {
                String nome = utente.getNome();
                String cognome = utente.getCognome();
                nomeCognome = nome + " " + cognome;
                break;
            }
        }

        if (isRelatore) {
            nomeRelatore = nomeCognome;
        } else {
            nomeCorelatore = nomeCognome;
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
                        loadFileFromFireStore(view);
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
     * @param media
     * @param esamiMancanti
     */
    private boolean  StudenteMatchesVincoli(Long media,Long esamiMancanti) {
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
                if(mediaStudente > media && esamiStudente < esamiMancanti ){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Metodo utilizzato per mostrare il dialog di conferma richiesta tesi
     * @param message
     */
    private void showConfirmationDialog(String message, Long idTesi, Long idStudente, boolean soddisfaRequisiti) {
        ConfermaRichiestaDialog dialog = new ConfermaRichiestaDialog(message, idTesi, idStudente, soddisfaRequisiti);
        dialog.show(getFragmentManager(), "ConfermaRichiestaDialog");
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



    /**
     * Metodo di generazione del QRCode in base all'id della tesi
     * @return  restituisce la variabile Bitmap per mostrare il QRCode
     */
    public Bitmap QRGenerator(Tesi tesi) {
        MultiFormatWriter writer = new MultiFormatWriter();
        Bitmap bitmap = null;
        try {
            // Creazione del contenuto del QR Code utilizzando i dati della tesi
            String content = "Titolo: " + tesi.getTitolo() +
                    "\nDescrizione: " + tesi.getAbstract_tesi() +
                    "\nTipologia: " + tesi.getTipologia() +
                    "\nData Pubblicazione: " + tesi.getData_pubblicazione() +
                    "\nCiclo CDL: " + tesi.getCiclo_cdl() +
                    // Aggiungi altri dati della tesi, se necessario.
                    "\nID Tesi: " + tesi.getId_tesi() +
                    "\nRelatore: " + nomeCognome;


            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 400, 400);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * Metodo utilizzato per mostrare la lista del materiale della tesi
     */

    private void loadFileFromFireStore(View view) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("FileTesi/" + id_tesi);

        storageRef.listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        nomiFile.add(item.getName());
                    }

                    InfoTesiProfessoreAdapter adapter = new InfoTesiProfessoreAdapter(getContext(), nomiFile);
                    ListView listViewFiles = view.findViewById(R.id.listViewFiles);
                    listViewFiles.setAdapter(adapter);


                    adapter.setDeleteButtonClickListener(new InfoTesiProfessoreAdapter.DeleteButtonClickListener() {
                        @Override
                        public void onDeleteButtonClick(int position) {
                            Toast.makeText(getContext(), "Non puoi eliminare questo file", Toast.LENGTH_SHORT).show();

                        }
                    });

                    adapter.setDownloadButtonClickListener(new InfoTesiProfessoreAdapter.DownloadButtonClickListener() {
                        @Override
                        public void onDownloadButtonClick(int position) {
                            // Ottieni il riferimento al file selezionato
                            StorageReference selectedFileRef = storageRef.child(nomiFile.get(position));

                            // Crea un file locale dove scaricare il file
                            File localFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), nomiFile.get(position));

                            selectedFileRef.getFile(localFile)
                                    .addOnSuccessListener(taskSnapshot -> {
                                        // Il file è stato scaricato con successo, puoi gestire il completamento qui
                                        // Ad esempio, puoi aprire il file o mostrare una notifica di download completato
                                        Toast.makeText(getContext(), "File scaricato con successo", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(exception -> {
                                        // Gestisci eventuali errori durante il download
                                        Log.e("Firebase Storage Error", "Errore nel download del file", exception);
                                        Toast.makeText(getContext(), "Errore nel download del file", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    });
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(getContext(), "Caricamento file non riuscito", Toast.LENGTH_SHORT).show();
                    Log.e("Firebase Storage Error", "Errore nel caricamento dell'elenco dei file", exception);
                });



    }


}