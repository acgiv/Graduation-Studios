package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.TesiProfessore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TesiProfessoreRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public TesiProfessoreRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    public void insertTesiProfessore(TesiProfessore tesiProfessore){
        executor.execute(() -> roomDbSqlLite.tesiProfessoreDao().insert(tesiProfessore));
    }

    public void updateTesiProfessore(TesiProfessore tesiProfessore){
        executor.execute(() -> roomDbSqlLite.tesiProfessoreDao().update(tesiProfessore));
    }

    public TesiProfessore findAllById(Long id){
        CompletableFuture<TesiProfessore> future = new CompletableFuture<>();
        executor.execute(() -> {
            TesiProfessore tesiProfessore = roomDbSqlLite.tesiProfessoreDao().findAllById(id);
            future.complete(tesiProfessore);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new TesiProfessore();
        }
    }

    public List<TesiProfessore> getAllTesiProfessore(){
        CompletableFuture<List<TesiProfessore>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<TesiProfessore> lista = roomDbSqlLite.tesiProfessoreDao().getAllTesiProfessore();
            future.complete(lista);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Restituire una lista vuota in caso di errore
        }
    }

    public boolean delateTesiProfessore(Long id){
        boolean result = false;
        TesiProfessore tesiProfessore =  this.findAllById(id);
        if (tesiProfessore.getId() != null) {
            executor.execute(() -> roomDbSqlLite.tesiProfessoreDao().delete(tesiProfessore));
            result = true;
        }
        return result;
    }
}
