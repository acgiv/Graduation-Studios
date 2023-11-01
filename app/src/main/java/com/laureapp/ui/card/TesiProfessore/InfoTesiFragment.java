package com.laureapp.ui.card.TesiProfessore;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.mbms.FileInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.laureapp.R;
import com.laureapp.ui.card.Adapter.InfoTesiProfessoreAdapter;
import com.laureapp.ui.roomdb.entity.Tesi;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InfoTesiFragment extends Fragment {

    Tesi tesi;
    String titolo;
    String descrizione;
    String tipologia;
    Date dataPubblicazione;
    String cicloCdl;

    Long id_tesi;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private static final int PICKFILE_REQUEST_CODE = 1;
     ArrayList<String> nomiFile = new ArrayList<>(); // FileInfo rappresenta le informazioni sui file da visualizzare



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_info_tesi_professore, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Prendo gli argomenti
        Bundle args = getArguments();

        //Inizializzo
        TextView titoloTesiProfessoreTextView = view.findViewById(R.id.insertTitoloTesiProfesore);
        TextView tipologiaTesiProfessoreTextView = view.findViewById(R.id.insertTipologiaTesiProfessore);
        TextView dataPubblicazioneProfessoreTextView = view.findViewById(R.id.insertDataPubblicazioneTesiProfessore);
        TextView cicloCdlProfessoreTextView = view.findViewById(R.id.insertCicloCdlTesiProfessore);
        TextView abstractProfessoreTextView = view.findViewById(R.id.insertAbstractTesiProfessore);

        ImageButton modificaTitoloButton = view.findViewById(R.id.modificaTitoloTesiProfessore);
        ImageButton modificaTipologiaButton = view.findViewById(R.id.modificaTipologiaTesiProfessore);
        ImageButton modificaDataPubblicazioneButton = view.findViewById(R.id.modificaDataPubblicazioneTesiProfessore);
        ImageButton modificaCicloCDLButton = view.findViewById(R.id.modificaCicloCdlTesiProfessore);
        ImageButton modificaAbstractButton = view.findViewById(R.id.modificaAbstractTesiProfessore);

        // Aggiungi altri ImageButton per gli altri campi qui

        if (args != null) { //se non sono null

            tesi = (Tesi) args.getSerializable("Tesi"); //prendo la tesi dagli args
            if (tesi != null) {
                //Mi passo tutti i parametri di una tesi
                titolo = tesi.getTitolo();
                tipologia = tesi.getTipologia();
                dataPubblicazione = tesi.getData_pubblicazione();
                cicloCdl = tesi.getCiclo_cdl();
                descrizione = tesi.getAbstract_tesi();
                id_tesi = tesi.getId_tesi();

                //Setto
                titoloTesiProfessoreTextView.setText(titolo);
                tipologiaTesiProfessoreTextView.setText(tipologia);

                //Formatto la data per convertirla da SQL a java.date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = dateFormat.format(dataPubblicazione);
                dataPubblicazioneProfessoreTextView.setText(formattedDate);

                cicloCdlProfessoreTextView.setText(cicloCdl);
                abstractProfessoreTextView.setText(descrizione);

                // Gestione del click sulla matita per il titolo
                modificaTitoloButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Mostra il popup e permetti la modifica
                        showEditPopup(titoloTesiProfessoreTextView, titolo);

                    }
                });

                // Gestione del click sulla matita per la tipologia
                modificaTipologiaButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditPopup(tipologiaTesiProfessoreTextView, tipologia);
                    }


                });

                // Gestione del click sulla matita per la data di pubb
                modificaDataPubblicazioneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePickerDialog(dataPubblicazioneProfessoreTextView, dataPubblicazione);
                    }


                });

                //Gestione del click sulla matita per il ciclo cdl
                modificaCicloCDLButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditPopup(cicloCdlProfessoreTextView, cicloCdl);
                    }


                });
                //Gestione del click sulla matita per l'abstract

                modificaAbstractButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditPopup(abstractProfessoreTextView, descrizione);
                    }


                });

                //gestione del click sul pulsante inserisci file
                ImageButton caricaMaterialeButton = view.findViewById(R.id.caricaMaterialeTesiProfessore);
                caricaMaterialeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openFileChooser();
                    }
                });

                //carico tutti i file presenti su firestore storage
                loadFileFromFireStore(view);



            }
        }
    }

    /**
     * Questo metodo serve a mostrare un popup generico dove posso modificare i campi di tipo String
     * In base all'id del textview verrà utilizzato il set opportuno per modificare il campo all'interno dell'oggetto Tesi
     * @param textView
     * @param currentText
     */
    private void showEditPopup(final TextView textView, String currentText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View popupView = getLayoutInflater().inflate(R.layout.fragment_popup_modifica_campi_tesi, null);
        final EditText editText = popupView.findViewById(R.id.editText);

        // Imposta il testo corrente nell'EditText
        editText.setText(currentText);

        builder.setView(popupView);
        builder.setPositiveButton("Salva", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ottieni il nuovo testo dalla casella di testo
                String nuovoTesto = editText.getText().toString();

                // Aggiorna il campo corrispondente (titolo, tipologia, ecc.)
                textView.setText(nuovoTesto);

                // Aggiorna l'oggetto Tesi con i nuovi valori
                if (textView.getId() == R.id.insertTitoloTesiProfesore) {
                    tesi.setTitolo(nuovoTesto);
                } else if (textView.getId() == R.id.insertTipologiaTesiProfessore) {
                    tesi.setTipologia(nuovoTesto);
                }else if(textView.getId() == R.id.insertCicloCdlTesiProfessore){
                    tesi.setCiclo_cdl(nuovoTesto);
                }else if(textView.getId() == R.id.insertAbstractTesiProfessore){
                    tesi.setAbstract_tesi(nuovoTesto);
                }


                // Aggiungi altre condizioni per gli altri campi
                Log.d("vedi5",tesi.toString());
                updateTesiData(tesi); //chiamata al metodo di aggiornamento della tesi su firestore
                dialog.dismiss(); //chiudo il popup
            }
        });

        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    /**
     * Metodo per aggiornare la tesi modificata su firestore
     * @param tesi
     */
    private void updateTesiData(Tesi tesi) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tesiRef = db.collection("Tesi");


        Query query = tesiRef.whereEqualTo("id_tesi", tesi.getId_tesi()); //cerco la tesi con l'id tesi corrispondente

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Ottieni il documento corrispondente (potrebbe esserci più di uno, ma ne usiamo il primo)
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                    // Estrai l'ID del documento
                    String documentId = documentSnapshot.getId();

                    // Crea un oggetto mappa con i nuovi valori per tutti i campi
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("titolo", tesi.getTitolo());
                    updateData.put("tipologia", tesi.getTipologia());
                    updateData.put("data_pubblicazione", tesi.getData_pubblicazione());
                    updateData.put("ciclo_cdl", tesi.getCiclo_cdl());
                    updateData.put("abstract_tesi", tesi.getAbstract_tesi());

                    // Esegui l'aggiornamento
                    tesiRef.document(documentId)
                            .update(updateData)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firestore Success", "Dati della tesi aggiornati con successo.");
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firestore Error", "Errore durante l'aggiornamento dei dati della tesi", e);
                            });
                }
            } else {
                Log.e("Firestore Error", "Error querying Tesi collection", task.getException());
            }
        });
    }

    /**
     * Questo metodo serve a mostrare un popup generico dove posso modificare i campi di tipo Date
     * @param textView
     * @param currentDate
     */
    private void showDatePickerDialog(final TextView textView, Date currentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Aggiorna il campo dataPubblicazione con la nuova data selezionata
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, month, dayOfMonth);
                        dataPubblicazione = newDate.getTime();

                        // Aggiorna il campo TextView per mostrare la nuova data
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = dateFormat.format(dataPubblicazione);
                        textView.setText(formattedDate);

                        // Aggiorna l'oggetto Tesi con la nuova data
                        tesi.setData_pubblicazione(dataPubblicazione);
                        updateTesiData(tesi);

                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    /**
     * Questo metodo serve per controllare se l'app ha i permessi necessari a leggere/scrivere la memoria
     * del dispositivo ed avviare l'Intent di selezione di file pdf da allegare alla tesi
     */
    private void openFileChooser() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Richiedi esplicitamente i permessi di lettura e scrittura sulla memoria esterna
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            // Se hai già i permessi, puoi aprire il file chooser
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(intent, PICKFILE_REQUEST_CODE);
        }
    }

    // Gestisci la risposta dell'utente alla richiesta dei permessi
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // I permessi sono stati concessi, puoi ora aprire il file chooser
                openFileChooser();
            } else {
                // L'utente ha rifiutato i permessi, mostra un messaggio o gestisci di conseguenza
                Toast.makeText(getContext(), "Permesso di lettura/scrittura negato", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Gestisci il risultato della selezione del file
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();
                // Ora hai l'URI del file selezionato dall'utente

                // Puoi procedere con il caricamento del file su Firebase Storage
                uploadFileToFirebaseStorage(fileUri);
            }
        }
    }

    private void uploadFileToFirebaseStorage(Uri fileUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String fileName;
        StorageReference fileRef;

        DocumentFile documentFile = DocumentFile.fromSingleUri(requireContext(), fileUri);
        if (documentFile != null && documentFile.exists()) {
            fileName = documentFile.getName();
             fileRef = storageRef.child("FileTesi/" + id_tesi + "/" + fileName);

        } else {
            fileName = null;
            fileRef = null;
            Toast.makeText(getContext(),"Caricamento file non riuscito", Toast.LENGTH_SHORT).show();

        }

        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Il file è stato caricato con successo
                    // Ora puoi ottenere l'URL del file caricato
                    fileRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {

                                Toast.makeText(getContext(),"Caricamento file completato", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(),"Caricamento file non riuscito", Toast.LENGTH_SHORT).show();


                            });
                })
                .addOnFailureListener(e -> {
                    // Gestisci l'errore nel caricamento del file
                });
    }

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
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference();

                            StorageReference fileRef = storageRef.child("FileTesi/" + id_tesi + "/" + nomiFile.get(position)); // Sostituisci "nome_del_tuo_file.txt" con il nome del tuo file

                            fileRef.delete()
                                    .addOnSuccessListener(aVoid -> {

                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(getContext(), "File eliminato con successo", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(exception -> {
                                        // Gestisci eventuali errori durante l'eliminazione
                                        Log.e("Firebase Storage Error", "Errore nell'eliminazione del file", exception);
                                        Toast.makeText(getContext(), "Errore nell'eliminazione del file", Toast.LENGTH_SHORT).show();
                                    });
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