package com.laureapp.ui.roomdb.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.sql.Timestamp;

@Entity(tableName ="Tesi", foreignKeys = {
        @ForeignKey(entity = Studente.class, parentColumns = "id", childColumns = "id_studente"),
        @ForeignKey(entity = Vincolo.class, parentColumns = "id", childColumns = "id_vincolo")
})
public class Tesi {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "id_studente")
    private Long id_studente;

    @ColumnInfo(name = "id_vincolo")
    private Long id_vincolo;

    private String titolo;

    private String Tipologia;

    private String Abstract;

    private Timestamp data_publicazione;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_studente() {
        return id_studente;
    }

    public void setId_studente(Long id_studente) {
        this.id_studente = id_studente;
    }

    public Long getId_vincolo() {
        return id_vincolo;
    }

    public void setId_vincolo(Long id_vincolo) {
        this.id_vincolo = id_vincolo;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getTipologia() {
        return Tipologia;
    }

    public void setTipologia(String tipologia) {
        Tipologia = tipologia;
    }

    public String getAbstract() {
        return Abstract;
    }

    public void setAbstract(String anAbstract) {
        Abstract = anAbstract;
    }

    public Timestamp getData_publicazione() {
        return data_publicazione;
    }

    public void setData_publicazione(Timestamp data_publicazione) {
        this.data_publicazione = data_publicazione;
    }

    @Override
    public String toString() {
        return "Tesi{" +
                "id=" + id +
                ", id_studente=" + id_studente +
                ", id_vincolo=" + id_vincolo +
                ", titolo='" + titolo + '\'' +
                ", Tipologia='" + Tipologia + '\'' +
                ", Abstract='" + Abstract + '\'' +
                ", data_publicazione=" + data_publicazione +
                '}';
    }
}

