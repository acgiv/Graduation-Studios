package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "Utente",
        indices = {@Index(value = "email", unique = true)})

public class Utente implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_utente")
    private Long id_utente;

    @ColumnInfo(name = "nome")
    private String nome;

    @ColumnInfo(name = "cognome")
    private String cognome;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    public Long getId_utente() {
        return id_utente;
    }

    public void setId_utente(Long idUtente) {
        this.id_utente = idUtente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nomeUtente) {
        this.nome = nomeUtente;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognomeUtente) {
        this.cognome = cognomeUtente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String emailUtente) {
        this.email = emailUtente;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String passwordUtente) {
        this.password = passwordUtente;
    }

    public Map<String, Object> getUtenteMap() {
        Map<String, Object> utenteMap = new HashMap<>();
        utenteMap.put("id_utente",this.id_utente);
        utenteMap.put("nome", this.nome);
        utenteMap.put("cognome", this.cognome);
        utenteMap.put("email", this.email);
        utenteMap.put("password", this.password);
        return utenteMap;
    }

    @NonNull
    @Override
    public String toString() {
        return "Utente{" +
                "id_utente=" + id_utente +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
