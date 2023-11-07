package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.Ricevimenti;

import java.util.List;

/**
 * Questa interfaccia fornisce metodi per eseguire operazioni CRUD (create, read, update, delete)
 * sull'entità Ricevimenti nel database Room
 */
@Dao
public interface RicevimentiDao {

    /**
     * Inserisce un nuovo oggetto Ricevimenti nel database
     * @param ricevimenti L'oggetto Ricevimenti da inserire
     */
    @Insert
    void insert(Ricevimenti ricevimenti);

    /**
     * Aggiorna un oggetto Ricevimenti esistente nel database
     * @param ricevimenti L'oggetto Ricevimenti da aggiornare
     */
    @Update
    void update(Ricevimenti ricevimenti);

    /**
     * Ottiene tutti gli oggetti Ricevimenti presenti nel database
     * @return lista di tutti gli oggetti Ricevimenti
     */
    @Query("SELECT * FROM Ricevimenti")
    List<Ricevimenti> getAllRicevimenti();

    /**
     * Trova un oggetto Ricevimenti nel database in base all'ID specificato
     * @param idRicevimento L'ID dell'oggetto Ricevimenti da trovare
     * @return L'oggetto Ricevimenti corrispondente all'ID specificato, se presente, altrimenti null
     */
    @Query("SELECT * FROM Ricevimenti where id_ricevimento = :idRicevimento")
    Ricevimenti findAllById(Long idRicevimento);

    /**
     * Elimina un oggetto Ricevimenti dal database.
     * @param ricevimenti L'oggetto Ricevimenti da eliminare
     */
    @Delete
    void delete(Ricevimenti ricevimenti);
}
