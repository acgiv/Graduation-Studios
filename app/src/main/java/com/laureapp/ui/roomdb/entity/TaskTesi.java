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

@Entity(tableName = "Task_tesi", foreignKeys = {
        @ForeignKey(entity = Tesi.class, parentColumns = "id_tesi", childColumns = "id_tesi", onDelete = ForeignKey.CASCADE)},
        indices = {@Index("id_tesi")})
@TypeConverters({Converters.class})
public class TaskTesi implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_task")
    private Long id_task;

    @ColumnInfo(name="id_tesi")
    private Long id_tesi;

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

    public void setId_task(Long idTask) {
        this.id_task = idTask;
    }

    public Long getId_tesi() {
        return id_tesi;
    }

    public void setId_tesi(Long idTesi) {
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


    public Map<String, Object> getTaskTesiMap() {
        Map<String, Object> taskTesiMap = new HashMap<>();
        taskTesiMap.put("id_task", this.id_task);
        taskTesiMap.put("id_tesi",this.id_tesi);
        taskTesiMap.put("titolo", this.titolo);
        taskTesiMap.put("stato", this.stato);
        taskTesiMap.put("data_inizio", this.data_inizio);
        taskTesiMap.put("data_scadenza", this.data_scadenza);


        return taskTesiMap;
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
