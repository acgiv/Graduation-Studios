package com.laureapp.ui.roomdb.repository;
import android.content.Context;
import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Professore;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProfessoreRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public ProfessoreRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    public void insertProfessore(Professore professore){
        executor.execute(() -> roomDbSqlLite.professoreDao().insert(professore));
    }

    public void updateProfessore (Professore professore){
        executor.execute(() -> roomDbSqlLite.professoreDao().update(professore));
    }

    public Professore findAllById(Long uid) {
        CompletableFuture<Professore> future = new CompletableFuture<>();
        executor.execute(() -> {
            Professore professore = roomDbSqlLite.professoreDao().findAllById(Long.valueOf(uid));
            future.complete(professore);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Professore();
        }
    }

    public Professore findAllById(String uid) {
        CompletableFuture<Professore> future = new CompletableFuture<>();
        executor.execute(() -> {
            Professore professore = roomDbSqlLite.professoreDao().findAllById(uid);
            future.complete(professore);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Professore();
        }
    }


    public List<Professore> getAllProfessore(){
        CompletableFuture<List<Professore>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<Professore> lista = roomDbSqlLite.professoreDao().getAllProfessore();
            future.complete(lista);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Restituire una lista vuota in caso di errore
        }
    }

    public boolean delateProfessore(Long id){
        boolean result = false;
        Professore professore =  this.findAllById(id);
        if (professore.getId() != null) {
            executor.execute(() -> roomDbSqlLite.professoreDao().delete(professore));
            result = true;
        }
        return result;
    }

}
