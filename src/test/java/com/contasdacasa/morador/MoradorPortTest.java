package com.contasdacasa.morador;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.contasdacasa.TestcontainersConfiguration;
import com.contasdacasa.casa.application.domain.Casa;
import com.contasdacasa.casa.application.usecase.CriarCasaUseCase;
import com.contasdacasa.morador.application.domain.Morador;
import com.contasdacasa.morador.application.exception.UsuarioJaEhMoradorDaCasaException;
import com.contasdacasa.morador.application.port.MoradorPort;
import com.contasdacasa.usuario.application.domain.Usuario;
import com.contasdacasa.usuario.application.usecase.CriarUsuarioUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class MoradorPortTest {

  @Autowired MoradorPort moradorPort;
  @Autowired CriarCasaUseCase criarCasaUseCase;
  @Autowired CriarUsuarioUseCase criarUsuarioUseCase;

  @Test
  void rejeitaSalvarUsuarioComoMoradorDuasVezesDaMesmaCasaNaCamadaDePersistencia() {
    Casa casa = criarCasaUseCase.executar("Republica das Flores");
    Usuario usuario = criarUsuarioUseCase.executar("Ana");
    moradorPort.salvar(Morador.adicionar(casa.getId(), usuario.getId(), "Ana"));

    assertThatThrownBy(
            () -> moradorPort.salvar(Morador.adicionar(casa.getId(), usuario.getId(), "Ana")))
        .isInstanceOf(UsuarioJaEhMoradorDaCasaException.class);
  }
}
