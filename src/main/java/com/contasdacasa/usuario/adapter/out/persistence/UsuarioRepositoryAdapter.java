package com.contasdacasa.usuario.adapter.out.persistence;

import com.contasdacasa.usuario.domain.Usuario;
import com.contasdacasa.usuario.domain.UsuarioRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class UsuarioRepositoryAdapter implements UsuarioRepository {

  private final UsuarioJpaRepository jpaRepository;

  UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Usuario salvar(Usuario usuario) {
    jpaRepository.save(new UsuarioJpaEntity(usuario.getId(), usuario.getNome()));
    return usuario;
  }

  @Override
  public Optional<Usuario> buscarPorId(UUID id) {
    return jpaRepository
        .findById(id)
        .map(entity -> Usuario.reconstituir(entity.getId(), entity.getNome()));
  }
}
