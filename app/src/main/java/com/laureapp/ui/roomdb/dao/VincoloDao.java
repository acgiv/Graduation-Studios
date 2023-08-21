package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.laureapp.ui.roomdb.entity.Vincolo;

import java.util.List;

@Dao
public interface VincoloDao {
    @Insert
    void insert(Vincolo vincolo);

    @Update
    void update(Vincolo vincolo);

    @Query("SELECT * FROM Vincolo")
    List<Vincolo> getAllVincolo();

    @Query("SELECT * FROM Vincolo where id = :id")
    Vincolo findAllById(Long id);

    @Delete
    void delete(Vincolo vincolo);
}
