# 09 — Workflow de CI (testes)

**What to build:** check obrigatorio de teste em toda PR, independente de branch/deploy.

**Status:** ready-for-agent

- [ ] `.github/workflows/ci.yml`, trigger em `pull_request` (qualquer branch alvo)
- [ ] Sobe Postgres de servico (ou usa Testcontainers, conforme os testes de integracao existentes)
- [ ] Roda `./mvnw -B test`
- [ ] Marcar como required status check nas branch protection rules de `staging`/`production` (parte do ticket 02)
