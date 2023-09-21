package com.laureapp.ui.roomdb.entity;

import androidx.room.Embedded;

public class StudenteWithUtente {

    @Embedded
    public Studente studente;

    @Embedded(prefix = "utente_") // Add prefix to avoid naming conflict
    public Utente utente;

    public Studente getStudente() {
        return studente;
    }

    public void setStudente(Studente studente) {
        this.studente = studente;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}
