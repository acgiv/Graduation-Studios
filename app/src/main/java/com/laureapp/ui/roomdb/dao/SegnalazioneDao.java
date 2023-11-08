package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.Segnalazione;

import java.util.List;

/** Data Access Object (DAO):
 *
 *   I DAO sono interfacce Java che definiscono le operazioni per l'accesso ai dati nel database.
 *   Vengono annotate con @Dao e dichiarano metodi per l'inserimento, l'aggiornamento, la ricerca e l'eliminazione dei dati.
 *   Le query SQL personalizzate vengono definite tramite l'annotazione @Query.
 **/

@Dao
public interface SegnalazioneDao {

    @Insert
    void insert(Segnalazione segnalazione);

    @Update
    void update(Segnalazione segnalazione);

    @Delete
    void delete(Segnalazione segnalazione);

    @Query("SELECT * FROM Segnalazione WHERE id_segnalazione = :idSegnalazione")
    Segnalazione findById(Long idSegnalazione);

    @Query("SELECT * FROM Segnalazione WHERE id_studente_tesi = :idStudenteTesi")
    List<Segnalazione> findByStudenteTesiId(Long idStudenteTesi);

    @Query("DELETE FROM Segnalazione WHERE id_segnalazione = :idSegnalazione")
    void deleteById(Long idSegnalazione);

    @Query("DELETE FROM Segnalazione WHERE id_studente_tesi = :idStudenteTesi")
    void deleteByTesiId(Long idStudenteTesi);
}
