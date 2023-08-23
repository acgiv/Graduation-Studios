package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.StudenteTesi;
import com.laureapp.ui.roomdb.repository.StudenteTesiRepository;

import java.util.List;

public class StudenteTesiModelView {

    private final StudenteTesiRepository studenteTesiRepository;

    public StudenteTesiModelView(Context context){
        studenteTesiRepository = new StudenteTesiRepository(context);
    }

    public void insertStudenteTesi(StudenteTesi studenteTesi){
        studenteTesiRepository.insertStudenteTesi(studenteTesi);
    }

    public void updateStudenteTesi(StudenteTesi studenteTesi){
        studenteTesiRepository.updateStudenteTesi(studenteTesi);
    }

    public boolean delateStudenteTesi(long id){
        return studenteTesiRepository.delateStudenteTesi(id);
    }

    public StudenteTesi findAllById(Long id){
        return studenteTesiRepository.findAllById(id);
    }

    public List<StudenteTesi> getAllStudenteTesi(){
        return studenteTesiRepository.getAllStudenteTesi();
    }
}
