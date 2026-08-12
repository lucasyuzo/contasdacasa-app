# API expõe DTO com HATEOAS; erro tratado por ControllerAdvice separado por origem

A API nunca expõe entidades de domínio diretamente — cada endpoint retorna um Resource/`RepresentationModel` (Spring HATEOAS) construído a partir de DTOs, com links pra recursos relacionados (ex: Despesa linkando pra suas Dívidas geradas). Erros são tratados por múltiplos `@ControllerAdvice`, separados por origem: um para violações de regra de negócio do domínio (ex: soma do Valor Fixo não bate), outro para falhas de infraestrutura (banco, serialização, etc.), ambos respondendo em Problem Details (RFC 7807, via `ProblemDetail` nativo do Spring).

## Consequences

Todo endpoint novo precisa de um DTO de saída e, se aplicável, um DTO de entrada — nunca reaproveitar a entidade JPA/domínio na assinatura do controller. Exceções de domínio devem ser tipadas de forma a permitir o roteamento correto entre os dois advices.
