package com.laureapp.ui.card.Task;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laureapp.R;
import com.laureapp.ui.controlli.ControlInput;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteTesi;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.entity.Utente;

import java.util.NoSuchElementException;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class DettagliTesistaFragment extends Fragment {

    
    Context context;
    private NavController mNav;

    String nome;
    String cognome;
    Long matricola;
    String facolta;
    String cicloCdl;
    String nome_cdl;
    String titolo;
    Bundle args;


    public DettagliTesistaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_dettagli_tesista, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button taskButton = view.findViewById(R.id.button_visualizza_task_tesista);
        mNav = Navigation.findNavController(view);
        Button deleteButton = view.findViewById(R.id.button_elimina_tesista);
        // Prendo gli argomenti passatomi dal layout precedente


        TextView nomeTesistaTextView = view.findViewById(R.id.insert_nome_tesista);
        TextView cognomeTesistaTextView = view.findViewById(R.id.insert_cognome_tesista);
        TextView matricolaTesistaTextView = view.findViewById(R.id.insert_matricola_tesista);
        TextView facoltaTextView = view.findViewById(R.id.insert_facolta_tesista);
        TextView ciclocdlTextView = view.findViewById(R.id.insert_ciclo_cdl_tesista);
        TextView nomeCdlTextView = view.findViewById(R.id.insert_cdl_tesista);
        TextView titoloTesiTextView = view.findViewById(R.id.insert_titolo_tesi_tesista);

        args = getArguments();

        if (args != null) {
            Utente utente = args.getSerializable("Utente", Utente.class);
            Studente studente = args.getSerializable("Studente", Studente.class);




            loadStudenteTesiForStudenteId(studente.getId_studente()).addOnCompleteListener(tesiTask -> {
                if (tesiTask.isSuccessful()) {
                    Tesi tesi = tesiTask.getResult();
                    if (tesi != null) {
                        // Ora puoi accedere ai dati della tesi come segue:
                        cicloCdl = tesi.getCiclo_cdl();
                        titolo = tesi.getTitolo();
                        if (utente != null && studente != null) {
                            Log.d("Dati tesista", args.toString());

                            nome = utente.getNome();
                            cognome = utente.getCognome();
                            matricola = studente.getMatricola();

                            facolta = utente.getFacolta();
                            nome_cdl = utente.getNome_cdl();

                            Log.d("Dati tesista", nome + cognome + matricola.toString() + facolta + cicloCdl + nome_cdl + titolo);

                            //Setto le textView
                            nomeTesistaTextView.setText(nome);
                            cognomeTesistaTextView.setText(cognome);
                            matricolaTesistaTextView.setText(matricola.toString());
                            facoltaTextView.setText(facolta);
                            ciclocdlTextView.setText(cicloCdl);
                            nomeCdlTextView.setText(nome_cdl);
                            titoloTesiTextView.setText(titolo);

                        }
                        Log.d("ciclo+titolo", cicloCdl + titolo);


                        // Usa il valore di cicloCdl e titolo come necessario
                    } else {
                        // Gestisci il caso in cui la tesi sia null
                    }
                } else {
                    //ControlInput.showToast(context, "Errore nel caricamento della tesi");
                }
            });



            taskButton.setOnClickListener(view1 -> mNav.navigate(R.id.action_dettagli_tesista_to_task, args));

            deleteButton.setOnClickListener(view1 -> {
                showConfirmationDialog(studente,utente);
            });


        }
    }




    /**
     * Questo metodo mi permette di caricare da firestore la tabella studente_tesi dando come parametro l'id dello studente
     *
     * @param id_studente id dello studente nella tabella Studente
     * @return l'id della tesi associata allo studente tesista
     */
    private Task<Long> loadStudenteTesiByStudenteId(Long id_studente) {
        return FirebaseFirestore.getInstance()
                .collection("StudenteTesi")
                .whereEqualTo("id_studente", id_studente)
                .limit(1)
                .get()
                .continueWith(task -> {


                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0); // Otteniamo il primo documento
                        StudenteTesi studenteTesi = new StudenteTesi();
                        studenteTesi.setId_studente(doc.getLong("id_studente"));
                        studenteTesi.setId_studente_tesi(doc.getLong("id_studente_tesi"));
                        studenteTesi.setId_tesi(doc.getLong("id_tesi"));




                        return studenteTesi.getId_tesi();
                    }
                    throw new NoSuchElementException("Utente non trovato con questa mail: " + id_studente);
                });
    }

    /**
     * Questo metodo mi permette di caricare da firestore la tabella tesi dando come parametro l'id della tesi nella tabella studenteTesi
     *
     * @param id_tesi id della tesi nella tabella StudenteTesi
     * @return l'id della tesi presente nella tabella studente_tesi
     */
    private Task<Tesi> loadTesiByIdTesiInStudenteTesi(Long id_tesi) {
        return FirebaseFirestore.getInstance()
                .collection("Tesi")
                .whereEqualTo("id_tesi", id_tesi)
                .limit(1)
                .get()
                .continueWith(task -> {

                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0); // Otteniamo il primo documento
                        Tesi tesi = new Tesi();
                        tesi.setId_tesi(doc.getLong("id_tesi"));
                        tesi.setId_vincolo(doc.getLong("id_vincolo"));
                        tesi.setAbstract_tesi(doc.getString("abstract_tesi"));
                        tesi.setTitolo(doc.getString("titolo"));
                        tesi.setTipologia(doc.getString("tipologia"));
                        tesi.setData_pubblicazione(Objects.requireNonNull(doc.getTimestamp("data_pubblicazione")).toDate());
                        tesi.setCiclo_cdl(doc.getString("ciclo_cdl"));



                        return tesi;
                    }
                    throw new NoSuchElementException("Utente non trovato con questa mail: " + id_tesi);
                });
    }

    /**
     * Questo metodo permette di accedere alla tabella StudenteTesi dalla tabella studente in base all'id dello studente.
     * E' il terzo metodo(3) utile per poter recuperare le tasks.
     *
     * @param id_studente è l'id dello studente nella tabella StudenteTesi
     */
    private Task<Tesi> loadStudenteTesiForStudenteId(Long id_studente) {
        return loadStudenteTesiByStudenteId(id_studente).continueWithTask(studenteTesiTask -> {
            if (studenteTesiTask.isSuccessful()) {
                Long id_tesi_in_studente_tesi = studenteTesiTask.getResult();
                return loadTesiForTesiIdInStudenteTesi(id_tesi_in_studente_tesi);
            } else {
                ControlInput.showToast(context, "Dati StudenteTesi non caricati correttamente");
                return Tasks.forResult(null); // Puoi restituire null o un valore di default in caso di errore
            }
        });
    }


    /**
     * Questo metodo ritorna la tesi in base all'id della tesi in studenteTesi
     * @param id_tesi_in_studente_tesi id della tesi nella tabella studenteTesi
     * @return tesi in base all'id della tesi in studenteTesi
     */
    private Task<Tesi> loadTesiForTesiIdInStudenteTesi(Long id_tesi_in_studente_tesi) {
        return loadTesiByIdTesiInStudenteTesi(id_tesi_in_studente_tesi);
    }


    private void deleteStudenteTesi(Long id_studente) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("StudenteTesi")
                .whereEqualTo("id_studente", id_studente)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        String documentId = doc.getId();

                        db.collection("StudenteTesi")
                                .document(documentId)
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Eliminazione completata con successo
                                    // Ora puoi navigare nuovamente al layout fragment_lista_tesisti
                                    mNav.navigate(R.id.action_dettagli_tesista_to_lista_tesisti);
                                })
                                .addOnFailureListener(e -> {
                                    // Gestisci eventuali errori nella cancellazione
                                });
                    } else {
                        // Nessun documento trovato, gestisci il caso in cui il record non esista
                    }
                });
    }

    /**
     * Metodo per mostrare il popup di conferma per l'eliminazione del tesista
     * @param studente studente presente nella card
     * @param utente utente associato allo studente
     */
    private void showConfirmationDialog(Studente studente, Utente utente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Conferma eliminazione");

        String messageText = "Sei sicuro di voler eliminare il tesista <b>" + " " + utente.getNome() + " " + utente.getCognome() + " </b>" + "con matricola" + " " + "<b>" + studente.getMatricola() + "</b>?";

        // Crea un oggetto SpannableString utilizzando Html.fromHtml per poter utilizzare la formattazione HTML
        // Il testo è ora in grassetto
        SpannableString message = new SpannableString(Html.fromHtml(messageText, Html.FROM_HTML_MODE_LEGACY));

        builder.setMessage(message);

        builder.setPositiveButton("Conferma", (dialog, which) -> {
            // Chiama il metodo per eliminare il record dalla tabella studente_tesi
            deleteStudenteTesi(studente.getId_studente());

            // Chiudi il popup
            dialog.dismiss();
        });

        builder.setNegativeButton("Annulla", (dialog, which) -> {
            // Chiudi il popup senza effettuare alcuna azione
            dialog.dismiss();
        });

        // Mostra il popup di conferma
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



}