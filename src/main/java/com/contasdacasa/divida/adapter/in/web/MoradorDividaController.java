package com.contasdacasa.divida.adapter.in.web;

import com.contasdacasa.casa.application.domain.Casa;
import com.contasdacasa.casa.application.usecase.BuscarCasaUseCase;
import com.contasdacasa.divida.application.usecase.ListarDividasPendentesComoCredorUseCase;
import com.contasdacasa.divida.application.usecase.ListarDividasPendentesComoDevedorUseCase;
import com.contasdacasa.morador.application.domain.Morador;
import com.contasdacasa.morador.application.usecase.BuscarMoradorUseCase;
import com.contasdacasa.morador.application.usecase.ValidarMoradorPertenceACasaUseCase;

import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/casas/{casaId}/moradores/{moradorId}")
class MoradorDividaController {

    private final BuscarCasaUseCase buscarCasaUseCase;
    private final BuscarMoradorUseCase buscarMoradorUseCase;
    private final ValidarMoradorPertenceACasaUseCase validarMoradorPertenceACasaUseCase;
    private final ListarDividasPendentesComoDevedorUseCase listarDividasPendentesComoDevedorUseCase;
    private final ListarDividasPendentesComoCredorUseCase listarDividasPendentesComoCredorUseCase;

    MoradorDividaController(
            BuscarCasaUseCase buscarCasaUseCase,
            BuscarMoradorUseCase buscarMoradorUseCase,
            ValidarMoradorPertenceACasaUseCase validarMoradorPertenceACasaUseCase,
            ListarDividasPendentesComoDevedorUseCase listarDividasPendentesComoDevedorUseCase,
            ListarDividasPendentesComoCredorUseCase listarDividasPendentesComoCredorUseCase) {
        this.buscarCasaUseCase = buscarCasaUseCase;
        this.buscarMoradorUseCase = buscarMoradorUseCase;
        this.validarMoradorPertenceACasaUseCase = validarMoradorPertenceACasaUseCase;
        this.listarDividasPendentesComoDevedorUseCase = listarDividasPendentesComoDevedorUseCase;
        this.listarDividasPendentesComoCredorUseCase = listarDividasPendentesComoCredorUseCase;
    }

    @GetMapping("/dividas-a-pagar")
    CollectionModel<DividaResponse> listarDividasAPagar(
            @PathVariable UUID casaId, @PathVariable UUID moradorId) {
        Morador morador = buscarEValidarMorador(casaId, moradorId);
        var dividas =
                listarDividasPendentesComoDevedorUseCase.executar(morador).stream()
                        .map(DividaController::toResponse)
                        .toList();
        return CollectionModel.of(dividas);
    }

    @GetMapping("/dividas-a-receber")
    CollectionModel<DividaResponse> listarDividasAReceber(
            @PathVariable UUID casaId, @PathVariable UUID moradorId) {
        Morador morador = buscarEValidarMorador(casaId, moradorId);
        var dividas =
                listarDividasPendentesComoCredorUseCase.executar(morador).stream()
                        .map(DividaController::toResponse)
                        .toList();
        return CollectionModel.of(dividas);
    }

    private Morador buscarEValidarMorador(UUID casaId, UUID moradorId) {
        Casa casa = buscarCasaUseCase.executar(casaId);
        Morador morador = buscarMoradorUseCase.executar(moradorId);
        validarMoradorPertenceACasaUseCase.executar(morador, casa);
        return morador;
    }
}
