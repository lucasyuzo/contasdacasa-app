package com.contasdacasa.casa.adapter.in.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.contasdacasa.casa.application.domain.Casa;
import com.contasdacasa.casa.application.usecase.BuscarCasaUseCase;
import com.contasdacasa.casa.application.usecase.CriarCasaUseCase;

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
@RequestMapping("/casas")
public class CasaController {

    private final CriarCasaUseCase criarCasaUseCase;
    private final BuscarCasaUseCase buscarCasaUseCase;

    CasaController(CriarCasaUseCase criarCasaUseCase, BuscarCasaUseCase buscarCasaUseCase) {
        this.criarCasaUseCase = criarCasaUseCase;
        this.buscarCasaUseCase = buscarCasaUseCase;
    }

    static CasaResponse toResponse(Casa casa) {
        CasaResponse response = new CasaResponse(casa.getId(), casa.getNome());
        response.add(linkTo(methodOn(CasaController.class).buscar(casa.getId())).withSelfRel());
        return response;
    }

    @PostMapping
    ResponseEntity<CasaResponse> criar(@Valid @RequestBody CasaRequest request) {
        Casa casa = criarCasaUseCase.executar(request.nome());
        return ResponseEntity.created(
                        linkTo(methodOn(CasaController.class).buscar(casa.getId())).toUri())
                .body(toResponse(casa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CasaResponse> buscar(@PathVariable UUID id) {
        Casa casa = buscarCasaUseCase.executar(id);
        return ResponseEntity.ok(toResponse(casa));
    }
}
