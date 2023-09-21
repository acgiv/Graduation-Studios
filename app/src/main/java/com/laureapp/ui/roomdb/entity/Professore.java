package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName ="Professore",foreignKeys = {
        @ForeignKey(entity = Tesi.class, parentColumns = "id_tesi", childColumns = "id_tesi"),
        @ForeignKey(entity = Utente.class, parentColumns = "id_utente", childColumns = "id_utente"
        )}
)
public class Professore implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_professore")
    private Long id_professore;

    @ColumnInfo(name = "id_tesi")
    private Long id_tesi;

    @ColumnInfo(name = "id_utente")
    private Long id_utente;

    @ColumnInfo(name = "matricola")
    private String matricola;

    public Long getId_professore() {
        return id_professore;
    }

    public void setId_professore(Long id_professore) {
        this.id_professore = id_professore;
    }

    public Long getId_tesi() {
        return id_tesi;
    }

    public void setId_tesi(Long id_tesi) {
        this.id_tesi = id_tesi;
    }

    public Long getId_utente() {
        return id_utente;
    }

    public void setId_utente(Long id_utente) {
        this.id_utente = id_utente;
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public Map<String, Object> getProfessoreMap() {
        Map<String, Object> utenteMap = new HashMap<>();
        utenteMap.put("id_professore", this.id_professore);
        utenteMap.put("matricola", this.matricola);
        utenteMap.put("id_utente", this.id_utente);
        utenteMap.put("id_tesi", this.id_tesi);
        return utenteMap;
    }

    @NonNull
    @Override
    public String toString() {
        return "Professore{" +
                "id=" + id_professore +
                ", id_tesi=" + id_tesi +
                ", id_utente=" + id_utente +
                ", matricola='" + matricola + '\'' +
                '}';
    }
}