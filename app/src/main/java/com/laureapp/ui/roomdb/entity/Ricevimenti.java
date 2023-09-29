package com.laureapp.ui.roomdb.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.type.DateTime;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "Ricevimenti", foreignKeys = {
        @ForeignKey(entity = TaskTesi.class, parentColumns = "id_task", childColumns = "id_task")},
        indices = {@Index("id_task")})
public class Ricevimenti implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_ricevimento")
    private Long id_ricevimento;

    @ColumnInfo(name="id_task")
    private String id_task;

    @ColumnInfo(name="argomento")
    private String argomento;

    @ColumnInfo(name="data_ricevimento")
    private Timestamp data_ricevimento;

    //Getter e setter
    public Long getId_ricevimento() {
        return id_ricevimento;
    }

    public void setId_ricevimento(Long idRicevimento) {
        this.id_ricevimento = idRicevimento;
    }

    public String getId_task() {
        return id_task;
    }

    public void setId_task(String idTask) {
        this.id_task = idTask;
    }

    public String getArgomento() {
        return argomento;
    }

    public void setArgomento(String argomento_ricevimento) {
        this.argomento = argomento_ricevimento;
    }

    public Timestamp getData_ricevimento() {
        return data_ricevimento;
    }

    public void setData_ricevimento(Timestamp dataRicevimento) {
        this.data_ricevimento = dataRicevimento;
    }

    public Map<String, Object> getRicevimentiMap() {
        Map<String, Object> getRicevimentiMap = new HashMap<>();
        getRicevimentiMap.put("id_ricevimento",this.id_ricevimento);
        getRicevimentiMap.put("id_task", this.id_task);
        getRicevimentiMap.put("argomento", this.argomento);
        getRicevimentiMap.put("data_ricevimento", this.data_ricevimento);
        return getRicevimentiMap;
    }

    @Override
    public String toString() {
        return "Ricevimenti{" +
                "id=" + id_ricevimento +
                ", id_task='" + id_task + '\'' +
                ", argomento='" + argomento + '\'' +
                ", data=" + data_ricevimento +
                '}';
    }

}

