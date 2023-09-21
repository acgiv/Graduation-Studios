package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.UtenteCdl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UtenteCdlRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public UtenteCdlRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    public void insertUtenteCdl(UtenteCdl utenteCdl){
        executor.execute(() -> roomDbSqlLite.utenteCdlDao().insert(utenteCdl));
    }

    public void updateUtenteCdl(UtenteCdl utenteCdl){
        executor.execute(() -> roomDbSqlLite.utenteCdlDao().update(utenteCdl));
    }

    public UtenteCdl findAllById(Long id){
        CompletableFuture<UtenteCdl> future = new CompletableFuture<>();
        executor.execute(() -> {
            UtenteCdl utenteCdl = roomDbSqlLite.utenteCdlDao().findAllById(id);
            future.complete(utenteCdl);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new UtenteCdl();
        }
    }

    public List<UtenteCdl> getAllUtenteCdl(){
        CompletableFuture<List<UtenteCdl>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<UtenteCdl> lista = roomDbSqlLite.utenteCdlDao().getAllUtenteCdl();
            future.complete(lista);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Restituire una lista vuota in caso di errore
        }
    }

    public boolean deleteUtenteCdl(Long id){
        boolean result = false;
        UtenteCdl utenteCdl =  this.findAllById(id);
        if (utenteCdl.getId_utente_cdl() != null) {
            executor.execute(() -> roomDbSqlLite.utenteCdlDao().delete(utenteCdl));
            result = true;
        }
        return result;
    }
}
