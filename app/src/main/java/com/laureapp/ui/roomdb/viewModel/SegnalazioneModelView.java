package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;
import com.laureapp.ui.roomdb.entity.Segnalazione;
import com.laureapp.ui.roomdb.repository.SegnalazioneRepository;

import java.util.List;

/**
 * Questa classe agisce come ViewModel per l'entità Segnalazione
 * Gestisce tutte le operazioni di business logic per l'entità Segnalazione,
 * comunicando con il repository associato per l'accesso al database
 */

public class SegnalazioneModelView {

    /*
    I ViewModel sono classi utilizzate per gestire la logica di visualizzazione dei dati nell'applicazione Android
    Forniscono un'interfaccia semplificata per l'accesso ai dati e la comunicazione tra la UI e il repository
     */

    /*
    In un'applicazione Android, la business logic si riferisce alle operazioni e ai calcoli che gestiscono i dati,
    le regole di validazione, le trasformazioni dei dati e altre logiche specifiche dell'applicazione.
    Questa logica è responsabile di gestire le interazioni tra i dati e l'interfaccia utente,
    garantendo che le operazioni siano eseguite correttamente e in modo coerente.
     */
    private final SegnalazioneRepository segnalazioneRepository;

    /**
     * Costruttore SegnalazioneModelView
     * @param context Il contesto dell'applicazione
     */
    public SegnalazioneModelView(Context context) {
        segnalazioneRepository = new SegnalazioneRepository(context);
    }

    /**
     * Inserisce una nuova segnalazione nel database.
     * @param segnalazione l'oggetto Segnalazione da inserire.
     */
    public void insertSegnalazione(Segnalazione segnalazione) {
        segnalazioneRepository.insertSegnalazione(segnalazione);
        // Puoi eseguire ulteriori operazioni dopo l'inserimento se necessario
    }

    /**
     * Aggiorna una segnalazione esistente nel database.
     * @param segnalazione l'oggetto Segnalazione da aggiornare.
     */
    public void updateSegnalazione(Segnalazione segnalazione) {
        segnalazioneRepository.updateSegnalazione(segnalazione);
        // Puoi eseguire ulteriori operazioni dopo l'aggiornamento se necessario
    }

    /**
     * Elimina una segnalazione dal database in base all'ID della segnalazione specificato.
     * @param idSegnalazione l'ID della segnalazione da eliminare.
     * @return true se la segnalazione viene eliminata con successo, altrimenti false.
     */
    public boolean deleteSegnalazioneById(Long idSegnalazione) {
        return segnalazioneRepository.deleteSegnalazioneById(idSegnalazione);
    }

    /**
     * Trova una segnalazione nel database in base all'ID della segnalazione specificato.
     * @param idSegnalazione l'ID della segnalazione da cercare.
     * @return l'oggetto Segnalazione corrispondente all'ID specificato.
     */
    public Segnalazione findSegnalazioneById(Long idSegnalazione) {
        return segnalazioneRepository.findSegnalazioneById(idSegnalazione);
    }

    /**
     * Trova tutte le segnalazioni correlate a un determinato ID dello studente nella tabella Segnalazione.
     * @param idStudenteTesi l'ID dello studente collegato alle segnalazioni da cercare.
     * @return una lista di Segnalazioni correlate all'ID dello studente specificato.
     */
    public List<Segnalazione> findSegnalazioniByStudenteTesiId(Long idStudenteTesi) {
        return segnalazioneRepository.findSegnalazioniByStudenteTesiId(idStudenteTesi);
    }

    // Altri metodi specifici per la gestione delle segnalazioni



}

