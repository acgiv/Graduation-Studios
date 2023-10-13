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
    public Long findIdTesiByIdTesi(Long idStudente){
        CompletableFuture<Long> future = new CompletableFuture<>();
        executor.execute(() -> {
            Long id = roomDbSqlLite.studenteTesiDao().findIdTesiByIdTesi(idStudente);
            future.complete(id);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1L;
        }
    }

    // Update the StudenteTesi database with the latest data
    public void updateStudenteTesiDatabase(List<StudenteTesi> latestData) {
        // Fetch the existing data from the Room database
        List<StudenteTesi> existingData = roomDbSqlLite.studenteTesiDao().getAllStudenteTesi();

        // Compare the existing data with the latest data
        for (StudenteTesi latestStudenteTesi : latestData) {
            boolean isFound = false;
            for (StudenteTesi existingStudenteTesi : existingData) {
                if (latestStudenteTesi.getId_studente_tesi() != null &&
                        latestStudenteTesi.getId_studente_tesi().equals(existingStudenteTesi.getId_studente_tesi())) {
                    // The record exists in the database, update it
                    existingStudenteTesi.setId_studente_tesi(latestStudenteTesi.getId_studente_tesi());
                    existingStudenteTesi.setId_tesi(latestStudenteTesi.getId_tesi());
                    existingStudenteTesi.setId_studente(latestStudenteTesi.getId_studente());

                    roomDbSqlLite.studenteTesiDao().update(existingStudenteTesi);
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                // The record doesn't exist in the database, insert it
                roomDbSqlLite.studenteTesiDao().insert(latestStudenteTesi);
            }
        }
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
        if (studenteTesi.getId_studente_tesi() != null) {
            executor.execute(() -> roomDbSqlLite.studenteTesiDao().delete(studenteTesi));
            result = true;
        }
        return result;
    }

    public Long getIdTesiFromIdStudente(Long id_studente){
        CompletableFuture<Long> future = new CompletableFuture<>();
        executor.execute(() -> {
            Long id = roomDbSqlLite.studenteTesiDao().findIdTesiByIdStudente(id_studente);
            future.complete(id);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1L;
        }
    }
}
