package com.hectorkaul.documentos.dto;

import com.hectorkaul.documentos.model.Situacao;

import java.util.List;
import java.util.Map;

public record PainelResponse(Map<Situacao, Long> totais, List<DocumentoResponse> proximosVencimentos) {
}
