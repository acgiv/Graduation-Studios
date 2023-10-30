package com.laureapp.ui.roomdb.viewModel.sharedDataModelView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.Utente;

public class SharedDataModelView extends ViewModel {

    private MutableLiveData<Studente> studenteData = new MutableLiveData<>();

    public void setStudenteData(Studente studente) {
        studenteData.setValue(studente);
    }

    public LiveData<Studente> getStudenteData() {
        return studenteData;
    }

    private MutableLiveData<Utente> utenteData = new MutableLiveData<>();

    public void setUtenteData(Utente utente) {
        utenteData.setValue(utente);
    }

    public LiveData<Utente> getUtenteData() {
        return utenteData;
    }
}
