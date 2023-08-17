package com.laureapp.ui.roomdb.repository;
import android.content.Context;
import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Utente;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class UtenteRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();


    public UtenteRepository(Context context){
        roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }


    public void insertUtente(Utente utente){
        executor.execute(() -> roomDbSqlLite.utenteDao().insert(utente));
    }


    public void updateUtente(Utente utente){
        executor.execute(() -> roomDbSqlLite.utenteDao().update(utente));
    }


    public Utente findAllById(Long id){
        CompletableFuture<Utente> future = new CompletableFuture<>();
        executor.execute(() -> {
            Utente utente = roomDbSqlLite.utenteDao().findAllById(id);
            future.complete(utente);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Utente();
        }
    }


    public List<Utente> getAllUtente(){
        CompletableFuture<List<Utente>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<Utente> lista = roomDbSqlLite.utenteDao().getAllUtente();
            future.complete(lista);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public boolean delateUtente(Long id){
        boolean result = false;
        Utente utente =  this.findAllById(id);
        if (utente.getId() != null) {
            executor.execute(() -> roomDbSqlLite.utenteDao().delete(utente));
            result = true;
        }
        return result;
    }
}
