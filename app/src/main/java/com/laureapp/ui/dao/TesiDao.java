package com.laureapp.ui.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.laureapp.ui.entity.Tesi;

import java.util.List;

@Dao
public interface TesiDao {

    @Insert
    void insert(Tesi tesi);

    @Update
    void update(Tesi tesi);

    @Query("SELECT * FROM Tesi")
    List<Tesi> getAllTesi();

    @Query("SELECT * FROM Tesi where id = :id")
    Tesi findAllById(Long id);

    @Delete
    void delete(Tesi tesi);
}
