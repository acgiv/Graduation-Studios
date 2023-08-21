package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.repository.ProfessoreRepository;

import java.util.List;

public class ProfessoreModelView {

    private final ProfessoreRepository professoreRepository;

    public ProfessoreModelView(Context context){
        professoreRepository = new ProfessoreRepository(context);
    }

    public void insertProfessore(Professore professore){
        professoreRepository.insertProfessore(professore);
    }

    public void updateProfessore(Professore professore){
        professoreRepository.updateProfessore(professore);
    }

    public boolean delateProfessore(long id){
        return professoreRepository.delateProfessore(id);
    }

    public Professore findAllById(Long id){
        return professoreRepository.findAllById(id);
    }

    public List<Professore> getAllProfessore(){
        return professoreRepository.getAllProfessore();
    }
}
