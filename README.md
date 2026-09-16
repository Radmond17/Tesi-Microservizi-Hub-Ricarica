# Backend a Microservizi per Hub di Ricarica EV ⚡

Questo repository contiene il codice sorgente del mio progetto di Tesi di Laurea in Ingegneria Elettronica e Informatica presso l'Università degli Studi della Campania "Luigi Vanvitelli"[cite: 11]. 
Il progetto consiste in un'architettura backend basata su microservizi per la gestione sicura e scalabile delle prenotazioni di colonnine elettriche[cite: 11].

## 🏗️ Architettura del Sistema
Il sistema implementa il pattern **Database-per-Service** ed è diviso in due domini isolati che comunicano tramite orchestrazione REST sincrona[cite: 11]:
*   **Servizio Utenti (Porta 8081):** Gestisce l'anagrafica, verifica le autorizzazioni e gli abbonamenti dei guidatori e orchestra la transazione di ricarica[cite: 11].
*   **Servizio Colonnine (Porta 8082):** Traccia lo stato fisico dell'infrastruttura (LIBERA, IN_USO, FUORI_SERVIZIO)[cite: 11].

## 🛡️ Gestione della Concorrenza (Optimistic Locking)
La sfida ingegneristica principale è stata la prevenzione delle *race condition* nell'assegnazione delle risorse[cite: 11]. Per evitare che richieste simultanee occupino la stessa colonnina, è stato implementato un meccanismo transazionale di **Lock Ottimistico** (`@Version` tramite Hibernate)[cite: 11]. In caso di collisione, il sistema intercetta l'eccezione e restituisce un errore HTTP 409 (Conflict) in formato JSON standardizzato[cite: 11].

## 💻 Stack Tecnologico
*   **Linguaggio & Framework:** Java 17, Spring Boot 3.x (Web, Data JPA)[cite: 11]
*   **Database:** H2 Database (in-memory) con popolamento automatico tramite Data Loader[cite: 11]
*   **Build Automation:** Apache Maven (fat-JAR packaging)[cite: 11]
*   **Documentazione API:** OpenAPI / Swagger UI[cite: 11]
*   **Testing:** JUnit 5, Mockito (Unit/Integration Test), Collaudi E2E tramite Postman[cite: 11]

## 🚀 Come avviare il progetto in locale
1. Clonare il repository.
2. Avviare il Servizio Utenti dalla sua cartella principale: `mvn spring-boot:run`
3. Avviare il Servizio Colonnine dalla sua cartella principale: `mvn spring-boot:run`
4. Consultare la documentazione delle API su Swagger UI (es. `http://localhost:8081/swagger-ui.html`)[cite: 11].

📄 *Per i dettagli architetturali completi, il Diagramma dei Casi d'Uso e il Sequence Diagram dell'orchestrazione REST, consultare il PDF della tesi allegato in questa repository.*
