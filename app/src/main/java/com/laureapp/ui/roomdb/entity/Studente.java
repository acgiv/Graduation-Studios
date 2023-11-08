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
     * Questa classe rappresenta l'entità Studente nel database Room.
     */
    @Entity(tableName = "Studente", foreignKeys = {
            @ForeignKey(entity = Utente.class, parentColumns = "id_utente", childColumns = "id_utente", onDelete = ForeignKey.CASCADE)
    }, indices = {@Index("id_utente")})
    public class Studente implements Serializable {

        //Colonne tabella
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id_studente")
        private Long id_studente;
        @ColumnInfo(name = "id_utente")
        private Long id_utente;
        @ColumnInfo(name = "matricola")
        private Long matricola;
        @ColumnInfo(name = "media")
        private int media;
        @ColumnInfo(name = "esami_mancanti")
        private int esami_mancanti;

        /**
         * Costruttore classe Studente
         * @param id_studente
         * @param id_utente
         * @param matricola
         * @param media
         * @param esami_mancanti
         */
        public Studente(Long id_studente, Long id_utente, Long matricola, int media, int esami_mancanti) {
            this.id_studente = id_studente;
            this.id_utente = id_utente;
            this.matricola = matricola;
            this.media = media;
            this.esami_mancanti = esami_mancanti;
        }

        public Studente(){}
        //Getter e setter

        /**
         * Restituisce l'ID dello studente.
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
         * Restituisce l'ID dell'utente associato allo studente.
         * @return L'ID dell'utente
         */
        public Long getId_utente() {
            return id_utente;
        }

        /**
         * Imposta l'ID dell'utente associato allo studente.
         * @param idUtente L'ID dell'utente da impostare
         */
        public void setId_utente(Long idUtente) {
            this.id_utente = idUtente;
        }

        /**
         * Restituisce la matricola dello studente.
         * @return La matricola dello studente
         */
        public Long getMatricola() {
            return matricola;
        }

        /**
         * Imposta la matricola dello studente.
         * @param matricolaStudente La matricola dello studente da impostare
         */
        public void setMatricola(Long matricolaStudente) {
            this.matricola = matricolaStudente;
        }

        /**
         * Restituisce la media dei voti dello studente.
         * @return La media dei voti dello studente
         */
        public int getMedia() {
            return media;
        }

        /**
         * Imposta la media dei voti dello studente.
         * @param mediaVoti La media dei voti da impostare
         */
        public void setMedia(int mediaVoti) {
            this.media = mediaVoti;
        }

        /**
         * Restituisce il numero di esami mancanti dello studente.
         * @return Il numero di esami mancanti dello studente
         */
        public int getEsami_mancanti() {
            return esami_mancanti;
        }

        /**
         * Imposta il numero di esami mancanti dello studente.
         * @param esamiMancanti Il numero di esami mancanti da impostare
         */
        public void setEsami_mancanti(int esamiMancanti) {this.esami_mancanti = esamiMancanti;}

        /**
         * Restituisce una mappa contenente le informazioni dell'entità Studente.
         * @return Mappa che contiene le informazioni dell'entità Studente
         */
        public Map<String, Object> getStudenteMap() {
            Map<String, Object> studenteMap = new HashMap<>();
            studenteMap.put("id_studente",this.id_studente);
            studenteMap.put("id_utente", this.id_utente);
            studenteMap.put("matricola", this.matricola);
            studenteMap.put("media" , this.media);
            studenteMap.put("esami_mancanti", this.esami_mancanti);
            return studenteMap;
        }

        /**
         * Metodo toString per la rappresentazione testuale dell'oggetto Studente.
         * @return Una stringa che rappresenta l'oggetto Studente
         */
        @NonNull
        @Override
        public String toString() {
            return "Studente{" +
                    "id_studente=" + id_studente +
                    ", id_utente=" + id_utente +
                    ", matricola=" + matricola +
                    ", media=" + media +
                    ", esami_mancanti=" + esami_mancanti +
                    '}';
        }




    }
