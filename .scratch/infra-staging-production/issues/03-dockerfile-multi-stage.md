# 03 — Dockerfile multi-stage

**What to build:** imagem docker da app, stage de build com `eclipse-temurin:26-jdk` (`./mvnw package`) e stage de runtime com `eclipse-temurin:26-jre` copiando so o jar gerado.

**Status:** ready-for-agent

- [x] `Dockerfile` na raiz do projeto, multi-stage (build + runtime)
- [x] Stage de build usa `eclipse-temurin:26-jdk`, roda `./mvnw -B package -DskipTests` (testes ja rodam no `ci.yml`, ticket 09)
- [x] Stage de runtime usa `eclipse-temurin:26-jre`, copia so o jar final
- [x] `ENTRYPOINT` roda `java -jar app.jar`, aceitando `JAVA_OPTS`/`-Xmx` via variavel de ambiente (usado pelos limites de memoria do compose, tickets 05/06)
- [x] Expõe a porta 8080

## Comments

`./mvnw` estava quebrado no repo (faltava `.mvn/wrapper/maven-wrapper.properties`, nunca existiu no git) — adicionado, senao o stage de build nao roda. Imagem `eclipse-temurin:26-jdk` nao tem `curl`/`unzip` por padrao; instalados no stage de build (sem eles o `mvnw` cai no downloader Java, que baixa o `.tar.gz` em vez do `.zip` e quebra a validacao de checksum). Validado com build real + Postgres em container: app sobe, `/actuator/health` retorna UP, `JAVA_OPTS` aplicado no processo.
