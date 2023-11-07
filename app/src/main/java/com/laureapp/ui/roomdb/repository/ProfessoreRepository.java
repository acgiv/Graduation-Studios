package com.laureapp.ui.roomdb.repository;
import android.content.Context;
import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Professore;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Questa classe funge da repository per l'entità Professore.
 * Gestisce tutte le operazioni di accesso al database per l'entità Professore,
 * incluso l'inserimento, l'aggiornamento, la ricerca e l'eliminazione dei dati correlati
 */
public class ProfessoreRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Costruttore della classe ProfessoreRepository
     * @param context Il contesto dell'applicazione
     */
    public ProfessoreRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    /**
     * Inserisce un oggetto Professore nel database
     * @param professore  L'oggetto Profossore da inserire
     */
    public void insertProfessore(Professore professore){
        executor.execute(() -> roomDbSqlLite.professoreDao().insert(professore));
    }

    /**
     * Aggiorna un oggetto Professore nel database
     * @param professore L'oggetto Professore da aggiornare
     */
    public void updateProfessore (Professore professore){
        executor.execute(() -> roomDbSqlLite.professoreDao().update(professore));
    }

    /**
     * Trova un oggetto Professore nel database in base all'ID specificato
     * @param uid L'ID dell'oggetto Professore da trovare
     * @return L'oggetto Professore corrispondente all'ID specificato, se presente, altrimenti un nuovo oggetto Professore vuoto
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public Professore findAllById(Long uid) {
        CompletableFuture<Professore> future = new CompletableFuture<>();
        executor.execute(() -> {
            Professore professore = roomDbSqlLite.professoreDao().findAllById(Long.valueOf(uid));
            future.complete(professore);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Professore();
        }
    }

    /**
     * Trova un oggetto Professore nel database in base alla matricola specificato
     * @param matricola La matricola dell'oggetto Professore da trovare
     * @return L'oggetto Professore corrispondente alla matricola, se presente, altrimenti un nuovo oggetto Professore vuoto
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public Long findProfessoreMatricola(Long matricola){
        CompletableFuture<Long> future = new CompletableFuture<>();
        executor.execute(() -> {
            Long id = roomDbSqlLite.professoreDao().findProfessoreMatricola(matricola);
            future.complete(id);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1L;
        }
    }

    /**
     * Trova un oggetto Professore nel database in base all'ID utente
     * @param id_utente L'ID dell'oggetto Professore da trovare
     * @return L'oggetto Professore corrispondente all'ID specificato, se presente, altrimenti un nuovo oggetto Professore vuoto
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public Long findProfessore(Long id_utente){
        CompletableFuture<Long> future = new CompletableFuture<>();
        executor.execute(() -> {
            Long id = roomDbSqlLite.professoreDao().findPorfessore(id_utente);
            future.complete(id);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1L;
        }
    }

    /**
     * Trova un oggetto Professore nel database in base all'ID specificato
     * @param uid L'ID dell'oggetto Professore da trovare
     * @return L'oggetto Professore corrispondente all'ID specificato, se presente, altrimenti un nuovo oggetto Professore vuoto
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public Professore findAllById(String uid) {
        CompletableFuture<Professore> future = new CompletableFuture<>();
        executor.execute(() -> {
            Professore professore = roomDbSqlLite.professoreDao().findAllById(uid);
            future.complete(professore);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Professore();
        }
    }

    /**
     * Ottiene tutti gli oggetti Professore presenti nel database
     * @return lista di tutti gli oggetti Professore presenti nel database
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public List<Professore> getAllProfessore(){
        CompletableFuture<List<Professore>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<Professore> lista = roomDbSqlLite.professoreDao().getAllProfessore();
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
     * Elimina un oggetto Professore dal database in base all'ID specificato
     * @param id dell'oggetto Professore da eliminare
     * @return True se l'eliminazione ha avuto successo, altrimenti False
     */
    public boolean deleteProfessore(Long id){
        boolean result = false;
        Professore professore =  this.findAllById(id);
        if (professore.getId_professore() != null) {
            executor.execute(() -> roomDbSqlLite.professoreDao().delete(professore));
            result = true;
        }
        return result;
    }

}
