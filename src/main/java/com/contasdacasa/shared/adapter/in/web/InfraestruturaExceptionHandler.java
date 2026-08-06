package com.contasdacasa.shared.adapter.in.web;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class InfraestruturaExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    ProblemDetail handle(DataAccessException ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno de infraestrutura");
    }
}
