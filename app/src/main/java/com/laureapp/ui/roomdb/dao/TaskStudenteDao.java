package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.TaskStudente;

import java.util.List;

/**
 * Questa interfaccia fornisce metodi per eseguire operazioni CRUD (create, read, update, delete)
 * sull'entità TaskStudente nel database Room
 */
@Dao
public interface TaskStudenteDao {

    /**
     * Inserisce un oggetto TaskStudente nel database.
     * @param taskStudente l'oggetto TaskStudente da inserire nel database.
     */
    @Insert
    void insert(TaskStudente taskStudente);

    /**
     * Aggiorna un oggetto TaskStudente esistente nel database.
     * @param taskStudente l'oggetto TaskStudente da aggiornare nel database.
     */
    @Update
    void update(TaskStudente taskStudente);

    /**
     * Ottiene tutti gli oggetti TaskStudente presenti nel database.
     * @return una lista di tutti gli oggetti TaskStudente presenti nel database.
     */
    @Query("SELECT * FROM Task_studente")
    List<TaskStudente> getAllTaskStudente();

    /**
     * Trova un oggetto TaskStudente nel database in base all'ID specificato.
     * @param idTask l'ID dell'oggetto TaskStudente da cercare nel database.
     * @return l'oggetto TaskStudente corrispondente all'ID specificato.
     */
    @Query("SELECT * FROM Task_studente where id_task = :idTask")
    TaskStudente findAllById(Long idTask);

    /**
     * Elimina un oggetto TaskStudente dal database.
     * @param taskStudente l'oggetto TaskStudente da eliminare dal database.
     */
    @Delete
    void delete(TaskStudente taskStudente);
}
