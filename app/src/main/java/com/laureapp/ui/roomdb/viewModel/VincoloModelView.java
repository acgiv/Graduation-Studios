package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.Vincolo;
import com.laureapp.ui.roomdb.repository.VincoloRepository;

import java.util.List;

public class VincoloModelView {

    private final VincoloRepository vincoloRepository;

    public VincoloModelView(Context context){
        vincoloRepository = new VincoloRepository(context);
    }

    public void insertVincolo(Vincolo vincolo){
        vincoloRepository.insertVincolo(vincolo);
    }

    public void updateVincolo(Vincolo vincolo){
        vincoloRepository.updateVincolo(vincolo);
    }

    public boolean delateVincolo(long id){
        return vincoloRepository.delateVincolo(id);
    }

    public Vincolo findAllById(Long id){
        return vincoloRepository.findAllById(id);
    }

    public List<Vincolo> getAllVincolo(){
        return vincoloRepository.getAllVincolo();
    }

}
