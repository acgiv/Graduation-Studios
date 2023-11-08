package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.laureapp.ui.roomdb.entity.Tesi;

import java.util.List;

/**
 * Questa classe rappresenta l'entità Tesi nel database Room.
 */
@Dao
public interface TesiDao {

    /**
     * Inserisce una nuova entità Tesi nel database.
     * @param tesi L'oggetto Tesi da inserire nel database.
     */
    @Insert
    void insert(Tesi tesi);

    /**
     * Aggiorna un'entità Tesi esistente nel database.
     * @param tesi L'oggetto Tesi da aggiornare nel database.
     */
    @Update
    void update(Tesi tesi);

    /**
     * Ottiene tutte le entità Tesi presenti nel database.
     * @return Una lista di tutte le entità Tesi presenti nel database.
     */
    @Query("SELECT * FROM Tesi")
    List<Tesi> getAllTesi();

    /**
     * Trova un'entità Tesi nel database in base all'ID specificato.
     * @param idTesi L'ID della Tesi da trovare.
     * @return L'oggetto Tesi corrispondente all'ID specificato.
     */
    @Query("SELECT * FROM Tesi where id_tesi = :idTesi")
    Tesi findAllById(Long idTesi);

    /**
     * Elimina un'entità Tesi esistente dal database.
     * @param tesi L'oggetto Tesi da eliminare dal database.
     */
    @Delete
    void delete(Tesi tesi);


}
