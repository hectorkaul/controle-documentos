package com.hectorkaul.documentos.exception;

/** Pedido valido no formato, mas que quebra uma regra (ex: CNH cadastrada para um caminhao). */
public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
