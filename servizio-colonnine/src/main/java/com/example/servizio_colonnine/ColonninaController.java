package com.example.servizio_colonnine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/colonnine")
public class ColonninaController {

    @Autowired
    private ColonninaRepository repo;

    @PostMapping("/{id}/occupa")
    public ResponseEntity<?> occupaColonnina(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {

        // 1. Cerchiamo la colonnina (gestendo il caso 404 in modo pulito)
        Optional<Colonnina> colOpt = repo.findById(id);

        if (colOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("errore", "Colonnina inesistente"));
        }

        Colonnina colonnina = colOpt.get();

        // 2. Controllo type-safe tramite Enum
        if (colonnina.getStato() != StatoColonnina.LIBERA) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("errore", "Colonnina già occupata o fuori servizio"));
        }

        // 3. Modifica dello stato
        colonnina.setStato(StatoColonnina.IN_USO);

        try {
            // Il salvataggio scatena il controllo del Lock Ottimistico (@Version)
            repo.save(colonnina);

            // Manteniamo il tuo payload di ritorno e ci aggiungiamo i dati della colonnina
            return ResponseEntity.ok(Map.of(
                    "esito", "SUCCESS",
                    "dettaglio", "Colonnina sbloccata per utente " + payload.get("idUtente"),
                    "idColonnina", colonnina.getId(),
                    "stato", colonnina.getStato().name(), // Trasforma l'enum in stringa JSON
                    "seriale", colonnina.getCodiceSeriale()
            ));

        } catch (ObjectOptimisticLockingFailureException e) {
            // 4. Conflitto rilevato: un'altra transazione ha rubato la colonnina nel frattempo!
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("errore", "Conflitto: risorsa modificata da un'altra transazione."));
        }
    }
}