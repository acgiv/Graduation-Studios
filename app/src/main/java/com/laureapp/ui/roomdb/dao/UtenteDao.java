package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.laureapp.ui.roomdb.entity.Utente;

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

    @Query("SELECT * FROM Utente where email = :email and password =:password")
    Utente is_exist_email_password(String email, String password);

    @Query("SELECT id FROM Utente where email = :email ")
    Long getIdUtente(String email);

    @Delete
    void delete(Utente utente);
}