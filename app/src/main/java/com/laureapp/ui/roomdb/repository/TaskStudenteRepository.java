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

public class TaskStudenteRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public TaskStudenteRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    public void insertTaskStudente(TaskStudente taskStudente){
        executor.execute(() -> roomDbSqlLite.taskStudenteDao().insert(taskStudente));
    }

    public void updateTaskStudente(TaskStudente taskStudente){
        executor.execute(() -> roomDbSqlLite.taskStudenteDao().update(taskStudente));
    }

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
