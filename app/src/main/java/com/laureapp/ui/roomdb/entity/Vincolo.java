package com.laureapp.ui.roomdb.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName ="Vincolo")
public class Vincolo {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String tempistiche;

    private int media_voti;

    private String esami_necessari;

    private String Skill;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTempistiche() {
        return tempistiche;
    }

    public void setTempistiche(String tempistiche) {
        this.tempistiche = tempistiche;
    }

    public int getMedia_voti() {
        return media_voti;
    }

    public void setMedia_voti(int media_voti) {
        this.media_voti = media_voti;
    }

    public String getEsami_necessari() {
        return esami_necessari;
    }

    public void setEsami_necessari(String esami_necessari) {
        this.esami_necessari = esami_necessari;
    }

    public String getSkill() {
        return Skill;
    }

    public void setSkill(String skill) {
        Skill = skill;
    }

    @Override
    public String toString() {
        return "Vincolo{" +
                "id=" + id +
                ", tempistiche='" + tempistiche + '\'' +
                ", media_voti=" + media_voti +
                ", esami_necessari='" + esami_necessari + '\'' +
                ", Skill='" + Skill + '\'' +
                '}';
    }
}
