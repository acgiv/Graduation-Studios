package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName ="FileTesi", foreignKeys = {
        @ForeignKey(entity = Tesi.class, parentColumns = "id_tesi", childColumns = "id_tesi", onDelete = ForeignKey.CASCADE)
},
        indices = {@Index("id_file") })
public class FileTesi implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_file")
    private Long id_file;

    @ColumnInfo(name = "id_tesi")
    private Long id_tesi;

    @ColumnInfo(name = "nomeFile")
    private String nomeFile;

    public String getNomeFile() {
        return nomeFile;
    }

    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }

    public Long getId_file() {
        return id_file;
    }

    public void setId_file(Long idFile) {
        this.id_file = idFile;
    }

    public Long getId_tesi() {
        return id_tesi;
    }

    public void setId_tesi(Long id_tesi) {
        this.id_tesi = id_tesi;
    }



    public Map<String, Object> getFileTesiMap() {
        Map<String, Object> fileTesiMap = new HashMap<>();
        fileTesiMap.put("id_tesi", this.id_tesi);
        fileTesiMap.put("id_file",this.id_file);
        fileTesiMap.put("nome_file",this.nomeFile);
        return fileTesiMap;
    }


    @NonNull
    @Override
    public String toString() {
        return "RichiesteTesi{" +
                "id_tesi=" + id_tesi +
                ", id_file=" + id_file +
                ", nome_file=" + nomeFile +
                '}';
    }
}
