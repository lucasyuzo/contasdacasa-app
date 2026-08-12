# 15 — Dominio e TLS

**What to build:** trocar acesso via IP/HTTP por subdominio + TLS automatico (Let's Encrypt via `certbot --nginx`), conforme decidido na sessao de grilling.

**Blocked by:** 08 — Nginx no host (aguardando aquisicao do dominio)

**Status:** needs-info

- [ ] Comprar/definir o dominio
- [ ] Apontar DNS: subdominio de staging (ex: `staging.dominio.com`) e de production (ex: `dominio.com` ou `app.dominio.com`) pro IP da VPS
- [ ] Rodar `certbot --nginx` pra cada subdominio
- [ ] Confirmar renovacao automatica (`certbot renew` via systemd timer)
- [ ] Atualizar server blocks do ticket 08 pra redirect HTTP->HTTPS

## Comments

Bloqueado ate o dominio ser adquirido — sem data definida.
