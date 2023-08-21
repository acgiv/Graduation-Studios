package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.StudenteTesi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class StudenteTesiRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public StudenteTesiRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    public void insertStudenteTesi(StudenteTesi studenteTesi){
        executor.execute(() -> roomDbSqlLite.studenteTesiDao().insert(studenteTesi));
    }

    public void updateStudenteTesi(StudenteTesi studenteTesi){
        executor.execute(() -> roomDbSqlLite.studenteTesiDao().update(studenteTesi));
    }

    public StudenteTesi findAllById(Long id){
        CompletableFuture<StudenteTesi> future = new CompletableFuture<>();
        executor.execute(() -> {
            StudenteTesi studenteTesi = roomDbSqlLite.studenteTesiDao().findAllById(id);
            future.complete(studenteTesi);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new StudenteTesi();
        }
    }

    public List<StudenteTesi> getAllStudenteTesi(){
        CompletableFuture<List<StudenteTesi>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<StudenteTesi> lista = roomDbSqlLite.studenteTesiDao().getAllStudenteTesi();
            future.complete(lista);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Restituire una lista vuota in caso di errore
        }
    }

    public boolean delateStudenteTesi(Long id){
        boolean result = false;
        StudenteTesi studenteTesi =  this.findAllById(id);
        if (studenteTesi.getId() != null) {
            executor.execute(() -> roomDbSqlLite.studenteTesiDao().delete(studenteTesi));
            result = true;
        }
        return result;
    }
}
