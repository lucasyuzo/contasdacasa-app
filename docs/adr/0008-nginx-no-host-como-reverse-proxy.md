# Nginx instalado no host da VPS como reverse proxy (não containerizado, não Traefik)

Diferente do padrão mais comum em setups Docker-first (Traefik com auto-discovery via labels), o Nginx roda direto no sistema operacional da VPS, fora do Docker, proxeando pras portas que os containers de app publicam em `127.0.0.1`. Escolha deliberada por objetivo de aprendizado — configurar Nginx e certbot manualmente — em vez de automação; troca a conveniência de auto-configuração do Traefik por controle explícito da config e integração direta com `certbot --nginx` pra TLS (quando um domínio existir).

## Consequences

Toda vez que uma nova porta/serviço for exposto, a config do Nginx precisa ser editada manualmente (não há auto-discovery); rotas de `staging` e `production` ficam em server blocks separados no host.
