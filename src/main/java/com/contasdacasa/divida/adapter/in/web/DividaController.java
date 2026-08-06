package com.contasdacasa.divida.adapter.in.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.contasdacasa.divida.application.domain.Divida;
import com.contasdacasa.divida.application.usecase.BuscarDividaUseCase;
import com.contasdacasa.divida.application.usecase.RegistrarQuitacaoUseCase;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dividas")
public class DividaController {

    private final BuscarDividaUseCase buscarDividaUseCase;
    private final RegistrarQuitacaoUseCase registrarQuitacaoUseCase;

    DividaController(
            BuscarDividaUseCase buscarDividaUseCase, RegistrarQuitacaoUseCase registrarQuitacaoUseCase) {
        this.buscarDividaUseCase = buscarDividaUseCase;
        this.registrarQuitacaoUseCase = registrarQuitacaoUseCase;
    }

    public static DividaResponse toResponse(Divida divida) {
        List<QuitacaoResponse> quitacoes =
                divida.getQuitacoes().stream()
                        .map(
                                quitacao ->
                                        new QuitacaoResponse(
                                                quitacao.getId(),
                                                quitacao.getDividaId(),
                                                quitacao.getData(),
                                                quitacao.getValor()))
                        .toList();
        DividaResponse response =
                new DividaResponse(
                        divida.getId(),
                        divida.getDespesaId(),
                        divida.getParticipanteId(),
                        divida.getPagadorId(),
                        divida.getValor(),
                        divida.getSaldo(),
                        divida.estaPendente(),
                        quitacoes);
        response.add(linkTo(methodOn(DividaController.class).buscar(divida.getId())).withSelfRel());
        return response;
    }

    @GetMapping("/{dividaId}")
    ResponseEntity<DividaResponse> buscar(@PathVariable UUID dividaId) {
        Divida divida = buscarDividaUseCase.executar(dividaId);
        return ResponseEntity.ok(toResponse(divida));
    }

    @PostMapping("/{dividaId}/quitacoes")
    ResponseEntity<DividaResponse> registrarQuitacao(
            @PathVariable UUID dividaId, @Valid @RequestBody QuitacaoRequest request) {
        Divida divida = buscarDividaUseCase.executar(dividaId);
        registrarQuitacaoUseCase.executar(divida, request.data(), request.valor());
        Divida dividaAtualizada = buscarDividaUseCase.executar(dividaId);
        return ResponseEntity.created(
                        linkTo(methodOn(DividaController.class).buscar(dividaId)).toUri())
                .body(toResponse(dividaAtualizada));
    }
}
