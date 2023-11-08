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
        @ForeignKey(entity = Tesi.class, parentColumns = "id_tesi", childColumns = "id_tesi", onDelete = ForeignKey.CASCADE),
},
        indices = {@Index("id_tesi")})
public class RichiesteTesi implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_richiesta_tesi")
    private Long id_richiesta_tesi;

    @ColumnInfo(name = "id_tesi")
    private Long id_tesi;

    @ColumnInfo(name = "stato")
    private String stato;

    @ColumnInfo(name = "id_studente")
    private Long id_studente;

    @ColumnInfo(name = "soddisfa_requisiti")
    private boolean soddisfa_requisiti;
    //Getter e setter

    public boolean isSoddisfa_requisiti() {
        return soddisfa_requisiti;
    }

    public void setSoddisfa_requisiti(boolean soddisfa_requisiti) {
        this.soddisfa_requisiti = soddisfa_requisiti;
    }

    public Long getId_studente(){return id_studente;}

    public void setId_studente(Long id_studente){
        this.id_studente = id_studente;
    }
    public Long getId_richiesta_tesi() {
        return id_richiesta_tesi;
    }

    public void setId_richiesta_tesi(Long idRichiestaTesi) {
        this.id_richiesta_tesi = idRichiestaTesi;
    }

    public Long getId_tesi() {
        return id_tesi;
    }

    public void setId_tesi(Long id_tesi) {
        this.id_tesi = id_tesi;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }


    public Map<String, Object> getRichiesteTesiMap() {
        Map<String, Object> richiesteTesiMap = new HashMap<>();
        richiesteTesiMap.put("id_tesi", this.id_tesi);
        richiesteTesiMap.put("id_richiesta_tesi",this.id_richiesta_tesi);
        richiesteTesiMap.put("id_studente",this.id_studente);
        richiesteTesiMap.put("soddisfa_requisiti",this.soddisfa_requisiti);
        richiesteTesiMap.put("stato", this.stato);



        return richiesteTesiMap;
    }


    @NonNull
    @Override
    public String toString() {
        return "RichiesteTesi{" +
                "id_tesi=" + id_tesi +
                ", id_richiesta_tesi=" + id_richiesta_tesi +
                ", id_studente" + id_studente +
                ", soddisfa_requisiti " + soddisfa_requisiti +
                ", stato='" + stato + '\'' +
                '}';
    }
}
