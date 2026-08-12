# Versão do pom.xml bumped manualmente; release/tag gerados automaticamente no deploy de production

O bump de versão no `pom.xml` (major/minor/patch) é manual, feito pelo dev na PR que vai pra `staging` — não há automação tipo conventional-commits/semantic-release. Quando essa versão é promovida pra `production`, o workflow de deploy lê a versão atual do `pom.xml`, cria a tag git `vX.Y.Z` e um GitHub Release (notas geradas automaticamente a partir dos PRs), e usa a mesma versão como tag da imagem Docker no GHCR (`X.Y.Z` + `latest`). Optamos por manual em vez de automático pela simplicidade — automatizar semver exigiria disciplina de conventional commits e ferramenta extra, sem ganho claro pro tamanho atual do projeto.

## Consequences

Esquecer de bumpar o `pom.xml` numa PR pra staging faz o release de production sair com a mesma versão do release anterior — hoje não há validação automática que impeça isso.
