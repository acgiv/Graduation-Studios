package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.FileTesi;
import com.laureapp.ui.roomdb.dao.FileTesiDao;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FileTesiRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public FileTesiRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    public void insertFileTesi(FileTesi fileTesi){
        executor.execute(() -> roomDbSqlLite.fileTesiDao().insert(fileTesi));
    }

    public void updateRichiesteTesi(FileTesi fileTesi){
        executor.execute(() -> roomDbSqlLite.fileTesiDao().update(fileTesi));
    }

    public FileTesi findAllById(Long id){
        CompletableFuture<FileTesi> future = new CompletableFuture<>();
        executor.execute(() -> {
            FileTesi fileTesi = roomDbSqlLite.fileTesiDao().findAllById(id);
            future.complete(fileTesi);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new FileTesi();
        }
    }

    public List<FileTesi> getAllFileTesi(){
        CompletableFuture<List<FileTesi>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<FileTesi> lista = roomDbSqlLite.fileTesiDao().getAllFileTesi();
            future.complete(lista);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Restituire una lista vuota in caso di errore
        }
    }

    public boolean deleteFileTesi(Long id){
        boolean result = false;
        FileTesi fileTesi =  this.findAllById(id);
        if (fileTesi.getId_file() != null) {
            executor.execute(() -> roomDbSqlLite.fileTesiDao().delete(fileTesi));
            result = true;
        }
        return result;
    }

}
