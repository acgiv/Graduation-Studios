package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.TaskTesi;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Questa classe funge da repository per l'entità TaskTesi
 * Gestisce tutte le operazioni di accesso al database per l'entità TaskTesi,
 * inclusa l'inserimento, l'aggiornamento, la ricerca e l'eliminazione dei dati correlati
 */
public class TaskTesiRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Costruttore per TaskTesiRepository che inizializza il database Room.
     * @param context Il contesto corrente dell'applicazione.
     */
    public TaskTesiRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    /**
     * Inserisce un'istanza di TaskTesi nel database.
     * @param taskTesi L'istanza di TaskTesi da inserire nel database.
     */
    public void insertTaskTesi(TaskTesi taskTesi){
        executor.execute(() -> roomDbSqlLite.taskTesiDao().insert(taskTesi));
    }

    /**
     * Aggiorna un'istanza esistente di TaskTesi nel database.
     * @param taskTesi L'istanza di TaskTesi da aggiornare nel database.
     */
    public void updateTaskTesi(TaskTesi taskTesi){
        executor.execute(() -> roomDbSqlLite.taskTesiDao().update(taskTesi));
    }

    /**
     * Trova un'istanza di TaskTesi nel database tramite l'ID specificato.
     * @param id L'ID dell'istanza di TaskTesi da trovare.
     * @return L'istanza di TaskTesi corrispondente all'ID specificato, se presente nel database.
     * @throws InterruptedException Se il thread viene interrotto durante l'operazione.
     * @throws ExecutionException Se si verifica un errore durante l'esecuzione dell'operazione.
     */
    public TaskTesi findAllById(Long id){
        CompletableFuture<TaskTesi> future = new CompletableFuture<>();
        executor.execute(() -> {
            TaskTesi taskTesi = roomDbSqlLite.taskTesiDao().findAllById(id);
            future.complete(taskTesi);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new TaskTesi();
        }
    }

    /**
     * Ottiene tutti gli elementi di TaskTesi dal database.
     * @return Una lista di tutte le istanze di TaskTesi presenti nel database.
     * @throws InterruptedException Se il thread viene interrotto durante l'operazione.
     * @throws ExecutionException Se si verifica un errore durante l'esecuzione dell'operazione.
     */
    public List<TaskTesi> getAllTaskTesi(){
        CompletableFuture<List<TaskTesi>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<TaskTesi> lista = roomDbSqlLite.taskTesiDao().getAllTaskTesi();
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
     * Elimina un'istanza di TaskTesi dal database tramite l'ID specificato.
     * @param id L'ID dell'istanza di TaskTesi da eliminare.
     * @return true se l'eliminazione ha avuto successo, false altrimenti.
     */
    public boolean deleteTaskTesi(Long id){
        boolean result = false;
        TaskTesi taskTesi =  this.findAllById(id);
        if (taskTesi.getId_task() != null) {
            executor.execute(() -> roomDbSqlLite.taskTesiDao().delete(taskTesi));
            result = true;
        }
        return result;
    }
}
