# Usuario como identidade separada de Morador

Precisávamos decidir onde guardar a identidade de login (futuramente email/senha): direto no Morador, ou numa entidade própria. Optamos por criar `Usuario` como entidade separada, com ciclo de vida independente de Casa/Morador (sobrevive mesmo sem nenhum vínculo). `Morador` passa a ser o vínculo obrigatório de um `Usuario` a uma Casa específica (`usuario_id` NOT NULL, único por `casa_id`), mantendo seu próprio `nome` como apelido local à Casa.

## Consequences

Um Usuario precisa existir antes de virar Morador de qualquer Casa — não há mais criação implícita de morador a partir só de um nome. Em compensação, um mesmo Usuario pode ser Morador em N Casas, cada vínculo com seu próprio registro de Morador.
