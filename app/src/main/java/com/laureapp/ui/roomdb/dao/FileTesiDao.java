package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.FileTesi;

import java.util.List;

/**
 * Interfaccia utilizzata per eseguire operazioni CRUD (create, read, update, delete)
 * sull'entità FileTesi nel database Room
 */
@Dao
public interface FileTesiDao {
    /**
     * Inserisce un nuovo oggetto FileTesi nel database
     * @param fileTesi L'oggetto FileTesi da inserire
     */
    @Insert
    void insert(FileTesi fileTesi);

    /**
     * Aggiorna un oggetto FileTesi esistente nel database
     * @param fileTesi L'oggetto FileTesi da aggiornare
     */
    @Update
    void update(FileTesi fileTesi);

    /**
     * Ottiene tutti gli oggetti FileTesi presenti nel database
     * @return lista di tutti gli oggetti FileTesi
     */
    @Query("SELECT * FROM FileTesi")
    List<FileTesi> getAllFileTesi();

    /**
     * Trova un oggetto FileTesi nel database in base all'ID specificato
     * @param idFile L'ID dell'oggetto FileTesi da trovare
     * @return L'oggetto FileTesi corrispondente all'ID specificato, se presente, altrimenti null
     */
    @Query("SELECT * FROM FileTesi where id_file = :idFile")
    FileTesi findAllById(Long idFile);

    /**
     * Elimina un oggetto FileTesi dal database.
     * @param fileTesi L'oggetto FileTesi da eliminare
     */
    @Delete
    void delete(FileTesi fileTesi);

}
