# 06 — docker-compose.yml de production

**What to build:** compose de production com app + Postgres, seguindo ADR-0007 e ADR-0011.

**Blocked by:** 03 — Dockerfile multi-stage, 04 — Profiles Spring por ambiente

**Status:** done

- [x] `docker-compose.production.yml` na raiz do repo
- [x] Servico `app`: imagem do GHCR (tag `X.Y.Z` da release, mais `latest`), `SPRING_PROFILES_ACTIVE=production`, porta publicada so em `127.0.0.1:8080:8080`, `mem_limit: 1024m`, `JAVA_OPTS=-Xmx768m`
- [x] Servico `db`: `postgres`, sem porta publicada, `mem_limit: 512m`, volume nomeado pra persistir dado, credenciais via `.env` (nao commitado)
- [x] Rede dedicada `production-net`, so os dois servicos desse compose nela
- [x] `env_file: .env` no servico `app` (o `.env` real fica so na VPS, ticket 13)
- [x] Nome do container/servico `db` estavel e previsivel (ex: `contasdacasa-production-db`), usado pelo script de backup (ticket 14)

## Comments

`app` referencia so a tag `latest` (fixa) — ticket 11 sempre republica `latest` a cada release, entao `docker compose pull` no deploy ja pega a versao nova sem precisar de var externa (diferente do staging/ticket 05, onde a tag muda por sha). `SPRING_PROFILES_ACTIVE=production` hardcoded em `environment:` do `app` — spec lista junto com `mem_limit`/`JAVA_OPTS`, entao mantido explicito no compose em vez de depender so do `.env` da VPS (ticket 13, ainda nao entregue); mesmo ajuste retroaplicado no `docker-compose.staging.yml`. Validado com `docker compose -f docker-compose.production.yml config`.
