package com.example.servizio_utenti;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@RestController
@RequestMapping("/api/ricarica")
public class RicaricaController {

    @Autowired
    private UtenteRepository repo;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/avvia")
    public ResponseEntity<?> avviaRicarica(@RequestBody Map<String, Object> payload) {
        String matricola = (String) payload.get("idUtente");
        Long idColonnina = ((Number) payload.get("idColonnina")).longValue();

        // 1. Verifica che l'utente esista nel DB locale
        Utente utente = repo.findByMatricola(matricola)
                .orElseThrow(() -> new RuntimeException("Utente inesistente"));

        // 2. Controlla se ha l'abbonamento valido
        if (!utente.isAbilitato()) {
            return ResponseEntity.status(403).body(Map.of("errore", "Utente non abilitato alla ricarica"));
        }

        // 3. Fa una chiamata REST verso il Servizio Colonnine (sulla porta 8082)
        String urlColonnine = "http://localhost:8082/api/colonnine/" + idColonnina + "/occupa";

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(urlColonnine, Map.of("idUtente", matricola), Map.class);
            return ResponseEntity.ok(Map.of("messaggio", "Ricarica avviata con successo"));

        } catch (Exception e) {
            // Se la colonnina risponde con un errore o se scatta il Lock Ottimistico
            return ResponseEntity.status(409).body(Map.of("errore", "La colonnina e' occupata o si e' verificato un conflitto"));
        }
    }
}
