# 12 — GitHub Secrets pro deploy

**What to build:** credenciais que os workflows de deploy precisam, sem senha de banco passando pelo CI (ADR: `.env` fica so na VPS).

**Blocked by:** 01 — Provisionar VPS

**Status:** ready-for-human

- [x] `VPS_HOST`, `VPS_USER` — dados de acesso da VPS (usuario nao-root criado no ticket 01)
- [x] `VPS_SSH_KEY` — chave privada correspondente a chave publica autorizada na VPS
- [ ] Token do GHCR (o `GITHUB_TOKEN` padrao do Actions ja basta pra push no GHCR do proprio repo — confirmar permissao `packages: write` no workflow)
- [x] Segredos cadastrados nos Environments `staging` e `production` (nao no nivel do repo), pra respeitar o gate de aprovacao manual (ticket 02)

## Comments

Cadastro de segredo real — nao da pra um agente executar.
