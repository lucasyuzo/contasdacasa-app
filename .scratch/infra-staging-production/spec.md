Status: ready-for-agent

# Infra: ambientes staging e production

## Problema

Hoje a app so roda local (`./mvnw spring-boot:run` contra Postgres local). Nao ha ambiente publicado, nem processo repetivel de deploy, nem separacao entre um ambiente de homologacao e producao — qualquer validacao depende de rodar local.

## Solucao

Uma VPS Hostinger unica (1 vCPU, 4GB RAM, 50GB SSD) hospeda dois ambientes Docker isolados, `staging` e `production`, cada um com seu proprio container de app e container Postgres, em redes docker separadas. CI/CD via GitHub Actions: PR mergeado em `staging` builda e sobe a imagem no ambiente de homologacao; PR mergeado em `staging -> production` builda, gera tag/release do GitHub refletindo a versao do `pom.xml`, e sobe em producao. Ambos os deploys exigem aprovacao manual (GitHub Environments). Nginx roda no host da VPS como reverse proxy; comeca servindo por IP/HTTP e migra pra dominio+TLS (Let's Encrypt) quando um dominio for adquirido.

Decisoes arquiteturais registradas em ADR: `docs/adr/0007` (VPS unica + compose por ambiente), `0008` (Nginx no host), `0009` (fluxo staging->production com aprovacao manual), `0010` (versionamento manual + release automatica), `0011` (Postgres em container por ambiente).

## Objetivos

1. Como mantenedor, quero um Dockerfile multi-stage da app, para gerar uma imagem leve de runtime (JRE) sem carregar o toolchain de build.
2. Como mantenedor, quero profiles Spring separados (`local`/`staging`/`production`), para cada ambiente usar suas proprias credenciais de banco sem tocar no `application.properties` local.
3. Como mantenedor, quero um `docker-compose.yml` por ambiente (app + Postgres, rede isolada, portas so em `127.0.0.1`, limites de memoria), para os dois ambientes conviverem na mesma VPS sem brigar por recurso.
4. Como mantenedor, quero a VPS provisionada (usuario nao-root, SSH so por chave, ufw, docker, swap), para ter uma base segura antes de publicar qualquer coisa.
5. Como mantenedor, quero as branches `staging` e `production` criadas com GitHub Environments protegidos por aprovacao manual, para nenhum deploy sair sem revisao.
6. Como mantenedor, quero um workflow de CI rodando `./mvnw test` em toda PR, para pegar quebra antes de qualquer deploy.
7. Como mantenedor, quero workflows de deploy separados pra staging e production, para cada um publicar a imagem certa (GHCR) e atualizar o container certo na VPS via SSH.
8. Como mantenedor, quero que o deploy de production leia a versao do `pom.xml` e gere tag git + GitHub Release automaticamente, para o historico de releases refletir o codigo publicado.
9. Como mantenedor, quero Nginx configurado no host da VPS roteando pra cada ambiente, para acessar staging/production por HTTP (e depois HTTPS) sem expor a porta da app direto.
10. Como mantenedor, quero backup diario (`pg_dump`) do banco de production, para nao perder dado real dos moradores se algo quebrar.
