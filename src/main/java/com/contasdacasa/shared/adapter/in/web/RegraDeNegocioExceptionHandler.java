package com.contasdacasa.shared.adapter.in.web;

import com.contasdacasa.casa.application.exception.CasaNaoEncontradaException;
import com.contasdacasa.despesa.application.exception.DespesaNaoEncontradaException;
import com.contasdacasa.despesa.application.exception.DespesaNaoPertenceACasaException;
import com.contasdacasa.despesa.application.exception.ParticipanteSemRendaException;
import com.contasdacasa.divida.application.exception.DividaNaoEncontradaException;
import com.contasdacasa.divida.application.exception.QuitacaoExcedeSaldoDaDividaException;
import com.contasdacasa.divida.application.exception.QuitacaoValorInvalidoException;
import com.contasdacasa.morador.application.exception.MoradorNaoEncontradoException;
import com.contasdacasa.morador.application.exception.MoradorNaoPertenceACasaException;
import com.contasdacasa.morador.application.exception.UsuarioJaEhMoradorDaCasaException;
import com.contasdacasa.usuario.application.exception.UsuarioNaoEncontradoException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
class RegraDeNegocioExceptionHandler {

    @ExceptionHandler(CasaNaoEncontradaException.class)
    ProblemDetail handleCasaNaoEncontrada(CasaNaoEncontradaException ex) {
        return notFound(ex.getMessage());
    }

    @ExceptionHandler(MoradorNaoEncontradoException.class)
    ProblemDetail handleMoradorNaoEncontrado(MoradorNaoEncontradoException ex) {
        return notFound(ex.getMessage());
    }

    @ExceptionHandler(MoradorNaoPertenceACasaException.class)
    ProblemDetail handleMoradorNaoPertenceACasa(MoradorNaoPertenceACasaException ex) {
        return notFound(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    ProblemDetail handleUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex) {
        return notFound(ex.getMessage());
    }

    @ExceptionHandler(DespesaNaoEncontradaException.class)
    ProblemDetail handleDespesaNaoEncontrada(DespesaNaoEncontradaException ex) {
        return notFound(ex.getMessage());
    }

    @ExceptionHandler(DespesaNaoPertenceACasaException.class)
    ProblemDetail handleDespesaNaoPertenceACasa(DespesaNaoPertenceACasaException ex) {
        return notFound(ex.getMessage());
    }

    @ExceptionHandler(ParticipanteSemRendaException.class)
    ProblemDetail handleParticipanteSemRenda(ParticipanteSemRendaException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UsuarioJaEhMoradorDaCasaException.class)
    ProblemDetail handleUsuarioJaEhMoradorDaCasa(UsuarioJaEhMoradorDaCasaException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DividaNaoEncontradaException.class)
    ProblemDetail handleDividaNaoEncontrada(DividaNaoEncontradaException ex) {
        return notFound(ex.getMessage());
    }

    @ExceptionHandler(QuitacaoExcedeSaldoDaDividaException.class)
    ProblemDetail handleQuitacaoExcedeSaldoDaDivida(QuitacaoExcedeSaldoDaDividaException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(QuitacaoValorInvalidoException.class)
    ProblemDetail handleQuitacaoValorInvalido(QuitacaoValorInvalidoException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handle(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Dados invalidos");
        List<String> erros =
                ex.getBindingResult().getFieldErrors().stream()
                        .map(
                                fieldError ->
                                        fieldError.getField()
                                                + ": "
                                                + fieldError.getDefaultMessage())
                        .toList();
        problemDetail.setProperty("erros", erros);
        return problemDetail;
    }

    private ProblemDetail notFound(String mensagem) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, mensagem);
    }
}
