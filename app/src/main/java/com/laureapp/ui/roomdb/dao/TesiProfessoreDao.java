package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.TesiProfessore;

import java.util.List;

/**
 * Questa interfaccia fornisce metodi per eseguire operazioni CRUD (create, read, update, delete)
 * sull'entità TesiProfessore nel database Room
 */
@Dao
public interface TesiProfessoreDao {
    /**
     * Inserisce una tesi professore nel database.
     * @param tesiProfessore La tesi professore da inserire
     */
    @Insert
    void insert(TesiProfessore tesiProfessore);

    /**
     * Aggiorna un oggetto TesiProfessore esistente nel database
     * @param tesiProfessore L'oggetto TesiProfessore da aggiornare
     */
    @Update
    void update(TesiProfessore tesiProfessore);

    /**
     * Ottiene tutti le tesi professori presenti nel database.
     * @return Una lista di oggetti che rappresentano le tesi del professore
     */
    @Query("SELECT * FROM Tesi_Professore")
    List<TesiProfessore> getAllTesiProfessore();

    /**
     * Trova una tesi professore nel database in base all'ID specificato.
     * @param idTesiProfessore L'ID TesiProfessore da cercare
     * @return La tesi professore con l'ID specificato
     */
    @Query("SELECT * FROM Tesi_Professore where id_tesi_professore = :idTesiProfessore")
    TesiProfessore findAllById(Long idTesiProfessore);

    /**
     * Elimina un oggetto TesiProfessore dal database.
     * @param tesiProfessore L'oggetto TesiProfessore da eliminare
     */
    @Delete
    void delete(TesiProfessore tesiProfessore);
}
