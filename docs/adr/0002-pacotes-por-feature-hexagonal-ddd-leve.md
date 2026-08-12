# Pacotes por feature, arquitetura hexagonal com DDD leve

Organizamos o código por feature/domínio (`despesa/`, `casa/`, `reserva/`, etc.) em vez de por camada técnica, alinhado ao vocabulário do `CONTEXT.md` e às tickets vertical-slice. Dentro de cada feature, adotamos arquitetura hexagonal (ports and adapters): o domínio (agregados como `Despesa`, com `Rateio`/`Participante`/`Dívida` internos) fica isolado de Spring/JPA/HTTP atrás de portas, com adapters de entrada (controllers) e saída (repositories JPA) implementando essas portas. Agregados concentram as invariantes de negócio (soma do Valor Fixo, arredondamento, geração de Dívida), testáveis sem subir Spring context ou banco.

## Consequences

Mais código de mapeamento (entidade de domínio ↔ entidade JPA) do que expor JPA entity direto. Em troca, a lógica de rateio (a parte mais específica e mutável do domínio) fica isolada e testável em unidade, sem depender da fronteira de teste HTTP definida na spec.
