# 14 — Backup diario do Postgres de production

**What to build:** `pg_dump` diario com retencao, so pra production (ADR-0011). Staging fica sem backup.

**Blocked by:** 06 — docker-compose.yml de production

**Status:** ready-for-agent

- [ ] Script `scripts/backup-production.sh` versionado no repo: `docker exec` no container do Postgres de production, `pg_dump` pra arquivo com data no nome, apaga backups com mais de N dias (definir N, sugestao 7)
- [ ] Instrucao no proprio script (comentario de topo) de como instalar no crontab da VPS (ex: `0 3 * * * /opt/contasdacasa/production/scripts/backup-production.sh`)

## Comments

O script em si e agent-doable; instalar no crontab da VPS e passo manual (marcar quando feito).
