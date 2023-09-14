package com.laureapp.ui.roomdb.viewModel;
import android.content.Context;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.repository.TesiRepository;
import java.util.List;


public class TesiModelView {

    private final TesiRepository tesiRepository;

    public TesiModelView(Context context){
        tesiRepository = new TesiRepository(context);
    }

    public void insertTesi(Tesi tesi){
        tesiRepository.insertTesi(tesi);
    }

    public void updateTesi(Tesi tesi){
        tesiRepository.updateTesi(tesi);
    }

    public boolean delateTesi(long id){
        return tesiRepository.deleteTesi(id);
    }

    public Tesi findAllById(Long id){
        return tesiRepository.findAllById(id);
    }

    public List<Tesi> getAllTesi(){
        return tesiRepository.getAllTesi();
    }
}
