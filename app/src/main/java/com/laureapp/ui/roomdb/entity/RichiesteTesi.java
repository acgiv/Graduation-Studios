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
 * Questa classe rappresenta l'entità RichiestaTesi nel database Room.
 */
@Entity(tableName = "Richieste_Tesi", foreignKeys = {
        @ForeignKey(entity = Tesi.class, parentColumns = "id_tesi", childColumns = "id_tesi", onDelete = ForeignKey.CASCADE),
},
        indices = {@Index("id_tesi")})
public class RichiesteTesi implements Serializable {

    //Colonne tabella
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_richiesta_tesi")
    private Long id_richiesta_tesi;

    @ColumnInfo(name = "id_tesi")
    private Long id_tesi;

    @ColumnInfo(name = "stato")
    private String stato;

    @ColumnInfo(name = "id_studente")
    private Long id_studente;

    @ColumnInfo(name = "soddisfa_requisiti")
    private boolean soddisfa_requisiti;
    //Getter e setter

    /**
     * Restituisce se i requisiti sono soddisfatti o meno.
     * @return true se i requisiti sono soddisfatti, false altrimenti
     */
    public boolean isSoddisfa_requisiti() {
        return soddisfa_requisiti;
    }

    /**
     * Imposta se i requisiti sono soddisfatti o meno.
     * @param soddisfa_requisiti valore booleano che indica se i requisiti sono soddisfatti
     */
    public void setSoddisfa_requisiti(boolean soddisfa_requisiti) {
        this.soddisfa_requisiti = soddisfa_requisiti;
    }

    /**
     * Ottieni ID studente associato a RichiestaTesi
     * @return ID utente
     */
    public Long getId_studente(){return id_studente;}

    /**
     * Modifica ID studente
     * @param id_studente da impostare
     */
    public void setId_studente(Long id_studente){
        this.id_studente = id_studente;
    }

    /**
     * Ottiene ID di richiesta tesi
     * @return ID
     */
    public Long getId_richiesta_tesi() {
        return id_richiesta_tesi;
    }

    /**
     * Modifica ID richiesta tesi
     * @param idRichiestaTesi ID da impostare
     */
    public void setId_richiesta_tesi(Long idRichiestaTesi) {
        this.id_richiesta_tesi = idRichiestaTesi;
    }

    /**
     * Ottieni ID della tesi associata alla richiesta
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
     * Ottiene lo stato corrente.
     * @return Lo stato corrente
     */
    public String getStato() {
        return stato;
    }

    /**
     * Imposta lo stato specificato.
     * @param stato Lo stato da impostare
     */
    public void setStato(String stato) {
        this.stato = stato;
    }

    /**
     * Restituisce una mappa contenente le informazioni dell'entità RichiestaTesi
     * @return Mappa che contiene le informazioni dell'entità RichiestaTesi
     */
    public Map<String, Object> getRichiesteTesiMap() {
        Map<String, Object> richiesteTesiMap = new HashMap<>();
        richiesteTesiMap.put("id_tesi", this.id_tesi);
        richiesteTesiMap.put("id_richiesta_tesi",this.id_richiesta_tesi);
        richiesteTesiMap.put("id_studente",this.id_studente);
        richiesteTesiMap.put("soddisfa_requisiti",this.soddisfa_requisiti);
        richiesteTesiMap.put("stato", this.stato);



        return richiesteTesiMap;
    }

    /**
     * Metodo toString rappresentazione testuale dell'oggetto RichiestaTesi
     * @return Una stringa che rappresenta l'oggetto RichiestaTesi
     */
    @NonNull
    @Override
    public String toString() {
        return "RichiesteTesi{" +
                "id_tesi=" + id_tesi +
                ", id_richiesta_tesi=" + id_richiesta_tesi +
                ", id_studente" + id_studente +
                ", soddisfa_requisiti " + soddisfa_requisiti +
                ", stato='" + stato + '\'' +
                '}';
    }
}
