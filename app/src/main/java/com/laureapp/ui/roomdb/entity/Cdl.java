package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Cdl")
public class Cdl implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_cdl")
    private Long id_cdl;

    @ColumnInfo(name="ciclo_cdl")
    private String ciclo_cdl;

    @ColumnInfo(name="nome_cdl")
    private String nome_cdl;

    @ColumnInfo(name="facolta")
    private String facolta;

    //Getter e setter
    public Long getId_cdl() {
        return id_cdl;
    }

    public void setId_cdl(Long idCdl) {
        this.id_cdl = idCdl;
    }

    public String getCiclo_cdl() {
        return ciclo_cdl;
    }

    public void setCiclo_cdl(String cicloCdl) {
        this.ciclo_cdl = cicloCdl;
    }

    public String getNome_cdl() {
        return nome_cdl;
    }

    public void setNome_cdl(String nomeCdl) {
        this.nome_cdl = nomeCdl;
    }

    public String getFacolta() {
        return facolta;
    }

    public void setFacolta(String facolta_uni) {
        this.facolta = facolta_uni;
    }

    @NonNull
    @Override
    public String toString() {
        return "Cdl{" +
                "id=" + id_cdl +
                ", ciclo_cdl=" + ciclo_cdl +
                ", nome_cdl=" + nome_cdl +
                ", facolta=" + facolta +
                '}';
    }
}
