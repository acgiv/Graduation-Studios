package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Studente_Tesi", foreignKeys = {
        @ForeignKey(entity = Tesi.class, parentColumns = "id", childColumns = "id_tesi"),
        @ForeignKey(entity = Studente.class, parentColumns = "studente_id", childColumns = "studente_id")
})
public class StudenteTesi implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "id_tesi")
    private Long id_tesi;

    @ColumnInfo(name = "studente_id")
    private Long studente_id;

    public Long getStudente_id() {
        return studente_id;
    }

    public void setStudente_id(Long studente_id) {
        this.studente_id = studente_id;
    }

    private String cld;

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

    public String getCld() {
        return cld;
    }

    public void setCld(String cld) {
        this.cld = cld;
    }

    @NonNull
    @Override
    public String toString() {
        return "StudenteTesi{" +
                "id=" + id +
                ", id_tesi=" + id_tesi +
                ", cld='" + cld + '\'' +
                '}';
    }
}
