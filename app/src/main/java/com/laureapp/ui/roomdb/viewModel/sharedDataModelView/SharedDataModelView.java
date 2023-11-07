package com.laureapp.ui.roomdb.viewModel.sharedDataModelView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.Utente;

/**
 * Questa classe rappresenta un ViewModel condiviso che memorizza e gestisce i dati degli oggetti Studente e Utente.
 */
public class SharedDataModelView extends ViewModel {

    private MutableLiveData<Studente> studenteData = new MutableLiveData<>();

    /**
     * Imposta i dati dello Studente all'interno del ViewModel condiviso.
     * @param studente l'oggetto Studente da memorizzare nel ViewModel condiviso.
     */
    public void setStudenteData(Studente studente) {
        studenteData.setValue(studente);
    }

    /**
     * Ottiene i dati dello Studente dal ViewModel condiviso come LiveData.
     * @return i dati dello Studente come LiveData dal ViewModel condiviso.
     */
    public LiveData<Studente> getStudenteData() {
        return studenteData;
    }

    private MutableLiveData<Utente> utenteData = new MutableLiveData<>();

    /**
     * Imposta i dati dell'Utente all'interno del ViewModel condiviso.
     * @param utente l'oggetto Utente da memorizzare nel ViewModel condiviso.
     */
    public void setUtenteData(Utente utente) {
        utenteData.setValue(utente);
    }

    /**
     * Ottiene i dati dell'Utente dal ViewModel condiviso come LiveData.
     * @return i dati dell'Utente come LiveData dal ViewModel condiviso.
     */
    public LiveData<Utente> getUtenteData() {
        return utenteData;
    }
}
