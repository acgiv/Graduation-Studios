package com.laureapp.ui.roomdb.viewModel;
import android.content.Context;
import android.util.Log;

import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.repository.UtenteRepository;


import java.util.List;

/**
 * Questa classe agisce come ViewModel per l'entità Utente
 * Gestisce tutte le operazioni di business logic per l'entità Utente,
 * comunicando con il repository associato per l'accesso al database
 */
public class UtenteModelView{


    private UtenteRepository utenteRepository;

    /**
     * Costruttore per UtenteModelView che inizializza il repository.
     * @param context Il contesto corrente dell'applicazione.
     */
    public UtenteModelView(Context context){
        utenteRepository = new UtenteRepository(context);
    }

    /**
     * Inserisce un oggetto Utente nel database.
     * @param utente l'oggetto Utente da inserire nel database.
     */
    public void insertUtente(Utente utente){
        utenteRepository.insertUtente(utente);
    }

    /**
     * Aggiorna un oggetto Utente esistente nel database.
     * @param utente l'oggetto Utente da aggiornare nel database.
     */
    public void updateUtente(Utente utente){
        utenteRepository.updateUtente(utente);
    }

    /**
     * Elimina un oggetto Utente dal database in base all'ID specificato.
     * @param id l'ID dell'utente da eliminare dal database.
     * @return true se l'eliminazione ha successo, altrimenti false.
     */
    public boolean deleteUtente(long id){
        return utenteRepository.deleteUtente(id);
    }

    /**
     * Trova un oggetto Utente nel database in base all'ID specificato.
     * @param id l'ID dell'utente da cercare nel database.
     * @return l'oggetto Utente corrispondente all'ID specificato.
     */
    public Utente findAllById(Long id){
        return utenteRepository.findAllById(id);
    }

    /**
     * Ottiene l'ID dell'utente in base all'email specificata.
     * @param email l'email dell'utente per cui ottenere l'ID.
     * @return l'ID dell'utente corrispondente all'email specificata.
     */
    public Long getIdUtente(String email){
        return utenteRepository.getIdUtente(email);
    }

    public String getNome(){
        return utenteRepository.getNome();
    }

    public String getCognome(){
        return utenteRepository.getCognome();
    }

    public String getEmail(Long id_utente){
        return utenteRepository.getEmail(id_utente);
    }
    public String getFacolta(Long id_utente){
        return utenteRepository.getFacolta(id_utente);
    }
    public String getNomeCdl(Long id_utente){
        return utenteRepository.getNomeCdl(id_utente);
    }

    /**
     * Verifica l'esistenza di un utente nel database in base all'email e alla password specificate.
     * @param email l'email dell'utente da verificare.
     * @param password la password dell'utente da verificare.
     * @return l'oggetto Utente corrispondente all'email e alla password specificate se esiste, altrimenti un nuovo oggetto Utente.
     */
    public Utente is_exist_email_password(String email, String password){
        Utente utente = utenteRepository.is_exist_email_password(email, password);
        if (utente != null) {
            return utente;
        }else{
            return new Utente();
        }
    }

    /**
     * Ottiene tutti gli oggetti Utente presenti nel database.
     * @return una lista di tutti gli oggetti Utente presenti nel database.
     */
    public List<Utente> getAllUtente(){
        return utenteRepository.getAllUtente();
    }

}