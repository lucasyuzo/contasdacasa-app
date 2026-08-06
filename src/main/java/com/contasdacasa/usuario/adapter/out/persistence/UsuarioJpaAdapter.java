package com.contasdacasa.usuario.adapter.out.persistence;

import com.contasdacasa.usuario.application.domain.Usuario;
import com.contasdacasa.usuario.application.port.UsuarioPort;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
class UsuarioJpaAdapter implements UsuarioPort {

    private final UsuarioJpaRepository jpaRepository;

    UsuarioJpaAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioJpaEntity usuarioJpaEntity =
                jpaRepository.save(new UsuarioJpaEntity(usuario.getId(), usuario.getNome()));
        return new Usuario(usuarioJpaEntity.getId(), usuario.getNome());
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return jpaRepository
                .findById(id)
                .map(entity -> new Usuario(entity.getId(), entity.getNome()));
    }
}
