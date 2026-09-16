package com.example.servizio_utenti;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {
    @Bean
    CommandLineRunner caricaUtenti(UtenteRepository repo) {
        return args -> {
            Utente u = new Utente("U123", "Mario", "Rossi", true);
            repo.save(u);
            System.out.println("Utente di test inserito con successo nel DB! Matricola: " + u.getMatricola());
        };
    }
}
