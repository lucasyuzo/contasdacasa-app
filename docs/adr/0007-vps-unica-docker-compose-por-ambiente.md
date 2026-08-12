# VPS única com Docker Compose por ambiente (staging/production)

A aplicação é hospedada em uma única VPS Hostinger (1 vCPU, 4GB RAM), sem orquestrador (Kubernetes) nem múltiplas VPS por ambiente. `staging` e `production` rodam como containers Docker separados, cada um em diretório próprio (`/opt/contasdacasa/{staging,production}`) com checkout git da branch correspondente e seu próprio `docker-compose.yml`, em redes docker isoladas (`staging-net`/`production-net`). O deploy é push-based: o GitHub Actions conecta via SSH e roda `git pull` + `docker compose pull && up -d` — sem watchtower nem poll automático do registry. Escolhido pelo custo (uma VPS pequena só) e pela escala do projeto (uso pessoal/aprendizado); o trade-off é que os dois ambientes competem pelo mesmo vCPU/RAM, mitigado com limites de memória por container e swap.

## Consequences

Migrar pra VPS separadas ou um orquestrador no futuro exige recriar a infra do zero, mas cada ambiente já é autocontido (diretório + compose próprios), o que facilita essa separação depois.
