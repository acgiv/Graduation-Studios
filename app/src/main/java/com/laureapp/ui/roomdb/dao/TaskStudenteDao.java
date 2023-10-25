package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.TaskStudente;

import java.util.List;

@Dao
public interface TaskStudenteDao {

    @Insert
    void insert(TaskStudente taskStudente);

    @Update
    void update(TaskStudente taskStudente);

    @Query("SELECT * FROM Task_studente")
    List<TaskStudente> getAllTaskStudente();

    @Query("SELECT * FROM Task_studente where id_task = :idTask")
    TaskStudente findAllById(Long idTask);

    @Delete
    void delete(TaskStudente taskStudente);
}
