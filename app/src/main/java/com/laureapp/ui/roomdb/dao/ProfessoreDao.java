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
    List<Professore> getAllProfessore();

    @Query("DELETE FROM professore")
    public void deleteAll();

    @Query("SELECT * FROM professore where id_professore = :id")
    Professore findAllById(Long id);

    @Query("SELECT * FROM professore where id_professore = :id")
    Professore findAllById(String id);

    @Query("SELECT id_professore FROM professore where matricola = :matricola")
    Long findProfessoreMatricola(Long matricola);

    @Query("SELECT id_professore FROM professore WHERE id_utente = :id_utente")
    Long findProfessore(Long id_utente);

    @Delete
    void delete(Professore professore);

}
