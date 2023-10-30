package com.laureapp.ui.card.TesiProfessore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.laureapp.R;
import com.laureapp.databinding.FragmentInfoTesiProfessoreBinding;
import com.laureapp.databinding.FragmentProfiloBinding;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.viewModel.TesiModelView;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;

public class InfoTesiFragment extends Fragment {

    Tesi tesi;
    String titolo;
    String descrizione;
    String tipologia;
    Date dataPubblicazione;
    String cicloCdl;

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

    private static final int PICKFILE_REQUEST_CODE = 1; // Codice di richiesta per il selettore di file

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf"); // Accetta qualsiasi tipo di file
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

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
        String fileName = "nome_file"; // Sostituisci con un nome di file desiderato
        StorageReference fileRef = storageRef.child("FileTesi/" + fileName);

        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Il file è stato caricato con successo
                    // Ora puoi ottenere l'URL del file caricato
                    fileRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                String downloadUrl = uri.toString();
                                Log.d("vedi",downloadUrl);
                            })
                            .addOnFailureListener(e -> {
                                // Gestisci l'errore nell'ottenere l'URL del file
                            });
                })
                .addOnFailureListener(e -> {
                    // Gestisci l'errore nel caricamento del file
                });
    }


}