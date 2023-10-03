package com.laureapp.ui.card.Task;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

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
import java.util.List;

/**
 * Questo adapter fornisce l'accesso ai data Item. In questo caso,
 * ai ListItem della ListView e si occupa di settare graficamente
 * gli elementi della lista
 */
public class StudentAdapter extends ArrayAdapter<StudenteWithUtente> {
    private final List<StudenteWithUtente> studentList;


    /**
     *
     * @param context si riferisce al contesto in cui viene utilizzato
     * @param studentList corrisponde alla lista di studenti da passare
     */
    public StudentAdapter(@NonNull Context context, List<StudenteWithUtente> studentList) {
        super(context, 0, studentList);
        this.studentList = new ArrayList<>(studentList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return studentFilter;
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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lista_tesisti, parent, false);
        }
        Log.d("VistaAdapter", "Adapter View Visualizzata");
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

    private Filter studentFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<StudenteWithUtente> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(studentList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (StudenteWithUtente item : studentList) {
                    if (item.getUtente().getNome().toLowerCase().contains(filterPattern) || item.getUtente().getCognome().toLowerCase().contains(filterPattern) || item.getStudente().getMatricola().toString().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((StudenteWithUtente) resultValue).getUtente().getNome();
        }
    };
}

