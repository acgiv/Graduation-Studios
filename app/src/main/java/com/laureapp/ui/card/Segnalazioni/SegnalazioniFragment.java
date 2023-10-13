package com.laureapp.ui.card.Segnalazioni;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import java.util.List;

import android.app.AlertDialog;

import com.laureapp.R;
import com.laureapp.ui.card.Adapter.SegnalazioniAdapter;
import com.laureapp.ui.roomdb.entity.Segnalazione;
import com.laureapp.ui.roomdb.repository.SegnalazioneRepository;
import com.laureapp.ui.roomdb.viewModel.StudenteModelView;
import com.laureapp.ui.roomdb.viewModel.StudenteTesiModelView;
import com.laureapp.ui.roomdb.viewModel.UtenteModelView;

public class SegnalazioniFragment extends Fragment {

    Bundle args;
    Context context;
    String ruolo;
    String email;
    Long id_utente;
    Long id_studente;
    Long idTesiDesiderata;

    //FragmentSegnBuilding binding;

    UtenteModelView utenteView = new UtenteModelView(context); // Inizializza utenteView con un'istanza di UtenteModelView
    StudenteModelView studenteView = new StudenteModelView(context);
    StudenteTesiModelView studenteTesiView = new StudenteTesiModelView(context);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = requireContext();
        args= getArguments();
        if (args != null) {
            ruolo = args.getString("ruolo");
            Log.d("Segn ruolo ", ruolo);
        }

        View rootView = inflater.inflate(R.layout.fragment_segnalazioni_studente, container, false);;


        //Bottone nuova segnalazione
        ImageButton btnNS = rootView.findViewById(R.id.add_segn_ImageButton);
        btnNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_segnStudentiFragment_to_newSegnalazioni);

            }
        });

        email = getEmailFromSharedPreferences(context);
        Log.d("segnM", email);
        if(email != null) {
            id_utente = utenteView.getIdUtente(email); //ottengo l'id dell'utente corrispondente a tale mail
            id_studente = studenteView.findStudente(id_utente); //ottengo l'id dello studente corrispondente all'id dell'utente

            // Ottieni l'ID della tesi desiderata
            idTesiDesiderata = studenteTesiView.findIdTesiByIdTesi(id_studente);
            Log.d("tesiStudente", String.valueOf(idTesiDesiderata));

        }


        // Ottieni il riferimento alla ListView
        ListView listView = rootView.findViewById(R.id.segn_list_view);

        // Crea un'istanza del repository delle segnalazioni
        SegnalazioneRepository segnalazioneRepository = new SegnalazioneRepository(requireContext());

        // Ottieni le segnalazioni per la tesi desiderata dal repository
        List<Segnalazione> segnalazioniList = segnalazioneRepository.findSegnalazioniByTesiId(idTesiDesiderata);


        // Crea l'adapter personalizzato e imposta sulla ListView
        SegnalazioniAdapter adapter = new SegnalazioniAdapter(requireContext(), segnalazioniList);
        // Collega l'adapter alla ListView
        listView.setAdapter(adapter);

        // Imposta un listener per gli elementi della ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Ottieni il NavController dalla tua Activity principale
                NavController navController = NavHostFragment.findNavController(SegnalazioniFragment.this);

                // Esegui la navigazione verso DiscussioneFragment
                navController.navigate(R.id.action_segnalazioniFragment_to_discussioneFragment);
            }
        });


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){

    }

    public static String getEmailFromSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("preferenze", Context.MODE_PRIVATE);
        return preferences.getString("email", null);
    }

    public void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Nuova Segnalazione");

        View view = LayoutInflater.from(requireContext()).inflate(R.layout.add_segn_popup, null);
        builder.setView(view);

        EditText editTextTitolo = view.findViewById(R.id.titolo_register);
        EditText editTextRichiesta = view.findViewById(R.id.richiesta_register);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String inputTitolo = editTextTitolo.getText().toString();
                String inputRichiesta = editTextRichiesta.getText().toString();

                //Gestione dati
            }
        });



    }


}