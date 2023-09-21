package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Utente_Cdl", foreignKeys = {
        @ForeignKey(entity = Utente.class, parentColumns = "id_utente", childColumns = "id_utente"),
        @ForeignKey(entity = Cdl.class, parentColumns = "id_cdl", childColumns = "id_cdl")
        },
        indices = {@Index("id_utente"), @Index("id_cdl")})
public class UtenteCdl implements Serializable {

    @PrimaryKey
    @ColumnInfo (name = "id_utente_cdl")
    private Long id_utente_cdl;

    @ColumnInfo(name = "id_utente")
    private Long id_utente;

    @ColumnInfo(name = "id_cdl")
    private Long id_cdl;


    //Getter e setter
    public Long getId_utente_cdl(){return id_utente_cdl;}
    public void setId_utente_cdl(Long idUtenteCdl){this.id_utente_cdl = idUtenteCdl;}

    public Long getId_utente() {
        return id_utente;
    }

    public void setId_utente(Long idUtente) {
        this.id_utente = idUtente;
    }


    public Long getId_cdl() {
        return id_cdl;
    }

    public void setId_cdl(Long idCdl) {
        this.id_cdl = idCdl;
    }

       @NonNull
    @Override
    public String toString() {
        return "UtenteCdl{" +
                "id_utente_cdl=" + id_utente_cdl +
                "id_utente=" + id_utente +
                ", id_cdl=" + id_cdl +
                '}';
    }
}
