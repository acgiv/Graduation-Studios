package com.laureapp.ui.roomdb.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.laureapp.ui.roomdb.Converters;


import java.io.Serializable;
import java.sql.Timestamp;

@Entity(tableName = "Task_tesi", foreignKeys = {
        @ForeignKey(entity = Tesi.class, parentColumns = "id_tesi", childColumns = "id_tesi")},
        indices = {@Index("id_tesi")})
@TypeConverters({Converters.class})
public class TaskTesi implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_task")
    private Long id_task;

    @ColumnInfo(name="id_tesi")
    private String id_tesi;

    @ColumnInfo(name="titolo")
    private String titolo;

    @ColumnInfo(name="stato")
    private String stato;

    @ColumnInfo(name="data_inizio")
    private Timestamp data_inizio;

    @ColumnInfo(name="data_scadenza")
    private Timestamp data_scadenza;

    //Getter e setter
    public Long getId_task() {
        return id_task;
    }

    public void setId_task(Long idTask) {
        this.id_task = idTask;
    }

    public String getId_tesi() {
        return id_tesi;
    }

    public void setId_tesi(String idTesi) {
        this.id_tesi = idTesi;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titoloTask) {
        this.titolo = titoloTask;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String statoTask) {
        this.stato = statoTask;
    }

    public Timestamp getData_inizio() {
        return data_inizio;
    }

    public void setData_inizio(Timestamp data_inizio) {
        this.data_inizio = data_inizio;
    }

    public Timestamp getData_scadenza() {
        return data_scadenza;
    }

    public void setData_scadenza(Timestamp data_scadenza) {
        this.data_scadenza = data_scadenza;
    }

    @Override
    public String toString() {
        return "TaskTesi{" +
                "id=" + id_task +
                ", id_tesi='" + id_tesi + '\'' +
                ", titolo='" + titolo + '\'' +
                ", stato='" + stato + '\'' +
                ", data_inizio=" + data_inizio +
                ", data_scadenza=" + data_scadenza +
                '}';
    }
}