package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.Ricevimenti;
import com.laureapp.ui.roomdb.repository.RicevimentiRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Questa classe agisce come ViewModel per l'entità Ricevimenti
 * Gestisce tutte le operazioni di business logic per l'entità Ricevimenti,
 * comunicando con il repository associato per l'accesso al database
 */
public class RicevimentiModelView {

    private final RicevimentiRepository ricevimentiRepository;

    /**
     * Costruttore della classe RicevimentiModelView
     * @param context Il contesto dell'applicazione
     */
    public RicevimentiModelView(Context context){
        ricevimentiRepository = new RicevimentiRepository(context);
    }

    /**
     * Inserisce un oggetto Ricevimenti nel database
     * @param ricevimenti L'oggetto Ricevimenti da inserire
     */
    public void insertRicevimenti(Ricevimenti ricevimenti){
        ricevimentiRepository.insertRicevimenti(ricevimenti);
    }

    /**
     * Aggiorna un oggetto Ricevimenti nel database
     * @param ricevimenti L'oggetto Ricevimenti da aggiornare
     */
    public void updateRicevimenti(Ricevimenti ricevimenti){ricevimentiRepository.updateRicevimenti(ricevimenti);
    }

    /**
     * Elimina un oggetto Ricevimenti dal database in base all'ID specificato
     * @param id dell'oggetto Ricevimenti da eliminare
     * @return True se l'eliminazione ha avuto successo, altrimenti False
     */
    public boolean deleteRicevimenti(long id){
        return ricevimentiRepository.deleteRicevimenti(id);
    }

    /**
     * Trova un oggetto Ricevimenti nel database in base all'ID specificato
     * @param id L'ID dell'oggetto Ricevimenti da trovare
     * @return L'oggetto Ricevimenti corrispondente all'ID specificato, se presente, altrimenti un nuovo oggetto FileTesi vuoto
    */
    public Ricevimenti findAllById(Long id){
        return ricevimentiRepository.findAllById(id);
    }

    /**
     * Ottiene tutti gli oggetti Ricevimenti presenti nel database
     * @return lista di tutti gli oggetti Ricevimenti presenti nel database
     */
    public List<Ricevimenti> getAllRicevimenti(){return ricevimentiRepository.getAllRicevimenti();}
}
