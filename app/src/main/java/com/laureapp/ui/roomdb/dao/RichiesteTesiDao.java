package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.RichiesteTesi;

import java.util.List;

@Dao
public interface RichiesteTesiDao {

    @Insert
    void insert(RichiesteTesi tesi);

    @Update
    void update(RichiesteTesi tesi);

    @Query("SELECT * FROM Richieste_Tesi")
    List<RichiesteTesi> getAllRichiesteTesi();

    @Query("SELECT * FROM Richieste_Tesi where id_richiesta_tesi = :id_richiesta_tesi")
    RichiesteTesi findAllById(Long id_richiesta_tesi);

    @Delete
    void delete(RichiesteTesi id_richiesta_tesi);


}
