package com.hectorkaul.documentos.exception;

public class JaExisteException extends RuntimeException {

    public JaExisteException(String mensagem) {
        super(mensagem);
    }
}
