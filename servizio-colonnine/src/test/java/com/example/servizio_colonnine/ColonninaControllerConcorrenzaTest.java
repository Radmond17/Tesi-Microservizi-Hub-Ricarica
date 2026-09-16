package com.example.servizio_colonnine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ColonninaControllerConcorrenzaTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ColonninaRepository colonninaRepository;

    private Long idColonninaTest;

    @BeforeEach
    void setup() {
        // 1. Arrange: Puliamo il DB e inseriamo una colonnina vergine apposta per questo test
        colonninaRepository.deleteAll();
        Colonnina c = new Colonnina("TEST-CONCORRENZA", StatoColonnina.LIBERA);
        c = colonninaRepository.save(c);
        idColonninaTest = c.getId();
    }

    @Test
    void testOccupazioneConcorrente_OptimisticLocking() throws Exception {
        // 2. Prepariamo due utenti fittizi che vogliono la STESSA colonnina
        Map<String, String> payloadUtente1 = Map.of("idUtente", "U001");
        Map<String, String> payloadUtente2 = Map.of("idUtente", "U002");
        String url = "/api/colonnine/" + idColonninaTest + "/occupa";

        // 3. Act: Lanciamo le due richieste HTTP su due Thread separati nello stesso istante
        CompletableFuture<ResponseEntity<Map>> request1 = CompletableFuture.supplyAsync(() ->
                restTemplate.postForEntity(url, payloadUtente1, Map.class)
        );

        CompletableFuture<ResponseEntity<Map>> request2 = CompletableFuture.supplyAsync(() ->
                restTemplate.postForEntity(url, payloadUtente2, Map.class)
        );

        // Aspettiamo che entrambi i thread abbiano finito il loro lavoro
        CompletableFuture.allOf(request1, request2).join();

        ResponseEntity<Map> response1 = request1.get();
        ResponseEntity<Map> response2 = request2.get();

        // 4. Assert: Verifichiamo il trionfo dell'Optimistic Locking!
        // Uno dei due DEVE aver ricevuto un 200 OK, l'altro DEVE essersi schiantato su un 409 CONFLICT
        boolean unoHaSuccesso = response1.getStatusCode() == HttpStatus.OK || response2.getStatusCode() == HttpStatus.OK;
        boolean unoInConflitto = response1.getStatusCode() == HttpStatus.CONFLICT || response2.getStatusCode() == HttpStatus.CONFLICT;

        assertTrue(unoHaSuccesso, "Il sistema deve permettere a una delle due richieste di passare (200 OK)");
        assertTrue(unoInConflitto, "Il sistema deve bloccare la seconda richiesta con un'eccezione di concorrenza (409 CONFLICT)");
    }
}