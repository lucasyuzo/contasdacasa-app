# Postgres em container Docker por ambiente, não banco gerenciado

`staging` e `production` têm cada um seu próprio container Postgres na mesma VPS (sem porta publicada, acessível só pela rede docker interna do ambiente), em vez de um serviço de banco gerenciado (RDS, Hostinger DB, etc). Escolhido por custo zero adicional, dado o orçamento de uma VPS única. Como contrapartida, backup e disponibilidade do banco ficam por nossa conta: `production` tem `pg_dump` diário com retenção agendado via cron; `staging` não tem backup, por ser dado descartável de homologação.

## Consequences

Perda de dados em `production` depende inteiramente do cron de backup funcionar; não há failover automático — se o container Postgres cair, a app cai junto até restart manual/automático do compose.
