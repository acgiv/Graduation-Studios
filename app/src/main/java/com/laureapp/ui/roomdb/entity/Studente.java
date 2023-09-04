package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName ="Studente" , foreignKeys = {
        @ForeignKey(entity = Utente.class, parentColumns = "id", childColumns = "id_utente")
})
public class Studente implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private Long matricola;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String nome;
    private String cognome;
    private String email;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    @ColumnInfo(name = "id_utente")
    private Long id_utente;

    public Long getMatricola() {
        return matricola;
    }

    public void setMatricola(Long matricola) {
        this.matricola = matricola;
    }

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

    @NonNull
    @Override
    public String toString() {
        return "Studente{" +
                "id=" + id +
                ", matricola=" + matricola +
                ", id_utente=" + id_utente +
                '}';
    }
}
