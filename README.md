# Novera

School Management application.

| Folder | What |
|---|---|
| `novera-server` | Spring Boot 4 / Java 21 / Postgres API |
| `novera-web` | React 19 / Vite UI |
| `novera-doc` | Docs, ADRs, sprint notes |
| `novera-setup` | docker-compose, env templates, scripts |

## Run the API locally
```bash
cd novera-setup && cp .env.example .env && docker compose up -d
cd ../novera-server
mvn wrapper:wrapper          # one-time: generates mvnw
./mvnw test                  # needs Docker running (Testcontainers)
./mvnw spring-boot:run
curl http://localhost:8080/actuator/health
```
