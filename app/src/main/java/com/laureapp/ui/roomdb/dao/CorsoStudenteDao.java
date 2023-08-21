package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.laureapp.ui.roomdb.entity.CorsoStudente;
import java.util.List;

@Dao
public interface CorsoStudenteDao {

    @Insert
    void insert(CorsoStudente corsoStudente);

    @Update
    void update(CorsoStudente corsoStudente);

    @Query("SELECT * FROM Corso_Studente")
    List<CorsoStudente> getAllCorsoStudente();

    @Query("SELECT * FROM Corso_Studente where id = :id")
    CorsoStudente findAllById(Long id);

    @Delete
    void delete(CorsoStudente corsoStudente);
}
