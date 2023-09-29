package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

    public Long getId_tesi_From_studente(Long idStudente) {
        return id_tesi;
    }

    public Map<String, Object> getStudenteTesiMap() {
        Map<String, Object> studenteTesiMap = new HashMap<>();
        studenteTesiMap.put("id_studente_tesi", this.id_studente_tesi);
        studenteTesiMap.put("id_tesi",this.id_tesi);
        studenteTesiMap.put("id_studente", this.id_studente);


        return studenteTesiMap;
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
