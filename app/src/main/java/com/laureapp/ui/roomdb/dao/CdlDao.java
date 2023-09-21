package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.laureapp.ui.roomdb.entity.Cdl;
import java.util.List;

@Dao
public interface CdlDao {

    @Insert
    void insert(Cdl cdl);

    @Update
    void update(Cdl cdl);

    @Query("SELECT * FROM cdl")
    List<Cdl> getAllCdl();

    @Query("SELECT * FROM cdl where id_cdl = :idCdl")
    Cdl findAllById(Long idCdl);

    @Delete
    void delete(Cdl cdl);
}
