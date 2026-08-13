# 09 — Workflow de CI (testes)

**What to build:** check obrigatorio de teste em toda PR, independente de branch/deploy.

**Status:** ready-for-human

- [x] `.github/workflows/ci.yml`, trigger em `pull_request` (qualquer branch alvo)
- [x] Sobe Postgres de servico (ou usa Testcontainers, conforme os testes de integracao existentes)
- [x] Roda `./mvnw -B test`
- [ ] Marcar como required status check nas branch protection rules de `staging`/`production` (parte do ticket 02)

## Comments

Testes usam Testcontainers (`TestcontainersConfiguration.java`), sobem Postgres via Docker automatico — sem precisar servico Postgres no workflow. Ultimo item eh config de settings do GitHub, precisa acesso admin, mesmo caso do ticket 02.

