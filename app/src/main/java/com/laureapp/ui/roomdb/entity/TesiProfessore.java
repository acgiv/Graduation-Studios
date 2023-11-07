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
 * Questa classe rappresenta l'entità TesiProfessore nel database Room.
 */
@Entity(tableName ="Tesi_Professore", foreignKeys = {
        @ForeignKey(entity = Professore.class, parentColumns = "id_professore", childColumns = "id_professore", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Tesi.class, parentColumns = "id_tesi", childColumns = "id_tesi", onDelete = ForeignKey.CASCADE)
},
        indices = {@Index("id_professore") })
public class TesiProfessore implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_tesi_professore")
    private Long id_tesi_professore;

    @ColumnInfo(name = "id_professore")
    private Long id_professore;

    @ColumnInfo(name = "id_tesi")
    private Long id_tesi;

    @ColumnInfo(name = "ruolo_professore")
    private String ruolo_professore;

    //Getter e setter
    /**
     * Restituisce l'ID di TesiProfessore
     * @return l'ID di TesiProfessore
     */
    public Long getId_tesi_professore() {
        return id_tesi_professore;
    }

    /**
     * Imposta l'ID di TesiProfessore.
     * @param idTesiProfessore l'ID da impostare per TesiProfessore
     */
    public void setId_tesi_professore(Long idTesiProfessore) {
        this.id_tesi_professore = idTesiProfessore;
    }

    /**
     * Restituisce l'ID del professore associato
     * @return L'ID dello professore
     */
    public Long getId_professore() {
        return id_professore;
    }

    /**
     * Imposta l'ID del professore
     * @param idProfessore L'ID dello professore da impostare
     */
    public void setId_professore(Long idProfessore) {
        this.id_professore = idProfessore;
    }

    /**
     * Restituisce il ruolo del professore
     * @return ruolo professore
     */
    public String getRuolo_professore() {
        return ruolo_professore;
    }

    /**
     * Imposta il ruolo al professore
     * @param ruoloProfessore
     */
    public void setRuolo_professore(String ruoloProfessore) {
        this.ruolo_professore = ruoloProfessore;
    }

    /**
     * Ottieni ID della tesi associata
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
     * Restituisce una mappa contenente le informazioni dell'entità TesiProfessore
     * @return Mappa che contiene le informazioni dell'entità TesiProfessore
     */
    public Map<String, Object> getTesiProfessoreMap() {
        Map<String, Object> tesiProfessoreMap = new HashMap<>();
        tesiProfessoreMap.put("id_tesi_professore",this.id_tesi_professore);
        tesiProfessoreMap.put("id_professore", this.id_professore);
        tesiProfessoreMap.put("id_Tesi", this.id_tesi);
        tesiProfessoreMap.put("ruolo_professore", this.ruolo_professore);
        return tesiProfessoreMap;
    }

    /**
     * Metodo toString rappresentazione testuale dell'oggetto TesiProfessore
     * @return Una stringa che rappresenta l'oggetto TesiProfessore
     */
    @NonNull
    @Override
    public String toString() {
        return "TesiProfessore{" +
                "id=" + id_tesi_professore +
                ", id_professore=" + id_professore +
                ", id_tesi= " + id_tesi +
                ", ruolo_professore='" + ruolo_professore + '\'' +
                '}';
    }
}
