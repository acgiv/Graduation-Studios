package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Questa classe rappresenta l'entità Professore nel database Room.
 */
@Entity(tableName ="Professore",foreignKeys = {
        @ForeignKey(entity = Utente.class, parentColumns = "id_utente", childColumns = "id_utente",onDelete = ForeignKey.CASCADE
        )}
)
public class Professore implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_professore")
    private Long id_professore;

    @ColumnInfo(name = "id_utente")
    private Long id_utente;

    @ColumnInfo(name = "matricola")
    private Long matricola;

    /**
     * Costruttore per la classe Professore
     * @param id_professore
     * @param id_utente
     * @param matricola
     */
    public Professore(Long id_professore, Long id_utente, Long matricola) {
        this.id_professore = id_professore;
        this.id_utente = id_utente;
        this.matricola = matricola;
    }

    public Professore(){}

    /**
     * Ottieni ID del professore
     * @return ID file
     */
    public Long getId_professore() {
        return id_professore;
    }

    /**
     * Modifica ID rofessore
     * @param id_professore da impostare
     */
    public void setId_professore(Long id_professore) {
        this.id_professore = id_professore;
    }

    /**
     * Ottieni ID utente del professore
     * @return ID utente
     */
    public Long getId_utente() {
        return id_utente;
    }

    /**
     * Modifica ID utente
     * @param id_utente da impostare
     */
    public void setId_utente(Long id_utente) {
        this.id_utente = id_utente;
    }

    /**
     * Ottiene la matricola professore
     * @return matricola professore
     */
    public Long getMatricola() {
        return matricola;
    }

    /**
     * Modifica matricola professore
     * @param matricola da impostare
     */
    public void setMatricola(Long matricola) {
        this.matricola = matricola;
    }

    /**
     * Restituisce una mappa contenente le informazioni dell'entità Professore
     * @return Mappa che contiene le informazioni dell'entità Professore
     */
    public Map<String, Object> getProfessoreMap() {
        Map<String, Object> utenteMap = new HashMap<>();
        utenteMap.put("id_professore", this.id_professore);
        utenteMap.put("matricola", this.matricola);
        utenteMap.put("id_utente", this.id_utente);
        return utenteMap;
    }

    /**
     * Metodo toString rappresentazione testuale dell'oggetto Professore
     * @return Una stringa che rappresenta l'oggetto Professore
     */
    @NonNull
    @Override
    public String toString() {
        return "Professore{" +
                "id=" + id_professore +
                ", id_utente=" + id_utente +
                ", matricola='" + matricola + '\'' +
                '}';
    }
}