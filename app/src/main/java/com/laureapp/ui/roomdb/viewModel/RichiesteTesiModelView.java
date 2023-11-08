package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.RichiesteTesi;
import com.laureapp.ui.roomdb.entity.StudenteTesi;
import com.laureapp.ui.roomdb.repository.RichiesteTesiRepository;
import com.laureapp.ui.roomdb.repository.StudenteTesiRepository;

import java.util.List;

public class RichiesteTesiModelView {

    private final RichiesteTesiRepository richiesteTesiRepository;

    public RichiesteTesiModelView(Context context){
        richiesteTesiRepository = new RichiesteTesiRepository(context);
    }

    public void insertRichiesteTesi(RichiesteTesi richiesteTesi){
        richiesteTesiRepository.insertRichiesteTesi(richiesteTesi);
    }

    public void updateRichiesteTesi(RichiesteTesi richiesteTesi){
        richiesteTesiRepository.updateRichiesteTesi(richiesteTesi);
    }

    public boolean deleteRichiesteTesi(long id){
        return richiesteTesiRepository.deleteRichiesteTesi(id);
    }

    public RichiesteTesi findAllById(Long id){
        return richiesteTesiRepository.findAllById(id);
    }

    public List<RichiesteTesi> getAllRichiesteTesi(){
        return richiesteTesiRepository.getAllRichiesteTesi();
    }





}
