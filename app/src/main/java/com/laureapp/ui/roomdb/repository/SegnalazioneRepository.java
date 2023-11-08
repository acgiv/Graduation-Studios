package com.laureapp.ui.roomdb.repository;

import android.content.Context;

import com.laureapp.ui.roomdb.RoomDbSqlLite;
import com.laureapp.ui.roomdb.entity.Segnalazione;
import com.laureapp.ui.roomdb.dao.SegnalazioneDao;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Questa classe funge da repository per l'entità Segnalazioni
 * Gestisce tutte le operazioni di accesso al database per l'entità Segnalazioni,
 * inclusa l'inserimento, l'aggiornamento, la ricerca e l'eliminazione dei dati correlati
 */
public class SegnalazioneRepository {

    /* Repository:
    I repository sono classi che fungono da intermediari tra l'applicazione e il database.
    Gestiscono la logica di accesso ai dati utilizzando i DAO.
    Eseguono operazioni CRUD in thread separati per evitare il blocco dell'interfaccia utente.
     */
    private final RoomDbSqlLite roomDbSqlLite;
    private final SegnalazioneDao segnalazioneDao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Costruttore della classe SegnalazioniRepository
     * @param context Il contesto dell'applicazione
     */
    public SegnalazioneRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
        this.segnalazioneDao = roomDbSqlLite.segnalazioneDao();
    }

    /**
     * Inserisce una nuova segnalazione nel database.
     * @param segnalazione l'oggetto Segnalazione da inserire.
     */
    public void insertSegnalazione(Segnalazione segnalazione) {
        executor.execute(() -> segnalazioneDao.insert(segnalazione));
    }

    /**
     * Aggiorna una segnalazione nel database.
     * @param segnalazione l'oggetto Segnalazione da aggiornare.
     */
    public void updateSegnalazione(Segnalazione segnalazione) {
        executor.execute(() -> segnalazioneDao.update(segnalazione));
    }

    /**
     * Elimina una segnalazione nel database.
     * @param segnalazione l'oggetto Segnalazione da eliminare.
     */
    public void deleteSegnalazione(Segnalazione segnalazione) {
        executor.execute(() -> segnalazioneDao.delete(segnalazione));
    }

    /**
     * Trova una segnalazione nel database in base all'ID della segnalazione specificato.
     * @param idSegnalazione l'ID della segnalazione da cercare.
     * @return l'oggetto Segnalazione corrispondente all'ID specificato.
     * @throws InterruptedException se l'operazione viene interrotta mentre è in attesa.
     * @throws ExecutionException se l'operazione ha un errore durante l'esecuzione.
     */
    public Segnalazione findSegnalazioneById(Long idSegnalazione) {
        CompletableFuture<Segnalazione> future = new CompletableFuture<>();
        executor.execute(() -> {
            Segnalazione segnalazione = segnalazioneDao.findById(idSegnalazione);
            future.complete(segnalazione);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Trova tutte le segnalazioni correlate a un determinato ID dello studente nella tabella Segnalazione.
     * @param idStudenteTesi l'ID dello studente collegato alle segnalazioni da cercare.
     * @return una lista di Segnalazioni correlate all'ID dello studente specificato.
     * @throws InterruptedException se l'operazione viene interrotta mentre è in attesa.
     * @throws ExecutionException se l'operazione ha un errore durante l'esecuzione.
     */
    public List<Segnalazione> findSegnalazioniByStudenteTesiId(Long idStudenteTesi) {
        CompletableFuture<List<Segnalazione>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<Segnalazione> segnalazioni = segnalazioneDao.findByStudenteTesiId(idStudenteTesi);
            future.complete(segnalazioni);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Elimina una segnalazione dal database in base all'ID della segnalazione specificato.
     * @param idSegnalazione l'ID della segnalazione da eliminare.
     * @return true se la segnalazione viene eliminata con successo, altrimenti false.
     * @throws InterruptedException se l'operazione viene interrotta mentre è in attesa.
     * @throws ExecutionException se l'operazione ha un errore durante l'esecuzione.
     */
    public boolean deleteSegnalazioneById(Long idSegnalazione) {
        boolean result = false;
        Segnalazione segnalazione = findSegnalazioneById(idSegnalazione);
        if (segnalazione != null) {
            executor.execute(() -> segnalazioneDao.delete(segnalazione));
            result = true;
        }
        return result;
    }

    // Altri metodi specifici per la gestione delle segnalazioni

}


