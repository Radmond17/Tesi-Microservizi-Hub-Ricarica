package com.example.servizio_utenti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    // Istruiamo Spring a cercare un utente usando la sua matricola
    Optional<Utente> findByMatricola(String matricola);
}
