package com.hectorkaul.documentos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MotoristaRequest(
        @NotBlank(message = "informe o nome") @Size(max = 100) String nome,
        @NotBlank(message = "informe a matricula") @Size(max = 20) String matricula) {
}
