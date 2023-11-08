package com.laureapp.ui.roomdb.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Questa classe rappresenta l'entità Segnalazione nel database Room.
 */

@Entity(
        tableName = "Segnalazione",
        foreignKeys = {
                @ForeignKey(
                        entity = StudenteTesi.class, // Nome dell'entità correlata
                        parentColumns = "id_studente_tesi", // Colonna chiave primaria nella tabella correlata
                        childColumns = "id_studente_tesi", // Colonna chiave esterna in questa tabella
                        onDelete = ForeignKey.CASCADE // Azione da intraprendere in caso di eliminazione della tesi correlata
                )
        },
        indices = {@Index("id_studente_tesi")} // Creazione di un indice sulla colonna id_tesi per le prestazioni delle query
)
public class Segnalazione implements Serializable {

    /* Entità del Database:
    Le entità del database sono classi Java che rappresentano le tabelle del database.
    Ogni campo nella classe rappresenta una colonna nella tabella del database.
    Vengono annotate con @Entity per definire il nome della tabella, le chiavi primarie, le chiavi esterne, gli indici, ecc.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_segnalazione")
    private Long id_segnalazione;

    @ColumnInfo(name = "id_studente_tesi")
    private Long id_studente_tesi;

    @ColumnInfo(name = "titolo")
    private String titolo;


    // Costruttore vuoto (richiesto da Room)
    public Segnalazione() {
    }

    /**
     * Costruttore della classe Segnalazione
     * @param idSegnalazione
     * @param idStudenteTesi
     * @param titolo
     */
    // Costruttore per creare una nuova segnalazioni
    public Segnalazione(Long idSegnalazione, Long idStudenteTesi, String titolo) {
        this.id_segnalazione = idSegnalazione;
        this.id_studente_tesi = idStudenteTesi;
        this.titolo = titolo;
    }

    // Getter e Setter per gli attributi
    /**
     * Restituisce l'ID di Segnalazione
     * @return l'ID di Segnalazione
     */
    public Long getId_segnalazione() {
        return id_segnalazione;
    }

    /**
     * Imposta l'ID di Segnalazione
     * @param idSegnalazione l'ID da impostare per Segnalazione
     */
    public void setId_segnalazione(Long idSegnalazione) {
        this.id_segnalazione = idSegnalazione;
    }

    /**
     * Restituisce l'ID StudenteTesi associato
     * @return L'ID StudenteTesi
     */
    public Long getId_studente_tesi() {
        return id_studente_tesi;
    }

    /**
     * Modifica ID StudenteTesi
     * @param id_studente_tesi
     */
    public void setId_studente_tesi(Long id_studente_tesi) {
        this.id_studente_tesi = id_studente_tesi;
    }

    /**
     * Ottieni titolo segnalazione
     * @return titolo
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Modifica titolo segnalazione
     * @param titolo
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Metodo toString rappresentazione testuale dell'oggetto Segnalazione
     * @return Una stringa che rappresenta l'oggetto Segnalazione
     */
    @Override
    public String toString() {
        return "Segnalazione{" +
                "idSegnalazione=" + id_segnalazione +
                ", idTesi=" + id_studente_tesi +
                ", titolo='" + titolo + '\'' +
                '}';
    }
}

