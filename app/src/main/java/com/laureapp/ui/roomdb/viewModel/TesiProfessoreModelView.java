package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.TesiProfessore;
import com.laureapp.ui.roomdb.repository.TesiProfessoreRepository;

import java.util.List;

public class TesiProfessoreModelView {

    private final TesiProfessoreRepository tesiProfessoreRepository;

    public TesiProfessoreModelView(Context context){
        tesiProfessoreRepository = new TesiProfessoreRepository(context);
    }

    public void insertTesiProfessore(TesiProfessore tesiProfessore){
        tesiProfessoreRepository.insertTesiProfessore(tesiProfessore);
    }

    public void updateTesiProfessore(TesiProfessore tesiProfessore){
        tesiProfessoreRepository.updateTesiProfessore(tesiProfessore);
    }

    public boolean delateTesiProfessore(long id){
        return tesiProfessoreRepository.delateTesiProfessore(id);
    }

    public TesiProfessore findAllById(Long id){
        return tesiProfessoreRepository.findAllById(id);
    }

    public List<TesiProfessore> getAllTesiProfessore(){
        return tesiProfessoreRepository.getAllTesiProfessore();
    }
}
