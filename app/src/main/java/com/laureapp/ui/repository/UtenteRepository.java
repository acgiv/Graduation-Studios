package com.laureapp.ui.repository;

import android.content.Context;



import com.laureapp.ui.RoomDbSqlLite;
import com.laureapp.ui.entity.Utente;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UtenteRepository {

    private RoomDbSqlLite roomDbSqlLite;
    // questa classe ritorna un singolo thread ogni volta  consentendo alle query di essere eseguite in background
    private Executor executor = Executors.newSingleThreadExecutor();

    public UtenteRepository(Context context){
        roomDbSqlLite = RoomDbSqlLite.getDatabase(context);
    }

    public boolean insertUtente(Utente utente){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                roomDbSqlLite.utenteDao().insert(utente);
            }
        });

        return true;
    }

    public boolean updateUtente(Utente utente){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                roomDbSqlLite.utenteDao().update(utente);
            }
        });

        return true;
    }

    public Utente findAllById(Long id){
        final Utente[] utente = new Utente[1];
        executor.execute(new Runnable() {
            @Override
            public void run() {
               utente[0] =  roomDbSqlLite.utenteDao().findAllById(id);
            }
        });
        return utente[0];
    }

    public boolean delateUtente(Long id){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                roomDbSqlLite.utenteDao().delete(findAllById(id));
            }
        });

        return true;
    }
}
