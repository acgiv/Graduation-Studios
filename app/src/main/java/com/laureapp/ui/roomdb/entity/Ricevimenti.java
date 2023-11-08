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

/**
 * Questa classe rappresenta l'entità Ricevimenti nel database Room.
 */
@Entity(tableName = "Ricevimenti", foreignKeys = {
        @ForeignKey(entity = TaskTesi.class, parentColumns = "id_task", childColumns = "id_task")},
        indices = {@Index("id_task")})
public class Ricevimenti implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_ricevimento")
    private Long id_ricevimento;

    @ColumnInfo(name="id_task")
    private Long id_task;

    @ColumnInfo(name="argomento")
    private String argomento;

    @ColumnInfo(name="data_ricevimento")
    private Date data_ricevimento;

    //Getter e setter
    /**
     * Ottiene ID ricevimento
     * @return ID ricevimento
     */
    public Long getId_ricevimento() {
        return id_ricevimento;
    }

    /**
     * Modifica ID Ricevimenti
     * @param idRicevimento ID da impostare
     */
    public void setId_ricevimento(Long idRicevimento) {
        this.id_ricevimento = idRicevimento;
    }

    /**
     * Ottieni ID della task associata al Ricevimento
     * @return ID task
     */
    public Long getId_task() {
        return id_task;
    }

    /**
     * Modifica ID task
     * @param idTask ID da impostare
     */
    public void setId_task(Long idTask) {
        this.id_task = idTask;
    }

    /**
     * Ottieni argomento del ricevimento
     * @return argomento
     */
    public String getArgomento() {
        return argomento;
    }

    /**
     * Modifica argomento ricevimento
     * @param argomento_ricevimento da impostare
     */
    public void setArgomento(String argomento_ricevimento) {
        this.argomento = argomento_ricevimento;
    }

    /**
     * Ottieni data ricevimemto
     * @return data ricevimento
     */
    public Date getData_ricevimento() {
        return data_ricevimento;
    }

    /**
     * Modifica data ricevimento
     * @param dataRicevimento da impostare
     */
    public void setData_ricevimento(Date dataRicevimento) {
        this.data_ricevimento = dataRicevimento;
    }

    /**
     * Restituisce una mappa contenente le informazioni dell'entità Ricevimenti
     * @return Mappa che contiene le informazioni dell'entità Ricevimenti
     */
    public Map<String, Object> getRicevimentiMap() {
        Map<String, Object> getRicevimentiMap = new HashMap<>();
        getRicevimentiMap.put("id_ricevimento",this.id_ricevimento);
        getRicevimentiMap.put("id_task", this.id_task);
        getRicevimentiMap.put("argomento", this.argomento);
        getRicevimentiMap.put("data_ricevimento", this.data_ricevimento);
        return getRicevimentiMap;
    }

    /**
     * Metodo toString rappresentazione testuale dell'oggetto Ricevimenti
     * @return Una stringa che rappresenta l'oggetto Ricevimenti
     */
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

