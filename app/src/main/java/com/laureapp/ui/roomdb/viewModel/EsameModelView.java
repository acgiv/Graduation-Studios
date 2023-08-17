package com.laureapp.ui.roomdb.viewModel;
import android.content.Context;
import com.laureapp.ui.roomdb.entity.Esame;
import com.laureapp.ui.roomdb.repository.EsameRepository;
import java.util.List;

public class EsameModelView {

    private final EsameRepository esameRepository;

    public EsameModelView(Context context){
        esameRepository = new EsameRepository(context);
    }

    public void insertEsame(Esame esame){
        esameRepository.insertEsame(esame);
    }

    public void updateEsame(Esame esame){
        esameRepository.updateEsame(esame);
    }

    public boolean delateEsame(long id){
        return esameRepository.delateEsame(id);
    }

    public Esame findAllById(Long id){
        return esameRepository.findAllById(id);
    }

    public List<Esame> getAllEsame(){
        return esameRepository.getAllEsame();
    }
}
