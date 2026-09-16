package com.example.servizio_colonnine;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Bean
    CommandLineRunner caricaColonnine(ColonninaRepository repo) {
        return args -> {
            // Pulisce il database ad ogni avvio per evitare duplicati
            repo.deleteAll();

            // Usiamo il costruttore compatto che abbiamo creato prima
            Colonnina c1 = new Colonnina("XYZ-999", StatoColonnina.LIBERA);
            Colonnina c2 = new Colonnina("ABC-123", StatoColonnina.LIBERA);
            Colonnina c3 = new Colonnina("QWE-456", StatoColonnina.IN_USO); // Una già occupata per i test

            // Salviamo tutto nel database H2
            repo.save(c1);
            repo.save(c2);
            repo.save(c3);

            System.out.println("Database Colonnine popolato con successo!");
            System.out.println("Colonnina 1 ID: " + c1.getId());
            System.out.println("Colonnina 2 ID: " + c2.getId());
        };
    }
}