package com.laureapp.ui.card.TesiStudente;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LeMieTesiFragment extends Fragment {
    Context context;

    Long id_utente;
    Long id_studente;
    String email;
    private ListView listView;
    private LeMieTesiAdapter adapter;
    StudenteModelView studenteView = new StudenteModelView(context);

    ArrayList<Long> idTesiList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        return inflater.inflate(R.layout.fragment_lemietesi, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();
        email = getEmailFromSharedPreferences(); //chiamata al metodo per ottenere la mail

        if (email != null) { // se la mail non è nulla

            UtenteModelView utenteView = new UtenteModelView(context); // Inizializza utenteView con un'istanza di UtenteModelView
            id_utente = utenteView.getIdUtente(email); //ottengo l'id dell'utente corrispondente a tale mail
            id_studente = studenteView.findStudente(id_utente); //ottengo l'id dello studente corrispondente all'id dell'utente


            //carico l'elenco degli id delle tesi appartenenti allo studente
            loadIdTesiData(id_studente).addOnCompleteListener(task -> {
                if (task.isSuccessful()) { //se il task è completato con successo
                    idTesiList = task.getResult(); //assegno gli id delle tesi ad una lista di tipo Long
                    Log.d("Id Tesi", "Id Tesi " + idTesiList.toString());
                }else {
                    Log.e("Firestore Error", "Error getting data", task.getException());
                }

                    loadTesiData(idTesiList).addOnCompleteListener(tesiTask -> { //chiamo il metodo per ottenere le tesi in base alle id tesi ottenute
                        if (tesiTask.isSuccessful()) {
                            ArrayList<Tesi> tesiList = tesiTask.getResult();
                            Log.d(" Tesi", "Id Tesi " + tesiList.toString());


                            //mostro sulla listview tutte le tesi dello studente
                            listView = view.findViewById(R.id.listTesiView);
                            adapter = new LeMieTesiAdapter(getContext(), tesiList);
                            listView.setAdapter(adapter);
                        } else {
                            Log.e("Tesi Firestore Error", "Error getting Tesi data", tesiTask.getException());
                        }
                    });

            });

        } else {
            Log.d("Email salvata:", "Non trovata");
        }

    }

    /**
     * Si utilizza questo metodo per prendere le preferenze salvate nel metodo presente in HomeFragment
     * Esso prende la cartella "preferenze" e ne ricava la mail o l'oggetto che ci serve.
     * @return
     */
    private String getEmailFromSharedPreferences() {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences("preferenze", Context.MODE_PRIVATE);
            return preferences.getString("email", null);
        }
        return null;
    }

    /**
     * Questo metodo mi permette di caricare da firestore gli id delle tesi dando come parametro l'id dello studente
     *
     * @param id_studente
     * @return una lista di tipo Long contenente gli id delle tesi associate allo studente
     */
    private Task<ArrayList<Long>> loadIdTesiData(Long id_studente) {
        final ArrayList<Long> idTesiList = new ArrayList<>();

        return FirebaseFirestore.getInstance()
                .collection("StudenteTesi")
                .whereEqualTo("id_studente", id_studente)
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
        // Create a Firestore query to fetch Tesi documents based on idTesiList
        CollectionReference tesiCollection = FirebaseFirestore.getInstance().collection("Tesi");



        // Create a list of tasks to fetch Tesi documents
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        for (Long idTesi : idTesiList) {
            Log.d("Id Tese323i", "Id432 Tesi " + idTesi);
            tasks.add(tesiCollection.document(String.valueOf(idTesi)).get());
        }

        // Combine all tasks into a single task
        return Tasks.whenAllSuccess(tasks).continueWith(task -> {
            if (task.isSuccessful()) {
                List<Object> results = task.getResult();
                Log.d("Id Tese323i", "tesy " + results.toString());

                for (Object result : results) {
                    if (result instanceof DocumentSnapshot document) {
                        if (document.exists()) {
                            // Create a Tesi object from Firestore data
                            Tesi tesi = new Tesi();
                            tesi.setId_tesi((Long) document.get("id_tesi"));
                            tesi.setId_vincolo((Long) document.get("id_vincolo"));

                            //codice che serve per convertire la data da firebase a sql
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
                    }
                }
            }
            return tesiList;
        });
    }




}