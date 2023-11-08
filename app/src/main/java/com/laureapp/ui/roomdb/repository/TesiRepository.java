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

/**
 * Questa classe funge da repository per l'entità Tesi
 * Gestisce tutte le operazioni di accesso al database per l'entità Tesi,
 * inclusa l'inserimento, l'aggiornamento, la ricerca e l'eliminazione dei dati correlati
 */
public class TesiRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Costruttore per TesiRepository che inizializza il database Room.
     * @param context Il contesto corrente dell'applicazione.
     */
    public TesiRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    /**
     * Inserisce un'istanza di Tesi nel database.
     * @param tesi L'istanza di Tesi da inserire nel database.
     */
    public void insertTesi(Tesi tesi){
        executor.execute(() -> roomDbSqlLite.tesiDao().insert(tesi));
    }

    /**
     * Aggiorna un'istanza esistente di Tesi nel database.
     * @param tesi L'istanza di Tesi da aggiornare nel database.
     */
    public void updateTesi(Tesi tesi){
        executor.execute(() -> roomDbSqlLite.tesiDao().update(tesi));
    }

    /**
     * Trova un'istanza di Tesi nel database tramite l'ID specificato.
     * @param id L'ID dell'istanza di Tesi da trovare.
     * @return L'istanza di Tesi corrispondente all'ID specificato, se presente nel database.
     * @throws InterruptedException Se il thread viene interrotto durante l'operazione.
     * @throws ExecutionException Se si verifica un errore durante l'esecuzione dell'operazione.
     */
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

    /**
     * Ottiene tutti gli elementi di Tesi dal database.
     * @return Una lista di tutte le istanze di Tesi presenti nel database.
     * @throws InterruptedException Se il thread viene interrotto durante l'operazione.
     * @throws ExecutionException Se si verifica un errore durante l'esecuzione dell'operazione.
     */
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

    /**
     * Elimina un'istanza di Tesi dal database tramite l'ID specificato.
     * @param id L'ID dell'istanza di Tesi da eliminare.
     * @return true se l'eliminazione ha avuto successo, false altrimenti.
     */
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
