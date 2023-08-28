package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Studente;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class StudenteRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public StudenteRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    public void insertStudente(Studente studente){
        executor.execute(() -> roomDbSqlLite.studenteDao().insert(studente));
    }

    public void updateStudente(Studente studente){
        executor.execute(() -> roomDbSqlLite.studenteDao().update(studente));
    }

    public Long findStudente(Long id_utente){
        CompletableFuture<Long> future = new CompletableFuture<>();
        executor.execute(() -> {
            Long id = roomDbSqlLite.studenteDao().findStudente(id_utente);
            future.complete(id);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1L;
        }
    }

    public Studente findAllById(Long id){
        CompletableFuture<Studente> future = new CompletableFuture<>();
        executor.execute(() -> {
            Studente studente = roomDbSqlLite.studenteDao().findAllById(id);
            future.complete(studente);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Studente();
        }
    }

    public List<Studente> getAllStudente(){
        CompletableFuture<List<Studente>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<Studente> lista = roomDbSqlLite.studenteDao().getAllStudente();
            future.complete(lista);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Restituire una lista vuota in caso di errore
        }
    }

    public boolean delateStudente(Long id){
        boolean result = false;
        Studente studente =  this.findAllById(id);
        if (studente.getId() != null) {
            executor.execute(() -> roomDbSqlLite.studenteDao().delete(studente));
            result = true;
        }
        return result;
    }

}
