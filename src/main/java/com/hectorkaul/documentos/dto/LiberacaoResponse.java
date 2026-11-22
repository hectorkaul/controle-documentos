package com.hectorkaul.documentos.dto;

import java.util.List;

/**
 * Resposta do "pode sair para a viagem?".
 * pendencias = o que impede a liberacao. avisos = o que nao impede, mas precisa de atencao logo.
 */
public record LiberacaoResponse(boolean liberado, List<String> pendencias, List<String> avisos) {
}
