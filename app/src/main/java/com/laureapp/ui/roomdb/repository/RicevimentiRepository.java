package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Ricevimenti;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RicevimentiRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public RicevimentiRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    public void insertRicevimenti(Ricevimenti ricevimenti){
        executor.execute(() -> roomDbSqlLite.ricevimentiDao().insert(ricevimenti));
    }

    public void updateRicevimenti(Ricevimenti ricevimenti){
        executor.execute(() -> roomDbSqlLite.ricevimentiDao().update(ricevimenti));
    }

    public Ricevimenti findAllById(Long id){
        CompletableFuture<Ricevimenti> future = new CompletableFuture<>();
        executor.execute(() -> {
            Ricevimenti ricevimenti = roomDbSqlLite.ricevimentiDao().findAllById(id);
            future.complete(ricevimenti);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Ricevimenti();
        }
    }

    public List<Ricevimenti> getAllRicevimenti(){
        CompletableFuture<List<Ricevimenti>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<Ricevimenti> lista = roomDbSqlLite.ricevimentiDao().getAllRicevimenti();
            future.complete(lista);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Restituire una lista vuota in caso di errore
        }
    }

    public boolean deleteRicevimenti(Long id){
        boolean result = false;
        Ricevimenti ricevimenti =  this.findAllById(id);
        if (ricevimenti.getId_ricevimento() != null) {
            executor.execute(() -> roomDbSqlLite.ricevimentiDao().delete(ricevimenti));
            result = true;
        }
        return result;
    }
}
