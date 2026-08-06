Status: ready-for-agent

# Gestão de Despesas da Casa

## Problema

Moradores de uma casa com n habitantes dividem despesas do dia a dia, contas fixas, e gastos de projetos pontuais (
viagem, obra), mas hoje isso é controlado informalmente (planilhas, mensagens). Não há registro confiável de quem pagou
o quê, quem deve pra quem, quanto cada Morador já quitou, nem controle sobre o saldo de uma eventual reserva financeira
da casa. Rateios "iguais" nem sempre são justos (rendas diferentes), e rateios personalizados feitos manualmente são
propensos a erro de conta.

## Solução

Um sistema que centraliza o cadastro de Despesas de uma Casa com n Moradores. Cada Despesa pode ser categorizada por
Natureza (Fixa/Variável) e opcionalmente agrupada num Projeto criado livremente (ex: "Viagem pra praia", "Obra do
quintal"). Toda Despesa tem um Pagador (um Morador ou a Reserva da Casa) e um Rateio — Igual, Por Renda ou Valor Fixo —
aplicado só aos Participantes daquela Despesa (que não precisam ser todos os Moradores da Casa). O Rateio gera Dívidas
individuais de cada Participante em relação ao Pagador, quitáveis (total ou parcialmente) via Quitação. A Casa mantém
uma Reserva com saldo próprio, custodiada na conta bancária de um Morador, que sobe com aportes e desce com saques (
inclusive podendo ser Pagadora direta de Despesas). Despesas Fixas podem ser configuradas como Recorrência, gerando
automaticamente novas ocorrências a cada período.

## Histórias de Usuário

1. Como Morador, quero cadastrar uma Casa, para começar a organizar as Despesas compartilhadas.
2. Como Morador, quero adicionar outros Moradores à Casa, para incluí-los no rateio das Despesas.
3. Como Morador, quero remover um Morador da Casa, para refletir quando alguém se muda.
4. Como Morador, quero que um Morador pertença a apenas uma Casa por vez, para não haver ambiguidade de rateio.
5. Como Morador, quero cadastrar/atualizar minha Renda, para que o Rateio Por Renda reflita minha situação atual.
6. Como Morador, quero que a Renda não precise ser digitada a cada Despesa, para agilizar o cadastro.
7. Como Morador, quero configurar um Rateio padrão para a Casa (Igual, Por Renda ou Valor Fixo), para não escolher o
   tipo em toda Despesa.
8. Como Morador, quero cadastrar uma Despesa informando valor, Pagador, Natureza (Fixa/Variável) e Participantes, para
   registrar um gasto compartilhado.
9. Como Morador, quero que uma Despesa não precise incluir todos os Moradores da Casa como Participantes, para cobrir
   gastos que não dizem respeito a todo mundo.
10. Como Morador, quero poder sobrescrever o tipo de Rateio numa Despesa específica, para casos que fogem do padrão da
    Casa.
11. Como Morador, quero que o Rateio Igual divida o valor igualmente só entre os Participantes daquela Despesa, para
    refletir quem de fato participou do gasto.
12. Como Morador, quero que o Rateio Por Renda divida proporcionalmente à Renda dos Participantes daquela Despesa (não
    de todos os Moradores da Casa), para que quem ganha mais pague proporcionalmente mais.
13. Como Morador, quero poder definir Valor Fixo por Participante numa Despesa, para casos onde a divisão não é
    proporcional nem igual.
14. Como Morador, quero que o cadastro seja rejeitado se a soma dos Valores Fixos não bater exatamente com o total da
    Despesa, para evitar Despesas com valores inconsistentes.
15. Como Morador, quero que, quando a divisão do Rateio Igual/Por Renda não fechar em centavos exatos, a diferença seja
    absorvida pelo primeiro Participante (ordem de cadastro), para ter uma regra determinística e sem ambiguidade.
16. Como Morador, quero que o Rateio gere uma Dívida de cada Participante (exceto o Pagador) em relação ao Pagador, para
    saber quem deve pra quem.
17. Como Morador, quero consultar minhas Dívidas pendentes (como devedor e como credor/Pagador), para saber minha
    situação financeira na Casa.
18. Como Morador, quero registrar uma Quitação (total ou parcial, com data e valor) numa Dívida específica, para dar
    baixa no que já foi pago.
19. Como Morador, quero que a Quitação seja vinculada a uma Dívida específica (não a um saldo consolidado entre dois
    Moradores), para manter rastreabilidade de cada pagamento.
20. Como Morador, quero criar um Projeto livremente (ex: "Viagem pra praia", "Obra do quintal"), para agrupar Despesas
    relacionadas a um propósito específico.
21. Como Morador, quero associar uma Despesa a um Projeto opcionalmente, para não ser obrigado a categorizar toda
    Despesa do dia a dia.
22. Como Morador, quero consultar o total gasto por Projeto, para acompanhar o custo de uma viagem/obra/etc.
23. Como Morador, quero que Despesas sem Projeto sejam tratadas como Geral, para diferenciar gasto do dia a dia de gasto
    de projeto.
24. Como Morador, quero consultar o saldo atual da Reserva da Casa, para saber quanto a Casa tem guardado.
25. Como Morador, quero registrar um aporte à Reserva como uma Despesa rateada entre Participantes, para formalizar quem
    contribuiu e quanto.
26. Como Morador, quero que um aporte à Reserva aumente o saldo da Reserva, para manter o saldo sempre correto.
27. Como Morador, quero registrar um saque da Reserva (Despesa com Pagador = Reserva), para usar o saldo guardado sem
    gerar Dívida entre Moradores.
28. Como Morador, quero cadastrar a Reserva com nome do Morador Custodiante e dados bancários (agência/conta), para
    saber onde o dinheiro está fisicamente guardado.
29. Como Morador, quero marcar uma Despesa como Fixa e configurar uma Recorrência (valor, Rateio, Pagador,
    periodicidade), para não recadastrar todo mês contas como aluguel.
30. Como Morador, quero que a Recorrência gere automaticamente uma nova Despesa a cada período, para reduzir trabalho
    manual repetitivo.
31. Como Morador, quero poder editar ou pular uma ocorrência gerada pela Recorrência sem alterar o modelo, para lidar
    com exceções pontuais (ex: mês em que o valor do aluguel mudou).
32. Como Morador, quero que qualquer Morador da Casa possa cadastrar Despesas e gerenciar a Casa (sem hierarquia de
    permissões), para simplificar o uso no MVP.

## Decisões de Implementação

- Novo domínio a ser modelado do zero (projeto é um esqueleto Spring Boot sem código de negócio ainda). Stack já fixada
  no `pom.xml`: Spring Web MVC, Spring HATEOAS, Spring Data JPA, PostgreSQL, Bean Validation, Lombok.
- Entidades principais: `Casa`, `Morador`, `Despesa`, `Rateio` (com tipo: IGUAL, POR_RENDA, VALOR_FIXO), item de rateio
  por Participante, `Divida`, `Quitacao`, `Projeto`, `Reserva`, `Recorrencia`.
- `Pagador` é uma referência polimórfica: `Morador` OU `Reserva` (ver ADR-0001,
  `docs/adr/0001-pagador-polimorfico-morador-ou-reserva.md`). Quando o Pagador é a Reserva, nenhuma Dívida é gerada — só
  um débito no saldo da Reserva.
- `Participante` de uma Despesa é um subconjunto de Moradores da Casa, não necessariamente todos.
- `Renda` é um atributo persistente do Morador, atualizável via operação própria, nunca informado no cadastro de
  Despesa.
- Rateio padrão é configurado no nível da Casa; cada Despesa pode sobrescrever esse tipo individualmente.
- Rateio Por Renda: proporção calculada só entre os Participantes daquela Despesa (renda relativa do participante / soma
  das rendas dos participantes da despesa).
- Rateio Valor Fixo: validação obrigatória — soma dos valores por Participante deve igualar exatamente o valor total da
  Despesa, senão o cadastro é rejeitado (erro de validação, não salva).
- Regra de arredondamento: no Rateio Igual/Por Renda, quando a divisão não fecha em centavos, a diferença de centavo é
  atribuída ao primeiro Participante por ordem de cadastro.
- `Divida` é sempre referenciada individualmente por `Quitacao` — não existe conceito de saldo consolidado entre dois
  Moradores.
- `Reserva` é entidade própria da Casa (não um Projeto), com saldo, Morador Custodiante e dados bancários (
  agência/conta) desse Custodiante.
- `Recorrencia` é o modelo/template de uma Despesa Fixa (valor, Rateio, Pagador, periodicidade) que gera automaticamente
  novas instâncias de `Despesa` a cada período; cada instância gerada pode ser editada/pulada individualmente sem
  alterar o template.
- Sem hierarquia de papéis dentro da Casa: qualquer Morador pode cadastrar Despesa e gerenciar configurações da Casa.
- Despesa possui data de vencimento distinta da data de cadastro (relevante sobretudo para Despesa Fixa).
- Camada de API segue estilo REST + HATEOAS (recursos com links), conforme dependência já presente no `pom.xml`.

## Decisões de Teste

- Fronteira única de teste: **API HTTP**, via MockMvc + Spring REST Docs, batendo na stack real (controller → service →
  repository) contra PostgreSQL real via Testcontainers — já configurado em `TestcontainersConfiguration`. Nenhum mock
  de repositório ou serviço nessa fronteira.
- Bom teste aqui verifica comportamento externo observável: request HTTP entra, response (status, corpo, links HATEOAS)
  e estado persistido no banco são o que importa — não detalhes de implementação interna (nomes de métodos privados,
  etc.).
- Cobertura mínima esperada:
    - Cadastro de Despesa feliz para cada tipo de Rateio (Igual, Por Renda, Valor Fixo).
    - Rejeição de Valor Fixo cuja soma não bate com o total da Despesa.
    - Fallback para o Rateio padrão da Casa quando a Despesa não especifica um tipo.
    - Geração de Dívidas a partir do Rateio, excluindo o próprio Pagador.
    - Quitação total e parcial de uma Dívida específica.
    - Reserva como Pagadora: saque debita saldo da Reserva e não gera Dívida.
    - Aporte à Reserva: Despesa rateada que credita o saldo da Reserva.
    - Recorrência: geração automática de nova Despesa a partir do template, e edição/skip de uma ocorrência sem afetar o
      template.
    - Consulta de Despesas por Projeto e total agregado.
- Prior art no repo: `TestcontainersConfiguration` já provê `PostgreSQLContainer` via `@ServiceConnection`; testes de
  contexto (`ContasdacasaAppApplicationTests`, `TestContasdacasaAppApplication`) são o ponto de partida da
  infraestrutura de teste.

## Fora do Escopo

- Morador pertencer a mais de uma Casa simultaneamente.
- Hierarquia/papéis de permissão dentro da Casa (ex: Administrador).
- Mecanismo de convite/onboarding de novo Morador na Casa.
- Upload de comprovante/anexo de Despesa.
- Notificações/lembretes de vencimento de Despesa Fixa.
- Suporte a múltiplas moedas.
- Histórico de troca de Casa por um Morador.

## Notas Adicionais

- Vocabulário canônico em `CONTEXT.md` — usar exatamente os termos definidos ali (Despesa, Rateio, Participante,
  Pagador, Dívida, Quitação, Projeto, Reserva, Custodiante, Recorrência, Renda, Casa, Morador).
- ADR-0001 documenta a decisão de Pagador polimórfico (Morador ou Reserva); qualquer implementação que trate Pagador
  como FK simples pra Morador contradiz essa decisão.
- Dados bancários do Custodiante (agência/conta) são dado sensível — considerar cuidado de segurança (ex: não logar,
  restringir exposição em respostas de API) na fase de implementação.
- `application.properties` já aponta pra um Postgres local (`contasdacasa`) com credenciais de desenvolvimento — não
  usar essas credenciais como referência de produção.
