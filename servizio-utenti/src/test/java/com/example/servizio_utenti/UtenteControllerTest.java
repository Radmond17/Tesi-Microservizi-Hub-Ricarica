package com.example.servizio_utenti; // Assicurati che il package sia quello giusto del tuo progetto

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UtenteControllerTest {

    @Mock
    private UtenteRepository utenteRepository;

    @Mock
    private RestTemplate restTemplate;

    // Sostituisci la vecchia dichiarazione rossa con questa:
    @InjectMocks
    private RicaricaController ricaricaController;

    @Test
    void testAvviaRicarica_UtenteNonAbilitato() {
        // Arrange: Preparo i dati finti e istruisco il finto database (Mock)
        String matricola = "U999";
        Utente utenteDisabilitato = new Utente(matricola, "Raimondo", "Panico", false);

        when(utenteRepository.findByMatricola(matricola))
                .thenReturn(Optional.of(utenteDisabilitato));

        Map<String, Object> payload = Map.of("idUtente", matricola, "idColonnina", 1);

        // Act: Eseguo il metodo del controller isolato usando il nome corretto!
        ResponseEntity<?> response = ricaricaController.avviaRicarica(payload);

        // Assert: Verifico che il sistema abbia bloccato la richiesta (HTTP 403 Forbidden)
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        Map<String, String> body = (Map<String, String>) response.getBody();
        assertEquals("Utente non abilitato alla ricarica", body.get("errore"));
    }
}
