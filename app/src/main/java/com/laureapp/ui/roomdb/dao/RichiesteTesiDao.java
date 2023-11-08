package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.RichiesteTesi;

import java.util.List;

/**
 * Interfaccia utilizzata per eseguire operazioni CRUD (create, read, update, delete)
 * sull'entità RichiestaTesi nel database Room
 */
@Dao
public interface RichiesteTesiDao {

    /**
     * Inserisce una nuova richiesta di tesi nel database.
     * @param tesi La richiesta di tesi da inserire
     */
    @Insert
    void insert(RichiesteTesi tesi);

    /**
     * Aggiorna una richiesta di tesi esistente nel database.
     * @param tesi La richiesta di tesi da aggiornare
     */
    @Update
    void update(RichiesteTesi tesi);

    /**
     * Ottiene tutte le richieste di tesi presenti nel database.
     * @return Lista delle richieste di tesi presenti nel database
     */
    @Query("SELECT * FROM Richieste_Tesi")
    List<RichiesteTesi> getAllRichiesteTesi();

    /**
     * Trova una richiesta di tesi per l'ID specificato.
     * @param id_richiesta_tesi L'ID della richiesta di tesi da trovare
     * @return La richiesta di tesi trovata, se presente
     */
    @Query("SELECT * FROM Richieste_Tesi where id_richiesta_tesi = :id_richiesta_tesi")
    RichiesteTesi findAllById(Long id_richiesta_tesi);

    /**
     * Elimina una richiesta di tesi dal database.
     * @param id_richiesta_tesi L'ID della richiesta di tesi da eliminare
     */
    @Delete
    void delete(RichiesteTesi id_richiesta_tesi);


}
