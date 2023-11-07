package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.TaskTesi;

import java.util.List;

/**
 * Questa interfaccia fornisce metodi per eseguire operazioni CRUD (create, read, update, delete)
 * sull'entità TaskTesi nel database Room
 */
@Dao
public interface TaskTesiDao {

    /**
     * Inserisce un oggetto TaskTesi nel database.
     * @param taskTesi l'oggetto TaskTesi da inserire nel database.
     */
    @Insert
    void insert(TaskTesi taskTesi);

    /**
     * Aggiorna un oggetto TaskTesi esistente nel database.
     * @param taskTesi l'oggetto TaskTesi da aggiornare nel database.
     */
    @Update
    void update(TaskTesi taskTesi);

    /**
     * Ottiene tutti gli oggetti TaskTesi presenti nel database.
     * @return una lista di tutti gli oggetti TaskTesi presenti nel database.
     */
    @Query("SELECT * FROM Task_tesi")
    List<TaskTesi> getAllTaskTesi();

    /**
     * Trova un oggetto TaskTesi nel database in base all'ID specificato.
     * @param idTask l'ID dell'oggetto TaskTesi da cercare nel database.
     * @return l'oggetto TaskTesi corrispondente all'ID specificato.
     */
    @Query("SELECT * FROM Task_tesi where id_task = :idTask")
    TaskTesi findAllById(Long idTask);

    /**
     * Elimina un oggetto TaskTesi dal database.
     * @param taskTesi l'oggetto TaskTesi da eliminare dal database.
     */
    @Delete
    void delete(TaskTesi taskTesi);
}
