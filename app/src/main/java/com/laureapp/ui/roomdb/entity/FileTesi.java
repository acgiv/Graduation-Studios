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

/**
 * Questa classe rappresenta l'entità FileTesi nel database Room.
 */
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

    /**
     * Ottiene il nome del file
     * @return nome del file
     */
    public String getNomeFile() {
        return nomeFile;
    }

    /**
     * Modifica nome del file
     * @param nomeFile nome del file da impostare
     */
    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }

    /**
     * Ottiene ID del file
     * @return ID del file
     */
    public Long getId_file() {
        return id_file;
    }

    /**
     * Modifica ID file
     * @param idFile ID da impostare
     */
    public void setId_file(Long idFile) {
        this.id_file = idFile;
    }

    /**
     * Ottieni ID della tesi associata al file
     * @return ID tesi
     */
    public Long getId_tesi() {
        return id_tesi;
    }

    /**
     * Modifica ID tesi
     * @param id_tesi
     */
    public void setId_tesi(Long id_tesi) {
        this.id_tesi = id_tesi;
    }

    /**
     * Restituisce una mappa contenente le informazioni dell'entità FileTesi
     * @return Mappa che contiene le informazioni dell'entità FileTesi
     */
    public Map<String, Object> getFileTesiMap() {
        Map<String, Object> fileTesiMap = new HashMap<>();
        fileTesiMap.put("id_tesi", this.id_tesi);
        fileTesiMap.put("id_file",this.id_file);
        fileTesiMap.put("nome_file",this.nomeFile);
        return fileTesiMap;
    }

    /**
     * Metodo toString rappresentazione testuale dell'oggetto FileTesi
     * @return Una stringa che rappresenta l'oggetto FileTesi
     */
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
