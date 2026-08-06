package com.contasdacasa.usuario.adapter.in.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.contasdacasa.usuario.application.domain.Usuario;
import com.contasdacasa.usuario.application.usecase.BuscarUsuarioUseCase;
import com.contasdacasa.usuario.application.usecase.CriarUsuarioUseCase;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final BuscarUsuarioUseCase buscarUsuarioUseCase;

    UsuarioController(
            CriarUsuarioUseCase criarUsuarioUseCase, BuscarUsuarioUseCase buscarUsuarioUseCase) {
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.buscarUsuarioUseCase = buscarUsuarioUseCase;
    }

    static UsuarioResponse toResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse(usuario.getId(), usuario.getNome());
        response.add(
                linkTo(methodOn(UsuarioController.class).buscar(usuario.getId())).withSelfRel());
        return response;
    }

    @PostMapping
    ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest request) {
        Usuario usuario = criarUsuarioUseCase.executar(request.nome());
        UsuarioResponse response = toResponse(usuario);
        return ResponseEntity.created(
                        linkTo(methodOn(UsuarioController.class).buscar(usuario.getId())).toUri())
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscar(@PathVariable UUID id) {
        Usuario usuario = buscarUsuarioUseCase.executar(id);
        UsuarioResponse response = toResponse(usuario);
        return ResponseEntity.ok(response);
    }
}
