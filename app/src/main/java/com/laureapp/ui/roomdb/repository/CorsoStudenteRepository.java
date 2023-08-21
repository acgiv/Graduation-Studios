package com.laureapp.ui.roomdb.repository;
import android.content.Context;
import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.CorsoStudente;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class CorsoStudenteRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public CorsoStudenteRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    public void insertCorsoStudente(CorsoStudente corsoStudente){
        executor.execute(() -> roomDbSqlLite.corsoStudenteDao().insert(corsoStudente));
    }

    public void updateCorsoStudente(CorsoStudente corsoStudente){
        executor.execute(() -> roomDbSqlLite.corsoStudenteDao().update(corsoStudente));
    }

    public CorsoStudente findAllById(Long id){
        CompletableFuture<CorsoStudente> future = new CompletableFuture<>();
        executor.execute(() -> {
            CorsoStudente corsoStudente = roomDbSqlLite.corsoStudenteDao().findAllById(id);
            future.complete(corsoStudente);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new CorsoStudente();
        }
    }

    public List<CorsoStudente> getAllCorsoStudente(){
        CompletableFuture<List<CorsoStudente>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<CorsoStudente> lista = roomDbSqlLite.corsoStudenteDao().getAllCorsoStudente();
            future.complete(lista);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Restituire una lista vuota in caso di errore
        }
    }

    public boolean delateCorsoStudente(Long id){
        boolean result = false;
        CorsoStudente corsoStudente =  this.findAllById(id);
        if (corsoStudente.getId() != null) {
            executor.execute(() -> roomDbSqlLite.corsoStudenteDao().delete(corsoStudente));
            result = true;
        }
        return result;
    }
}
