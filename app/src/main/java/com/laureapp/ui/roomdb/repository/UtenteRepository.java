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

/**
 * Questa classe funge da repository per l'entità Utente
 * Gestisce tutte le operazioni di accesso al database per l'entità Utente,
 * inclusa l'inserimento, l'aggiornamento, la ricerca e l'eliminazione dei dati correlati
 */
public class UtenteRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();


    /**
     * Costruttore per la classe UtenteRepository che inizializza il database.
     * @param context Il contesto corrente dell'applicazione.
     */
    public UtenteRepository(Context context){
        roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }


    /**
     * Inserisce un oggetto Utente nel database.
     * @param utente l'oggetto Utente da inserire nel database.
     */
    public void insertUtente(Utente utente){
        executor.execute(() -> roomDbSqlLite.utenteDao().insert(utente));
    }


    /**
     * Aggiorna un oggetto Utente esistente nel database.
     * @param utente l'oggetto Utente da aggiornare nel database.
     */
    public void updateUtente(Utente utente){
        executor.execute(() -> roomDbSqlLite.utenteDao().update(utente));
    }


    /**
     * Ottiene l'oggetto Utente dal database in base all'ID specificato.
     * @param id l'ID dell'utente da cercare nel database.
     * @return l'oggetto Utente corrispondente all'ID specificato.
     * @throws InterruptedException se l'operazione viene interrotta.
     * @throws ExecutionException se l'esecuzione fallisce.
     */
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


    public Long getIdUtente(String email){
        CompletableFuture<Long> future = new CompletableFuture<>();
        executor.execute(() -> {
            Long id = roomDbSqlLite.utenteDao().getIdUtente(email);
            future.complete(id);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1L;
        }
    }

    public String getNome(){
        CompletableFuture<String> future = new CompletableFuture<>();
        executor.execute(() -> {
            String nome = roomDbSqlLite.utenteDao().getNome();
            future.complete(nome);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "-1L";
        }
    }

    public String getCognome(){
        CompletableFuture<String> future = new CompletableFuture<>();
        executor.execute(() -> {
            String cognome = roomDbSqlLite.utenteDao().getCognome();
            future.complete(cognome);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "-1L";
        }
    }

    public String getEmail(Long id_utente){
        CompletableFuture<String> future = new CompletableFuture<>();
        executor.execute(() -> {
            String email = roomDbSqlLite.utenteDao().getEmail(id_utente);
            future.complete(email);
        });
        try {

            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "-1L";
        }
    }

    public String getFacolta(Long id_utente){
        CompletableFuture<String> future = new CompletableFuture<>();
        executor.execute(() -> {
            String facolta = roomDbSqlLite.utenteDao().getFacolta(id_utente);
            future.complete(facolta);
        });
        try {

            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "-1L";
        }
    }

    public String getNomeCdl(Long id_utente){
        CompletableFuture<String> future = new CompletableFuture<>();
        executor.execute(() -> {
            String nome_cdl = roomDbSqlLite.utenteDao().getNomeCdl(id_utente);
            future.complete(nome_cdl);
        });
        try {

            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "-1L";
        }
    }

    /**
     * Verifica l'esistenza di un utente nel database in base all'email e alla password specificate.
     * @param email l'email dell'utente da verificare.
     * @param password la password dell'utente da verificare.
     * @return l'oggetto Utente corrispondente all'email e alla password specificate se esiste, altrimenti un nuovo oggetto Utente.
     * @throws InterruptedException se l'operazione viene interrotta.
     * @throws ExecutionException se l'esecuzione fallisce.
     */
    public Utente is_exist_email_password(String email, String password){
        CompletableFuture<Utente> future = new CompletableFuture<>();
        executor.execute(() -> {
            Utente utente = roomDbSqlLite.utenteDao().is_exist_email_password(email, password);
            future.complete(utente);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Utente();
        }
    }

    /**
     * Ottiene tutti gli oggetti Utente presenti nel database.
     * @return una lista di tutti gli oggetti Utente presenti nel database.
     * @throws InterruptedException se l'operazione viene interrotta.
     * @throws ExecutionException se l'esecuzione fallisce.
     */
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

    /**
     * Elimina un oggetto Utente dal database in base all'ID specificato.
     * @param id l'ID dell'utente da eliminare dal database.
     * @return true se l'eliminazione ha successo, altrimenti false.
     */
    public boolean deleteUtente(Long id){
        boolean result = false;
        Utente utente =  this.findAllById(id);
        if (utente.getId_utente() != null) {
            executor.execute(() -> roomDbSqlLite.utenteDao().delete(utente));
            result = true;
        }
        return result;
    }
}
