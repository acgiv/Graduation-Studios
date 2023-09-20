package com.laureapp.ui.card.Task;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.laureapp.R;
import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteWithUtente;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Questa classe gestisce la lista degli studenti mostrata dopo aver cliccato la card
 * Tesisti, lato professore. Questa lista viene filtrata in base al testo inserito dall'utente.
 *
 */
public class TesistiFragment extends Fragment {

    private NavController mNav;
    String ruolo;
    Context context;
    Bundle args;
    private List<StudenteWithUtente> studentList = new ArrayList<>();

    public TesistiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = requireContext();
        args = getArguments();
        if (args != null) {
            ruolo = args.getString("ruolo");
            Log.d("ruolo ", ruolo);
        }
        return inflater.inflate(R.layout.fragment_tesisti, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SearchView searchView = view.findViewById(R.id.searchView);
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        CollectionReference studentsRef = firestoreDB.collection("Utenti").document("Studenti").collection("Studenti");
        mNav = Navigation.findNavController(view);
        ListView listView = view.findViewById(R.id.listView);

        studentsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    studentList.clear(); // Cancella la lista esistente
                    StudenteModelView stModelView = new StudenteModelView(context);
                    studentList = stModelView.findStudenteIdByUtente();
                    Log.d("Lista studenti-utenti", stModelView.findStudenteIdByUtente().toString());

                    Log.d("Lista Studenti Tesisti", studentList.toString());

                    StudentAdapter adapter = new StudentAdapter(requireContext(), studentList);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener((parent, view1, position, id) -> {
                        mNav.navigate(R.id.action_fragment_tesisti_to_dettagli_tesista);
                        Log.d("Tesi", "cliccato il tesista");
                    });

                    // Aggiungi il TextWatcher per il campo di ricerca
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            adapter.getFilter().filter(query);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            // Quando il testo nel campo di ricerca cambia, filtra gli studenti
                            adapter.getFilter().filter(newText);
                            return true;
                        }
                    });
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Errore nel recupero degli studenti: " + e.getMessage()));

    }


    /**
     * Questo adapter fornisce l'accesso ai data Item. In questo caso,
     * ai ListItem della ListView e si occupa di settare graficamente
     * gli elementi della lista
     */
    class StudentAdapter extends ArrayAdapter<StudenteWithUtente> {
        private final List<StudenteWithUtente> studentList;
        private List<StudenteWithUtente> filteredStudentList;


        /**
         *
         * @param context si riferisce al contesto in cui viene utilizzato
         * @param studentList corrisponde alla lista di studenti da passare
         */
        public StudentAdapter(Context context, List<StudenteWithUtente> studentList ) {
            super(context, 0, studentList);
            LayoutInflater.from(context);
            this.studentList = studentList;
            this.filteredStudentList = new ArrayList<>(studentList);

            Log.d("Numero studenti adapter", "Numero di studenti nell'adapter: " + filteredStudentList.size());
        }

        /**
         *
         * @param position si riferisce alla posizione dell'item della lista
         * @param convertView si riferisce alla variabile che gestisce il cambiamento della view
         * @param parent Interfaccia per le informazioni globali riguardo all'ambiente dell'applicazione.
         *               usata per chiamare operazioni a livello applicazione launching activities, broadcasting e receiving intents
         * @return la view con la lista aggiornata
         */
        @NonNull
        @Override
        public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
            // Check if convertView is null, and if so, inflate the layout
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.lista_tesisti, parent, false);
            }

            Studente studente = getItem(position).getStudente();
            Utente utente = getItem(position).getUtente();


            if (studente != null && utente != null) {
                TextView nomeTextView = convertView.findViewById(R.id.nomeTextView);
                TextView matricolaTextView = convertView.findViewById(R.id.matricolaTextView);

                    String studentName = getString(R.string.student_name_placeholder, utente.getNome(), utente.getCognome());
                    nomeTextView.setText(studentName);
                    matricolaTextView.setText(String.valueOf(studente.getMatricola()));
            }

            return convertView;
        }



        @NonNull
        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults filterResults = new FilterResults();
                    String filterText = charSequence.toString().toLowerCase();

                    List<StudenteWithUtente> filteredList = new ArrayList<>();

                    if (filterText.isEmpty()) {
                        // Se il testo di ricerca è vuoto, mostra l'intera lista originale
                        filteredList.addAll(studentList);
                    } else {
                        // Altrimenti, filtra gli studenti in base al testo di ricerca
                        for (StudenteWithUtente studente : studentList) {
                            String matricola = String.valueOf(studente.getStudente().getMatricola());
                            String studentName = studente.getUtente().getNome() + studente.getUtente().getCognome();
                            if (studentName.toLowerCase().contains(filterText) || matricola.contains(filterText)) {
                                filteredList.add(studente);
                            }
                        }
                    }

                    filterResults.values = filteredList;
                    filterResults.count = filteredList.size();
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    List<StudenteWithUtente> filteredList = (List<StudenteWithUtente>) filterResults.values;
                    studentList.clear();
                    Log.d("Numero studenti adapter", "Numero di studenti nell'adapter: " + filteredStudentList.size());

                    if (filteredList != null) {

                        filteredStudentList.addAll(filteredList);
                    }
                    notifyDataSetChanged(); // Notifica all'adapter di aggiornare la vista
                }
            };
        }
    }

    /**
     * Questo metodo aggiorna il db locale di sqlite con quello di Firebase.
     * Ho dovuto leggere tutte le collection per poterle aggiornare perchè altrimenti
     * i dati non venivano letti correttamente.
     */
    private void aggiornaDb() {
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        RoomDbSqlLite db = RoomDbSqlLite.getDatabase(context.getApplicationContext());

        //Qui leggo la collection degli utenti. L'id dell'utente collegato allo studente è corretto
        firestoreDB.collection("Utenti")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //Per pulire la cache del db
                        db.utenteDao().deleteAll();

                        //Per salvare i dati in SQLite da Firestore
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        for (DocumentSnapshot document : documents) {
                            StudenteWithUtente studenteWithUtente = document.toObject(StudenteWithUtente.class);// Converte il documento in un oggetto Studente

                            assert studenteWithUtente != null;
                            if (studenteWithUtente.getUtente() != null) {
                                int count = db.utenteDao().countUtenteById(studenteWithUtente.getUtente().getId_utente());
                                if (count > 0) {
                                    db.utenteDao().insert(studenteWithUtente.getUtente());


                                }
                                Exception exception = task.getException();
                                if (exception != null) {
                                    Log.d("Firestore", "Errore nella lettura dei dati: " + exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        }
                        aggiornaStudentiDb();
                        Log.d("utenti", String.valueOf(db.utenteDao().getAllUtente()));


                    } else {
                        Log.d("Firestore", "Errore nella lettura dei dati: " + task.getException());
                    }
                });



        //Qui leggo la collection professori da Firebase
        firestoreDB.collection("Utenti").document("Professori").collection("Professori")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //Per pulire la cache del db
                        db.professoreDao().deleteAll();

                        //Per salvare i dati in SQLite da Firestore
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        for (DocumentSnapshot document : documents) {
                            Professore professore = document.toObject(Professore.class);
                            Log.d("studenti with utenti", String.valueOf(professore));

                            if (professore != null) {
                                db.professoreDao().insert(professore); // Chiama il metodo per l'inserimento o l'aggiornamento
                            } else {
                                Exception exception = task.getException();
                                if (exception != null) {
                                    Log.d("Firestore", "Errore nella lettura dei dati: " + exception.getMessage());
                                    exception.printStackTrace();
                                }
                            }
                        }
                        Log.d("studenti", String.valueOf(db.professoreDao().getAllProfessore()));
                    } else {
                        Log.d("Firestore", "Errore nella lettura dei dati: " + task.getException());
                    }
                });
    }


    private void aggiornaStudentiDb() {
        //Qui leggo la collection degli studenti. L'id che collega lo studente all'utente è corretto e rimane invariato.
        // Ma ad ogni aggiornamento l'id dello studente viene incrementato.
        //TODO: Verificare in futuro che l'aggiornamento del db e quindi l'incremento dell'id dello studente ad ogni aggiornamento non causi problemi
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        RoomDbSqlLite db = RoomDbSqlLite.getDatabase(context.getApplicationContext());
        firestoreDB.collection("Utenti").document("Studenti").collection("Studenti")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //Per pulire la cache del db
                        db.studenteDao().deleteAll();

                        //Per salvare i dati in SQLite da Firestore
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        for (DocumentSnapshot document : documents) {
                            StudenteWithUtente studenteWithUtente = document.toObject(StudenteWithUtente.class);// Converte il documento in un oggetto Studente
                            Log.d("studenti with utenti", String.valueOf(studenteWithUtente));

                            if (studenteWithUtente != null && studenteWithUtente.getUtente() != null) {
                                int count = db.utenteDao().countUtenteById(studenteWithUtente.getUtente().getId_utente());
                                if (count > 0) {
                                    db.studenteDao().insert(studenteWithUtente.getStudente());
                                } else {
                                    // Gestisci il caso in cui l'utente non esiste

                                    /*assert studenteWithUtente != null;
                                    if (studenteWithUtente.getUtente() != null) {
                                        db.utenteDao().insert(studenteWithUtente.getUtente());
                                        db.studenteDao().insert(studenteWithUtente.getStudente());
                                    } else {*/
                                    Exception exception = task.getException();
                                    if (exception != null) {
                                        Log.d("Firestore", "Errore nella lettura dei dati: " + exception.getMessage());
                                        exception.printStackTrace();
                                    }
                                    // }
                                }
                            }

                        }
                        Log.d("studenti", String.valueOf(db.studenteDao().getAllStudente()));


                    } else {
                        Log.d("Firestore", "Errore nella lettura dei dati: " + task.getException());
                    }
                });
    }
}

