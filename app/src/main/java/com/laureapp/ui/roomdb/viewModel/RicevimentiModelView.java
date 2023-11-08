package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.Ricevimenti;
import com.laureapp.ui.roomdb.repository.RicevimentiRepository;

import java.util.List;

public class RicevimentiModelView {

    private final RicevimentiRepository ricevimentiRepository;

    public RicevimentiModelView(Context context){
        ricevimentiRepository = new RicevimentiRepository(context);
    }

    public void insertRicevimenti(Ricevimenti ricevimenti){
        ricevimentiRepository.insertRicevimenti(ricevimenti);
    }

    public void updateRicevimenti(Ricevimenti ricevimenti){ricevimentiRepository.updateRicevimenti(ricevimenti);
    }

    public boolean deleteRicevimenti(long id){
        return ricevimentiRepository.deleteRicevimenti(id);
    }

    public Ricevimenti findAllById(Long id){
        return ricevimentiRepository.findAllById(id);
    }

    public List<Ricevimenti> getAllRicevimenti(){return ricevimentiRepository.getAllRicevimenti();}
}
