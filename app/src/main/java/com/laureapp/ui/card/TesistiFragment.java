package com.laureapp.ui.card;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laureapp.R;
import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Studente;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TesistiFragment extends Fragment {

    private NavController mNav;
    String ruolo;
    Context context;
    Bundle args;

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
        aggiornaDb();

        studentsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Studente> studentList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Studente student = document.toObject(Studente.class);
                        studentList.add(student);
                    }

                    StudentAdapter adapter = new StudentAdapter(requireContext(), studentList);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener((parent, view1, position, id) -> {
                        if (StringUtils.equals("Professore", ruolo)) {
                            mNav.navigate(R.id.action_fragment_tesisti_to_dettagli_tesista);
                            Log.d("Tesi", "cliccato il tesista");
                        } else {
                            mNav.navigate(R.id.action_fragment_tesisti_to_dettagli_tesista);
                            Log.d("Tesi", "cliccato il tesista");
                        }
                    });

                    // Aggiungi il TextWatcher per il campo di ricerca
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
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


    class StudentAdapter extends ArrayAdapter<Studente> {
        private StudentFilter studentFilter;
        private final LayoutInflater inflater;
        private final List<Studente> studentList;
        private List<Studente> filteredStudentList;

        public StudentAdapter(Context context, List<Studente> studentList) {
            super(context, 0, studentList);
            inflater = LayoutInflater.from(context);
            this.studentList = studentList;
            this.filteredStudentList = new ArrayList<>(studentList);
            this.studentFilter = new StudentFilter();

            Log.d("Adapter", "Numero di studenti nell'adapter: " + filteredStudentList.size());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Studente studente = getItem(position);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.lista_tesisti, parent, false);
            }

            if (studente != null) {
                TextView nomeTextView = convertView.findViewById(R.id.nomeTextView);
                TextView matricolaTextView = convertView.findViewById(R.id.matricolaTextView);

                if (nomeTextView != null && matricolaTextView != null) {
                    String studentName = getString(R.string.student_name_placeholder, studente.getNome(), studente.getCognome());
                    nomeTextView.setText(studentName);
                    matricolaTextView.setText(String.valueOf(studente.getMatricola()));
                }
            }

            return convertView;
        }


        @NonNull
        @Override
        public Filter getFilter() {
            if (studentFilter == null) {
                studentFilter = new StudentFilter();
            }
            return studentFilter;
        }

        class StudentFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                String filterText = charSequence.toString().toLowerCase();

                List<Studente> filteredList = new ArrayList<>();

                if (filterText.isEmpty()) {
                    // Se il testo di ricerca è vuoto, mostra l'intera lista originale
                    filteredList.addAll(studentList);
                } else {
                    // Altrimenti, filtra gli studenti in base al testo di ricerca
                    for (Studente studente : studentList) {
                        String studentName = (studente.getNome() + " " + studente.getCognome()).toLowerCase();
                        if (studentName.contains(filterText)) {
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
                List<Studente> filteredList = (List<Studente>) filterResults.values;
                filteredStudentList.clear();
                if (filteredList != null) {
                    filteredStudentList.addAll(filteredList);
                }
                notifyDataSetChanged(); // Notifica all'adapter di aggiornare la vista
            }


        }
    }


    private void aggiornaDb() {
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        RoomDbSqlLite db = RoomDbSqlLite.getDatabase(context.getApplicationContext());
        firestoreDB.collection("Utenti").document("Studenti").collection("Studenti")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        db.studenteDao().deleteAll();
                        db.professoreDao().deleteAll();
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        for (DocumentSnapshot document : documents) {
                            Studente studente = document.toObject(Studente.class);
                            db.studenteDao().insert(studente);
                        }

                        for (DocumentSnapshot document : documents) {
                            Professore professore = document.toObject(Professore.class);
                            db.professoreDao().insert(professore);
                        }

                        Log.d("studenti", String.valueOf(db.studenteDao().getAllStudente()));
                        Log.d("professori", String.valueOf(db.professoreDao().getAllProfessore()));

                    } else {
                        Log.d("Firestore", "Errore nella lettura dei dati: " + task.getException());
                    }
                });
    }
}
