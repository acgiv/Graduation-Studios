package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Tesi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TesiRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public TesiRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    public void insertTesi(Tesi tesi){
        executor.execute(() -> roomDbSqlLite.tesiDao().insert(tesi));
    }

    public void updateTesi(Tesi tesi){
        executor.execute(() -> roomDbSqlLite.tesiDao().update(tesi));
    }

    public Tesi findAllById(Long id){
        CompletableFuture<Tesi> future = new CompletableFuture<>();
        executor.execute(() -> {
            Tesi tesi = roomDbSqlLite.tesiDao().findAllById(id);
            future.complete(tesi);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Tesi();
        }
    }

    public List<Tesi> getAllTesi(){
        CompletableFuture<List<Tesi>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<Tesi> lista = roomDbSqlLite.tesiDao().getAllTesi();
            future.complete(lista);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Restituire una lista vuota in caso di errore
        }
    }

    public boolean deleteTesi(Long id){
        boolean result = false;
        Tesi tesi =  this.findAllById(id);
        if (tesi.getId_tesi() != null) {
            executor.execute(() -> roomDbSqlLite.tesiDao().delete(tesi));
            result = true;
        }
        return result;
    }


}
