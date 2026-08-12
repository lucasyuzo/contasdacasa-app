# Flyway para migração de schema, ddl-auto=validate

Trocamos `hibernate.ddl-auto=update` por `validate`, e adotamos Flyway com migrations versionadas (`V1__...sql`, `V2__...sql`) como fonte de verdade do schema. Cada ticket que introduz ou altera uma entidade vem acompanhado da migration correspondente. Evita drift entre o schema inferido pelo Hibernate e o que roda em produção, e dá histórico/rollback real de mudanças de schema.

## Consequences

Toda entidade nova exige uma migration explícita antes de rodar os testes de integração (que sobem Postgres real via Testcontainers). `application.properties` precisa apontar `spring.jpa.hibernate.ddl-auto=validate` e adicionar a dependência do Flyway ao `pom.xml`.
