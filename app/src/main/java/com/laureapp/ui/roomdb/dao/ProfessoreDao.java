package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.Professore;

import java.util.List;

/**
 * Questa interfaccia fornisce metodi per eseguire operazioni CRUD (create, read, update, delete)
 * sull'entità Professore nel database Room
 */
@Dao
public interface ProfessoreDao {

    /**
     * Inserisce un nuovo oggetto Professore nel database
     * @param professore
     */
    @Insert
    void insert(Professore professore);

    /**
     * Aggiorna un oggetto Professore esistente nel database
     * @param professore
     */
    @Update
    void update(Professore professore);

    /**
     * Ottiene tutti gli oggetti Professore presenti nel database
     * @return lista di tutti gli oggetti Professore
     */
    @Query("SELECT * FROM professore")
    List<Professore> getAllProfessore();

    /**
     * Elimina tutti gli oggetti Professore nel database
     */
    @Query("DELETE FROM professore")
    public void deleteAll();

    /**
     * Trova un Professore tramite l'ID utente associato.
     * @param id_utente L'ID utente associato al Professore.
     * @return L'ID del Professore.
     */
    @Query("SELECT id_professore FROM professore where id_utente = :id_utente")
    Long findProfessore(Long id_utente);

    /**
     * Trova un Professore tramite l'ID specificato.
     * @param id L'ID del Professore da cercare.
     * @return L'oggetto Professore corrispondente all'ID specificato.
     */
    @Query("SELECT * FROM professore where id_professore = :id")
    Professore findAllById(Long id);

    /**
     * Trova un Professore tramite l'ID specificato in forma di stringa.
     * @param id L'ID del Professore da cercare (in forma di stringa).
     * @return L'oggetto Professore corrispondente all'ID specificato.
     */
    @Query("SELECT * FROM professore where id_professore = :id")
    Professore findAllById(String id);

    /**
     * Trova un Professore tramite l'ID utente associato.
     * @param id_utente L'ID utente associato al Professore.
     * @return L'ID del Professore.
     */
    @Query("SELECT id_professore FROM professore where id_utente = :id_utente")
    Long findPorfessore(Long id_utente);

    /**
     * Trova un Professore tramite la matricola specificata.
     * @param matricola La matricola del Professore da cercare.
     * @return L'ID del Professore corrispondente alla matricola specificata.
     */
    @Query("SELECT id_professore FROM professore where matricola = :matricola")
    Long findProfessoreMatricola(Long matricola);

    /**
     * Elimina un oggetto Professore dal database.
     * @param professore L'oggetto Professore da eliminare.
     */
    @Delete
    void delete(Professore professore);

}
