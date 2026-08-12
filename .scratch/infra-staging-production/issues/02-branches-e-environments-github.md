# 02 — Criar branches staging/production e GitHub Environments

**What to build:** estrutura de branches e os gates de aprovacao manual no GitHub (ADR-0009).

**Status:** ready-for-human

- [x] Criar a branch `staging` (a partir de `main`)
- [x] Criar a branch `production` (a partir de `staging`)
- [x] Criar o GitHub Environment `staging`, com "required reviewers" habilitado
- [x] Criar o GitHub Environment `production`, com "required reviewers" habilitado
- [x] Definir regra de branch protection: PR obrigatorio (sem push direto) em `staging` e `production`

## Comments

Configuracao de settings do repositorio no GitHub — nao da pra um agente executar sem acesso administrativo.
