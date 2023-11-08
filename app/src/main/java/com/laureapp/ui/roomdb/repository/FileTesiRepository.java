package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.FileTesi;
import com.laureapp.ui.roomdb.dao.FileTesiDao;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Questa classe funge da repository per l'entità FileTesi.
 * Gestisce tutte le operazioni di accesso al database per l'entità FileTesi,
 * inclusa l'inserimento, l'aggiornamento, la ricerca e l'eliminazione dei dati correlati
 */
public class FileTesiRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Costruttore della classe FileTesiRepository
     * @param context Il contesto dell'applicazione
     */
    public FileTesiRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    /**
     * Inserisce un oggetto StudenteTesi nel database
     * @param fileTesi L'oggetto StudenteeTesi da inserire
     */
    public void insertFileTesi(FileTesi fileTesi){
        executor.execute(() -> roomDbSqlLite.fileTesiDao().insert(fileTesi));
    }

    /**
     * Aggiorna un oggetto FileTesi nel database
     * @param fileTesi L'oggetto FileTesi da aggiornare
     */
    public void updateRichiesteTesi(FileTesi fileTesi){
        executor.execute(() -> roomDbSqlLite.fileTesiDao().update(fileTesi));
    }

    /**
     * Trova un oggetto FileTesi nel database in base all'ID specificato
     * @param id L'ID dell'oggetto FileTesi da trovare
     * @return L'oggetto FileTesi corrispondente all'ID specificato, se presente, altrimenti un nuovo oggetto FileTesi vuoto
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public FileTesi findAllById(Long id){
        CompletableFuture<FileTesi> future = new CompletableFuture<>();
        executor.execute(() -> {
            FileTesi fileTesi = (FileTesi) roomDbSqlLite.fileTesiDao().getAllFileTesi();
            future.complete(fileTesi);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new FileTesi();
        }
    }

    /**
     * Ottiene tutti gli oggetti FileTesi presenti nel database
     * @return lista di tutti gli oggetti FileTesi presenti nel database
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public List<FileTesi> getAllFileTesi(){
        CompletableFuture<List<FileTesi>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<FileTesi> lista = roomDbSqlLite.fileTesiDao().getAllFileTesi();
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
     * Elimina un oggetto FileTesi dal database in base all'ID specificato
     * @param id dell'oggetto FileTesi da eliminare
     * @return True se l'eliminazione ha avuto successo, altrimenti False
     */
    public boolean deleteFileTesi(Long id){
        boolean result = false;
        FileTesi fileTesi =  this.findAllById(id);
        if (fileTesi.getId_file() != null) {
            executor.execute(() -> roomDbSqlLite.fileTesiDao().delete(fileTesi));
            result = true;
        }
        return result;
    }

}
