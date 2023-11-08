package com.laureapp.ui.roomdb.entity;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Questa classe rappresenta l'entità Tesi nel database Room
 */
@Entity(tableName ="Tesi", foreignKeys = {
        @ForeignKey(entity = Vincolo.class, parentColumns = "id_vincolo", childColumns = "id_vincolo", onDelete = ForeignKey.CASCADE)
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
    private Date data_pubblicazione;


    @ColumnInfo(name="ciclo_cdl")
    private String ciclo_cdl;

    @ColumnInfo(name="visualizzazioni")
    private Long visualizzazioni;

    //Getter e setter
    /**
     * Restituisce l'ID della tesi.
     * @return l'ID della tesi.
     */
    public Long getId_tesi() {
        return id_tesi;
    }

    /**
     * Imposta l'ID della tesi.
     * @param idTesi l'ID della tesi da impostare.
     */
    public void setId_tesi(Long idTesi) {this.id_tesi = idTesi;}

    /**
     * Restituisce l'ID del vincolo associato.
     * @return l'ID del vincolo.
     */
    public Long getId_vincolo() {
        return id_vincolo;
    }

    /**
     * Imposta l'ID del vincolo.
     * @param idVincolo l'ID del vincolo da impostare.
     */
    public void setId_vincolo(Long idVincolo) {
        this.id_vincolo = idVincolo;
    }

    /**
     * Restituisce il titolo della tesi.
     * @return il titolo della tesi.
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Imposta il titolo della tesi.
     * @param titoloTesi il titolo della tesi da impostare.
     */
    public void setTitolo(String titoloTesi) {
        this.titolo = titoloTesi;
    }

    /**
     * Restituisce la tipologia della tesi.
     * @return la tipologia della tesi.
     */
    public String getTipologia() {
        return tipologia;
    }

    /**
     * Imposta la tipologia della tesi.
     * @param tipologiaTesi la tipologia della tesi da impostare.
     */
    public void setTipologia(String tipologiaTesi) {
        tipologia = tipologiaTesi;
    }

    /**
     * Restituisce l'abstract della tesi.
     * @return l'abstract della tesi.
     */
    public String getAbstract_tesi() {
        return abstract_tesi;
    }

    /**
     * Imposta l'abstract della tesi.
     * @param an_abstract_tesi l'abstract della tesi da impostare.
     */
    public void setAbstract_tesi(String an_abstract_tesi) {
        abstract_tesi = an_abstract_tesi;
    }

    /**
     * Restituisce la data di pubblicazione della tesi.
     * @return la data di pubblicazione della tesi.
     */
    public Date getData_pubblicazione() {
        return data_pubblicazione;
    }

    /**
     * Imposta la data di pubblicazione della tesi.
     * @param dataPubblicazione la data di pubblicazione della tesi da impostare.
     */
    public void setData_pubblicazione(Date dataPubblicazione) {
        this.data_pubblicazione = dataPubblicazione;
    }

    /**
     * Restituisce il ciclo di laurea della tesi.
     * @return il ciclo di laurea della tesi.
     */
    public String getCiclo_cdl() {
        return ciclo_cdl;
    }

    /**
     * Imposta il ciclo di laurea della tesi.
     * @param ciclo_cdl il ciclo di laurea della tesi da impostare.
     */
    public void setCiclo_cdl(String ciclo_cdl) {
        this.ciclo_cdl = ciclo_cdl;
    }

    /**
     * Restituisce il numero di visualizzazioni della tesi.
     * @return il numero di visualizzazioni della tesi.
     */
    public Long getVisualizzazioni(){ return visualizzazioni;}

    /**
     * Imposta il numero di visualizzazioni della tesi.
     * @param visualizzazioni il numero di visualizzazioni della tesi da impostare.
     */
    public void setVisualizzazioni(Long visualizzazioni){this.visualizzazioni = visualizzazioni;}

    /**
     * Restituisce una mappa di valori della tesi.
     * @return una mappa di valori della tesi.
     */
    public Map<String, Object> getTesiMap() {
        Map<String, Object> tesiMap = new HashMap<>();
        tesiMap.put("id_tesi",this.id_tesi);
        tesiMap.put("id_vincolo", this.id_vincolo);
        tesiMap.put("titolo", this.titolo);
        tesiMap.put("tipologia", this.tipologia);
        tesiMap.put("abstract_tesi", this.abstract_tesi);
        tesiMap.put("data_pubblicazione", this.data_pubblicazione);
        tesiMap.put("ciclo_cdl", this.ciclo_cdl);
        tesiMap.put("visualizzazioni",this.visualizzazioni);



        return tesiMap;
    }

    /**
     * Metodo toString rappresentazione testuale dell'oggetto Tesi.
     * @return una stringa che rappresenta l'oggetto Tesi.
     */
    @NonNull
    @Override
    public String toString() {
        return "Tesi{" +
                "id=" + id_tesi +
                ", id_vincolo=" + id_vincolo +
                ", titolo='" + titolo + '\'' +
                ", Tipologia='" + tipologia + '\'' +
                ", Abstract='" + abstract_tesi + '\'' +
                ", data_publicazione=" + data_pubblicazione + '\''+
                ", ciclo_cdl=" + ciclo_cdl+ '\''+
                ", visualizzazioni="+visualizzazioni+

                '}';
    }





}

