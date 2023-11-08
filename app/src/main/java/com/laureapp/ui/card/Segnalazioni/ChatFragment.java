package com.laureapp.ui.card.Segnalazioni;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
 * La chat potrebbe essere parte di una funzionalità di segnalazioni o comunicazioni tra gli utenti.
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
    Long id_tesi;
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                utenteSend = args.getSerializable("Utente", Utente.class);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                segnalazione = args.getSerializable("SelectedSegnalazione", Segnalazione.class);
            }
        }
        if (StringUtils.equals(ruolo, getString(R.string.studente))){
            info_search_receiver.put("receiver", "id_professore");
            info_search_receiver.put("path_id_reciver", "TesiProfessore");
        }else{
            info_search_receiver.put("receiver", "id_studente");
            info_search_receiver.put("path_id_reciver", "StudenteTesi");
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

        loadIdTesi().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                info_search_receiver.put("id_tesi", task.getResult());
                loadIdReceiver(task.getResult()).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        utenteRecive = task2.getResult();
                        readMessage();
                    }
                });
            }});

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

    private Task<Long> loadIdReceiver(Long id_tesi) {
        return FirebaseFirestore.getInstance()
                .collection(Objects.requireNonNull(info_search_receiver.get("path_id_reciver")).toString())
                .whereEqualTo("id_tesi", id_tesi)
                .limit(1) // Limita la query a un solo documento
                .get()
                .continueWith(task3 -> {
                    if (task3.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task3.getResult()) {
                            Long idProfessore = doc.getLong(info_search_receiver.get("receiver").toString());
                            if (idProfessore != null) {
                                return idProfessore;
                            }
                        }
                    }
                    return -1L;
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
                        if ((chat.getReceiver().equals(utenteSend.getId_utente()) && chat.getSender().equals(utenteRecive)) || (chat.getReceiver().equals(utenteRecive) && chat.getSender().equals(utenteSend.getId_utente()))) {
                            mChat.add(chat);
                            messageAdapter = new MessageAdapter(context, mChat,utenteSend.getId_utente(),"Default");
                            binding.recycleView.setAdapter(messageAdapter);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}