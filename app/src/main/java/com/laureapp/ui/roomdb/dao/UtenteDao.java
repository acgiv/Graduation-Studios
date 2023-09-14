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
    void insert(Utente... utente);

    @Update
    void update(Utente utente);

    @Query("SELECT * FROM Utente")
    List<Utente> getAllUtente();

    @Query("SELECT * FROM Utente where utente_id = :id")
    Utente findAllById(Long id);

    @Query("SELECT * FROM Utente where email = :email and password =:password")
    Utente is_exist_email_password(String email, String password);

    @Query("SELECT utente_id FROM Utente where email = :email ")
    Long getIdUtente(String email);

    @Query("SELECT u.nome FROM Utente u, Studente s WHERE u.utente_id = s.studente_id")
    String getNome();

    @Query("SELECT u.cognome FROM Utente u, Studente s WHERE u.utente_id = s.studente_id")
    String getCognome();

    @Query("DELETE FROM professore")
    public void deleteAll();




    @Delete
    void delete(Utente utente);

}
