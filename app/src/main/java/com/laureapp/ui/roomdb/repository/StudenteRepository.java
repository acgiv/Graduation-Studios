package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteWithUtente;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Questa classe funge da repository per l'entità Studente.
 * Gestisce tutte le operazioni di accesso al database per l'entità Studente,
 * incluso l'inserimento, l'aggiornamento, la ricerca e l'eliminazione dei dati correlati
 */
public class StudenteRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Costruttore della classe StudenteRepository
     * @param context Il contesto dell'applicazione
     */
    public StudenteRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    /**
     * Inserisce un oggetto Studente nel database
     * @param studente L'oggetto Profossore da inserire
     */
    public void insertStudente(Studente studente){
        executor.execute(() -> roomDbSqlLite.studenteDao().insert(studente));
    }

    /**
     * Aggiorna un oggetto Studente nel database
     * @param studente L'oggetto Studente da aggiornare
     */
    public void updateStudente(Studente studente){
        executor.execute(() -> roomDbSqlLite.studenteDao().update(studente));
    }

    /**
     * Trova un oggetto Studente nel database in base all'ID utente
     * @param id_utente L'ID dell'oggetto Studente da trovare
     * @return L'oggetto Studente corrispondente all'ID specificato, se presente, altrimenti un nuovo oggetto Professore vuoto
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public Long findStudente(Long id_utente){
        CompletableFuture<Long> future = new CompletableFuture<>();
        executor.execute(() -> {
            Long id = roomDbSqlLite.studenteDao().findStudente(id_utente);
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
     * Trova un oggetto Studente nel database in base alla matricola specificato
     * @param matricola La matricola dell'oggetto Studente da trovare
     * @return L'oggetto Studente corrispondente alla matricola, se presente, altrimenti un nuovo oggetto Professore vuoto
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public Long findStudenteMatricola(Long matricola){
        CompletableFuture<Long> future = new CompletableFuture<>();
        executor.execute(() -> {
            Long id = roomDbSqlLite.studenteDao().findStudenteMatricola(matricola);
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
     * Recupera la matricola dello studente in base all'ID studente specificato nel database.
     * @param id_studente L'ID dello studente
     * @return La matricola dello studente associato all'ID specificato, -1L in caso di errore
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public Long getMatricola(Long id_studente){
        CompletableFuture<Long> future = new CompletableFuture<>();
        executor.execute(() -> {
            Long matricola = roomDbSqlLite.studenteDao().getMatricola(id_studente);
            future.complete(matricola);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1L;
        }
    }

    /**
     * Trova un oggetto Studente nel database in base all'ID specificato
     * @param id L'ID dell'oggetto Studente da trovare
     * @return L'oggetto Studente corrispondente all'ID specificato, se presente, altrimenti un nuovo oggetto Professore vuoto
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public Studente findAllById(Long id){
        CompletableFuture<Studente> future = new CompletableFuture<>();
        executor.execute(() -> {
            Studente studente = roomDbSqlLite.studenteDao().findAllById(id);
            future.complete(studente);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Studente();
        }
    }

    /**
     * Ottiene tutti gli oggetti Studente presenti nel database
     * @return lista di tutti gli oggetti Studente presenti nel database
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public List<Studente> getAllStudente(){
        CompletableFuture<List<Studente>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<Studente> lista = roomDbSqlLite.studenteDao().getAllStudente();
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
     * Elimina un oggetto Studente dal database in base all'ID specificato
     * @param id dell'oggetto Studente da eliminare
     * @return True se l'eliminazione ha avuto successo, altrimenti False
     */
    public boolean deleteStudente(Long id){
        boolean result = false;
        Studente studente =  this.findAllById(id);
        if (studente.getId_studente() != null) {
            executor.execute(() -> roomDbSqlLite.studenteDao().delete(studente));
            result = true;
        }
        return result;
    }

    /**
     * Recupera l'ID dello studente in base all'ID dell'utente associato nel database.
     * @return Una lista di oggetti StudenteWithUtente che rappresentano gli studenti con gli utenti associati nel database
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public List<StudenteWithUtente> findStudenteIdByUtenteId() {
        CompletableFuture<List<StudenteWithUtente>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<StudenteWithUtente> lista = roomDbSqlLite.studenteDao().findStudentiWithUtenti();
            future.complete(lista);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Restituire una lista vuota in caso di errore
        }
    }

}
