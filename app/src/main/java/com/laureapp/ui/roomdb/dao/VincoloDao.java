package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.laureapp.ui.roomdb.entity.Vincolo;

import java.util.List;

/**
 * Questa classe rappresenta l'entità Vincolo nel database Room.
 */
@Dao
public interface VincoloDao {

    /**
     * Inserisce uno oggetto Vincolo nel database.
     * @param vincolo l'oggetto Vincolo da inserire nel database.
     */
    @Insert
    void insert(Vincolo vincolo);

    /**
     * Aggiorna un oggetto Vincolo esistente nel database.
     * @param vincolo l'oggetto Vincolo da aggiornare nel database.
     */
    @Update
    void update(Vincolo vincolo);

    /**
     * Ottiene tutti gli oggetti Vincolo presenti nel database.
     * @return una lista di tutti gli oggetti Vincolo presenti nel database.
     */
    @Query("SELECT * FROM Vincolo")
    List<Vincolo> getAllVincolo();

    /**
     * Trova un oggetto Vincolo nel database in base all'ID specificato.
     * @param idVincolo l'ID vincolo da cercare nel database.
     * @return l'oggetto Vincolo corrispondente all'ID specificato.
     */
    @Query("SELECT * FROM Vincolo where id_vincolo = :idVincolo")
    Vincolo findAllById(Long idVincolo);

    /**
     * Elimina un'entità Vincolo esistente dal database.
     * @param vincolo L'oggetto Vincolo da eliminare dal database.
     */
    @Delete
    void delete(Vincolo vincolo);
}
