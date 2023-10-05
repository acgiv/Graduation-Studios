package com.laureapp.ui.card.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.laureapp.R;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteWithUtente;
import com.laureapp.ui.roomdb.entity.Utente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Questo adapter fornisce l'accesso ai data Item. In questo caso,
 * ai ListItem della ListView e si occupa di settare graficamente
 * gli elementi della lista
 */
public class StudentAdapter extends ArrayAdapter<StudenteWithUtente> implements Filterable {
    private final List<StudenteWithUtente> studentList;
    private List<StudenteWithUtente> filteredStudentList;
    private Filter studentFilter;
    private StudentFilter filter;





    /**
     * @param context     si riferisce al contesto in cui viene utilizzato
     * @param studentList corrisponde alla lista di studenti da passare
     */
    public StudentAdapter(@NonNull Context context, List<StudenteWithUtente> studentList, List<StudenteWithUtente> filteredStudentList) {
        super(context, 0, studentList);
        this.studentList = new ArrayList<>(studentList);
        this.filteredStudentList = filteredStudentList; // Inizializza filteredStudentList con la stessa lista
    }




    /**
     * @param position    si riferisce alla posizione dell'item della lista
     * @param convertView si riferisce alla variabile che gestisce il cambiamento della view
     * @param parent      Interfaccia per le informazioni globali riguardo all'ambiente dell'applicazione.
     *                    usata per chiamare operazioni a livello applicazione launching activities, broadcasting e receiving intents
     * @return la view con la lista aggiornata
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lista_tesisti, parent, false);
        }
        TextView nomeTextView = convertView.findViewById(R.id.nomeTextView);
        TextView matricolaTextView = convertView.findViewById(R.id.matricolaTextView);

        StudenteWithUtente studenteWithUtenteItem = getItem(position);


        if (studenteWithUtenteItem != null && studenteWithUtenteItem.getUtente() != null) {
            Utente utente = studenteWithUtenteItem.getUtente();

            if (utente.getNome() != null && utente.getCognome() != null) {
                String nomeCompleto = utente.getNome() + " " + utente.getCognome();
                nomeTextView.setText(nomeCompleto);
            } else {
                // Gestisci il caso in cui il nome o il cognome sia null
                nomeTextView.setText("Nome e/o Cognome mancanti");
            }

            // Imposta la matricola dello studente
            matricolaTextView.setText(String.valueOf(studenteWithUtenteItem.getStudente().getMatricola()));
        } else {
            // Gestisci il caso in cui studenteWithUtente o Utente sia null
            nomeTextView.setText("Informazioni mancanti");
            matricolaTextView.setText("");
        }


        return convertView;
    }


    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new StudentFilter();
        }
        return filter;
    }

    private class StudentFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<StudenteWithUtente> filteredStudents = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                // Se il testo di ricerca è vuoto, mostra tutti gli studenti
                filteredStudents.addAll(studentList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (StudenteWithUtente student : studentList) {
                    // Filtra in base alla matricola (sostituisci "getMatricola()" con il metodo corretto)
                    if (student.getStudente().getMatricola().toString().toLowerCase().contains(filterPattern)) {
                        filteredStudents.add(student);
                    }
                }
            }

            results.values = filteredStudents;
            results.count = filteredStudents.size();
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredStudentList.clear();
            filteredStudentList.addAll((List<StudenteWithUtente>) results.values);
            notifyDataSetChanged();
        }

    }



}



