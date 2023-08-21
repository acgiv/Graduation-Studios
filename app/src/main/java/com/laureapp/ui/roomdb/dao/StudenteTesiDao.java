package com.laureapp.ui.roomdb.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.laureapp.ui.roomdb.entity.StudenteTesi;

import java.util.List;
@Dao
public interface StudenteTesiDao {

    @Insert
    void insert(StudenteTesi studenteTesi);

    @Update
    void update(StudenteTesi studenteTesi);

    @Query("SELECT * FROM Studente_Tesi")
    List<StudenteTesi> getAllStudenteTesi();

    @Query("SELECT * FROM Studente_Tesi where id = :id")
    StudenteTesi findAllById(Long id);

    @Delete
    void delete(StudenteTesi studenteTesi);
}
