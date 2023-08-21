package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.CorsoStudente;
import com.laureapp.ui.roomdb.repository.CorsoStudenteRepository;

import java.util.List;

public class CorsoStudenteModelView {

    private final CorsoStudenteRepository corsoStudenteRepository;

    public CorsoStudenteModelView(Context context){
        corsoStudenteRepository = new CorsoStudenteRepository(context);
    }

    public void insertCorsoStudente(CorsoStudente corsoStudente){
        corsoStudenteRepository.insertCorsoStudente(corsoStudente);
    }

    public void updateCorsoStudente(CorsoStudente corsoStudente){
        corsoStudenteRepository.updateCorsoStudente(corsoStudente);
    }

    public boolean delateCorsoStudente(long id){
        return corsoStudenteRepository.delateCorsoStudente(id);
    }

    public CorsoStudente findAllById(Long id){
        return corsoStudenteRepository.findAllById(id);
    }

    public List<CorsoStudente> getAllCorsoStudente(){
        return corsoStudenteRepository.getAllCorsoStudente();
    }
}
