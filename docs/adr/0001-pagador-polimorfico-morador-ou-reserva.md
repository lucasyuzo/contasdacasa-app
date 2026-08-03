# Pagador de uma Despesa pode ser Morador ou Reserva

Uma Despesa sempre tem um Pagador. Quando a Reserva da Casa banca a Despesa (ex: material de Obra), a Reserva assume o papel de Pagador diretamente — em vez de exigir que o Custodiante saque o valor e apareça como Pagador pessoal. Optamos por isso porque reflete a realidade (ninguém fica devendo a ninguém; só o saldo da Reserva cai) e evita um passo intermediário artificial de saque manual antes do cadastro da Despesa.

## Consequences

Pagador é uma referência polimórfica (Morador ou Reserva), não uma FK simples pra Morador. Rateio e geração de Dívida precisam checar o tipo do Pagador: se for Reserva, nenhuma Dívida é criada, só um débito no saldo da Reserva.
