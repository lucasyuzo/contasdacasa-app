# Camada de aplicação vira UseCases de responsabilidade única; controller orquestra

Substituímos `*Service` (sufixo genérico, múltiplas responsabilidades por classe) por `*UseCase`, uma classe por responsabilidade, em três categorias: **Construção** (cria entidade nova a partir de atributos primitivos, ou reconstrói a partir de id/critério — incluindo listagem 1:N — lançando exceção de domínio se não encontrada), **Validação** (recebe entidade já construída, `void`, lança exceção própria de domínio se a regra for violada) e **Remoção** (exclui entidade). Nenhum UseCase chama outro UseCase; o controller orquestra a sequência de chamadas. Quando um UseCase depende de uma pré-condição garantida por outro (ex: Morador só pode ser criado se a Casa existe), o UseCase seguinte recebe a **entidade já validada** como parâmetro (não o id solto) — a garantia fica no tipo do parâmetro, não na disciplina de quem chama.

## Consequences

Mais classes (uma por responsabilidade) e controllers mais espessos, já que orquestram a sequência que antes ficava escondida dentro do Service. Exceções de domínio ficam mais precisas — ex: `MoradorNaoPertenceACasaException` passa a existir em vez de reaproveitar `MoradorNaoEncontradoException` pra um caso que não é bem esse.
