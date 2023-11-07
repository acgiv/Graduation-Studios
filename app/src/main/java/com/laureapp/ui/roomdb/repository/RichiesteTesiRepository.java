package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.RichiesteTesi;
import com.laureapp.ui.roomdb.entity.TesiProfessore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Questa classe funge da repository per l'entità RichiestaTesi.
 * Gestisce tutte le operazioni di accesso al database per l'entità RichiestaTesi,
 * inclusa l'inserimento, l'aggiornamento, la ricerca e l'eliminazione dei dati correlati
 */
public class RichiesteTesiRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Costruttore della classe RichiestaTesiRepository
     * @param context Il contesto dell'applicazione
     */
    public RichiesteTesiRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    /**
     * Inserisce un oggetto RichiestaTesi nel database
     * @param richiesteTesi L'oggetto RichiestaTesi da inserire
     */
    public void insertRichiesteTesi(RichiesteTesi richiesteTesi){
        executor.execute(() -> roomDbSqlLite.richiesteTesiDao().insert(richiesteTesi));
    }

    /**
     * Aggiorna un oggetto RichiestaTesi nel database
     * @param richiesteTesi L'oggetto FileTesi da aggiornare
     */
    public void updateRichiesteTesi(RichiesteTesi richiesteTesi){
        executor.execute(() -> roomDbSqlLite.richiesteTesiDao().update(richiesteTesi));
    }

    /**
     * Trova un oggetto RichiestaTesi nel database in base all'ID specificato
     * @param id L'ID dell'oggetto RichiestaTesi da trovare
     * @return L'oggetto FileTesi corrispondente all'ID specificato, se presente, altrimenti un nuovo oggetto FileTesi vuoto
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public RichiesteTesi findAllById(Long id){
        CompletableFuture<RichiesteTesi> future = new CompletableFuture<>();
        executor.execute(() -> {
            RichiesteTesi richiesteTesi = roomDbSqlLite.richiesteTesiDao().findAllById(id);
            future.complete(richiesteTesi);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new RichiesteTesi();
        }
    }

    /**
     * Ottiene tutti gli oggetti RichiestaTesi presenti nel database
     * @return lista di tutti gli oggetti RichiestaTesi presenti nel database
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public List<RichiesteTesi> getAllRichiesteTesi(){
        CompletableFuture<List<RichiesteTesi>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<RichiesteTesi> lista = roomDbSqlLite.richiesteTesiDao().getAllRichiesteTesi();
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
     * Elimina un oggetto RichiestaTesi dal database in base all'ID specificato
     * @param id dell'oggetto RichiestaTesi da eliminare
     * @return True se l'eliminazione ha avuto successo, altrimenti False
     */
    public boolean deleteRichiesteTesi(Long id){
        boolean result = false;
        RichiesteTesi richiesteTesi =  this.findAllById(id);
        if (richiesteTesi.getId_richiesta_tesi() != null) {
            executor.execute(() -> roomDbSqlLite.richiesteTesiDao().delete(richiesteTesi));
            result = true;
        }
        return result;
    }
}
