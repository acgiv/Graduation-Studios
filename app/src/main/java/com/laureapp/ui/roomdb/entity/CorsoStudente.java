package com.laureapp.ui.roomdb.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Corso_Studente", foreignKeys = {
        @ForeignKey(entity = Studente.class, parentColumns = "id", childColumns = "id_studente"),
        @ForeignKey(entity = Esame.class, parentColumns = "id", childColumns = "id_esame")
})
public class CorsoStudente {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private Long id_esame;

    private Long id_studente;

    private int voto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_esame() {
        return id_esame;
    }

    public void setId_esame(Long id_esame) {
        this.id_esame = id_esame;
    }

    public Long getId_studente() {
        return id_studente;
    }

    public void setId_studente(Long id_studente) {
        this.id_studente = id_studente;
    }

    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }

    @Override
    public String toString() {
        return "CorsoStudente{" +
                "id=" + id +
                ", id_esame=" + id_esame +
                ", id_studente=" + id_studente +
                ", voto=" + voto +
                '}';
    }
}
