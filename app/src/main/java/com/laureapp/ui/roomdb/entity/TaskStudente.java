package com.laureapp.ui.roomdb.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.laureapp.ui.roomdb.Converters;


import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "Task_studente", foreignKeys = {
        @ForeignKey(entity = StudenteTesi.class, parentColumns = "id_studente_tesi", childColumns = "id_studente_tesi", onDelete = ForeignKey.CASCADE)},
        indices = {@Index("id_studente_tesi")})
@TypeConverters({Converters.class})
public class TaskStudente implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_task")
    private Long id_task;

    @ColumnInfo(name="id_studente_tesi")
    private Long id_studente_tesi;

    @ColumnInfo(name="titolo")
    private String titolo;

    @ColumnInfo(name="stato")
    private String stato;

    @ColumnInfo(name="data_inizio")
    private Date data_inizio;

    @ColumnInfo(name="data_scadenza")
    private Date data_scadenza;

    //Getter e setter


    public Long getId_task() {
        return id_task;
    }

    public void setId_task(Long id_task) {
        this.id_task = id_task;
    }

    public Long getId_studente_tesi() {
        return id_studente_tesi;
    }

    public void setId_studente_tesi(Long id_studente_tesi) {
        this.id_studente_tesi = id_studente_tesi;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public Date getData_inizio() {
        return data_inizio;
    }

    public void setData_inizio(Date data_inizio) {
        this.data_inizio = data_inizio;
    }

    public Date getData_scadenza() {
        return data_scadenza;
    }

    public void setData_scadenza(Date data_scadenza) {
        this.data_scadenza = data_scadenza;
    }

    public Map<String, Object> getTaskStudenteMap() {
        Map<String, Object> taskStudenteMap = new HashMap<>();
        taskStudenteMap.put("id_task", this.id_task);
        taskStudenteMap.put("id_studente_tesi",this.id_studente_tesi);
        taskStudenteMap.put("titolo", this.titolo);
        taskStudenteMap.put("stato", this.stato);
        taskStudenteMap.put("data_inizio", this.data_inizio);
        taskStudenteMap.put("data_scadenza", this.data_scadenza);


        return taskStudenteMap;
    }

    @Override
    public String toString() {
        return "TaskTesi{" +
                "id=" + id_task +
                ", id_studente_tesi='" + id_studente_tesi + '\'' +
                ", titolo='" + titolo + '\'' +
                ", stato='" + stato + '\'' +
                ", data_inizio=" + data_inizio +
                ", data_scadenza=" + data_scadenza +
                '}';
    }


}
