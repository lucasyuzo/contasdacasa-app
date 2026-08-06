# 02 — Despesa com Rateio Igual

**What to build:** cadastrar uma Despesa (valor, Natureza, Pagador, Participantes) com Rateio Igual, gerando Dívidas dos Participantes em relação ao Pagador.

**Blocked by:** 01 — Casa e Morador

**Status:** ready-for-agent

- [ ] Cadastrar Despesa com valor, Natureza (Fixa/Variável), Pagador (Morador), lista de Participantes e data de vencimento
- [ ] Participantes de uma Despesa não precisam ser todos os Moradores da Casa
- [ ] Rateio Igual divide o valor igualmente só entre os Participantes daquela Despesa
- [ ] Diferença de centavo (quando a divisão não fecha exata) fica com o primeiro Participante por ordem de cadastro
- [ ] Rateio gera uma Dívida de cada Participante (exceto o próprio Pagador) em relação ao Pagador
