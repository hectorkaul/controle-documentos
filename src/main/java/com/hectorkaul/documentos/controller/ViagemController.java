package com.hectorkaul.documentos.controller;

import com.hectorkaul.documentos.dto.LiberacaoResponse;
import com.hectorkaul.documentos.service.LiberacaoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/viagens")
public class ViagemController {

    private final LiberacaoService liberacao;

    public ViagemController(LiberacaoService liberacao) {
        this.liberacao = liberacao;
    }

    /** Ex: /viagens/liberacao?motoristaId=1&veiculoId=2 */
    @GetMapping("/liberacao")
    public LiberacaoResponse liberar(@RequestParam Long motoristaId, @RequestParam Long veiculoId) {
        return liberacao.viagem(motoristaId, veiculoId);
    }
}
