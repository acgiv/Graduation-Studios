package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.Cdl;
import com.laureapp.ui.roomdb.repository.CdlRepository;

import java.util.List;

public class CdlModelView {

    private final CdlRepository cdlRepository;

    public CdlModelView(Context context){
        cdlRepository = new CdlRepository(context);
    }

    public void insertCdl(Cdl cdl){
        cdlRepository.insertCdl(cdl);
    }

    public void updateCdl(Cdl cdl){
        cdlRepository.updateCdl(cdl);
    }

    public boolean deleteCdl(long id){
        return cdlRepository.deleteCdl(id);
    }

    public Cdl findAllById(Long id){
        return cdlRepository.findAllById(id);
    }

    public List<Cdl> getAllCdl(){
        return cdlRepository.getAllCdl();
    }
}
