    package com.laureapp.ui.roomdb.entity;


    import androidx.annotation.NonNull;
    import androidx.room.ColumnInfo;
    import androidx.room.Entity;
    import androidx.room.ForeignKey;
    import androidx.room.PrimaryKey;

    import com.laureapp.ui.roomdb.RoomDbSqlLite;

    import java.io.Serializable;
    import java.util.HashMap;
    import java.util.Map;

    @Entity(tableName ="Studente" , foreignKeys = {
            @ForeignKey(entity = Utente.class, parentColumns = "utente_id", childColumns = "id_utente")
    })
    public class Studente implements Serializable {

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "studente_id")
        private Long id;

        @ColumnInfo(name = "id_utente")
        private Long id_utente;
        @ColumnInfo(name = "matricola")
        private Long matricola;
        @ColumnInfo(name = "media")
        private int media;
        @ColumnInfo(name = "esami_mancati")
        private int esami_mancati;
        @ColumnInfo(name = "facolta")
        private String facolta;
        @ColumnInfo(name = "corso_laurea")
        private String corso_laurea;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getId_utente() {
            return id_utente;
        }


        public void setId_utente(Long id_utente) {
            this.id_utente = id_utente;
        }

        public Long getMatricola() {
            return matricola;
        }

        public void setMatricola(Long matricola) {
            this.matricola = matricola;
        }

        public int getMedia() {
            return media;
        }

        public void setMedia(int media) {
            this.media = media;
        }

        public int getEsami_mancati() {
            return esami_mancati;
        }

        public void setEsami_mancati(int esami_mancati) {
            this.esami_mancati = esami_mancati;
        }

        public String getFacolta() {
            return facolta;
        }

        public void setFacolta(String facolta) {
            this.facolta = facolta;
        }

        public String getCorso_laurea() {
            return corso_laurea;
        }

        public void setCorso_laurea(String corso_laurea) {
            this.corso_laurea = corso_laurea;
        }

        public Map<String, Object> getStudenteMap() {
            Map<String, Object> studenteMap = new HashMap<>();
            studenteMap.put("id_utente", id_utente);
            studenteMap.put("matricola", this.matricola);
            studenteMap.put("media" , this.media);
            studenteMap.put("esami_mancati", esami_mancati);
            studenteMap.put("facolta", facolta);
            studenteMap.put("corso_laurea", corso_laurea);

            return studenteMap;
        }

        @NonNull
        @Override
        public String toString() {
            return "Studente{" +
                    "id=" + id +
                    ", id_utente=" + id_utente +
                    ", matricola=" + matricola +
                    ", media=" + media +
                    ", esami_mancati=" + esami_mancati +
                    ", facolta=" + facolta +
                    ", corso_laurea=" + corso_laurea +
                    '}';
        }




    }
