## Ambiente local

Stack: Java 26, Spring Boot 4.1.0, Maven (wrapper `./mvnw`), Postgres, Flyway.

Banco: Postgres local na porta 5432, db `contasdacasa`, user `postgres` (ver `src/main/resources/application.properties`). Subir Postgres manualmente antes de rodar a app ou os testes de integracao (`docker-compose.staging.yml`/`docker-compose.production.yml` sao so pros ambientes de VPS, ver ADR-0007/0011).

Comandos:
- Build: `./mvnw compile`
- Testes: `./mvnw test`
- Rodar: `./mvnw spring-boot:run`
- Migration: Flyway roda automatico no boot (`spring.flyway.enabled=true`), arquivos em `src/main/resources/db/migration`

## Agent skills

### Issue tracker

Local markdown, arquivos em `.scratch/`. See `docs/agents/issue-tracker.md`.

### Triage labels

Labels padrão (needs-triage, needs-info, ready-for-agent, ready-for-human, wontfix). See `docs/agents/triage-labels.md`.

### Domain docs

Single-context (CONTEXT.md + docs/adr/ na raiz). See `docs/agents/domain.md`.
