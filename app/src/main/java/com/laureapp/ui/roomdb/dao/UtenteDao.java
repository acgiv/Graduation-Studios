package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.laureapp.ui.roomdb.entity.Utente;

import java.util.List;

/**
 * Questa classe rappresenta l'entità Tesi nel database Room.
 */
@Dao
public interface UtenteDao {

    /**
     * Inserisce uno o più oggetti Utente nel database.
     * @param utente l'oggetto Utente da inserire nel database.
     */
    @Insert
    void insert(Utente... utente);

    /**
     * Aggiorna un oggetto Utente esistente nel database.
     * @param utente l'oggetto Utente da aggiornare nel database.
     */
    @Update
    void update(Utente utente);

    /**
     * Ottiene tutti gli oggetti Utente presenti nel database.
     * @return una lista di tutti gli oggetti Utente presenti nel database.
     */
    @Query("SELECT * FROM Utente")
    List<Utente> getAllUtente();

    /**
     * Trova un oggetto Utente nel database in base all'ID specificato.
     * @param idUtente l'ID dell'utente da cercare nel database.
     * @return l'oggetto Utente corrispondente all'ID specificato.
     */
    @Query("SELECT * FROM Utente where id_utente = :idUtente")
    Utente findAllById(Long idUtente);

    /**
     * Verifica l'esistenza di un utente nel database in base all'email e alla password specificate.
     * @param email l'email dell'utente da verificare.
     * @param password la password dell'utente da verificare.
     * @return l'oggetto Utente corrispondente all'email e alla password specificate.
     */
    @Query("SELECT * FROM Utente where email = :email and password =:password")
    Utente is_exist_email_password(String email, String password);


    /**
     * Ottiene l'ID dell'utente in base all'email specificata.
     * @param email l'email dell'utente per cui ottenere l'ID.
     * @return l'ID dell'utente corrispondente all'email specificata.
     */
    @Query("SELECT id_utente FROM Utente where email = :email ")
    Long getIdUtente(String email);

    /**
     * Ottiene il nome dell'utente.
     * @return il nome dell'utente.
     */
    @Query("SELECT u.nome FROM Utente u, Studente s WHERE u.id_utente = s.id_studente")
    String getNome();

    /**
     * Ottiene il cognome dell'utente.
     * @return il cognome dell'utente.
     */
    @Query("SELECT u.cognome FROM Utente u, Studente s WHERE u.id_utente = s.id_studente")
    String getCognome();

    /**
     * Cancella tutti gli oggetti Utente presenti nel database.
     */
    @Query("DELETE FROM Utente")
    public void deleteAll();

    /**
     * Restituisce il numero di occorrenze di un oggetto Utente nel database in base all'ID.
     * @param idUtente l'ID dell'utente da controllare nel database.
     * @return il numero di occorrenze dell'utente nel database.
     */
    @Query("SELECT COUNT(*) FROM Utente WHERE id_utente = :idUtente")
    int countUtenteById(long idUtente);

    /**
     * Ottiene l'email dell'utente in base all'ID specificato.
     * @param idUtente l'ID dell'utente per cui ottenere l'email.
     * @return l'email dell'utente corrispondente all'ID specificato.
     */
    @Query("SELECT u.email FROM Utente u WHERE u.id_utente =:idUtente")
    String getEmail(Long idUtente);

    /**
     * Ottiene il nome della facoltà dell'utente in base all'ID specificato.
     * @param idUtente l'ID dell'utente per cui ottenere il nome della facoltà.
     * @return il nome della facoltà dell'utente corrispondente all'ID specificato.
     */
    @Query("SELECT u.facolta FROM Utente u WHERE u.id_utente =:idUtente")
    String getFacolta(Long idUtente);

    /**
     * Ottiene il nome del corso di laurea dell'utente in base all'ID specificato.
     * @param idUtente l'ID dell'utente per cui ottenere il nome del corso di laurea.
     * @return il nome del corso di laurea dell'utente corrispondente all'ID specificato.
     */
    @Query("SELECT u.nome_cdl FROM Utente u WHERE u.id_utente =:idUtente")
    String getNomeCdl(Long idUtente);

    /**
     * Elimina un'entità Utente esistente dal database.
     * @param utente L'oggetto Utente da eliminare dal database.
     */
    @Delete
    void delete(Utente utente);


}