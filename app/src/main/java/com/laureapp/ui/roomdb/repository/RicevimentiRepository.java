package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Ricevimenti;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Questa classe funge da repository per l'entità Ricevimenti
 * Gestisce tutte le operazioni di accesso al database per l'entità Ricevimenti,
 * inclusa l'inserimento, l'aggiornamento, la ricerca e l'eliminazione dei dati correlati
 */
public class RicevimentiRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Costruttore della classe RicevimentiRepository
     * @param context Il contesto dell'applicazione
     */
    public RicevimentiRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    /**
     * Inserisce un oggetto Ricevimenti nel database
     * @param ricevimenti L'oggetto Ricevimenti da inserire
     */
    public void insertRicevimenti(Ricevimenti ricevimenti){
        executor.execute(() -> roomDbSqlLite.ricevimentiDao().insert(ricevimenti));
    }

    /**
     * Aggiorna un oggetto Ricevimenti nel database
     * @param ricevimenti L'oggetto Ricevimenti da aggiornare
     */
    public void updateRicevimenti(Ricevimenti ricevimenti){
        executor.execute(() -> roomDbSqlLite.ricevimentiDao().update(ricevimenti));
    }

    /**
     * Trova un oggetto Ricevimenti nel database in base all'ID specificato
     * @param id L'ID dell'oggetto Ricevimenti da trovare
     * @return L'oggetto Ricevimenti corrispondente all'ID specificato, se presente, altrimenti un nuovo oggetto FileTesi vuoto
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public Ricevimenti findAllById(Long id){
        CompletableFuture<Ricevimenti> future = new CompletableFuture<>();
        executor.execute(() -> {
            Ricevimenti ricevimenti = roomDbSqlLite.ricevimentiDao().findAllById(id);
            future.complete(ricevimenti);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Ricevimenti();
        }
    }

    /**
     * Ottiene tutti gli oggetti Ricevimenti presenti nel database
     * @return lista di tutti gli oggetti Ricevimenti presenti nel database
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public List<Ricevimenti> getAllRicevimenti(){
        CompletableFuture<List<Ricevimenti>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<Ricevimenti> lista = roomDbSqlLite.ricevimentiDao().getAllRicevimenti();
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
     * Elimina un oggetto Ricevimenti dal database in base all'ID specificato
     * @param id dell'oggetto FileTesi da eliminare
     * @return True se l'eliminazione ha avuto successo, altrimenti False
     */
    public boolean deleteRicevimenti(Long id){
        boolean result = false;
        Ricevimenti ricevimenti =  this.findAllById(id);
        if (ricevimenti.getId_ricevimento() != null) {
            executor.execute(() -> roomDbSqlLite.ricevimentiDao().delete(ricevimenti));
            result = true;
        }
        return result;
    }
}
