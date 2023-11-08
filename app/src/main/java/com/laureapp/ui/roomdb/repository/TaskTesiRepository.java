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

public class TaskTesiRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public TaskTesiRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    public void insertTaskTesi(TaskTesi taskTesi){
        executor.execute(() -> roomDbSqlLite.taskTesiDao().insert(taskTesi));
    }

    public void updateTaskTesi(TaskTesi taskTesi){
        executor.execute(() -> roomDbSqlLite.taskTesiDao().update(taskTesi));
    }

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
