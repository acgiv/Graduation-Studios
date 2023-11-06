package com.laureapp.ui.card.TesiStudente;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
    Bitmap qrCodeBitmap;
    ImageButton share;
    ArrayList<String> nomiFile = new ArrayList<>(); // FileInfo rappresenta le informazioni sui file da visualizzare

    List<TesiProfessore> tesiProfessoreList = new ArrayList<>();

    StudenteModelView studenteView = new StudenteModelView(context);
    private static final int PERMISSION_REQUEST_CODE = 123;
    private static final int DOWNLOAD_REQUEST_CODE = 456;
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

                incrementaVisualizzazioni(id_tesi,visualizzazioni); //incremento le visualizzazioni della tesi che sto visualizzando

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
                    qrCodeBitmap = QRGenerator(tesi);




                    // Avvia un'Activity per condividere il QR code
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/*");
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] qrCodeBytes = byteArrayOutputStream.toByteArray();
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), qrCodeBitmap, "qr_code_bitmap", null)));
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
     * Ottiene il ruolo (Relatore o Co-Relatore) di ciascun professore associato a una lista di TesiProfessore
     * e imposta i nomi dei professori nei TextView specificati.
     *
     * @param tesiProfessoreList la lista di oggetti TesiProfessore da cui ottenere le informazioni.
     * @param context
     * @param relatoreTextView il TextView in cui impostare il nome del Relatore.
     * @param corelatoreTextView il TextView in cui impostare il nome del Co-Relatore.
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
     * Ottiene l'ID dell'utente associato a un professore specifico e richiede il nome del professore.
     * @param context il contesto dell'applicazione per l'accesso ai dati.
     * @param idProfessore l'ID del professore di cui ottenere l'ID dell'utente associato.
     * @param isRelatore un flag che indica se il professore è un Relatore.
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
     * Ottiene il nome e il cognome di un professore associato a un utente specifico e imposta il nome del professore
     * come Relatore o Co-Relatore in base al flag isRelatore.
     * @param context
     * @param idUtente l'ID dell'utente di cui ottenere il nome e cognome del professore associato.
     * @param isRelatore un flag che indica se il professore deve essere impostato come Relatore.
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
     * Verifica se uno studente ha già effettuato una richiesta per una tesi specifica.
     *
     * @param id_tesi l'ID della tesi per cui verificare la richiesta dello studente.
     * @param view la vista da cui nascondere il bottone se lo studente ha già effettuato una richiesta.
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
     * Verifica se uno studente soddisfa i vincoli specificati relativi a media e esami mancanti.
     *
     * Questo metodo confronta i valori di media e esami mancanti di uno studente con i valori passati come parametri.
     * Se la media dello studente è maggiore della media specificata e il numero di esami mancanti è inferiore a quello specificato,
     * il metodo restituirà `true`, altrimenti restituirà `false`.
     *
     * @param media la media specificata da confrontare con quella dello studente.
     * @param esamiMancanti il numero di esami mancanti specificato da confrontare con quello dello studente.
     * @return `true` se lo studente soddisfa i vincoli specificati, altrimenti `false`.
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
     * Questo metodo crea e mostra una finestra di dialogo che chiede all'utente di confermare l'invio di una richiesta di tesi.
     * La finestra di dialogo visualizzerà un messaggio specifico, l'ID della tesi, l'ID dello studente e se i requisiti sono soddisfatti.
     *
     * @param message il messaggio da visualizzare nella finestra di dialogo.
     * @param idTesi l'ID della tesi per cui si sta inviando la richiesta.
     * @param idStudente l'ID dello studente che sta inviando la richiesta.
     * @param soddisfaRequisiti un valore booleano che indica se lo studente soddisfa i requisiti richiesti per la tesi.
     */
    private void showConfirmationDialog(String message, Long idTesi, Long idStudente, boolean soddisfaRequisiti) {
        ConfermaRichiestaDialog dialog = new ConfermaRichiestaDialog(message, idTesi, idStudente, soddisfaRequisiti);
        dialog.show(getFragmentManager(), "ConfermaRichiestaDialog");
    }

    /**
     * Incrementa il conteggio delle visualizzazioni per una tesi specifica nel database Firestore.
     *
     * Questo metodo esegue una query per trovare la tesi corrispondente al titolo fornito e incrementa il conteggio
     * delle visualizzazioni per quella tesi sia nel database Firestore che nel valore locale.
     *
     * @param idTesi il idTesi della tesi per cui incrementare le visualizzazioni.
     */
    private void incrementaVisualizzazioni(Long idTesi,Long visualizzazioniDaIncr) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tesiRef = db.collection("Tesi");

        Query query = tesiRef.whereEqualTo("id_tesi", idTesi);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Ottieni il documento corrispondente (potrebbe esserci più di uno, ma ne usiamo il primo)
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                    // Estrai l'ID del documento
                    String documentId = documentSnapshot.getId();

                    Long visualizzazioni = documentSnapshot.getLong("visualizzazioni");


                    // Incrementa le visualizzazioni utilizzando l'ID del documento
                    tesiRef
                            .document(documentId)
                            .update("visualizzazioni", visualizzazioni + 1)
                            .addOnSuccessListener(aVoid -> {

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
     * Genera un codice QR basato sui dati di una tesi specifica.
     *
     * Questo metodo utilizza i dati di una tesi per creare un contenuto testuale e quindi genera un codice QR
     * rappresentante questi dati. Il codice QR risultante verrà restituito come un oggetto Bitmap.
     *
     * @param tesi la tesi di cui generare il codice QR.
     * @return un oggetto Bitmap contenente il codice QR rappresentante i dati della tesi.
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
                    "\nRelatore: " + nomeRelatore +
                    "\nCo-Relatore: " + nomeCorelatore;


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
     * Carica l'elenco dei file relativi a una tesi dal Firebase Storage e visualizza i nomi dei file nell'UI.
     *
     * Questo metodo recupera l'elenco dei file relativi a una tesi dal Firebase Storage e li visualizza
     * nell'UI tramite un adattatore personalizzato. Gli utenti possono quindi visualizzare e/o scaricare
     * questi file, a seconda dei permessi disponibili.
     *
     * @param view la vista corrente in cui visualizzare l'elenco dei file.
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
                            // Verifica se hai i permessi di scrittura
                            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                // Richiedi esplicitamente il permesso di scrittura nella memoria esterna
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                            } else {
                                // Se hai già i permessi, puoi procedere con il download
                                performDownload(position,storageRef);
                            }
                        }
                    });


                });
    }

    /**
     * Esegue il download di un file dal Firebase Storage nel dispositivo locale.
     *
     * Questo metodo scarica un file specifico dal Firebase Storage nel dispositivo locale, nella directory
     * di download. È possibile gestire il completamento del download, ad esempio aprendo il file o mostrando
     * una notifica di download completato.
     *
     * @param position la posizione dell'elemento nell'elenco dei file da scaricare.
     * @param storageRef il riferimento al Firebase Storage contenente il file da scaricare.
     */
    private void performDownload(int position,StorageReference storageRef) {
            // Ottieni il riferimento al file selezionato
            StorageReference selectedFileRef = storageRef.child(nomiFile.get(position));

            // Crea un file locale dove scaricare il file nella directory di download
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File localFile = new File(downloadsDir, nomiFile.get(position));

            // Esegui il download
            selectedFileRef.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Il file è stato scaricato con successo
                        // Puoi gestire il completamento qui
                        // Ad esempio, puoi aprire il file o mostrare una notifica di download completato
                        Toast.makeText(getContext(), "File scaricato con successo", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(exception -> {
                        // Gestisci eventuali errori durante il download
                        Log.e("Firebase Storage Error", "Errore nel download del file", exception);
                        Toast.makeText(getContext(), "Errore nel download del file", Toast.LENGTH_SHORT).show();
                    });
        }

}