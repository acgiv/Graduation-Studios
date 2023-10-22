package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.RichiesteTesi;
import com.laureapp.ui.roomdb.entity.TesiProfessore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RichiesteTesiRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public RichiesteTesiRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    public void insertRichiesteTesi(RichiesteTesi richiesteTesi){
        executor.execute(() -> roomDbSqlLite.richiesteTesiDao().insert(richiesteTesi));
    }

    public void updateRichiesteTesi(RichiesteTesi richiesteTesi){
        executor.execute(() -> roomDbSqlLite.richiesteTesiDao().update(richiesteTesi));
    }

    public RichiesteTesi findAllById(Long id){
        CompletableFuture<RichiesteTesi> future = new CompletableFuture<>();
        executor.execute(() -> {
            RichiesteTesi richiesteTesi = roomDbSqlLite.richiesteTesiDao().findAllById(id);
            future.complete(richiesteTesi);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new RichiesteTesi();
        }
    }

    public List<RichiesteTesi> getAllRichiesteTesi(){
        CompletableFuture<List<RichiesteTesi>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<RichiesteTesi> lista = roomDbSqlLite.richiesteTesiDao().getAllRichiesteTesi();
            future.complete(lista);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Restituire una lista vuota in caso di errore
        }
    }

    public boolean deleteRichiesteTesi(Long id){
        boolean result = false;
        RichiesteTesi richiesteTesi =  this.findAllById(id);
        if (richiesteTesi.getId_richiesta_tesi() != null) {
            executor.execute(() -> roomDbSqlLite.richiesteTesiDao().delete(richiesteTesi));
            result = true;
        }
        return result;
    }
}
