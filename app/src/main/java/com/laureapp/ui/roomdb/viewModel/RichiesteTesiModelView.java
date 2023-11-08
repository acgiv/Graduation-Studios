package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.RichiesteTesi;
import com.laureapp.ui.roomdb.entity.StudenteTesi;
import com.laureapp.ui.roomdb.repository.RichiesteTesiRepository;
import com.laureapp.ui.roomdb.repository.StudenteTesiRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Questa classe agisce come ViewModel per l'entità FileTesi
 * Gestisce tutte le operazioni di business logic per l'entità FileTesi,
 * comunicando con il repository associato per l'accesso al database
 */
public class RichiesteTesiModelView {

    private final RichiesteTesiRepository richiesteTesiRepository;

    /**
     * Costruttore della classe RichiestaTesiModelView
     * @param context Il contesto dell'applicazione
     */
    public RichiesteTesiModelView(Context context){
        richiesteTesiRepository = new RichiesteTesiRepository(context);
    }

    /**
     * Inserisce un oggetto RichiestaTesi nel database
     * @param richiesteTesi L'oggetto RichiestaTesi da inserire
     */
    public void insertRichiesteTesi(RichiesteTesi richiesteTesi){
        richiesteTesiRepository.insertRichiesteTesi(richiesteTesi);
    }

    /**
     * Aggiorna un oggetto RichiestaTesi nel database
     * @param richiesteTesi L'oggetto FileTesi da aggiornare
     */
    public void updateRichiesteTesi(RichiesteTesi richiesteTesi){
        richiesteTesiRepository.updateRichiesteTesi(richiesteTesi);
    }

    /**
     * Elimina un oggetto RichiestaTesi dal database in base all'ID specificato
     * @param id dell'oggetto RichiestaTesi da eliminare
     * @return True se l'eliminazione ha avuto successo, altrimenti False
     */
    public boolean deleteRichiesteTesi(long id){
        return richiesteTesiRepository.deleteRichiesteTesi(id);
    }

    /**
     * Trova un oggetto RichiestaTesi nel database in base all'ID specificato
     * @param id L'ID dell'oggetto RichiestaTesi da trovare
     * @return L'oggetto FileTesi corrispondente all'ID specificato, se presente, altrimenti un nuovo oggetto FileTesi vuoto
     */
    public RichiesteTesi findAllById(Long id){
        return richiesteTesiRepository.findAllById(id);
    }

    /**
     * Ottiene tutti gli oggetti RichiestaTesi presenti nel database
     * @return lista di tutti gli oggetti RichiestaTesi presenti nel database
     */
    public List<RichiesteTesi> getAllRichiesteTesi(){
        return richiesteTesiRepository.getAllRichiesteTesi();
    }





}
