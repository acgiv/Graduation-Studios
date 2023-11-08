package com.laureapp.ui.roomdb.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
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

    @Query("SELECT * FROM Studente_Tesi where id_studente_tesi = :idStudenteTesi")
    StudenteTesi findAllById(Long idStudenteTesi);

    @Query("SELECT id_tesi FROM Studente_Tesi WHERE id_studente = :idStudente")
    Long findIdTesiByIdStudente(Long idStudente);


    @Delete
    void delete(StudenteTesi studenteTesi);


}
