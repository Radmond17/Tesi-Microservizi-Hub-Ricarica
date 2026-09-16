package com.example.servizio_colonnine;
import jakarta.persistence.*;

@Entity
public class Colonnina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codiceSeriale;

    @Enumerated(EnumType.STRING)
    private StatoColonnina stato;

    @Version
    private Long versione;

    public Colonnina() {}

    public Colonnina(String codiceSeriale, StatoColonnina stato) {
        this.codiceSeriale = codiceSeriale;
        this.stato = stato;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public StatoColonnina getStato() { return stato; }
    public void setStato(StatoColonnina stato) { this.stato = stato; }
    public String getCodiceSeriale() { return codiceSeriale; }
    public void setCodiceSeriale(String codiceSeriale) { this.codiceSeriale = codiceSeriale; }
}




