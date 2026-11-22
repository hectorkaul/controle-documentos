package com.hectorkaul.documentos.dto;

import com.hectorkaul.documentos.model.Veiculo;

public record VeiculoResponse(Long id, String placa, String descricao, boolean ativo) {

    public static VeiculoResponse de(Veiculo v) {
        return new VeiculoResponse(v.getId(), v.getPlaca(), v.getDescricao(), v.isAtivo());
    }
}
