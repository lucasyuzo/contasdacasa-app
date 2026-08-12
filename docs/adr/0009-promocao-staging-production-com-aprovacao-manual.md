# Promoção via merge staging -> production, com aprovação manual em cada deploy

O fluxo de branches é `feature -> PR pra staging -> PR staging pra production`; nada é mergeado direto em `production` sem antes passar por `staging`. Cada deploy — tanto `staging` quanto `production` — exige aprovação manual via GitHub Environments protection rules antes do job de deploy rodar, mesmo `staging` já sendo ambiente de teste. Motivado pela natureza da aplicação (controla despesas/dívidas reais entre moradores): o gate humano extra é barato e evita que um merge acidental dispare deploy sem revisão final, mesmo num projeto solo.

## Consequences

Nenhum push em `staging`/`production` dispara deploy sozinho; é preciso aprovar manualmente no GitHub toda vez, inclusive em `staging`.
