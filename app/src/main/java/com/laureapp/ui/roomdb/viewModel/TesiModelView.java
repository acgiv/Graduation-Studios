package com.laureapp.ui.roomdb.viewModel;
import android.content.Context;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.repository.TesiRepository;
import java.util.List;

/**
 * Questa classe agisce come ViewModel per l'entità Tesi
 * Gestisce tutte le operazioni di business logic per l'entità Tesi,
 * comunicando con il repository associato per l'accesso al database
 */
public class TesiModelView {

    private final TesiRepository tesiRepository;

    /**
     * Costruttore per TesiModelView che inizializza il repository.
     * @param context Il contesto corrente dell'applicazione.
     */
    public TesiModelView(Context context){
        tesiRepository = new TesiRepository(context);
    }

    /**
     * Inserisce un'istanza di Tesi nel repository.
     * @param tesi L'istanza di Tesi da inserire nel repository.
     */
    public void insertTesi(Tesi tesi){
        tesiRepository.insertTesi(tesi);
    }

    /**
     * Aggiorna un'istanza esistente di Tesi nel repository.
     * @param tesi L'istanza di Tesi da aggiornare nel repository.
     */
    public void updateTesi(Tesi tesi){
        tesiRepository.updateTesi(tesi);
    }

    /**
     * Elimina un'istanza di Tesi dal repository tramite l'ID specificato.
     * @param id L'ID dell'istanza di Tesi da eliminare.
     * @return true se l'eliminazione ha avuto successo, false altrimenti.
     */
    public boolean deleteTesi(long id){
        return tesiRepository.deleteTesi(id);
    }

    /**
     * Trova un'istanza di Tesi nel repository tramite l'ID specificato.
     * @param id L'ID dell'istanza di Tesi da trovare.
     * @return L'istanza di Tesi corrispondente all'ID specificato, se presente nel repository.
     */
    public Tesi findAllById(Long id){
        return tesiRepository.findAllById(id);
    }

    /**
     * Ottiene tutti gli elementi di Tesi dal repository.
     * @return Una lista di tutte le istanze di Tesi presenti nel repository.
     */
    public List<Tesi> getAllTesi(){
        return tesiRepository.getAllTesi();
    }
}
