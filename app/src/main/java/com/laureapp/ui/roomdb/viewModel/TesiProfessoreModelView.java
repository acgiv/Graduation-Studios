package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.TesiProfessore;
import com.laureapp.ui.roomdb.repository.TesiProfessoreRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Questa classe agisce come ViewModel per l'entità StudenteTesi
 * Gestisce tutte le operazioni di business logic per l'entità StudenteTesi,
 * comunicando con il repository associato per l'accesso al database
 */
public class TesiProfessoreModelView {

    private final TesiProfessoreRepository tesiProfessoreRepository;

    /**
     * Costruttore della classe TesiProfessoreModelView
     * @param context Il contesto dell'applicazione
     */
    public TesiProfessoreModelView(Context context){
        tesiProfessoreRepository = new TesiProfessoreRepository(context);
    }

    /**
     * Inserisce un oggetto TesiProfessore nel database
     * @param tesiProfessore L'oggetto TesiProfessore da inserire
     */
    public void insertTesiProfessore(TesiProfessore tesiProfessore){
        tesiProfessoreRepository.insertTesiProfessore(tesiProfessore);
    }

    /**
     * Aggiorna un oggetto TesiProfessore nel database
     * @param tesiProfessore L'oggetto TesiProfessore da aggiornare
     */
    public void updateTesiProfessore(TesiProfessore tesiProfessore){
        tesiProfessoreRepository.updateTesiProfessore(tesiProfessore);
    }

    /**
     * Elimina un oggetto TesiProfessore dal database in base all'ID specificato
     * @param id dell'oggetto TesiProfessore da eliminare
     * @return True se l'eliminazione ha avuto successo, altrimenti False
     */
    public boolean deleteTesiProfessore(long id){
        return tesiProfessoreRepository.deleteTesiProfessore(id);
    }

    /**
     * Trova TesiProfessore in base all'ID specificato.
     * @param id l'ID specificato
     * @return l'istanza di TesiProfessore corrispondente all'ID specificato,
     */
    public TesiProfessore findAllById(Long id){
        return tesiProfessoreRepository.findAllById(id);
    }

    /**
     * Ottiene tutti gli oggetti TesiProfessore dal database.
     * @return una lista di oggetti TesiProfessore, una lista vuota se si verifica un'eccezione
     */
    public List<TesiProfessore> getAllTesiProfessore(){
        return tesiProfessoreRepository.getAllTesiProfessore();
    }
}
