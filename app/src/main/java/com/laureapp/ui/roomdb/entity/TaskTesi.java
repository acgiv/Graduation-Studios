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

/**
 * Questa classe rappresenta l'entità TaskTesi nel database Room.
 */
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
    /**
     * Restituisce l'ID del task.
     * @return l'ID del task.
     */
    public Long getId_task() {
        return id_task;
    }

    /**
     * Imposta l'ID del task.
     * @param idTask l'ID del task da impostare.
     */
    public void setId_task(Long idTask) {
        this.id_task = idTask;
    }

    /**
     * Restituisce l'ID della tesi associata al task.
     * @return l'ID della tesi associata al task.
     */
    public Long getId_tesi() {
        return id_tesi;
    }

    /**
     * Imposta l'ID della tesi associata al task.
     * @param idTesi l'ID della tesi associata al task da impostare.
     */
    public void setId_tesi(Long idTesi) {
        this.id_tesi = idTesi;
    }

    /**
     * Restituisce il titolo della task.
     * @return il titolo della task.
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Imposta il titolo della task.
     * @param titoloTask il titolo della task da impostare.
     */
    public void setTitolo(String titoloTask) {
        this.titolo = titoloTask;
    }

    /**
     * Restituisce lo stato della task.
     * @return lo stato della task.
     */
    public String getStato() {
        return stato;
    }

    /**
     * Imposta lo stato del task.
     * @param statoTask lo stato del task da impostare.
     */
    public void setStato(String statoTask) {
        this.stato = statoTask;
    }

    /**
     * Restituisce la data di inizio della task.
     * @return la data di inizio della task.
     */
    public Date getData_inizio() {
        return data_inizio;
    }

    /**
     * Imposta la data di inizio della task.
     * @param data_inizio la data di inizio della task da impostare.
     */
    public void setData_inizio(Date data_inizio) {
        this.data_inizio = data_inizio;
    }

    /**
     * Restituisce la data di scadenza della task.
     * @return la data di scadenza della task.
     */
    public Date getData_scadenza() {
        return data_scadenza;
    }

    /**
     * Imposta la data di scadenza della task.
     * @param data_scadenza la data di scadenza della task da impostare.
     */
    public void setData_scadenza(Date data_scadenza) {
        this.data_scadenza = data_scadenza;
    }

    /**
     * Restituisce una mappa di attributi della task tesi.
     * @return una mappa di attributi della task tesi.
     */
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

    /**
     * Metodo toString rappresentazione testuale dell'oggetto TaskTesi
     * @return Una stringa che rappresenta l'oggetto TaskTesi
     */
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
