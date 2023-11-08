package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteWithUtente;

import java.util.List;

/**
 * Questa interfaccia fornisce metodi per eseguire operazioni CRUD (create, read, update, delete)
 * sull'entità Studente nel database Room
 */
@Dao
public interface StudenteDao {

    // è quell'oggetto che accederà al repository per fare le operazioni al db
    // a differenza del repository dove non vengono manipolati i dati. Nel dao invece si
    // esempio prima di fare una modifica vado a controllare che quella persona è presente

    /**
     * Inserisce uno studente nel database.
     * @param studente Lo studente da inserire
     */
    @Insert
    void insert(Studente studente);

    /**
     * Aggiorna le informazioni di uno studente nel database.
     * @param studente Lo studente da aggiornare
     */
    @Update
    void update(Studente studente);

    /**
     * Ottiene tutti gli studenti presenti nel database.
     * @return Una lista di oggetti Studente che rappresentano tutti gli studenti nel database
     */
    @Query("SELECT * FROM studente")
    List<Studente> getAllStudente();

    /**
     * Ottiene tutti gli studenti con gli utenti associati presenti nel database.
     * @return Una lista di oggetti StudenteWithUtente che rappresentano gli studenti con gli utenti associati nel database
     */
    @Query("SELECT * FROM studente")
    List<StudenteWithUtente> getAllStudenteWithUtente();

    /**
     * Trova uno studente nel database in base all'ID specificato.
     * @param idStudente L'ID dello studente da cercare
     * @return Lo studente con l'ID specificato
     */
    @Query("SELECT * FROM studente where id_studente = :idStudente")
    Studente findAllById(Long idStudente);

    /**
     * Trova lo studente nel database in base all'ID utente specificato.
     * @param id_utente L'ID dell'utente associato allo studente
     * @return L'ID dello studente associato all'ID utente specificato
     */
    @Query("SELECT id_studente FROM studente where id_utente = :id_utente")
    Long findStudente(Long id_utente);

    /**
     * Trova lo studente nel database in base alla matricola specificata.
     * @param matricola La matricola dello studente da cercare
     * @return L'ID dello studente associato alla matricola specificata
     */
    @Query("SELECT id_studente FROM studente where matricola = :matricola")
    Long findStudenteMatricola(Long matricola);

    /**
     * Ottiene la matricola dello studente nel database in base all'ID studente specificato.
     * @param id_studente L'ID dello studente
     * @return La matricola dello studente associato all'ID specificato
     */
    @Query("SELECT matricola FROM studente where id_studente = :id_studente")
    Long getMatricola(Long id_studente);

    /**
     * Trova tutti gli studenti con gli utenti associati nel database.
     * @return Una lista di oggetti StudenteWithUtente che rappresentano gli studenti con gli utenti associati nel database
     */
    @Query("SELECT s.*, u.* FROM Utente u, Studente s WHERE u.id_utente = s.id_utente")
    List<StudenteWithUtente> findStudentiWithUtenti();

    /**
     * Elimina tutti gli studenti presenti nel database.
     */
    @Query("DELETE FROM studente")
    void deleteAll();

    /**
     * Elimina lo studente specificato dal database.
     * @param studente Lo studente da eliminare
     */
    @Delete
    void delete(Studente studente);


}