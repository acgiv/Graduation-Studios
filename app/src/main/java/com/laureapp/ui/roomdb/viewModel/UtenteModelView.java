package com.laureapp.ui.roomdb.viewModel;
import android.content.Context;
import android.util.Log;

import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.repository.UtenteRepository;


import java.util.List;

public class UtenteModelView{


    private UtenteRepository utenteRepository;

    public UtenteModelView(Context context){
        utenteRepository = new UtenteRepository(context);
    }

    public void insertUtente(Utente utente){
        utenteRepository.insertUtente(utente);
    }

    public void updateUtente(Utente utente){
        utenteRepository.updateUtente(utente);
    }

    public boolean delateUtente(long id){
        return utenteRepository.delateUtente(id);
    }

    public Utente findAllById(Long id){
        return utenteRepository.findAllById(id);
   }

    public boolean is_exist_email_password(String email, String password){
        Utente utente = utenteRepository.is_exist_email_password(email, password);
        if (utente != null) {
            return utente.getId() != null;
        }else{
            return false;
        }
    }

    public List<Utente> getAllUtente(){
        return utenteRepository.getAllUtente();
    }

}
