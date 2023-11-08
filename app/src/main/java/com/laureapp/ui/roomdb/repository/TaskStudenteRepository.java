package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.TaskStudente;
import com.laureapp.ui.roomdb.entity.TaskTesi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Questa classe funge da repository per l'entità TaskStudente
 * Gestisce tutte le operazioni di accesso al database per l'entità TaskStudente,
 * inclusa l'inserimento, l'aggiornamento, la ricerca e l'eliminazione dei dati correlati
 */
public class TaskStudenteRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Costruttore per TaskStudenteRepository che inizializza il database Room.
     * @param context Il contesto corrente dell'applicazione.
     */
    public TaskStudenteRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    /**
     * Inserisce un'istanza di TaskStudente nel database.
     * @param taskStudente L'istanza di TaskStudente da inserire nel database.
     */
    public void insertTaskStudente(TaskStudente taskStudente){
        executor.execute(() -> roomDbSqlLite.taskStudenteDao().insert(taskStudente));
    }

    /**
     * Aggiorna un'istanza esistente di TaskStudente nel database.
     * @param taskStudente L'istanza di TaskStudente da aggiornare nel database.
     */
    public void updateTaskStudente(TaskStudente taskStudente){
        executor.execute(() -> roomDbSqlLite.taskStudenteDao().update(taskStudente));
    }

    /**
     * Trova un'istanza di TaskStudente nel database tramite l'ID specificato.
     * @param id L'ID dell'istanza di TaskStudente da trovare.
     * @return L'istanza di TaskStudente corrispondente all'ID specificato, se presente nel database.
     * @throws InterruptedException Se il thread viene interrotto durante l'operazione.
     * @throws ExecutionException Se si verifica un errore durante l'esecuzione dell'operazione.
     */
    public TaskStudente findAllById(Long id){
        CompletableFuture<TaskStudente> future = new CompletableFuture<>();
        executor.execute(() -> {
            TaskStudente taskStudente = roomDbSqlLite.taskStudenteDao().findAllById(id);
            future.complete(taskStudente);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new TaskStudente();
        }
    }

    /**
     * Ottiene tutti gli elementi di TaskStudente dal database.
     * @return Una lista di tutte le istanze di TaskStudente presenti nel database.
     * @throws InterruptedException Se il thread viene interrotto durante l'operazione.
     * @throws ExecutionException Se si verifica un errore durante l'esecuzione dell'operazione.
     */
    public List<TaskStudente> getAllTaskStudente(){
        CompletableFuture<List<TaskStudente>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<TaskStudente> lista = roomDbSqlLite.taskStudenteDao().getAllTaskStudente();
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
     * Elimina un'istanza di TaskStudente dal database tramite l'ID specificato.
     * @param id L'ID dell'istanza di TaskStudente da eliminare.
     * @return true se l'eliminazione ha avuto successo, false altrimenti.
     */
    public boolean deleteTaskStudente(Long id){
        boolean result = false;
        TaskStudente taskStudente =  this.findAllById(id);
        if (taskStudente.getId_task() != null) {
            executor.execute(() -> roomDbSqlLite.taskStudenteDao().delete(taskStudente));
            result = true;
        }
        return result;
    }
}
