package com.laureapp.ui.roomdb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Questa classe rappresenta l'entità Utente nel database Room
 */
@Entity(tableName = "Utente")

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

    @ColumnInfo(name = "nome_cdl")
    private String nome_cdl;

    @ColumnInfo(name = "facolta")
    private String facolta;

    /**
     * Costruttore per la classe Utente.
     * @param id_utente l'ID dell'utente.
     * @param nome il nome dell'utente.
     * @param cognome il cognome dell'utente.
     * @param email l'email dell'utente.
     * @param password la password dell'utente.
     * @param nome_cdl il nome del corso di laurea dell'utente.
     * @param facolta la facoltà dell'utente.
     */
    public Utente(Long id_utente, String nome, String cognome, String email, String password, String nome_cdl, String facolta) {
        this.id_utente = id_utente;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.nome_cdl = nome_cdl;
        this.facolta = facolta;
    }
    public Utente(){}

    /**
     * Restituisce l'ID dell'utente.
     * @return l'ID dell'utente.
     */
    public Long getId_utente() {
        return id_utente;
    }

    /**
     * Imposta l'ID dell'utente.
     * @param idUtente l'ID dell'utente da impostare.
     */
    public void setId_utente(Long idUtente) {
        this.id_utente = idUtente;
    }

    /**
     * Restituisce il nome dell'utente.
     * @return il nome dell'utente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome dell'utente.
     * @param nomeUtente il nome dell'utente da impostare.
     */
    public void setNome(String nomeUtente) {
        this.nome = nomeUtente;
    }

    /**
     * Restituisce il cognome dell'utente.
     * @return il cognome dell'utente.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome dell'utente.
     * @param cognomeUtente il cognome dell'utente da impostare.
     */
    public void setCognome(String cognomeUtente) {
        this.cognome = cognomeUtente;
    }

    /**
     * Restituisce l'email dell'utente.
     * @return l'email dell'utente.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta l'email dell'utente.
     * @param emailUtente l'email dell'utente da impostare.
     */
    public void setEmail(String emailUtente) {
        this.email = emailUtente;
    }

    /**
     * Restituisce la password dell'utente.
     * @return la password dell'utente.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Imposta la password dell'utente.
     * @param passwordUtente la password dell'utente da impostare.
     */
    public void setPassword(String passwordUtente) {
        this.password = passwordUtente;
    }

    /**
     * Restituisce il nome del corso di laurea dell'utente.
     * @return il nome del corso di laurea dell'utente.
     */
    public String getNome_cdl() {
        return nome_cdl;
    }

    /**
     * Imposta il nome del corso di laurea dell'utente.
     * @param nome_cdl il nome del corso di laurea dell'utente da impostare.
     */
    public void setNome_cdl(String nome_cdl) {
        this.nome_cdl = nome_cdl;
    }

    /**
     * Restituisce la facoltà dell'utente.
     * @return la facoltà dell'utente.
     */
    public String getFacolta() {
        return facolta;
    }

    /**
     * Imposta la facoltà dell'utente.
     * @param facolta la facoltà dell'utente da impostare.
     */
    public void setFacolta(String facolta) {
        this.facolta = facolta;
    }

    /**
     * Restituisce una mappa con le informazioni dell'utente.
     * @return una mappa con le informazioni dell'utente.
     */
    public Map<String, Object> getUtenteMap() {
        Map<String, Object> utenteMap = new HashMap<>();
        utenteMap.put("id_utente",this.id_utente);
        utenteMap.put("nome", this.nome);
        utenteMap.put("cognome", this.cognome);
        utenteMap.put("email", this.email);
        utenteMap.put("password", this.password);
        utenteMap.put("nome_cdl", this.nome_cdl);
        utenteMap.put("facolta", this.facolta);
        return utenteMap;
    }

    /**
     * Metodo toString rappresentazione testuale dell'oggetto Utente.
     * @return una stringa che rappresenta l'oggetto Utente.
     */
    @NonNull
    @Override
    public String toString() {
        return "Utente{" +
                "id_utente=" + id_utente +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nome_cdl='" + nome_cdl + '\'' +
                ", facolta='" + facolta + '\'' +
                '}';
    }
}
