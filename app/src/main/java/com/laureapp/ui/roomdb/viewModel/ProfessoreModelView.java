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
        professore.setId_professore(findProfessoreMatricola(Long.valueOf(professore.getMatricola())));
    }
    public  Long findProfessoreMatricola(Long matricola){
        return professoreRepository.findProfessoreMatricola(matricola);
    }

    public void updateProfessore(Professore professore){
        professoreRepository.updateProfessore(professore);
    }

    public Long findPorfessore(Long id_utente) {
        return professoreRepository.findPorfessore(id_utente);
    }


    public boolean deleteProfessore(long id){
        return professoreRepository.deleteProfessore(id);
    }

    public Professore findAllById(Long id){
        return professoreRepository.findAllById(id);
    }

    public List<Professore> getAllProfessore(){
        return professoreRepository.getAllProfessore();
    }
}
