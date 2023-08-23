package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.laureapp.ui.roomdb.entity.Studente;

import java.util.List;

// è quell'oggetto che accederà al repository per fare le operazioni al db
// a differenza del repository dove non vengono manipolati i dati. Nel dao invece si
// esempio prima di fare una modifica vado a controllare che quella persona è presente

@Dao
public interface StudenteDao {

    @Insert
    void insert(Studente studente);

    @Update
    void update(Studente studente);

    @Query("SELECT * FROM studente")
    List<Studente> getAllStudente();

    @Query("SELECT * FROM studente where id = :id")
    Studente findAllById(Long id);

    @Delete
    void delete(Studente studente);


}