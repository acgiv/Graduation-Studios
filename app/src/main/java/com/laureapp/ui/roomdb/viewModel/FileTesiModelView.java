package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.FileTesi;
import com.laureapp.ui.roomdb.entity.RichiesteTesi;
import com.laureapp.ui.roomdb.repository.FileTesiRepository;
import com.laureapp.ui.roomdb.repository.RichiesteTesiRepository;

import java.io.File;
import java.util.List;

/**
 * Questa classe agisce come ViewModel per l'entità FileTesi
 * Gestisce tutte le operazioni di business logic per l'entità FileTesi,
 * comunicando con il repository associato per l'accesso al database
 */
public class FileTesiModelView {

    private final FileTesiRepository fileTesiRepository;

    /**
     * Costruttore della classe FileTesiModelView
     * @param context Il contesto dell'applicazione
     */
    public FileTesiModelView(Context context){
        fileTesiRepository = new FileTesiRepository(context);
    }

    /**
     * Inserisce un oggetto FileTesi nel database
     * @param fileTesi L'oggetto da inserire
     */
    public void insertFileTesi(FileTesi fileTesi){
        fileTesiRepository.insertFileTesi(fileTesi);
    }

    /**
     * Aggiorna un oggetto FileTesi nel database
     * @param fileTesi L'oggetto FileTesi da aggiornare
     */
    public void updateRichiesteTesi(FileTesi fileTesi){
        fileTesiRepository.updateRichiesteTesi(fileTesi);
    }

    /**
     * Elimina un oggetto FileTesi dal database in base all'ID specificato.
     * @param id L'ID dell'oggetto FileTesi da eliminare.
     * @return True se l'eliminazione ha avuto successo, altrimenti False.
     */
    public boolean deleteRichiesteTesi(long id){
        return fileTesiRepository.deleteFileTesi(id);
    }

    /**
     * Trova un oggetto FileTesi nel database in base all'ID specificato.
     * @param id L'ID dell'oggetto FileTesi da trovare.
     * @return L'oggetto FileTesi corrispondente all'ID specificato.
     */
    public FileTesi findAllById(Long id){
        return fileTesiRepository.findAllById(id);
    }

    /**
     * Ottiene tutti gli oggetti FileTesi presenti nel database.
     * @return Una lista di tutti gli oggetti FileTesi presenti nel database.
     */
    public List<FileTesi> getAllFileTesi(){
        return fileTesiRepository.getAllFileTesi();
    }

}
