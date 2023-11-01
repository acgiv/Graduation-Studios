package com.laureapp.ui.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.laureapp.ui.roomdb.entity.FileTesi;

import java.util.List;
@Dao
public interface FileTesiDao {
    @Insert
    void insert(FileTesi fileTesi);

    @Update
    void update(FileTesi fileTesi);

    @Query("SELECT * FROM FileTesi")
    List<FileTesi> getAllFileTesi();

    @Query("SELECT * FROM FileTesi where id_file = :idFile")
    FileTesi findAllById(Long idFile);

    @Delete
    void delete(FileTesi fileTesi);

}
