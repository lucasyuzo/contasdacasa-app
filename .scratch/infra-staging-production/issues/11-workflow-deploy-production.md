# 11 — Workflow de deploy production

**What to build:** publica imagem, gera release, e atualiza o container de production a cada merge em `production` (ADR-0007, ADR-0009, ADR-0010).

**Blocked by:** 03 — Dockerfile, 06 — docker-compose.yml de production, 07 — Diretorios da VPS, 12 — GitHub Secrets

**Status:** ready-for-agent

- [ ] `.github/workflows/deploy-production.yml`, trigger em `push` na branch `production`
- [ ] `environment: production` (dispara o gate de aprovacao manual configurado no ticket 02)
- [ ] Le a versao atual do `<version>` no `pom.xml` (ex: `./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout`)
- [ ] Cria tag git `vX.Y.Z` e GitHub Release com `generate_release_notes: true`
- [ ] Build da imagem via Dockerfile (ticket 03), tag `X.Y.Z` e `latest`
- [ ] Login e push no GHCR
- [ ] Passo via SSH em `/opt/contasdacasa/production`: `git pull && docker compose -f docker-compose.production.yml pull && docker compose -f docker-compose.production.yml up -d`
