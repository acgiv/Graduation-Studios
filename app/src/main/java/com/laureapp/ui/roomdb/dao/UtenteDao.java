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

    @Query("SELECT * FROM Utente where id_utente = :idUtente")
    Utente findAllById(Long idUtente);

    @Query("SELECT * FROM Utente where email = :email and password =:password")
    Utente is_exist_email_password(String email, String password);

    @Query("SELECT id_utente FROM Utente where email = :email ")
    Long getIdUtente(String email);

    @Query("SELECT u.nome FROM Utente u, Studente s WHERE u.id_utente = s.id_studente")
    String getNome();

    @Query("SELECT u.cognome FROM Utente u, Studente s WHERE u.id_utente = s.id_studente")
    String getCognome();

    @Query("DELETE FROM Utente")
    public void deleteAll();

    @Query("SELECT COUNT(*) FROM Utente WHERE id_utente = :idUtente")
    int countUtenteById(long idUtente);



    @Query("SELECT u.email FROM Utente u WHERE u.id_utente =:idUtente")
    String getEmail(Long idUtente);

    @Delete
    void delete(Utente utente);


}