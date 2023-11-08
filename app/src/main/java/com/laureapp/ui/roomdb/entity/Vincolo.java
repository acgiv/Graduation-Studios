package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName ="Vincolo")
public class Vincolo implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_vincolo")
    private Long id_vincolo;

    @ColumnInfo(name = "tempistiche")
    private Long tempistiche;

    @ColumnInfo(name = "media_voti")
    private Long media_voti;

    @ColumnInfo(name = "esami_mancanti_necessari")
    private Long esami_mancanti_necessari;

    @ColumnInfo(name = "skill")
    private String skill;

    //Getter e setter

    /**
     * Restituisce l'ID del vincolo.
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
     * Restituisce le tempistiche del vincolo.
     * @return le tempistiche del vincolo.
     */
    public Long getTempistiche() {
        return tempistiche;
    }

    /**
     * Imposta le tempistiche del vincolo.
     * @param tempisticheTesi le tempistiche del vincolo da impostare.
     */
    public void setTempistiche(Long tempisticheTesi) {
        this.tempistiche = tempisticheTesi;
    }

    /**
     * Restituisce la media voti del vincolo.
     * @return la media voti del vincolo.
     */
    public Long getMedia_voti() {
        return media_voti;
    }

    /**
     * Imposta la media voti del vincolo.
     * @param mediaVoti la media voti del vincolo da impostare.
     */
    public void setMedia_voti(Long mediaVoti) {
        this.media_voti = mediaVoti;
    }

    /**
     * Restituisce il numero di esami mancanti necessari per il vincolo.
     * @return il numero di esami mancanti necessari per il vincolo.
     */
    public Long getEsami_mancanti_necessari() {
        return esami_mancanti_necessari;
    }

    /**
     * Imposta il numero di esami mancanti necessari per il vincolo.
     * @param esamiMancanti il numero di esami mancanti necessari per il vincolo da impostare.
     */
    public void setEsami_mancanti_necessari(Long esamiMancanti) {
        this.esami_mancanti_necessari = esamiMancanti;
    }

    /**
     * Restituisce le skill richieste per il vincolo.
     * @return le skill richieste per il vincolo.
     */
    public String getSkill() {
        return skill;
    }

    /**
     * Imposta le skill richieste per il vincolo.
     * @param skill_necessarie le skill necessarie per il vincolo da impostare.
     */
    public void setSkill(String skill_necessarie) {
        skill = skill_necessarie;
    }

    /**
     * Restituisce una mappa di valori della vincolo.
     * @return una mappa di valori della vincolo.
     */
    public Map<String, Object> getVincoloMap() {
        Map<String, Object> vincoloMap = new HashMap<>();
        vincoloMap.put("id_vincolo",this.id_vincolo);
        vincoloMap.put("tempistiche", this.tempistiche);
        vincoloMap.put("media_voti", this.media_voti);
        vincoloMap.put("esami_mancanti_necessari", this.esami_mancanti_necessari);
        vincoloMap.put("skill", this.skill);
        return vincoloMap;
    }

    /**
     * Metodo toString rappresentazione testuale dell'oggetto Vincolo.
     * @return una stringa che rappresenta l'oggetto Vincolo.
     */
    @NonNull
    @Override
    public String toString() {
        return "Vincolo{" +
                "id=" + id_vincolo +
                ", tempistiche='" + tempistiche + '\'' +
                ", media_voti=" + media_voti +
                ", esami_necessari='" + esami_mancanti_necessari + '\'' +
                ", Skill='" + skill + '\'' +
                '}';
    }
}
