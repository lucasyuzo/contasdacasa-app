package com.contasdacasa.morador;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.contasdacasa.TestcontainersConfiguration;
import com.contasdacasa.casa.application.CriarCasaUseCase;
import com.contasdacasa.casa.domain.Casa;
import com.contasdacasa.morador.domain.Morador;
import com.contasdacasa.morador.domain.MoradorRepository;
import com.contasdacasa.morador.domain.UsuarioJaEhMoradorDaCasaException;
import com.contasdacasa.usuario.application.CriarUsuarioUseCase;
import com.contasdacasa.usuario.domain.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class MoradorRepositoryTest {

  @Autowired MoradorRepository moradorRepository;
  @Autowired CriarCasaUseCase criarCasaUseCase;
  @Autowired CriarUsuarioUseCase criarUsuarioUseCase;

  @Test
  void rejeitaSalvarUsuarioComoMoradorDuasVezesDaMesmaCasaNaCamadaDePersistencia() {
    Casa casa = criarCasaUseCase.executar("Republica das Flores");
    Usuario usuario = criarUsuarioUseCase.executar("Ana");
    moradorRepository.salvar(Morador.adicionar(casa.getId(), usuario.getId(), "Ana"));

    assertThatThrownBy(
            () -> moradorRepository.salvar(Morador.adicionar(casa.getId(), usuario.getId(), "Ana")))
        .isInstanceOf(UsuarioJaEhMoradorDaCasaException.class);
  }
}
