package com.hectorkaul.documentos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VeiculoRequest(
        // aceita o padrao antigo (ABC-1234) e o Mercosul (ABC1D23), com ou sem hifen
        @NotBlank(message = "informe a placa")
        @Pattern(regexp = "(?i)[A-Z]{3}-?[0-9][A-Z0-9][0-9]{2}", message = "placa invalida")
        String placa,
        @NotBlank(message = "informe a descricao") @Size(max = 100) String descricao) {
}
