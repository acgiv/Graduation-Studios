package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.Esame;

import java.util.List;
@Dao
public interface EsameDao {
    @Insert
    void insert(Esame esame);

    @Update
    void update(Esame esame);

    @Query("SELECT * FROM esame")
    List<Esame> getAllEsame();

    @Query("SELECT * FROM esame where id = :id")
    Esame findAllById(Long id);

    @Delete
    void delete(Esame esame);

}
