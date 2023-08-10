package com.laureapp.ui.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;

@Entity(tableName ="Esame")
public class Esame {

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

    @Override
    public String toString() {
        return "Esame{" +
                "id=" + id +
                ", data_conseguimento=" + data_conseguimento +
                '}';
    }
}
