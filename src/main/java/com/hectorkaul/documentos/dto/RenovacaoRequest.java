package com.hectorkaul.documentos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RenovacaoRequest(
        @NotBlank(message = "informe o numero do documento novo") @Size(max = 50) String numero,
        @NotNull(message = "informe a nova validade") LocalDate validade) {
}
