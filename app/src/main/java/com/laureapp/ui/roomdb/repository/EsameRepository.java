package com.laureapp.ui.roomdb.repository;
import android.content.Context;
import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Esame;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EsameRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public EsameRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    public void insertEsame(Esame esame){
        executor.execute(() -> roomDbSqlLite.esameDao().insert(esame));
    }

    public void updateEsame(Esame esame){
        executor.execute(() -> roomDbSqlLite.esameDao().update(esame));
    }

    public Esame findAllById(Long id){
        CompletableFuture<Esame> future = new CompletableFuture<>();
        executor.execute(() -> {
            Esame esame = roomDbSqlLite.esameDao().findAllById(id);
            future.complete(esame);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Esame();
        }
    }

    public List<Esame> getAllEsame(){
        CompletableFuture<List<Esame>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<Esame> lista = roomDbSqlLite.esameDao().getAllEsame();
            future.complete(lista);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Restituire una lista vuota in caso di errore
        }
    }

    public boolean delateEsame(Long id){
        boolean result = false;
        Esame esame =  this.findAllById(id);
        if (esame.getId() != null) {
            executor.execute(() -> roomDbSqlLite.esameDao().delete(esame));
            result = true;
        }
        return result;
    }
}
