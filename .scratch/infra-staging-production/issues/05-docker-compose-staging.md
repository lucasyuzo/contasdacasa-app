# 05 — docker-compose.yml de staging

**What to build:** compose de staging com app + Postgres, seguindo ADR-0007 e ADR-0011.

**Blocked by:** 03 — Dockerfile multi-stage, 04 — Profiles Spring por ambiente

**Status:** done

- [x] `docker-compose.staging.yml` na raiz do repo
- [x] Servico `app`: imagem do GHCR (tag `staging-<sha>`), `SPRING_PROFILES_ACTIVE=staging`, porta publicada so em `127.0.0.1:8081:8080`, `mem_limit: 768m`, `JAVA_OPTS=-Xmx512m`
- [x] Servico `db`: `postgres`, sem porta publicada, `mem_limit: 384m`, volume nomeado pra persistir dado, credenciais via `.env` (nao commitado)
- [x] Rede dedicada `staging-net`, so os dois servicos desse compose nela
- [x] `env_file: .env` no servico `app` (o `.env` real fica so na VPS, ticket 13)

## Comments

Tag da imagem parametrizada via `${IMAGE_TAG:-staging-latest}` (compose var substitution) — ticket 10 seta `IMAGE_TAG=staging-<sha>` no passo de deploy. Credenciais do `db` vem do mesmo `.env` (`DB_USER`/`DB_PASSWORD`, via var substitution direta no compose, nao `env_file`, pra virar `POSTGRES_USER`/`POSTGRES_PASSWORD`); `POSTGRES_DB` fixo em `contasdacasa`, igual ao nome usado local. `SPRING_PROFILES_ACTIVE` no `app` vem so do `env_file: .env` (ticket 13), sem duplicar em `environment:`. Validado com `docker compose -f docker-compose.staging.yml config`. `.env` adicionado ao `.gitignore` (faltava). CLAUDE.md atualizado (dizia "sem docker-compose no repo").
