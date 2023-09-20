package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName ="Vincolo")
public class Vincolo implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_vincolo")
    private Long id_vincolo;

    @ColumnInfo(name = "tempistiche")
    private String tempistiche;

    @ColumnInfo(name = "media_voti")
    private int media_voti;

    @ColumnInfo(name = "esami_mancanti_necessari")
    private String esami_mancanti_necessari;

    @ColumnInfo(name = "skill")
    private String skill;

    //Getter e setter

    public Long getId_vincolo() {
        return id_vincolo;
    }

    public void setId_vincolo(Long idVincolo) {
        this.id_vincolo = idVincolo;
    }

    public String getTempistiche() {
        return tempistiche;
    }

    public void setTempistiche(String tempisticheTesi) {
        this.tempistiche = tempisticheTesi;
    }

    public int getMedia_voti() {
        return media_voti;
    }

    public void setMedia_voti(int mediaVoti) {
        this.media_voti = mediaVoti;
    }

    public String getEsami_mancanti_necessari() {
        return esami_mancanti_necessari;
    }

    public void setEsami_mancanti_necessari(String esamiMancanti) {
        this.esami_mancanti_necessari = esamiMancanti;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill_necessarie) {
        skill = skill_necessarie;
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
