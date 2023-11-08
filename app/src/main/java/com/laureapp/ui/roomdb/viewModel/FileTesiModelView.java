package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.FileTesi;
import com.laureapp.ui.roomdb.entity.RichiesteTesi;
import com.laureapp.ui.roomdb.repository.FileTesiRepository;
import com.laureapp.ui.roomdb.repository.RichiesteTesiRepository;

import java.io.File;
import java.util.List;

public class FileTesiModelView {

    private final FileTesiRepository fileTesiRepository;

    public FileTesiModelView(Context context){
        fileTesiRepository = new FileTesiRepository(context);
    }

    public void insertFileTesi(FileTesi fileTesi){
        fileTesiRepository.insertFileTesi(fileTesi);
    }

    public void updateRichiesteTesi(FileTesi fileTesi){
        fileTesiRepository.updateRichiesteTesi(fileTesi);
    }

    public boolean deleteRichiesteTesi(long id){
        return fileTesiRepository.deleteFileTesi(id);
    }

    public FileTesi findAllById(Long id){
        return fileTesiRepository.findAllById(id);
    }

    public List<FileTesi> getAllFileTesi(){
        return fileTesiRepository.getAllFileTesi();
    }

}
