package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Studente_Tesi", foreignKeys = {
        @ForeignKey(entity = Tesi.class, parentColumns = "id_tesi", childColumns = "id_tesi"),
        @ForeignKey(entity = Studente.class, parentColumns = "id_studente", childColumns = "id_studente")
},
        indices = {@Index("id_tesi"), @Index("id_studente") })
public class StudenteTesi implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_studente_tesi")
    private Long id_studente_tesi;

    @ColumnInfo(name = "id_tesi")
    private Long id_tesi;

    @ColumnInfo(name = "id_studente")
    private Long id_studente;


    //Getter e setter
    public Long getId_studente_tesi() {
        return id_studente_tesi;
    }

    public void setId_studente_tesi(Long idStudenteTesi) {
        this.id_studente_tesi = idStudenteTesi;
    }

    public Long getId_tesi() {
        return id_tesi;
    }

    public void setId_tesi(Long idTesi) {
        this.id_tesi = idTesi;
    }

    public Long getId_studente() {
        return id_studente;
    }

    public void setId_studente(Long idStudente) {
        this.id_studente = idStudente;
    }



    @NonNull
    @Override
    public String toString() {
        return "StudenteTesi{" +
                "id_studente_tesi=" + id_studente_tesi +
                ", id_tesi=" + id_tesi +
                ", id_studente='" + id_studente + '\'' +
                '}';
    }
}
