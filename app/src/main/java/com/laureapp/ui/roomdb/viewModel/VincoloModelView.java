package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.Vincolo;
import com.laureapp.ui.roomdb.repository.VincoloRepository;

import java.util.List;

/**
 * Questa classe agisce come ViewModel per l'entità Vincolo
 * Gestisce tutte le operazioni di business logic per l'entità Vincolo,
 * comunicando con il repository associato per l'accesso al database
 */
public class VincoloModelView {


    private final VincoloRepository vincoloRepository;

    /**
     * Costruttore per VincoloModelView che inizializza il repository.
     * @param context Il contesto corrente dell'applicazione.
     */
    public VincoloModelView(Context context){
        vincoloRepository = new VincoloRepository(context);
    }

    /**
     * Inserisce un'istanza di Vincolo nel repository.
     * @param vincolo L'istanza di Vincolo da inserire nel repository.
     */
    public void insertVincolo(Vincolo vincolo){
        vincoloRepository.insertVincolo(vincolo);
    }

    /**
     * Aggiorna un'istanza esistente di Vincolo nel repository.
     * @param vincolo L'istanza di Vincolo da aggiornare nel repository.
     */
    public void updateVincolo(Vincolo vincolo){
        vincoloRepository.updateVincolo(vincolo);
    }

    /**
     * Elimina un'istanza di Vincolo dal repository tramite l'ID specificato.
     * @param id L'ID dell'istanza di Vincolo da eliminare.
     * @return true se l'eliminazione ha avuto successo, false altrimenti.
     */
    public boolean deleteVincolo(long id){
        return vincoloRepository.deleteVincolo(id);
    }

    /**
     * Trova un'istanza di Vincolo nel repository tramite l'ID specificato.
     * @param id L'ID dell'istanza di Vincolo da trovare.
     * @return L'istanza di Vincolo corrispondente all'ID specificato, se presente nel repository.
     */
    public Vincolo findAllById(Long id){
        return vincoloRepository.findAllById(id);
    }

    /**
     * Ottiene tutti gli elementi di Vincolo dal repository.
     * @return Una lista di tutte le istanze di Vincolo presenti nel repository.
     */
    public List<Vincolo> getAllVincolo(){
        return vincoloRepository.getAllVincolo();
    }

}
