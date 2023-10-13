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
 * Repository:
 *
 * I repository sono classi che fungono da intermediari tra l'applicazione e il database.
 * Gestiscono la logica di accesso ai dati utilizzando i DAO.
 * Eseguono operazioni CRUD in thread separati per evitare il blocco dell'interfaccia utente.
 */

public class SegnalazioneRepository {

    private final RoomDbSqlLite roomDbSqlLite;
    private final SegnalazioneDao segnalazioneDao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public SegnalazioneRepository(Context context) {
        this.roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
        this.segnalazioneDao = roomDbSqlLite.segnalazioneDao();
    }

    public void insertSegnalazione(Segnalazione segnalazione) {
        executor.execute(() -> segnalazioneDao.insert(segnalazione));
    }

    public void updateSegnalazione(Segnalazione segnalazione) {
        executor.execute(() -> segnalazioneDao.update(segnalazione));
    }

    public void deleteSegnalazione(Segnalazione segnalazione) {
        executor.execute(() -> segnalazioneDao.delete(segnalazione));
    }

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

    public List<Segnalazione> findSegnalazioniByTesiId(Long idTesi) {
        CompletableFuture<List<Segnalazione>> future = new CompletableFuture<>();
        executor.execute(() -> {
            List<Segnalazione> segnalazioni = segnalazioneDao.findByTesiId(idTesi);
            future.complete(segnalazioni);
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

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


