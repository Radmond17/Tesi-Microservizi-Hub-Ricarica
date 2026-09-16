# Backend a Microservizi per Hub di Ricarica EV ⚡

Questo repository contiene il codice sorgente del mio progetto di Tesi di Laurea in Ingegneria Elettronica e Informatica presso l'Università degli Studi della Campania "Luigi Vanvitelli". 
Il progetto consiste in un'architettura backend basata su microservizi per la gestione sicura e scalabile delle prenotazioni di colonnine elettriche.

## 🏗️ Architettura del Sistema
Il sistema implementa il pattern **Database-per-Service** ed è diviso in due domini isolati che comunicano tramite orchestrazione REST sincrona:
*   **Servizio Utenti (Porta 8081):** Gestisce l'anagrafica, verifica le autorizzazioni e gli abbonamenti dei guidatori e orchestra la transazione di ricarica.
*   **Servizio Colonnine (Porta 8082):** Traccia lo stato fisico dell'infrastruttura (LIBERA, IN_USO, FUORI_SERVIZIO).

## 🛡️ Gestione della Concorrenza (Optimistic Locking)
La sfida ingegneristica principale è stata la prevenzione delle *race condition* nell'assegnazione delle risorse. Per evitare che richieste simultanee occupino la stessa colonnina, è stato implementato un meccanismo transazionale di **Lock Ottimistico** (`@Version` tramite Hibernate). In caso di collisione, il sistema intercetta l'eccezione e restituisce un errore HTTP 409 (Conflict) in formato JSON standardizzato.

## 💻 Stack Tecnologico
*   **Linguaggio & Framework:** Java 17, Spring Boot 3.x (Web, Data JPA)
*   **Database:** H2 Database (in-memory) con popolamento automatico tramite Data Loader
*   **Build Automation:** Apache Maven (fat-JAR packaging)
*   **Documentazione API:** OpenAPI / Swagger UI
*   **Testing:** JUnit 5, Mockito (Unit/Integration Test), Collaudi E2E tramite Postman

## 🚀 Come avviare il progetto in locale
1. Clonare il repository.
2. Avviare il Servizio Utenti dalla sua cartella principale: `mvn spring-boot:run`
3. Avviare il Servizio Colonnine dalla sua cartella principale: `mvn spring-boot:run`
4. Consultare la documentazione delle API su Swagger UI (es. `http://localhost:8081/swagger-ui.html`).

📄 *Per i dettagli architetturali completi, il Diagramma dei Casi d'Uso e il Sequence Diagram dell'orchestrazione REST, consultare il PDF della tesi allegato in questa repository.*
