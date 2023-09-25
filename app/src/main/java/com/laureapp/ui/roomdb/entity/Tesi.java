package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName ="Tesi", foreignKeys = {
        @ForeignKey(entity = Vincolo.class, parentColumns = "id_vincolo", childColumns = "id_vincolo")
},
        indices = {@Index("id_vincolo") })
public class Tesi implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_tesi")
    private Long id_tesi;


    @ColumnInfo(name = "id_vincolo")
    private Long id_vincolo;

    @ColumnInfo(name="titolo")
    private String titolo;

    @ColumnInfo(name="tipologia")
    private String tipologia;

    @ColumnInfo(name="abstract_tesi")
    private String abstract_tesi;

    @ColumnInfo(name="data_pubblicazione")
    private Timestamp data_pubblicazione;


    @ColumnInfo(name="ciclo_cdl")
    private String ciclo_cdl;

    //Getter e setter
    public Long getId_tesi() {
        return id_tesi;
    }

    public void setId_tesi(Long idTesi) {this.id_tesi = idTesi;}


    public Long getId_vincolo() {
        return id_vincolo;
    }

    public void setId_vincolo(Long idVincolo) {
        this.id_vincolo = idVincolo;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titoloTesi) {
        this.titolo = titoloTesi;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologiaTesi) {
        tipologia = tipologiaTesi;
    }

    public String getAbstract_tesi() {
        return abstract_tesi;
    }

    public void setAbstract_tesi(String an_abstract_tesi) {
        abstract_tesi = an_abstract_tesi;
    }

    public Timestamp getData_pubblicazione() {
        return data_pubblicazione;
    }

    public void setData_pubblicazione(Timestamp dataPubblicazione) {
        this.data_pubblicazione = dataPubblicazione;
    }

    public String getCiclo_cdl() {
        return ciclo_cdl;
    }

    public void setCiclo_cdl(String ciclo_cdl) {
        this.ciclo_cdl = ciclo_cdl;
    }


    public Map<String, Object> getTesiMap() {
        Map<String, Object> tesiMap = new HashMap<>();
        tesiMap.put("id_tesi",this.id_tesi);
        tesiMap.put("id_vincolo", this.id_vincolo);
        tesiMap.put("titolo", this.titolo);
        tesiMap.put("tipologia", this.tipologia);
        tesiMap.put("abstract_tesi", this.abstract_tesi);
        tesiMap.put("data_pubblicazione", this.data_pubblicazione);
        tesiMap.put("ciclo_cdl", this.ciclo_cdl);



        return tesiMap;
    }

    @NonNull
    @Override
    public String toString() {
        return "Tesi{" +
                "id=" + id_tesi +
                "id_vincolo=" + id_vincolo +
                ", titolo='" + titolo + '\'' +
                ", Tipologia='" + tipologia + '\'' +
                ", Abstract='" + abstract_tesi + '\'' +
                ", data_publicazione=" + data_pubblicazione +
                '}';
    }
}

