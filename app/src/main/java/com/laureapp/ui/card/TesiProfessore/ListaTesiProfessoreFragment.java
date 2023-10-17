package com.laureapp.ui.card.TesiProfessore;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laureapp.R;
import com.laureapp.ui.card.Adapter.ListaTesiProfessoreAdapter;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.viewModel.ProfessoreModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListaTesiProfessoreFragment extends Fragment {

    Context context;
    String email;
    Long id_utente;
    Long id_professore;
    private ListView listView;
    private ListaTesiProfessoreAdapter adapter;
    ProfessoreModelView professoreView = new ProfessoreModelView(context);

    ArrayList<Long> idTesiList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_tesi_professore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();
        email = getEmailFromSharedPreferences();
        if (email != null) { // se la mail non è nulla

            UtenteModelView utenteView =  new UtenteModelView(context); //inizializza utenteView con un'istanza di UtenteModelView
            id_utente = utenteView.getIdUtente(email); //ottengo l'id dell'utente corrispondente a tale mail
            id_professore = professoreView.findProfessore(id_utente); //ottengo l'id del professore corrispondente all'id dell'utente

            Log.d("scasca", String.valueOf(id_professore));
            Log.d("scuscu", String.valueOf(id_utente));

            //carico l'elenco degli id delle tesi collegate con il professore
            loadIdTesiDataByProfessoreId(id_professore).addOnCompleteListener(task -> {
                if(task.isSuccessful()) { //se il task è completato con successo
                    idTesiList = task.getResult(); //assegno gli id delle tesi ad una lista di tipo Long
                    Log.d("Id Tesi", "Id Tesi " + idTesiList.toString());

                    } else {
                    Log.e("Firestore Error","Error getting data", task.getException());
                    }

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

                });

            } else {
            Log.d("Email salvata: ", "Non trovata");
            }
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

        //Crea una query Firestore per prendere documenti Tesi basati su idTesiList
        CollectionReference tesiCollection = FirebaseFirestore.getInstance().collection("Tesi");

        //Crea una lista di task per prendere documenti Tesi
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        for(Long idTesi : idTesiList) {
            Log.d("Id Tese123i", "Id321 Tesi" + idTesi);
            tasks.add(tesiCollection.document(String.valueOf(idTesi)).get());
        }

        //Unisce tutte le task in una singola task
        return Tasks.whenAllSuccess(tasks).continueWith(task -> {
            if(task.isSuccessful()) {
                List<Object> results = task.getResult();
                Log.d("Id Tese323i", "tesy " + results.toString());

                for(Object result : results) {
                    if(result instanceof DocumentSnapshot document) {
                        //Crea un oggeto Tesi dalla data Firestore
                        Tesi tesi = new Tesi();
                        tesi.setId_tesi((Long) document.get("id_tesi"));
                        tesi.setId_vincolo((Long) document.get("id_vincolo"));

                        //Codice che serve per convertire la data da Firebase a SQL
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
            return tesiList;
        });
    }
}