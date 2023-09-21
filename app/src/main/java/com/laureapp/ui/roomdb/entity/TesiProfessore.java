package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName ="Tesi_Professore", foreignKeys = {
        @ForeignKey(entity = Professore.class, parentColumns = "id_professore", childColumns = "id_professore")
},
        indices = {@Index("id_professore") })
public class TesiProfessore implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_tesi_professore")
    private Long id_tesi_professore;

    @ColumnInfo(name = "id_professore")
    private Long id_professore;

    @ColumnInfo(name = "ruolo_professore")
    private String ruolo_professore;

    //Getter e setter
    public Long getId_tesi_professore() {
        return id_tesi_professore;
    }

    public void setId_tesi_professore(Long idTesiProfessore) {
        this.id_tesi_professore = idTesiProfessore;
    }

    public Long getId_professore() {
        return id_professore;
    }

    public void setId_professore(Long idProfessore) {
        this.id_professore = idProfessore;
    }

    public String getRuolo_professore() {
        return ruolo_professore;
    }

    public void setRuolo_professore(String ruoloProfessore) {
        this.ruolo_professore = ruoloProfessore;
    }

    @NonNull
    @Override
    public String toString() {
        return "TesiProfessore{" +
                "id=" + id_tesi_professore +
                ", id_professore=" + id_professore +
                ", ruolo_professore='" + ruolo_professore + '\'' +
                '}';
    }
}
