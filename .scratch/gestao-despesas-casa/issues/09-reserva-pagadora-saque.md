# 09 — Reserva como Pagadora (saque)

**What to build:** Despesa com Pagador = Reserva, debitando o saldo da Reserva sem gerar Dívida (ADR-0001).

**Blocked by:** 02 — Despesa com Rateio Igual, 08 — Reserva e Custodiante

**Status:** ready-for-agent

- [ ] Cadastrar Despesa com Pagador = Reserva (referência polimórfica Morador/Reserva, ver `docs/adr/0001-pagador-polimorfico-morador-ou-reserva.md`)
- [ ] Saque debita o saldo da Reserva no valor da Despesa
- [ ] Nenhuma Dívida é gerada quando o Pagador é a Reserva
