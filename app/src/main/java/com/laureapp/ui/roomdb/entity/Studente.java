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

        public Studente(Long id_studente, Long id_utente, Long matricola, int media, int esami_mancanti) {
            this.id_studente = id_studente;
            this.id_utente = id_utente;
            this.matricola = matricola;
            this.media = media;
            this.esami_mancanti = esami_mancanti;
        }

        public Studente(){}
        //Getter e setter

        public Long getId_studente() {
            return id_studente;
        }

        public void setId_studente(Long idStudente) {
            this.id_studente = idStudente;
        }

        public Long getId_utente() {
            return id_utente;
        }


        public void setId_utente(Long idUtente) {
            this.id_utente = idUtente;
        }

        public Long getMatricola() {
            return matricola;
        }

        public void setMatricola(Long matricolaStudente) {
            this.matricola = matricolaStudente;
        }

        public int getMedia() {
            return media;
        }

        public void setMedia(int mediaVoti) {
            this.media = mediaVoti;
        }

        public int getEsami_mancanti() {
            return esami_mancanti;
        }

        public void setEsami_mancanti(int esamiMancanti) {this.esami_mancanti = esamiMancanti;}



        public Map<String, Object> getStudenteMap() {
            Map<String, Object> studenteMap = new HashMap<>();
            studenteMap.put("id_studente",this.id_studente);
            studenteMap.put("id_utente", this.id_utente);
            studenteMap.put("matricola", this.matricola);
            studenteMap.put("media" , this.media);
            studenteMap.put("esami_mancanti", this.esami_mancanti);
            return studenteMap;
        }

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
