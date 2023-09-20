package com.laureapp.ui.roomdb.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.laureapp.ui.roomdb.entity.UtenteCdl;

import java.util.List;
@Dao
public interface UtenteCdlDao {

    @Insert
    void insert(UtenteCdl utenteCdl);

    @Update
    void update(UtenteCdl utenteCdl);

    @Query("SELECT * FROM Utente_Cdl")
    List<UtenteCdl> getAllUtenteCdl();

    @Query("SELECT * FROM Utente_Cdl where id_utente_cdl = :idUtenteCdl")
    UtenteCdl findAllById(Long idUtenteCdl);

    @Delete
    void delete(UtenteCdl utenteCdl);
}
