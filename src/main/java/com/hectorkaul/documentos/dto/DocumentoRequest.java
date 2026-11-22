package com.hectorkaul.documentos.dto;

import com.hectorkaul.documentos.model.TipoDocumento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/** Informe motoristaId OU veiculoId, de acordo com o tipo do documento. */
public record DocumentoRequest(
        @NotNull(message = "informe o tipo") TipoDocumento tipo,
        @NotBlank(message = "informe o numero") @Size(max = 50) String numero,
        @NotNull(message = "informe a validade") LocalDate validade,
        Long motoristaId,
        Long veiculoId,
        @Size(max = 255) String observacao) {
}
