package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName ="Studente")
public class Studente {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private Long matricola;

    public Long getMatricola() {
        return matricola;
    }

    public void setMatricola(Long matricola) {
        this.matricola = matricola;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Studente{" +
                "id=" + id +
                ", matricola=" + matricola +
                '}';
    }
}
