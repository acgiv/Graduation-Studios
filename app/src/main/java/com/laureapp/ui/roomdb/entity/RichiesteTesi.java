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

@Entity(tableName = "Richieste_Tesi", foreignKeys = {
        @ForeignKey(entity = StudenteTesi.class, parentColumns = "id_studente_tesi", childColumns = "id_studente_tesi", onDelete = ForeignKey.CASCADE),
},
        indices = {@Index("id_studente_tesi")})
public class RichiesteTesi implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_richiesta_tesi")
    private Long id_richiesta_tesi;

    @ColumnInfo(name = "id_studente_tesi")
    private Long id_studente_tesi;

    @ColumnInfo(name = "stato")
    private Long stato;


    //Getter e setter
    public Long getId_richiesta_tesi() {
        return id_richiesta_tesi;
    }

    public void setId_richiesta_tesi(Long idRichiestaTesi) {
        this.id_richiesta_tesi = idRichiestaTesi;
    }

    public Long getId_studente_tesi() {
        return id_studente_tesi;
    }

    public void setId_studente_tesi(Long idStudenteTesi) {
        this.id_studente_tesi = idStudenteTesi;
    }

    public Long getStato() {
        return stato;
    }

    public void setStato(Long stato) {
        this.stato = stato;
    }


    public Map<String, Object> getRichiesteTesiMap() {
        Map<String, Object> richiesteTesiMap = new HashMap<>();
        richiesteTesiMap.put("id_studente_tesi", this.id_studente_tesi);
        richiesteTesiMap.put("id_richiesta_tesi",this.id_richiesta_tesi);
        richiesteTesiMap.put("stato", this.stato);


        return richiesteTesiMap;
    }


    @NonNull
    @Override
    public String toString() {
        return "RichiesteTesi{" +
                "id_studente_tesi=" + id_studente_tesi +
                ", id_richiesta_tesi=" + id_richiesta_tesi +
                ", stato='" + stato + '\'' +
                '}';
    }
}
