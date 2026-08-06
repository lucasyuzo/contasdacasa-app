package com.contasdacasa.morador.adapter.in.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.contasdacasa.casa.adapter.in.web.CasaController;
import com.contasdacasa.casa.application.BuscarCasaUseCase;
import com.contasdacasa.casa.domain.Casa;
import com.contasdacasa.morador.application.BuscarMoradorUseCase;
import com.contasdacasa.morador.application.CriarMoradorUseCase;
import com.contasdacasa.morador.application.ListarMoradoresUseCase;
import com.contasdacasa.morador.application.RemoverMoradorUseCase;
import com.contasdacasa.morador.application.ValidarMoradorPertenceACasaUseCase;
import com.contasdacasa.morador.application.ValidarUsuarioDisponivelParaCasaUseCase;
import com.contasdacasa.morador.domain.Morador;
import com.contasdacasa.usuario.application.BuscarUsuarioUseCase;
import com.contasdacasa.usuario.domain.Usuario;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  MoradorController(
      BuscarCasaUseCase buscarCasaUseCase,
      BuscarUsuarioUseCase buscarUsuarioUseCase,
      ValidarUsuarioDisponivelParaCasaUseCase validarUsuarioDisponivelParaCasaUseCase,
      CriarMoradorUseCase criarMoradorUseCase,
      BuscarMoradorUseCase buscarMoradorUseCase,
      ListarMoradoresUseCase listarMoradoresUseCase,
      ValidarMoradorPertenceACasaUseCase validarMoradorPertenceACasaUseCase,
      RemoverMoradorUseCase removerMoradorUseCase) {
    this.buscarCasaUseCase = buscarCasaUseCase;
    this.buscarUsuarioUseCase = buscarUsuarioUseCase;
    this.validarUsuarioDisponivelParaCasaUseCase = validarUsuarioDisponivelParaCasaUseCase;
    this.criarMoradorUseCase = criarMoradorUseCase;
    this.buscarMoradorUseCase = buscarMoradorUseCase;
    this.listarMoradoresUseCase = listarMoradoresUseCase;
    this.validarMoradorPertenceACasaUseCase = validarMoradorPertenceACasaUseCase;
    this.removerMoradorUseCase = removerMoradorUseCase;
  }

  static MoradorResponse toResponse(Morador morador) {
    MoradorResponse response =
        new MoradorResponse(
            morador.getId(), morador.getNome(), morador.getCasaId(), morador.getUsuarioId());
    response.add(
        linkTo(methodOn(MoradorController.class).buscar(morador.getCasaId(), morador.getId()))
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
            linkTo(methodOn(MoradorController.class).buscar(casaId, morador.getId())).toUri())
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
  ResponseEntity<MoradorResponse> buscar(@PathVariable UUID casaId, @PathVariable UUID moradorId) {
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
}
