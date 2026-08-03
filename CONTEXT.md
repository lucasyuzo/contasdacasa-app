# Contas da Casa

Gestão de despesas de uma casa com n moradores, com rateio igual ou personalizado.

## Language

**Despesa**:
Evento de gasto que gera rateio entre moradores da casa.
_Avoid_: Conta, gasto, transação

**Reserva**:
Entidade própria da Casa com saldo: sobe com aportes (Despesas) e desce com saques. Mantida na conta bancária de um Morador Custodiante (agência/conta), não é um Projeto.
_Avoid_: Conta, poupança

**Custodiante**:
Morador em cuja conta bancária (agência/conta) o saldo da Reserva fica fisicamente guardado.

**Natureza** (da Despesa):
Recorrência do gasto: Fixa ou Variável.

**Recorrência**:
Modelo de Despesa Fixa (valor, Rateio, Pagador, periodicidade) que gera automaticamente uma nova Despesa a cada período. Cada Despesa gerada pode ser editada ou pulada individualmente sem alterar o modelo.

**Projeto**:
Agrupamento de Despesas criado livremente pelos moradores em torno de um propósito (ex: "Viagem pra praia", "Obra do quintal"). Lista aberta, não enum. Reserva não é um Projeto — é entidade própria (ver Reserva).
_Avoid_: Categoria, Finalidade

**Geral**:
Despesa do dia a dia sem Projeto associado.
_Avoid_: Categoria (usado sozinho, ambíguo — categoria tem duas dimensões: Natureza e Projeto)

**Rateio**:
Regra de divisão do valor de uma Despesa entre os Participantes dessa Despesa. Cada Despesa tem seu próprio Rateio; a Casa define um Rateio padrão aplicado quando a Despesa não especifica um. Tipos: Igual, Por Renda, Valor Fixo.

**Participante**:
Morador incluído no rateio de uma Despesa específica. Uma Despesa não precisa envolver todos os Moradores da Casa.

**Casa**:
Grupo de n Moradores que compartilham Despesas.

**Morador**:
Pessoa vinculada a uma única Casa por vez, elegível a ser Participante de Despesas.

**Renda**:
Valor persistente cadastrado no Morador, atualizável, usado no Rateio do tipo Por Renda. Não é informado no cadastro da Despesa.
_Avoid_: Salário

**Pagador**:
Morador ou a Reserva que desembolsou o valor de uma Despesa. Todo Despesa tem exatamente um Pagador. Quando o Pagador é a Reserva, o saldo da Reserva é debitado e nenhuma Dívida é gerada.

**Dívida**:
Valor que um Participante deve ao Pagador de uma Despesa, gerado pelo Rateio. O próprio Pagador não gera Dívida consigo mesmo.

**Quitação**:
Evento que registra o pagamento (total ou parcial) de uma Dívida específica, com data e valor. Não existe saldo consolidado entre Moradores — cada Dívida é quitada individualmente.
