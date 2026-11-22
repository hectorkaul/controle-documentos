package com.hectorkaul.documentos.controller;

import com.hectorkaul.documentos.dto.DocumentoResponse;
import com.hectorkaul.documentos.dto.LiberacaoResponse;
import com.hectorkaul.documentos.dto.VeiculoRequest;
import com.hectorkaul.documentos.dto.VeiculoResponse;
import com.hectorkaul.documentos.model.Veiculo;
import com.hectorkaul.documentos.service.CadastroService;
import com.hectorkaul.documentos.service.DocumentoService;
import com.hectorkaul.documentos.service.LiberacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    private final CadastroService cadastro;
    private final DocumentoService documentos;
    private final LiberacaoService liberacao;

    public VeiculoController(CadastroService cadastro, DocumentoService documentos, LiberacaoService liberacao) {
        this.cadastro = cadastro;
        this.documentos = documentos;
        this.liberacao = liberacao;
    }

    @PostMapping
    public ResponseEntity<VeiculoResponse> cadastrar(@RequestBody @Valid VeiculoRequest dados) {
        Veiculo veiculo = cadastro.cadastrarVeiculo(dados);
        URI endereco = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(veiculo.getId()).toUri();
        return ResponseEntity.created(endereco).body(VeiculoResponse.de(veiculo));
    }

    @GetMapping
    public List<VeiculoResponse> listar() {
        return cadastro.veiculosAtivos().stream().map(VeiculoResponse::de).toList();
    }

    @GetMapping("/{id}")
    public VeiculoResponse consultar(@PathVariable Long id) {
        return VeiculoResponse.de(cadastro.veiculo(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        cadastro.desativarVeiculo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/documentos")
    public List<DocumentoResponse> documentos(@PathVariable Long id) {
        return documentos.doVeiculo(id);
    }

    @GetMapping("/{id}/liberacao")
    public LiberacaoResponse liberacao(@PathVariable Long id) {
        return liberacao.veiculo(id);
    }
}
