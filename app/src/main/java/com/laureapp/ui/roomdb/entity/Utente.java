package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName ="Utente")
public class Utente implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "utente_id")
    private Long id;

    @ColumnInfo(name = "nome")
    private String nome;

    @ColumnInfo(name = "cognome")
    private String cognome;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String passwordl) {
        this.password = passwordl;
    }

    public Map<String, Object> getUtenteMap() {
        Map<String, Object> utenteMap = new HashMap<>();
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
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
