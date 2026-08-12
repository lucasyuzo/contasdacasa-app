# 01 — Provisionar a VPS

**What to build:** base segura na VPS Hostinger (1 vCPU, 4GB RAM, 50GB SSD) antes de publicar qualquer container.

**Status:** ready-for-human

- [x] Criar usuario nao-root dedicado ao deploy, com sudo
- [x] Instalar Docker Engine + plugin `docker compose`
- [x] Configurar ufw liberando so as portas 22, 80 e 443
- [x] Registrar IP da VPS e usuario de deploy pra uso nos proximos tickets (secrets do GitHub Actions, ticket 12)

## Comments

Depende de acesso ao painel da Hostinger e da propria VPS — nao da pra um agente executar.
