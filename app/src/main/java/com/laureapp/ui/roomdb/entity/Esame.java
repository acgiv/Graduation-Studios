package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity(tableName ="Esame")
public class Esame implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private Timestamp data_conseguimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getData_conseguimento() {
        return data_conseguimento;
    }

    public void setData_conseguimento(Timestamp data_conseguimento) {
        this.data_conseguimento = data_conseguimento;
    }

    @NonNull
    @Override
    public String toString() {
        return "Esame{" +
                "id=" + id +
                ", data_conseguimento=" + data_conseguimento +
                '}';
    }
}
