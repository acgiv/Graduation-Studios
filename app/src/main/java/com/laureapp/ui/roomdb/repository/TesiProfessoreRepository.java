package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.TesiProfessore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Questa classe funge da repository per l'entità TesiProfessore.
 * Gestisce tutte le operazioni di accesso al database per l'entità TesiProfessore,
 * inclusa l'inserimento, l'aggiornamento, la ricerca e l'eliminazione dei dati correlati
 */
public class TesiProfessoreRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Costruttore della classe TesiProfessoreRepository
     * @param context Il contesto dell'applicazione
     */
    public TesiProfessoreRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    /**
     * Inserisce un oggetto TesiProfessore nel database
     * @param tesiProfessore L'oggetto TesiProfessore da inserire
     */
    public void insertTesiProfessore(TesiProfessore tesiProfessore){
        executor.execute(() -> roomDbSqlLite.tesiProfessoreDao().insert(tesiProfessore));
    }

    /**
     * Aggiorna un oggetto TesiProfessore nel database
     * @param tesiProfessore L'oggetto TesiProfessore da aggiornare
     */
    public void updateTesiProfessore(TesiProfessore tesiProfessore){
        executor.execute(() -> roomDbSqlLite.tesiProfessoreDao().update(tesiProfessore));
    }

    /**
     * Trova TesiProfessore in base all'ID specificato.
     * @param id l'ID specificato
     * @return l'istanza di TesiProfessore corrispondente all'ID specificato,
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public TesiProfessore findAllById(Long id){
        CompletableFuture<TesiProfessore> future = new CompletableFuture<>();
        executor.execute(() -> {
            TesiProfessore tesiProfessore = roomDbSqlLite.tesiProfessoreDao().findAllById(id);
            future.complete(tesiProfessore);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new TesiProfessore();
        }
    }

    /**
     * Ottiene tutti gli oggetti TesiProfessore dal database.
     * @return una lista di oggetti TesiProfessore, una lista vuota se si verifica un'eccezione
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public List<TesiProfessore> getAllTesiProfessore(){
        CompletableFuture<List<TesiProfessore>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<TesiProfessore> lista = roomDbSqlLite.tesiProfessoreDao().getAllTesiProfessore();
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
     * Elimina un oggetto TesiProfessore dal database in base all'ID specificato
     * @param id dell'oggetto TesiProfessore da eliminare
     * @return True se l'eliminazione ha avuto successo, altrimenti False
     */
    public boolean deleteTesiProfessore(Long id){
        boolean result = false;
        TesiProfessore tesiProfessore =  this.findAllById(id);
        if (tesiProfessore.getId_tesi_professore() != null) {
            executor.execute(() -> roomDbSqlLite.tesiProfessoreDao().delete(tesiProfessore));
            result = true;
        }
        return result;
    }
}
