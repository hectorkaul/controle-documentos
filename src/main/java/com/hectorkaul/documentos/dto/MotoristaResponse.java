package com.hectorkaul.documentos.dto;

import com.hectorkaul.documentos.model.Motorista;

public record MotoristaResponse(Long id, String nome, String matricula, boolean ativo) {

    public static MotoristaResponse de(Motorista m) {
        return new MotoristaResponse(m.getId(), m.getNome(), m.getMatricula(), m.isAtivo());
    }
}
