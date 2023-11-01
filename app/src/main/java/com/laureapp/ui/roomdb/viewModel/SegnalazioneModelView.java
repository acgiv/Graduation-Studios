package com.laureapp.ui.roomdb.viewModel;

import android.content.Context;
import com.laureapp.ui.roomdb.entity.Segnalazione;
import com.laureapp.ui.roomdb.repository.SegnalazioneRepository;

import java.util.List;

/**
 * ViewModel:
*
*I ViewModel sono classi utilizzate per gestire la logica di visualizzazione dei dati nell'applicazione Android.
*Forniscono un'interfaccia semplificata per l'accesso ai dati e la comunicazione tra la UI e il repository.
**/

public class SegnalazioneModelView {

    private final SegnalazioneRepository segnalazioneRepository;

    public SegnalazioneModelView(Context context) {
        segnalazioneRepository = new SegnalazioneRepository(context);
    }

    public void insertSegnalazione(Segnalazione segnalazione) {
        segnalazioneRepository.insertSegnalazione(segnalazione);
        // Puoi eseguire ulteriori operazioni dopo l'inserimento se necessario
    }

    public void updateSegnalazione(Segnalazione segnalazione) {
        segnalazioneRepository.updateSegnalazione(segnalazione);
        // Puoi eseguire ulteriori operazioni dopo l'aggiornamento se necessario
    }

    public boolean deleteSegnalazioneById(Long idSegnalazione) {
        return segnalazioneRepository.deleteSegnalazioneById(idSegnalazione);
    }

    public Segnalazione findSegnalazioneById(Long idSegnalazione) {
        return segnalazioneRepository.findSegnalazioneById(idSegnalazione);
    }

    public List<Segnalazione> findSegnalazioniByStudenteTesiId(Long idStudenteTesi) {
        return segnalazioneRepository.findSegnalazioniByStudenteTesiId(idStudenteTesi);
    }

    // Altri metodi specifici per la gestione delle segnalazioni



}

