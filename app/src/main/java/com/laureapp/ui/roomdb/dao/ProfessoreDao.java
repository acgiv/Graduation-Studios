package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.Professore;

import java.util.List;

@Dao
public interface ProfessoreDao {

    @Insert
    void insert(Professore professore);

    @Update
    void update(Professore professore);

    @Query("SELECT * FROM professore")
    List<Professore> getAllProfesssore();

    @Query("SELECT * FROM professore where id = :id")
    Professore findAllById(Long id);

    @Delete
    void delete(Professore professore);

}
