package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.Ricevimenti;

import java.util.List;

@Dao
public interface RicevimentiDao {

    @Insert
    void insert(Ricevimenti ricevimenti);

    @Update
    void update(Ricevimenti ricevimenti);

    @Query("SELECT * FROM Ricevimenti")
    List<Ricevimenti> getAllRicevimenti();

    @Query("SELECT * FROM Ricevimenti where id_ricevimento = :idRicevimento")
    Ricevimenti findAllById(Long idRicevimento);

    @Delete
    void delete(Ricevimenti ricevimenti);
}
