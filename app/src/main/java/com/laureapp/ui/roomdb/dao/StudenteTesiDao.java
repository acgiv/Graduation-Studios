package com.laureapp.ui.roomdb.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.laureapp.ui.roomdb.entity.StudenteTesi;

import java.util.List;

/**
 * Questa interfaccia fornisce metodi per eseguire operazioni CRUD (create, read, update, delete)
 * sull'entità StudenteTesi nel database Room
 */
@Dao
public interface StudenteTesiDao {

    /**
     * Inserisce una tesi studente nel database.
     * @param studenteTesi La tesi studente da inserire
     */
    @Insert
    void insert(StudenteTesi studenteTesi);

    /**
     * Aggiorna un oggetto StudenteTesi esistente nel database
     * @param studenteTesi L'oggetto StudenteTesi da aggiornare
     */
    @Update
    void update(StudenteTesi studenteTesi);

    /**
     * Ottiene tutti le tesi studenti presenti nel database.
     * @return Una lista di oggetti che rappresentano le tesi dello studente
     */
    @Query("SELECT * FROM Studente_Tesi")
    List<StudenteTesi> getAllStudenteTesi();

    /**
     * Trova uno studente nel database in base all'ID specificato.
     * @param idStudenteTesi L'ID StudenteTesi da cercare
     * @return La tesi studente con l'ID specificato
     */
    @Query("SELECT * FROM Studente_Tesi where id_studente_tesi = :idStudenteTesi")
    StudenteTesi findAllById(Long idStudenteTesi);

    /**
     * Trova una tesi studente nel database in base all'ID dello studente
     * @param idStudente L'ID di StudenteTesi da cercare
     * @return Lo studente con l'ID specificato
     */
    @Query("SELECT id_tesi FROM Studente_Tesi WHERE id_studente = :idStudente")
    Long findIdTesiByIdStudente(Long idStudente);

    /**
     * Elimina un oggetto StudenteTesi dal database.
     * @param studenteTesi L'oggetto StudenteTesi da eliminare
     */
    @Delete
    void delete(StudenteTesi studenteTesi);


}
