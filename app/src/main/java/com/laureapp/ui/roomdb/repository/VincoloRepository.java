package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Vincolo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VincoloRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public VincoloRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    public void insertVincolo(Vincolo vincolo){
        executor.execute(() -> roomDbSqlLite.vincoloDao().insert(vincolo));
    }

    public void updateVincolo(Vincolo vincolo){
        executor.execute(() -> roomDbSqlLite.vincoloDao().update(vincolo));
    }

    public Vincolo findAllById(Long id){
        CompletableFuture<Vincolo> future = new CompletableFuture<>();
        executor.execute(() -> {
            Vincolo vincolo = roomDbSqlLite.vincoloDao().findAllById(id);
            future.complete(vincolo);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Vincolo();
        }
    }

    public List<Vincolo> getAllVincolo(){
        CompletableFuture<List<Vincolo>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<Vincolo> lista = roomDbSqlLite.vincoloDao().getAllVincolo();
            future.complete(lista);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Restituire una lista vuota in caso di errore
        }
    }

    public boolean deleteVincolo(Long id){
        boolean result = false;
        Vincolo vincolo =  this.findAllById(id);
        if (vincolo.getId_vincolo() != null) {
            executor.execute(() -> roomDbSqlLite.vincoloDao().delete(vincolo));
            result = true;
        }
        return result;
    }
}
