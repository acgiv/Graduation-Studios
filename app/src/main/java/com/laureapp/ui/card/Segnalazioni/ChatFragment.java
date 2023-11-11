package com.laureapp.ui.card.Segnalazioni;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laureapp.R;
import com.laureapp.databinding.FragmentChatBinding;
import com.laureapp.ui.card.Adapter.MessageAdapter;
import com.laureapp.ui.roomdb.entity.Segnalazione;
import com.laureapp.ui.roomdb.entity.Utente;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * Questa classe rappresenta un fragment utilizzato per gestire la chat all'interno dell'applicazione.
 * La chat potrebbe essere parte di una funzionalitÃ  di segnalazioni o comunicazioni tra gli utenti.
 * Il fragment visualizza una chat e permette agli utenti di scambiare messaggi.
 */

public class ChatFragment extends Fragment {

    FragmentChatBinding binding;
    private Context context;
    DatabaseReference reference;
    FirebaseUser fuser;
    String ruolo;
    Segnalazione segnalazione;
    Utente utenteSend;
    Long utenteRecive;
    HashMap<String, Object> info_search_receiver = new HashMap<>();
    Bundle args;
    MessageAdapter messageAdapter;
    private List<Chat> mChat = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fuser=  FirebaseAuth.getInstance().getCurrentUser();
        args = getArguments();
        if (args != null) {
            ruolo = args.getString("ruolo");
            segnalazione =(Segnalazione) args.getSerializable("SelectedSegnalazione");
            utenteSend = (Utente) args.getSerializable("Utente");
        }
        if (StringUtils.equals(ruolo, getString(R.string.studente))){
            info_search_receiver.put("receiver_ruolo", "id_professore");
            info_search_receiver.put("path_id_tesi_ruolo", "TesiProfessore");
            info_search_receiver.put("path_id_utente", "Utenti/Professori/Professori");
            utenteSend = (Utente) args.getSerializable("Utente");
        }else if (StringUtils.equals(ruolo, getString(R.string.professore))) {
            info_search_receiver.put("receiver_ruolo", "id_studente");
            info_search_receiver.put("path_id_tesi_ruolo", "StudenteTesi");
            info_search_receiver.put("path_id_utente", "Utenti/Studenti/Studenti");

        }else  if (StringUtils.equals(ruolo, "Ospite")){
            info_search_receiver.put("receiver_ruolo", "id_studente");
            info_search_receiver.put("path_id_tesi_ruolo", "StudenteTesi");
            info_search_receiver.put("path_id_utente", "Utenti/Studenti/Studenti");
            utenteSend = new Utente();
            utenteSend.setId_utente(0L);
            DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference("Segnalazione/0/Chats");

            chatReference.removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Firebase", "Messaggi eliminati con successo");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Firebase", "Errore durante l'eliminazione dei messaggi", e);
                        }
                    });
        }

        reference = FirebaseDatabase.getInstance().getReference("Segnalazione").child(String.valueOf(segnalazione.getId_segnalazione()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding =FragmentChatBinding.inflate(inflater, container, false);
        context = requireContext();
        binding.recycleView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setStackFromEnd(true);
        binding.recycleView.setLayoutManager(linearLayoutManager);
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if(!StringUtils.equals(ruolo,"Ospite")){
            loadIdTesi().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    info_search_receiver.put("id_tesi", task.getResult());
                    loadIdReceiver(task.getResult()).addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            utenteRecive = task2.getResult() ;
                            readMessage();
                        }
                    });
                }});
        }else{
            utenteRecive= -1L;
        }


        binding.btcSend.setOnClickListener(v -> {
            if (!StringUtils.isEmpty(binding.textSend.getText().toString())){
                send_message(utenteSend.getId_utente(), utenteRecive ,binding.textSend.getText().toString());
                binding.textSend.setText("");
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                readMessage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void send_message(Long sender, Long receiver, String message){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message",message);
        reference.child("Chats").push().setValue(hashMap);

    }

    private Task<Long> loadIdTesi() {
        return FirebaseFirestore.getInstance()
                .collection("StudenteTesi")
                .whereEqualTo("id_studente_tesi", segnalazione.getId_studente_tesi())
                .limit(1) // Limita la query a un solo documento
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        Long idTesi = doc.getLong("id_tesi");
                        if (idTesi != null) {
                            return idTesi;
                        }
                    }
                    return -1L;
                });
    }

    private Task<Long> loadIdUtente(Long id_ruolo) {
        return FirebaseFirestore.getInstance()
                .collection(Objects.requireNonNull(info_search_receiver.get("path_id_utente")).toString())
                .whereEqualTo(Objects.requireNonNull(info_search_receiver.get("receiver_ruolo")).toString(), id_ruolo)
                .limit(2) // Limita la query a un solo documento
                .get()
                .continueWith(task4 -> {
                    if (task4.isSuccessful()) {
                        QueryDocumentSnapshot doc2 = (QueryDocumentSnapshot) task4.getResult().getDocuments().get(0);
                        return doc2.getLong("id_utente");
                    }
                    return -1L;
                });
    }

    private Task<Long> loadIdReceiver(Long id_tesi) {
        return FirebaseFirestore.getInstance()
                .collection(Objects.requireNonNull(info_search_receiver.get("path_id_tesi_ruolo")).toString())
                .whereEqualTo("id_tesi", id_tesi)
                .limit(1) // Limita la query a un solo documento
                .get()
                .continueWithTask(task3 -> {
                    if (task3.isSuccessful()) {
                        QueryDocumentSnapshot doc = (QueryDocumentSnapshot) task3.getResult().getDocuments().get(0);
                        Long id_ruolo = doc.getLong(Objects.requireNonNull(info_search_receiver.get("receiver_ruolo")).toString());
                        if (id_ruolo != null) {
                            return loadIdUtente(id_ruolo).continueWith(task5 -> {
                                if (task5.isSuccessful()) {
                                    return (Long) task5.getResult();
                                } else {
                                    return -1L;
                                }
                            });
                        }
                    }
                    return Tasks.forResult(-1L);
                });
    }



    private void readMessage(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference = FirebaseDatabase.getInstance().getReference("Segnalazione").child(String.valueOf(segnalazione.getId_segnalazione()));

                mChat.clear();
                for (DataSnapshot dataSnapshot: snapshot.child("Chats").getChildren()){
                    Chat chat = new Chat(dataSnapshot.child("message").getValue(String.class),
                            dataSnapshot.child("receiver").getValue(Long.class),dataSnapshot.child("sender").getValue(Long.class) );

                    if (chat.getReceiver() != null && chat.getReceiver() != null && utenteSend != null && utenteRecive != null) {
                            mChat.add(chat);
                            messageAdapter = new MessageAdapter(context, mChat,utenteSend.getId_utente(),"Default");
                            binding.recycleView.setAdapter(messageAdapter);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}