# 13 — .env por ambiente na VPS

**What to build:** segredos de runtime (senha do banco, profile ativo) que o CI nunca toca.

**Blocked by:** 07 — Diretorios da VPS com checkout git

**Status:** ready-for-human

- [ ] `/opt/contasdacasa/staging/.env`: `DB_URL`, `DB_USER`, `DB_PASSWORD`, `SPRING_PROFILES_ACTIVE=staging`
- [ ] `/opt/contasdacasa/production/.env`: `DB_URL`, `DB_USER`, `DB_PASSWORD`, `SPRING_PROFILES_ACTIVE=production`
- [ ] Confirmar que os dois `.env` estao no `.gitignore` do checkout (nunca commitados)

## Comments

Segredo real, criado manualmente uma vez — nao da pra um agente executar nem deveria ficar versionado.
