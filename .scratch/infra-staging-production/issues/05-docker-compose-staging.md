# 05 — docker-compose.yml de staging

**What to build:** compose de staging com app + Postgres, seguindo ADR-0007 e ADR-0011.

**Blocked by:** 03 — Dockerfile multi-stage, 04 — Profiles Spring por ambiente

**Status:** ready-for-agent

- [ ] `docker-compose.staging.yml` na raiz do repo
- [ ] Servico `app`: imagem do GHCR (tag `staging-<sha>`), `SPRING_PROFILES_ACTIVE=staging`, porta publicada so em `127.0.0.1:8081:8080`, `mem_limit: 768m`, `JAVA_OPTS=-Xmx512m`
- [ ] Servico `db`: `postgres`, sem porta publicada, `mem_limit: 384m`, volume nomeado pra persistir dado, credenciais via `.env` (nao commitado)
- [ ] Rede dedicada `staging-net`, so os dois servicos desse compose nela
- [ ] `env_file: .env` no servico `app` (o `.env` real fica so na VPS, ticket 13)
