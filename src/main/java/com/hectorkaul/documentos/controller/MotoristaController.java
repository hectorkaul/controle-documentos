package com.hectorkaul.documentos.controller;

import com.hectorkaul.documentos.dto.DocumentoResponse;
import com.hectorkaul.documentos.dto.LiberacaoResponse;
import com.hectorkaul.documentos.dto.MotoristaRequest;
import com.hectorkaul.documentos.dto.MotoristaResponse;
import com.hectorkaul.documentos.model.Motorista;
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
@RequestMapping("/motoristas")
public class MotoristaController {

    private final CadastroService cadastro;
    private final DocumentoService documentos;
    private final LiberacaoService liberacao;

    public MotoristaController(CadastroService cadastro, DocumentoService documentos, LiberacaoService liberacao) {
        this.cadastro = cadastro;
        this.documentos = documentos;
        this.liberacao = liberacao;
    }

    @PostMapping
    public ResponseEntity<MotoristaResponse> cadastrar(@RequestBody @Valid MotoristaRequest dados) {
        Motorista motorista = cadastro.cadastrarMotorista(dados);
        URI endereco = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(motorista.getId()).toUri();
        return ResponseEntity.created(endereco).body(MotoristaResponse.de(motorista));
    }

    @GetMapping
    public List<MotoristaResponse> listar() {
        return cadastro.motoristasAtivos().stream().map(MotoristaResponse::de).toList();
    }

    @GetMapping("/{id}")
    public MotoristaResponse consultar(@PathVariable Long id) {
        return MotoristaResponse.de(cadastro.motorista(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        cadastro.desativarMotorista(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/documentos")
    public List<DocumentoResponse> documentos(@PathVariable Long id) {
        return documentos.doMotorista(id);
    }

    @GetMapping("/{id}/liberacao")
    public LiberacaoResponse liberacao(@PathVariable Long id) {
        return liberacao.motorista(id);
    }
}
