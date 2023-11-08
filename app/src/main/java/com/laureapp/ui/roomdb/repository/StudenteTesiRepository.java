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

/**
 * Questa classe funge da repository per l'entità StudenteTesi.
 * Gestisce tutte le operazioni di accesso al database per l'entità StudenteTesi,
 * inclusa l'inserimento, l'aggiornamento, la ricerca e l'eliminazione dei dati correlati
 */
public class StudenteTesiRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Costruttore della classe StudenteTesiRepository
     * @param context Il contesto dell'applicazione
     */
    public StudenteTesiRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    /**
     * Inserisce un oggetto StudenteTesi nel database
     * @param studenteTesi L'oggetto StudenteTesi da inserire
     */
    public void insertStudenteTesi(StudenteTesi studenteTesi){
        executor.execute(() -> roomDbSqlLite.studenteTesiDao().insert(studenteTesi));
    }

    /**
     * Aggiorna un oggetto StudenteTesi nel database
     * @param studenteTesi L'oggetto StudenteTesi da aggiornare
     */
    public void updateStudenteTesi(StudenteTesi studenteTesi){
        executor.execute(() -> roomDbSqlLite.studenteTesiDao().update(studenteTesi));
    }

    /**
     * Ottiene l'ID della tesi dal database in base all'ID dello studente associato.
     * @param idStudente l'ID dello studente
     * @return l'ID della tesi
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
    public Long findIdTesiByIdStudente(Long idStudente){
        CompletableFuture<Long> future = new CompletableFuture<>();
        executor.execute(() -> {
            Long id = roomDbSqlLite.studenteTesiDao().findIdTesiByIdStudente(idStudente);
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
     * Aggiorna il database StudenteTesi con i dati più recenti.
     * @param latestData la lista dei dati più recenti
     */
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

    /**
     * Trova StudenteTesi in base all'ID specificato.
     * @param id l'ID specificato
     * @return l'istanza di StudenteTesi corrispondente all'ID specificato,
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     *
     */
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

    /**
     * Ottiene tutti gli oggetti StudenteTesi dal database.
     * @return una lista di oggetti StudenteTesi, una lista vuota se si verifica un'eccezione
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
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

    /**
     * Elimina un oggetto StudenteTesi dal database in base all'ID specificato
     * @param id dell'oggetto StudenteTesi da eliminare
     * @return True se l'eliminazione ha avuto successo, altrimenti False
     */
    public boolean delateStudenteTesi(Long id){
        boolean result = false;
        StudenteTesi studenteTesi =  this.findAllById(id);
        if (studenteTesi.getId_studente_tesi() != null) {
            executor.execute(() -> roomDbSqlLite.studenteTesiDao().delete(studenteTesi));
            result = true;
        }
        return result;
    }

    /**
     * Ottiene l'ID della tesi dal database in base all'ID dello studente associato.
     * @param id_studente l'ID dello studente
     * @return l'ID della tesi
     * @throws InterruptedException Se si verifica un'interruzione durante l'esecuzione
     * @throws ExecutionException Se si verifica un'eccezione durante l'esecuzione
     */
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
