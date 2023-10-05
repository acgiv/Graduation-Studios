package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.Segnalazione;

import java.util.List;

/*  Data Access Object (DAO):
 *
 *   I DAO sono interfacce Java che definiscono le operazioni per l'accesso ai dati nel database.
 *   Vengono annotate con @Dao e dichiarano metodi per l'inserimento, l'aggiornamento, la ricerca e l'eliminazione dei dati.
 *   Le query SQL personalizzate vengono definite tramite l'annotazione @Query.
 */

@Dao
public interface SegnalazioneDao {

    @Insert
    void insert(Segnalazione segnalazione);

    @Update
    void update(Segnalazione segnalazione);

    @Delete
    void delete(Segnalazione segnalazione);

    @Query("SELECT * FROM Segnalazioni WHERE id_segn = :idSegnalazione")
    Segnalazione findById(int idSegnalazione);

    @Query("SELECT * FROM Segnalazioni WHERE id_tesi = :idTesi")
    List<Segnalazione> findByTesiId(int idTesi);

    @Query("DELETE FROM Segnalazioni WHERE id_segn = :idSegnalazione")
    void deleteById(int idSegnalazione);

    @Query("DELETE FROM Segnalazioni WHERE id_tesi = :idTesi")
    void deleteByTesiId(int idTesi);
}
