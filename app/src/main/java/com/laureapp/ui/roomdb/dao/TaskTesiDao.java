package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.Cdl;
import com.laureapp.ui.roomdb.entity.TaskTesi;

import java.util.List;

@Dao
public interface TaskTesiDao {

    @Insert
    void insert(TaskTesi taskTesi);

    @Update
    void update(TaskTesi taskTesi);

    @Query("SELECT * FROM Task_tesi")
    List<TaskTesi> getAllTaskTesi();

    @Query("SELECT * FROM Task_tesi where id_task = :idTask")
    TaskTesi findAllById(Long idTask);

    @Delete
    void delete(TaskTesi taskTesi);
}
