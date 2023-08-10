package com.laureapp.ui.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Studente_Tesi", foreignKeys = {
        @ForeignKey(entity = Tesi.class, parentColumns = "id", childColumns = "id_tesi")
})
public class StudenteTesi {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "id_tesi")
    private Long id_tesi;

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

    @Override
    public String toString() {
        return "StudenteTesi{" +
                "id=" + id +
                ", id_tesi=" + id_tesi +
                ", cld='" + cld + '\'' +
                '}';
    }
}
