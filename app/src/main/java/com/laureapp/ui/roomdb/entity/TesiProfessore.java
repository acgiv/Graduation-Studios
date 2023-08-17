package com.laureapp.ui.roomdb.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName ="Tesi_Professore", foreignKeys = {
        @ForeignKey(entity = Professore.class, parentColumns = "id", childColumns = "id_professore")
})
public class TesiProfessore {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "id_professore")
    private Long id_professore;

    @ColumnInfo(name = "ruolo_professore")
    private String ruolo_professore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_professore() {
        return id_professore;
    }

    public void setId_professore(Long id_professore) {
        this.id_professore = id_professore;
    }

    public String getRuolo_professore() {
        return ruolo_professore;
    }

    public void setRuolo_professore(String ruolo_professore) {
        this.ruolo_professore = ruolo_professore;
    }

    @Override
    public String toString() {
        return "TesiProfessore{" +
                "id=" + id +
                ", id_professore=" + id_professore +
                ", ruolo_professore='" + ruolo_professore + '\'' +
                '}';
    }
}
