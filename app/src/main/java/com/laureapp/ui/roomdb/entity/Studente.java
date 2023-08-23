package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName ="Studente" , foreignKeys = {
        @ForeignKey(entity = Utente.class, parentColumns = "id", childColumns = "id_utente")
})
public class Studente {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private Long matricola;

    @ColumnInfo(name = "id_utente")
    private Long id_utente;

    public Long getMatricola() {
        return matricola;
    }

    public void setMatricola(Long matricola) {
        this.matricola = matricola;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_utente() {
        return id_utente;
    }

    public void setId_utente(Long id_utente) {
        this.id_utente = id_utente;
    }

    @Override
    public String toString() {
        return "Studente{" +
                "id=" + id +
                ", matricola=" + matricola +
                ", id_utente=" + id_utente +
                '}';
    }
}
