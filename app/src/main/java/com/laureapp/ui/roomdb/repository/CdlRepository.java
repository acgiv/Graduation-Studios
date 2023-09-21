package com.laureapp.ui.roomdb.repository;
import android.content.Context;
import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Cdl;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class CdlRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public CdlRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    public void insertCdl(Cdl cdl){
        executor.execute(() -> roomDbSqlLite.cdlDao().insert(cdl));
    }

    public void updateCdl(Cdl cdl){
        executor.execute(() -> roomDbSqlLite.cdlDao().update(cdl));
    }

    public Cdl findAllById(Long id){
        CompletableFuture<Cdl> future = new CompletableFuture<>();
        executor.execute(() -> {
            Cdl cdl = roomDbSqlLite.cdlDao().findAllById(id);
            future.complete(cdl);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Cdl();
        }
    }

    public List<Cdl> getAllCdl(){
        CompletableFuture<List<Cdl>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<Cdl> lista = roomDbSqlLite.cdlDao().getAllCdl();
            future.complete(lista);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Restituire una lista vuota in caso di errore
        }
    }

    public boolean deleteCdl(Long id){
        boolean result = false;
        Cdl cdl =  this.findAllById(id);
        if (cdl.getId_cdl() != null) {
            executor.execute(() -> roomDbSqlLite.cdlDao().delete(cdl));
            result = true;
        }
        return result;
    }
}
