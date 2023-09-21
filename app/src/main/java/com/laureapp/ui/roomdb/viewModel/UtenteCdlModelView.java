package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.StudenteTesi;
import com.laureapp.ui.roomdb.entity.UtenteCdl;
import com.laureapp.ui.roomdb.repository.StudenteTesiRepository;
import com.laureapp.ui.roomdb.repository.UtenteCdlRepository;

import java.util.List;

public class UtenteCdlModelView {

    private final UtenteCdlRepository utenteCdlRepository;

    public UtenteCdlModelView(Context context){
        utenteCdlRepository = new UtenteCdlRepository(context);
    }

    public void insertUtenteCdl(UtenteCdl utenteCdl){
        utenteCdlRepository.insertUtenteCdl(utenteCdl);
    }

    public void updateUtenteCdl(UtenteCdl utenteCdl){
        utenteCdlRepository.updateUtenteCdl(utenteCdl);
    }

    public boolean deleteUtenteCdl(long id){
        return utenteCdlRepository.deleteUtenteCdl(id);
    }

    public UtenteCdl findAllById(Long id){
        return utenteCdlRepository.findAllById(id);
    }

    public List<UtenteCdl> getAllUtenteCdl(){
        return utenteCdlRepository.getAllUtenteCdl();
    }
}
