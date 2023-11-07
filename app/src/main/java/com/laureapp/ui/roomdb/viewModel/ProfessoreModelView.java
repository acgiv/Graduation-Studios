package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;

import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.repository.ProfessoreRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Questa classe agisce come ViewModel per l'entità Professore
 * Gestisce tutte le operazioni di business logic per l'entità Professore,
 * comunicando con il repository associato per l'accesso al database
 */
public class ProfessoreModelView {

    private final ProfessoreRepository professoreRepository;

    /**
     * Costruttore della classe ProfessoreModelView
     * @param context Il contesto dell'applicazione
     */
    public ProfessoreModelView(Context context){
        professoreRepository = new ProfessoreRepository(context);
    }

    /**
     * Inserisce un oggetto Profesore nel database
     * @param professore L'oggetto Professore da inserire
     */
    public void insertProfessore(Professore professore){
        professoreRepository.insertProfessore(professore);
        professore.setId_professore(findProfessoreMatricola(Long.valueOf(professore.getMatricola())));
    }

    /**
     * Trova un oggetto Professore nel database in base alla matricola specificato
     * @param matricola La matricola dell'oggetto Professore da trovare
     * @return L'oggetto Professore corrispondente alla matricola, se presente, altrimenti un nuovo oggetto Professore vuoto
     */
    public  Long findProfessoreMatricola(Long matricola){
        return professoreRepository.findProfessoreMatricola(matricola);
    }

    /**
     * Aggiorna un oggetto Professore nel database
     * @param professore L'oggetto Professore da aggiornare
     */
    public void updateProfessore(Professore professore){
        professoreRepository.updateProfessore(professore);
    }

    /**
     * Trova un oggetto Professore nel database in base all'ID utente
     * @param id_utente L'ID dell'oggetto Professore da trovare
     * @return L'oggetto Professore corrispondente all'ID specificato, se presente, altrimenti un nuovo oggetto Professore vuoto
     */
    public Long findProfessore(Long id_utente) {
        return professoreRepository.findProfessore(id_utente);
    }

    /**
     * Elimina un oggetto Professore dal database in base all'ID specificato
     * @param id dell'oggetto Professore da eliminare
     * @return True se l'eliminazione ha avuto successo, altrimenti False
     */
    public boolean deleteProfessore(long id){
        return professoreRepository.deleteProfessore(id);
    }

    /**
     * Trova un oggetto Professore nel database in base all'ID specificato
     * @param id L'ID dell'oggetto Professore da trovare
     * @return L'oggetto Professore corrispondente all'ID specificato, se presente, altrimenti un nuovo oggetto Professore vuoto
     */
    public Professore findAllById(Long id){
        return professoreRepository.findAllById(id);
    }

    /**
     * Ottiene tutti gli oggetti Professore presenti nel database
     * @return lista di tutti gli oggetti Professore presenti nel database
     */
    public List<Professore> getAllProfessore(){
        return professoreRepository.getAllProfessore();
    }
}
