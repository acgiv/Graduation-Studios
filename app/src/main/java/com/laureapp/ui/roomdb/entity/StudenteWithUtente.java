package com.laureapp.ui.roomdb.entity;

import androidx.room.Embedded;

/**
 * Questa classe rappresenta l'entità StudenteWithUtente nel database Room.
 */
public class StudenteWithUtente {

    //Costruttore vuoto
    public StudenteWithUtente(){}

    /**
     * Costruttore che inizializza un oggetto StudenteWithUtente con gli oggetti Studente e Utente specificati.
     * @param studente l'oggetto Studente da associare.
     * @param utente l'oggetto Utente da associare.
     */
    public StudenteWithUtente(Studente studente , Utente utente){
        this.studente = studente;
        this.utente = utente;
    }

    @Embedded
    public Studente studente;

    @Embedded(prefix = "utente_") // Add prefix to avoid naming conflict
    public Utente utente;

    /**
     * Restituisce l'oggetto Studente associato.
     * @return l'oggetto Studente associato.
     */
    public Studente getStudente() {
        return studente;
    }

    /**
     * Imposta l'oggetto Studente associato.
     * @param studente l'oggetto Studente da associare.
     */
    public void setStudente(Studente studente) {
        this.studente = studente;
    }

    /**
     * Restituisce l'oggetto Utente associato.
     * @return l'oggetto Utente associato.
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * Imposta l'oggetto Utente associato.
     * @param utente l'oggetto Utente da impostare.
     */

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    /**
     * Metodo toString rappresentazione testuale dell'oggetto StudenteWithUtente
     * @return Una stringa che rappresenta l'oggetto StudenteWithUtente
     */
    @Override
    public String toString() {
        return "StudenteWithUtente{" +
                "studente=" + studente +
                ", utente=" + utente +
                '}';
    }
}
