package com.laureapp.ui.roomdb.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName ="Utente")
public class Utente {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "nome")
    private String nome;

    @ColumnInfo(name = "cognome")
    private String cognome;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String passwordl;

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
        email = email;
    }

    public String getPasswordl() {
        return passwordl;
    }

    public void setPasswordl(String passwordl) {
        this.passwordl = passwordl;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", passwordl='" + passwordl + '\'' +
                '}';
    }
}
