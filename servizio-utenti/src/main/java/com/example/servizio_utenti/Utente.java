package com.example.servizio_utenti;
import jakarta.persistence.*;

@Entity
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String matricola;
    private String nome;
    private String cognome;
    private boolean abilitato;

    public Long getId() { return id; }
    public String getMatricola() { return matricola; }
    public boolean isAbilitato() { return abilitato; }

    // Un costruttore comodo che ci servirà dopo per inserire dati di prova
    public Utente() {}
    public Utente(String matricola, String nome, String cognome, boolean abilitato) {
        this.matricola = matricola;
        this.nome = nome;
        this.cognome = cognome;
        this.abilitato = abilitato;
    }
}
