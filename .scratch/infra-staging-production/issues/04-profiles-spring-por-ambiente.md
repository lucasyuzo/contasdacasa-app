# 04 — Profiles Spring local/staging/production

**What to build:** separar config por ambiente sem quebrar o fluxo local atual.

**Status:** ready-for-agent

- [ ] Manter `application.properties` como esta hoje (perfil `local` implicito, sem flag — `./mvnw spring-boot:run` continua funcionando igual)
- [ ] Criar `application-staging.properties` com `spring.datasource.url/username/password` via placeholder de env var (`${DB_URL}`, `${DB_USER}`, `${DB_PASSWORD}`)
- [ ] Criar `application-production.properties` com os mesmos placeholders
- [ ] `spring.jpa.hibernate.ddl-auto=validate` e Flyway habilitado nos dois (mesma config do local, ADR-0004)
- [ ] `spring.jpa.show-sql=false` em staging/production (so faz sentido local)
