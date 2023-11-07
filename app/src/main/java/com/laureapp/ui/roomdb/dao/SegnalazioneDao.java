package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.Segnalazione;

import java.util.List;

/**
 * Questa interfaccia fornisce metodi per eseguire operazioni CRUD (create, read, update, delete)
 * sull'entità Segnalazioni nel database Room
 */
@Dao
public interface SegnalazioneDao {

    /* Data Access Object (DAO):
     *
     *   I DAO sono interfacce Java che definiscono le operazioni per l'accesso ai dati nel database.
     *   Vengono annotate con @Dao e dichiarano metodi per l'inserimento, l'aggiornamento, la ricerca e l'eliminazione dei dati.
     *   Le query SQL personalizzate vengono definite tramite l'annotazione @Query.
     */

    /**
     * Inserisce una nuova segnalazione nel database.
     * @param segnalazione l'oggetto Segnalazione da inserire.
     */
    @Insert
    void insert(Segnalazione segnalazione);

    /**
     * Aggiorna una segnalazione esistente nel database.
     * @param segnalazione l'oggetto Segnalazione da aggiornare.
     */
    @Update
    void update(Segnalazione segnalazione);

    /**
     * Elimina una segnalazione dal database.
     * @param segnalazione l'oggetto Segnalazione da eliminare.
     */
    @Delete
    void delete(Segnalazione segnalazione);

    /**
     * Trova una segnalazione nel database in base all'ID della segnalazione specificato.
     * @param idSegnalazione l'ID della segnalazione da cercare.
     * @return l'oggetto Segnalazione corrispondente all'ID specificato.
     */
    @Query("SELECT * FROM Segnalazione WHERE id_segnalazione = :idSegnalazione")
    Segnalazione findById(Long idSegnalazione);

    /**
     * Trova tutte le segnalazioni correlate a un determinato ID dello studente nella tabella Segnalazione.
     * @param idStudenteTesi l'ID dello studente collegato alle segnalazioni da cercare.
     * @return una lista di Segnalazioni correlate all'ID dello studente specificato.
     */
    @Query("SELECT * FROM Segnalazione WHERE id_studente_tesi = :idStudenteTesi")
    List<Segnalazione> findByStudenteTesiId(Long idStudenteTesi);

    /**
     * Elimina una segnalazione dal database in base all'ID della segnalazione specificato.
     * @param idSegnalazione l'ID della segnalazione da eliminare.
     */
    @Query("DELETE FROM Segnalazione WHERE id_segnalazione = :idSegnalazione")
    void deleteById(Long idSegnalazione);

    /**
     * Elimina tutte le segnalazioni correlate a un determinato ID dello studente nella tabella Segnalazione.
     * @param idStudenteTesi l'ID dello studente collegato alle segnalazioni da eliminare.
     */
    @Query("DELETE FROM Segnalazione WHERE id_studente_tesi = :idStudenteTesi")
    void deleteByTesiId(Long idStudenteTesi);
}
