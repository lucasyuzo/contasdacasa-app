# 10 — Workflow de deploy staging

**What to build:** publica imagem e atualiza o container de staging a cada merge em `staging` (ADR-0007, ADR-0009).

**Blocked by:** 03 — Dockerfile, 05 — docker-compose.yml de staging, 07 — Diretorios da VPS, 12 — GitHub Secrets

**Status:** ready-for-agent

- [ ] `.github/workflows/deploy-staging.yml`, trigger em `push` na branch `staging`
- [ ] `environment: staging` (dispara o gate de aprovacao manual configurado no ticket 02)
- [ ] Build da imagem via Dockerfile (ticket 03), tag `staging-<sha-curto>`
- [ ] Login e push no GHCR
- [ ] Passo via SSH (`appleboy/ssh-action` ou equivalente) em `/opt/contasdacasa/staging`: `git pull && docker compose -f docker-compose.staging.yml pull && docker compose -f docker-compose.staging.yml up -d`
- [ ] Sem criacao de tag git nem GitHub Release (so `production` gera release, ADR-0010)
