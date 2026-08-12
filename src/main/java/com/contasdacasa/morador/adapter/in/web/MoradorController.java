package com.contasdacasa.morador.adapter.in.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.contasdacasa.casa.adapter.in.web.CasaController;
import com.contasdacasa.casa.application.domain.Casa;
import com.contasdacasa.casa.application.usecase.BuscarCasaUseCase;
import com.contasdacasa.morador.application.domain.Morador;
import com.contasdacasa.morador.application.usecase.AtualizarRendaUseCase;
import com.contasdacasa.morador.application.usecase.BuscarMoradorUseCase;
import com.contasdacasa.morador.application.usecase.CriarMoradorUseCase;
import com.contasdacasa.morador.application.usecase.ListarMoradoresUseCase;
import com.contasdacasa.morador.application.usecase.RemoverMoradorUseCase;
import com.contasdacasa.morador.application.usecase.ValidarMoradorPertenceACasaUseCase;
import com.contasdacasa.morador.application.usecase.ValidarUsuarioDisponivelParaCasaUseCase;
import com.contasdacasa.usuario.application.domain.Usuario;
import com.contasdacasa.usuario.application.usecase.BuscarUsuarioUseCase;

import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/casas/{casaId}/moradores")
class MoradorController {

    private final BuscarCasaUseCase buscarCasaUseCase;
    private final BuscarUsuarioUseCase buscarUsuarioUseCase;
    private final ValidarUsuarioDisponivelParaCasaUseCase validarUsuarioDisponivelParaCasaUseCase;
    private final CriarMoradorUseCase criarMoradorUseCase;
    private final BuscarMoradorUseCase buscarMoradorUseCase;
    private final ListarMoradoresUseCase listarMoradoresUseCase;
    private final ValidarMoradorPertenceACasaUseCase validarMoradorPertenceACasaUseCase;
    private final RemoverMoradorUseCase removerMoradorUseCase;
    private final AtualizarRendaUseCase atualizarRendaUseCase;

    MoradorController(
            BuscarCasaUseCase buscarCasaUseCase,
            BuscarUsuarioUseCase buscarUsuarioUseCase,
            ValidarUsuarioDisponivelParaCasaUseCase validarUsuarioDisponivelParaCasaUseCase,
            CriarMoradorUseCase criarMoradorUseCase,
            BuscarMoradorUseCase buscarMoradorUseCase,
            ListarMoradoresUseCase listarMoradoresUseCase,
            ValidarMoradorPertenceACasaUseCase validarMoradorPertenceACasaUseCase,
            RemoverMoradorUseCase removerMoradorUseCase,
            AtualizarRendaUseCase atualizarRendaUseCase) {
        this.buscarCasaUseCase = buscarCasaUseCase;
        this.buscarUsuarioUseCase = buscarUsuarioUseCase;
        this.validarUsuarioDisponivelParaCasaUseCase = validarUsuarioDisponivelParaCasaUseCase;
        this.criarMoradorUseCase = criarMoradorUseCase;
        this.buscarMoradorUseCase = buscarMoradorUseCase;
        this.listarMoradoresUseCase = listarMoradoresUseCase;
        this.validarMoradorPertenceACasaUseCase = validarMoradorPertenceACasaUseCase;
        this.removerMoradorUseCase = removerMoradorUseCase;
        this.atualizarRendaUseCase = atualizarRendaUseCase;
    }

    static MoradorResponse toResponse(Morador morador) {
        MoradorResponse response =
                new MoradorResponse(
                        morador.getId(),
                        morador.getNome(),
                        morador.getCasaId(),
                        morador.getUsuarioId(),
                        morador.getRenda());
        response.add(
                linkTo(
                                methodOn(MoradorController.class)
                                        .buscar(morador.getCasaId(), morador.getId()))
                        .withSelfRel());
        response.add(
                linkTo(methodOn(CasaController.class).buscar(morador.getCasaId())).withRel("casa"));
        return response;
    }

    @PostMapping
    ResponseEntity<MoradorResponse> adicionar(
            @PathVariable UUID casaId, @Valid @RequestBody MoradorRequest request) {
        Casa casa = buscarCasaUseCase.executar(casaId);
        Usuario usuario = buscarUsuarioUseCase.executar(request.usuarioId());
        validarUsuarioDisponivelParaCasaUseCase.executar(usuario, casa);
        Morador morador = criarMoradorUseCase.executar(casa, usuario, request.nome());
        return ResponseEntity.created(
                        linkTo(methodOn(MoradorController.class).buscar(casaId, morador.getId()))
                                .toUri())
                .body(toResponse(morador));
    }

    @GetMapping
    CollectionModel<MoradorResponse> listar(@PathVariable UUID casaId) {
        Casa casa = buscarCasaUseCase.executar(casaId);
        var moradores =
                listarMoradoresUseCase.executar(casa).stream()
                        .map(MoradorController::toResponse)
                        .toList();
        return CollectionModel.of(moradores);
    }

    @GetMapping("/{moradorId}")
    ResponseEntity<MoradorResponse> buscar(
            @PathVariable UUID casaId, @PathVariable UUID moradorId) {
        Casa casa = buscarCasaUseCase.executar(casaId);
        Morador morador = buscarMoradorUseCase.executar(moradorId);
        validarMoradorPertenceACasaUseCase.executar(morador, casa);
        return ResponseEntity.ok(toResponse(morador));
    }

    @DeleteMapping("/{moradorId}")
    ResponseEntity<Void> remover(@PathVariable UUID casaId, @PathVariable UUID moradorId) {
        Casa casa = buscarCasaUseCase.executar(casaId);
        Morador morador = buscarMoradorUseCase.executar(moradorId);
        validarMoradorPertenceACasaUseCase.executar(morador, casa);
        removerMoradorUseCase.executar(morador);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{moradorId}/renda")
    ResponseEntity<MoradorResponse> atualizarRenda(
            @PathVariable UUID casaId,
            @PathVariable UUID moradorId,
            @Valid @RequestBody RendaRequest request) {
        Casa casa = buscarCasaUseCase.executar(casaId);
        Morador morador = buscarMoradorUseCase.executar(moradorId);
        validarMoradorPertenceACasaUseCase.executar(morador, casa);
        Morador atualizado = atualizarRendaUseCase.executar(morador, request.valor());
        return ResponseEntity.ok(toResponse(atualizado));
    }
}
