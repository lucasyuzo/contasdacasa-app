package com.contasdacasa.despesa.adapter.in.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.contasdacasa.casa.adapter.in.web.CasaController;
import com.contasdacasa.casa.application.domain.Casa;
import com.contasdacasa.casa.application.usecase.BuscarCasaUseCase;
import com.contasdacasa.despesa.application.domain.Despesa;
import com.contasdacasa.despesa.application.usecase.BuscarDespesaUseCase;
import com.contasdacasa.despesa.application.usecase.CriarDespesaUseCase;
import com.contasdacasa.despesa.application.usecase.ValidarDespesaPertenceACasaUseCase;
import com.contasdacasa.divida.adapter.in.web.DividaController;
import com.contasdacasa.divida.adapter.in.web.DividaResponse;
import com.contasdacasa.morador.application.domain.Morador;
import com.contasdacasa.morador.application.usecase.BuscarMoradorUseCase;
import com.contasdacasa.morador.application.usecase.ValidarMoradorPertenceACasaUseCase;

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
@RequestMapping("/casas/{casaId}/despesas")
class DespesaController {

    private final BuscarCasaUseCase buscarCasaUseCase;
    private final BuscarMoradorUseCase buscarMoradorUseCase;
    private final ValidarMoradorPertenceACasaUseCase validarMoradorPertenceACasaUseCase;
    private final CriarDespesaUseCase criarDespesaUseCase;
    private final BuscarDespesaUseCase buscarDespesaUseCase;
    private final ValidarDespesaPertenceACasaUseCase validarDespesaPertenceACasaUseCase;

    DespesaController(
            BuscarCasaUseCase buscarCasaUseCase,
            BuscarMoradorUseCase buscarMoradorUseCase,
            ValidarMoradorPertenceACasaUseCase validarMoradorPertenceACasaUseCase,
            CriarDespesaUseCase criarDespesaUseCase,
            BuscarDespesaUseCase buscarDespesaUseCase,
            ValidarDespesaPertenceACasaUseCase validarDespesaPertenceACasaUseCase) {
        this.buscarCasaUseCase = buscarCasaUseCase;
        this.buscarMoradorUseCase = buscarMoradorUseCase;
        this.validarMoradorPertenceACasaUseCase = validarMoradorPertenceACasaUseCase;
        this.criarDespesaUseCase = criarDespesaUseCase;
        this.buscarDespesaUseCase = buscarDespesaUseCase;
        this.validarDespesaPertenceACasaUseCase = validarDespesaPertenceACasaUseCase;
    }

    static DespesaResponse toResponse(Despesa despesa) {
        List<DividaResponse> dividas =
                despesa.getDividas().stream().map(DividaController::toResponse).toList();
        DespesaResponse response =
                new DespesaResponse(
                        despesa.getId(),
                        despesa.getCasaId(),
                        despesa.getValor(),
                        despesa.getNatureza(),
                        despesa.getTipoRateio(),
                        despesa.getPagadorId(),
                        despesa.getParticipantesIds(),
                        despesa.getDataVencimento(),
                        dividas);
        response.add(
                linkTo(
                                methodOn(DespesaController.class)
                                        .buscar(despesa.getCasaId(), despesa.getId()))
                        .withSelfRel());
        response.add(
                linkTo(methodOn(CasaController.class).buscar(despesa.getCasaId())).withRel("casa"));
        return response;
    }

    @PostMapping
    ResponseEntity<DespesaResponse> cadastrar(
            @PathVariable UUID casaId, @Valid @RequestBody DespesaRequest request) {
        Casa casa = buscarCasaUseCase.executar(casaId);

        Morador pagador = buscarEValidarMorador(request.pagadorId(), casa);

        List<Morador> participantes =
                request.participantesIds().stream()
                        .map(participanteId -> buscarEValidarMorador(participanteId, casa))
                        .toList();

        Despesa despesa =
                criarDespesaUseCase.executar(
                        casa,
                        request.valor(),
                        request.natureza(),
                        request.tipoRateio(),
                        pagador,
                        participantes,
                        request.dataVencimento());

        return ResponseEntity.created(
                        linkTo(methodOn(DespesaController.class).buscar(casaId, despesa.getId()))
                                .toUri())
                .body(toResponse(despesa));
    }

    @GetMapping("/{despesaId}")
    ResponseEntity<DespesaResponse> buscar(
            @PathVariable UUID casaId, @PathVariable UUID despesaId) {
        Casa casa = buscarCasaUseCase.executar(casaId);
        Despesa despesa = buscarDespesaUseCase.executar(despesaId);
        validarDespesaPertenceACasaUseCase.executar(despesa, casa);
        return ResponseEntity.ok(toResponse(despesa));
    }

    private Morador buscarEValidarMorador(UUID moradorId, Casa casa) {
        Morador morador = buscarMoradorUseCase.executar(moradorId);
        validarMoradorPertenceACasaUseCase.executar(morador, casa);
        return morador;
    }
}
