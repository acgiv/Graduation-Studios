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
 * Questa classe rappresenta l'entità StudenteTesi nel database Room.
 */
@Entity(tableName = "Studente_Tesi", foreignKeys = {
        @ForeignKey(entity = Tesi.class, parentColumns = "id_tesi", childColumns = "id_tesi", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Studente.class, parentColumns = "id_studente", childColumns = "id_studente", onDelete = ForeignKey.CASCADE)
},
        indices = {@Index("id_tesi"), @Index("id_studente") })
public class StudenteTesi implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_studente_tesi")
    private Long id_studente_tesi;

    @ColumnInfo(name = "id_tesi")
    private Long id_tesi;

    @ColumnInfo(name = "id_studente")
    private Long id_studente;


    //Getter e setter
    /**
     * Restituisce l'ID di StudenteTesi.
     * @return l'ID di StudenteTesi
     */
    public Long getId_studente_tesi() {
        return id_studente_tesi;
    }

    /**
     * Imposta l'ID dell'istanza di StudenteTesi.
     * @param idStudenteTesi l'ID da impostare per l'istanza di StudenteTesi
     */
    public void setId_studente_tesi(Long idStudenteTesi) {
        this.id_studente_tesi = idStudenteTesi;
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
     * @param idTesi
     */
    public void setId_tesi(Long idTesi) {
        this.id_tesi = idTesi;
    }

    /**
     * Restituisce l'ID dello studente associato
     * @return L'ID dello studente
     */
    public Long getId_studente() {
        return id_studente;
    }

    /**
     * Imposta l'ID dello studente.
     * @param idStudente L'ID dello studente da impostare
     */
    public void setId_studente(Long idStudente) {
        this.id_studente = idStudente;
    }

    /**
     * Restituisce l'ID della tesi in base all'ID dello studente specificato.
     * @param idStudente l'ID dello studente specificato
     * @return l'ID della tesi associata allo studente specificato
     */
    public Long getId_tesi_From_studente(Long idStudente) {
        return id_tesi;
    }

    /**
     * Restituisce una mappa contenente le informazioni dell'entità StudenteTesi
     * @return Mappa che contiene le informazioni dell'entità StudenteTesi
     */
    public Map<String, Object> getStudenteTesiMap() {
        Map<String, Object> studenteTesiMap = new HashMap<>();
        studenteTesiMap.put("id_studente_tesi", this.id_studente_tesi);
        studenteTesiMap.put("id_tesi",this.id_tesi);
        studenteTesiMap.put("id_studente", this.id_studente);


        return studenteTesiMap;
    }

    /**
     * Metodo toString rappresentazione testuale dell'oggetto StudenteTesi
     * @return Una stringa che rappresenta l'oggetto StudenteTesi
     */
    @NonNull
    @Override
    public String toString() {
        return "StudenteTesi{" +
                "id_studente_tesi=" + id_studente_tesi +
                ", id_tesi=" + id_tesi +
                ", id_studente='" + id_studente + '\'' +
                '}';
    }
}
