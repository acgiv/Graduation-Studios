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
 * Questa classe rappresenta l'entità TaskStudente nel database Room.
 */
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
    /**
     * Restituisce l'ID del task.
     * @return l'ID del task.
     */
    public Long getId_task() {
        return id_task;
    }

    /**
     * Imposta l'ID del task.
     * @param id_task l'ID del task da impostare.
     */
    public void setId_task(Long id_task) {
        this.id_task = id_task;
    }

    /**
     * Restituisce l'ID della tesi studente associata al task.
     * @return l'ID della tesi studente associata al task.
     */
    public Long getId_studente_tesi() {
        return id_studente_tesi;
    }

    /**
     * Imposta l'ID della tesi studente associata al task.
     * @param id_studente_tesi l'ID della tesi studente associata al task da impostare.
     */
    public void setId_studente_tesi(Long id_studente_tesi) {
        this.id_studente_tesi = id_studente_tesi;
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
     * @param titolo il titolo della task da impostare.
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
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
     * @param stato lo stato del task da impostare.
     */
    public void setStato(String stato) {
        this.stato = stato;
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
     * Restituisce una mappa di attributi della task dello studente.
     * @return una mappa di attributi della task dello studente.
     */
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

    /**
     * Metodo toString rappresentazione testuale dell'oggetto TaskStudente
     * @return Una stringa che rappresenta l'oggetto TaskStudente
     */
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
