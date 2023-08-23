package com.laureapp.ui.roomdb.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName ="Professore",foreignKeys = {
        @ForeignKey(entity = Studente.class, parentColumns = "id", childColumns = "id_tesi"),
        @ForeignKey(entity = Utente.class, parentColumns = "id", childColumns = "id_utente")}
)
public class Professore {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "id_tesi")
    private Long id_tesi;

    @ColumnInfo(name = "id_utente")
    private Long id_utente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Professore{" +
                "id=" + id +
                ", id_tesi=" + id_tesi +
                ", id_utente=" + id_utente +
                '}';
    }
}
