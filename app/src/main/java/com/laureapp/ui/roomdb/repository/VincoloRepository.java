package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Vincolo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Questa classe funge da repository per l'entità Vincolo
 * Gestisce tutte le operazioni di accesso al database per l'entità Vincolo,
 * inclusa l'inserimento, l'aggiornamento, la ricerca e l'eliminazione dei dati correlati
 */
public class VincoloRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Costruttore per VincoloRepository che inizializza il database Room.
     * @param context Il contesto corrente dell'applicazione.
     */
    public VincoloRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    /**
     * Inserisce un'istanza di Vincolo nel repository.
     * @param vincolo L'istanza di Vincolo da inserire nel repository.
     */
    public void insertVincolo(Vincolo vincolo){
        executor.execute(() -> roomDbSqlLite.vincoloDao().insert(vincolo));
    }

    /**
     * Aggiorna un'istanza esistente di Vincolo nel repository.
     * @param vincolo L'istanza di Vincolo da aggiornare nel repository.
     */
    public void updateVincolo(Vincolo vincolo){
        executor.execute(() -> roomDbSqlLite.vincoloDao().update(vincolo));
    }

    /**
     * Trova un'istanza di Vincolo nel database tramite l'ID specificato.
     * @param id L'ID dell'istanza di Vincolo da trovare.
     * @return L'istanza di Vincolo corrispondente all'ID specificato, se presente nel database.
     * @throws InterruptedException Se il thread viene interrotto durante l'operazione.
     * @throws ExecutionException Se si verifica un errore durante l'esecuzione dell'operazione.
     */
    public Vincolo findAllById(Long id){
        CompletableFuture<Vincolo> future = new CompletableFuture<>();
        executor.execute(() -> {
            Vincolo vincolo = roomDbSqlLite.vincoloDao().findAllById(id);
            future.complete(vincolo);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Vincolo();
        }
    }

    /**
     * Ottiene tutti gli elementi di Vincolo dal database.
     * @return Una lista di tutte le istanze di Vincolo presenti nel database.
     * @throws InterruptedException Se il thread viene interrotto durante l'operazione.
     * @throws ExecutionException Se si verifica un errore durante l'esecuzione dell'operazione.
     */
    public List<Vincolo> getAllVincolo(){
        CompletableFuture<List<Vincolo>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<Vincolo> lista = roomDbSqlLite.vincoloDao().getAllVincolo();
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
     * Elimina un'istanza di Vincolo dal database tramite l'ID specificato.
     * @param id L'ID dell'istanza di Vincolo da eliminare.
     * @return true se l'eliminazione ha avuto successo, false altrimenti.
     */
    public boolean deleteVincolo(Long id){
        boolean result = false;
        Vincolo vincolo =  this.findAllById(id);
        if (vincolo.getId_vincolo() != null) {
            executor.execute(() -> roomDbSqlLite.vincoloDao().delete(vincolo));
            result = true;
        }
        return result;
    }
}
