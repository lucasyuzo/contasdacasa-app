# 08 — Nginx no host como reverse proxy

**What to build:** roteamento HTTP pros dois ambientes, direto no SO da VPS (ADR-0008).

**Blocked by:** 01 — Provisionar VPS, 07 — Diretorios da VPS com checkout git

**Status:** ready-for-human

- [ ] Instalar Nginx no host (fora do docker)
- [ ] Server block de staging: `proxy_pass` pra `127.0.0.1:8081`
- [ ] Server block de production: `proxy_pass` pra `127.0.0.1:8080`
- [ ] Validar acesso via IP da VPS (HTTP puro por enquanto — sem dominio ainda, ver ticket 15)

## Comments

Config manual no host da VPS — nao da pra um agente executar sem acesso remoto. Quando o dominio existir (ticket 15), os server blocks sao atualizados e o certbot entra.
