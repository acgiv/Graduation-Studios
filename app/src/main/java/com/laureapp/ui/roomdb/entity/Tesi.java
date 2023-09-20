package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Entity(tableName ="Tesi", foreignKeys = {
        @ForeignKey(entity = Studente.class, parentColumns = "id_studente", childColumns = "id_studente"),
        @ForeignKey(entity = Vincolo.class, parentColumns = "id_vincolo", childColumns = "id_vincolo")
},
        indices = {@Index("id_studente"), @Index("id_vincolo") })
public class Tesi implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_tesi")
    private Long id_tesi;

    @ColumnInfo(name = "id_studente")
    private Long id_studente;

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

    //Getter e setter
    public Long getId_tesi() {
        return id_tesi;
    }

    public void setId_tesi(Long idTesi) {this.id_tesi = idTesi;}

    public Long getId_studente() {
        return id_studente;
    }

    public void setId_studente(Long idStudente) {
        this.id_studente = idStudente;
    }

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

    @NonNull
    @Override
    public String toString() {
        return "Tesi{" +
                "id=" + id_tesi +
                ", id_studente=" + id_studente +
                ", id_vincolo=" + id_vincolo +
                ", titolo='" + titolo + '\'' +
                ", Tipologia='" + tipologia + '\'' +
                ", Abstract='" + abstract_tesi + '\'' +
                ", data_publicazione=" + data_pubblicazione +
                '}';
    }
}

