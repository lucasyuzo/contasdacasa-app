# 07 — Diretorios da VPS com checkout git por ambiente

**What to build:** estrutura de pastas na VPS que o deploy via SSH espera encontrar (ADR-0007).

**Blocked by:** 01 — Provisionar VPS, 02 — Branches e Environments, 05 — docker-compose.yml de staging, 06 — docker-compose.yml de production

**Status:** ready-for-human

- [ ] `/opt/contasdacasa/staging`: `git clone` do repo, checkout na branch `staging`
- [ ] `/opt/contasdacasa/production`: `git clone` do repo, checkout na branch `production`
- [ ] Confirmar que `docker compose -f docker-compose.staging.yml pull && up -d` roda sem erro dentro de `/opt/contasdacasa/staging` (depois do `.env`, ticket 13)
- [ ] Mesma checagem em `/opt/contasdacasa/production`

## Comments

Execucao direta na VPS via SSH — nao da pra um agente sem acesso remoto.
