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

    public Long getId_vincolo() {
        return id_vincolo;
    }

    public void setId_vincolo(Long idVincolo) {
        this.id_vincolo = idVincolo;
    }

    public Long getTempistiche() {
        return tempistiche;
    }

    public void setTempistiche(Long tempisticheTesi) {
        this.tempistiche = tempisticheTesi;
    }

    public Long getMedia_voti() {
        return media_voti;
    }

    public void setMedia_voti(Long mediaVoti) {
        this.media_voti = mediaVoti;
    }

    public Long getEsami_mancanti_necessari() {
        return esami_mancanti_necessari;
    }

    public void setEsami_mancanti_necessari(Long esamiMancanti) {
        this.esami_mancanti_necessari = esamiMancanti;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill_necessarie) {
        skill = skill_necessarie;
    }


    public Map<String, Object> getVincoloMap() {
        Map<String, Object> vincoloMap = new HashMap<>();
        vincoloMap.put("id_vincolo",this.id_vincolo);
        vincoloMap.put("tempistiche", this.tempistiche);
        vincoloMap.put("media_voti", this.media_voti);
        vincoloMap.put("esami_mancanti_necessari", this.esami_mancanti_necessari);
        vincoloMap.put("skill", this.skill);
        return vincoloMap;
    }

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
