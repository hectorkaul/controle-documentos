package com.hectorkaul.documentos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Todos os erros saem no mesmo formato (ProblemDetail, RFC 7807),
 * assim quem consome a API sempre sabe onde procurar a mensagem.
 */
@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(NaoEncontradoException.class)
    ProblemDetail naoEncontrado(NaoEncontradoException e) {
        return problema(HttpStatus.NOT_FOUND, "Nao encontrado", e.getMessage());
    }

    @ExceptionHandler(JaExisteException.class)
    ProblemDetail jaExiste(JaExisteException e) {
        return problema(HttpStatus.CONFLICT, "Ja cadastrado", e.getMessage());
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    ProblemDetail regra(RegraDeNegocioException e) {
        return problema(HttpStatus.UNPROCESSABLE_ENTITY, "Regra de negocio", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validacao(MethodArgumentNotValidException e) {
        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError erro : e.getBindingResult().getFieldErrors()) {
            campos.putIfAbsent(erro.getField(), erro.getDefaultMessage());
        }
        ProblemDetail problema = problema(HttpStatus.BAD_REQUEST, "Dados invalidos", "Confira os campos informados.");
        problema.setProperty("campos", campos);
        return problema;
    }

    /** JSON mal formado ou valor que nao existe no enum (ex: "tipo": "RG"). */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ProblemDetail jsonInvalido(HttpMessageNotReadableException e) {
        return problema(HttpStatus.BAD_REQUEST, "JSON invalido",
                "Nao consegui ler o corpo da requisicao. Confira o formato e os valores (datas em aaaa-mm-dd).");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ProblemDetail tipoErrado(MethodArgumentTypeMismatchException e) {
        return problema(HttpStatus.BAD_REQUEST, "Parametro invalido",
                "Valor '" + e.getValue() + "' invalido para o parametro " + e.getName() + ".");
    }

    private ProblemDetail problema(HttpStatus status, String titulo, String detalhe) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(status, detalhe);
        problema.setTitle(titulo);
        return problema;
    }
}
