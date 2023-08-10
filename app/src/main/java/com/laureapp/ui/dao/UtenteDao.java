package com.laureapp.ui.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.laureapp.ui.entity.Utente;

import java.util.List;

@Dao
public interface UtenteDao {
    @Insert
    void insert(Utente utente);

    @Update
    void update(Utente utente);

    @Query("SELECT * FROM Utente")
    List<Utente> getAllUtente();

    @Query("SELECT * FROM Utente where id = :id")
    Utente findAllById(Long id);

    @Delete
    void delete(Utente utente);
}
