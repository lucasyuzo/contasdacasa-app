package com.contasdacasa.shared.adapter.in.web;

import com.contasdacasa.casa.application.exception.CasaNaoEncontradaException;
import com.contasdacasa.morador.application.exception.MoradorNaoEncontradoException;
import com.contasdacasa.morador.application.exception.MoradorNaoPertenceACasaException;
import com.contasdacasa.morador.application.exception.UsuarioJaEhMoradorDaCasaException;
import com.contasdacasa.usuario.application.exception.UsuarioNaoEncontradoException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

  @ExceptionHandler(UsuarioJaEhMoradorDaCasaException.class)
  ProblemDetail handleUsuarioJaEhMoradorDaCasa(UsuarioJaEhMoradorDaCasaException ex) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ProblemDetail handle(MethodArgumentNotValidException ex) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Dados invalidos");
    List<String> erros =
        ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .toList();
    problemDetail.setProperty("erros", erros);
    return problemDetail;
  }

  private ProblemDetail notFound(String mensagem) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, mensagem);
  }
}
