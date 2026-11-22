package com.hectorkaul.documentos.dto;

import com.hectorkaul.documentos.model.Situacao;
import com.hectorkaul.documentos.model.TipoDocumento;

import java.time.LocalDate;

public record DocumentoResponse(
        Long id,
        TipoDocumento tipo,
        String descricao,
        String numero,
        LocalDate validade,
        long diasParaVencer,
        Situacao situacao,
        String dono,
        Long motoristaId,
        Long veiculoId) {
}
