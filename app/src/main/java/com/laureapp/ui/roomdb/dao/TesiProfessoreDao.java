package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.TesiProfessore;

import java.util.List;

@Dao
public interface TesiProfessoreDao {
    @Insert
    void insert(TesiProfessore tesiProfessore);

    @Update
    void update(TesiProfessore tesiProfessore);

    @Query("SELECT * FROM Tesi_Professore")
    List<TesiProfessore> getAllTesiProfessore();

    @Query("SELECT * FROM Tesi_Professore where id = :id")
    TesiProfessore findAllById(Long id);

    @Delete
    void delete(TesiProfessore tesiProfessore);
}
