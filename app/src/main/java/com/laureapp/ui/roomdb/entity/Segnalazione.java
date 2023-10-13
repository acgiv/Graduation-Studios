package com.laureapp.ui.roomdb.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/*  Entità del Database:
*
*   Le entità del database sono classi Java che rappresentano le tabelle del database.
*   Ogni campo nella classe rappresenta una colonna nella tabella del database.
*   Vengono annotate con @Entity per definire il nome della tabella, le chiavi primarie, le chiavi esterne, gli indici, ecc.
*/

@Entity(
        tableName = "Segnalazioni",
        foreignKeys = {
                @ForeignKey(
                        entity = Tesi.class, // Nome dell'entità correlata
                        parentColumns = "id_tesi", // Colonna chiave primaria nella tabella correlata
                        childColumns = "id_tesi", // Colonna chiave esterna in questa tabella
                        onDelete = ForeignKey.CASCADE // Azione da intraprendere in caso di eliminazione della tesi correlata
                )
        },
        indices = {@Index("id_tesi")} // Creazione di un indice sulla colonna id_tesi per le prestazioni delle query
)
public class Segnalazione {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_segn")
    private Long idSegnalazione;

    @ColumnInfo(name = "id_tesi")
    private Long idTesi;

    @ColumnInfo(name = "titolo")
    private String titolo;


    @ColumnInfo(name = "richiesta")
    private String richiesta;

    // Costruttore vuoto (richiesto da Room)
    public Segnalazione() {
    }

    // Costruttore per creare una nuova segnalazioni
    public Segnalazione(Long idTesi, String titolo, String richiesta) {
        this.idTesi = idTesi;
        this.titolo = titolo;
        this.richiesta = richiesta;
    }

    // Getter e Setter per gli attributi
    public Long getIdSegnalazione() {
        return idSegnalazione;
    }

    public void setIdSegnalazione(Long idSegnalazione) {
        this.idSegnalazione = idSegnalazione;
    }

    public Long getIdTesi() {
        return idTesi;
    }

    public void setIdTesi(Long idTesi) {
        this.idTesi = idTesi;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getRichiesta() {
        return richiesta;
    }

    public void setRichiesta(String richiesta) {
        this.richiesta = richiesta;
    }

    @Override
    public String toString() {
        return "Segnalazione{" +
                "idSegnalazione=" + idSegnalazione +
                ", idTesi=" + idTesi +
                ", titolo='" + titolo + '\'' +
                ", richiesta='" + richiesta + '\'' +
                '}';
    }
}

