package com.laureapp.ui.roomdb.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName ="Professore",foreignKeys = @ForeignKey(entity = Studente.class, parentColumns = "id", childColumns = "id_tesi"))
public class Professore {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "id_tesi")
    private Long id_tesi;

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

    @Override
    public String toString() {
        return "Professore{" +
                "id=" + id +
                ", id_tesi=" + id_tesi +
                '}';
    }
}
