package com.hectorkaul.documentos.controller;

import com.hectorkaul.documentos.dto.DocumentoRequest;
import com.hectorkaul.documentos.dto.DocumentoResponse;
import com.hectorkaul.documentos.dto.PainelResponse;
import com.hectorkaul.documentos.dto.RenovacaoRequest;
import com.hectorkaul.documentos.model.Situacao;
import com.hectorkaul.documentos.model.TipoDocumento;
import com.hectorkaul.documentos.service.DocumentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    private final DocumentoService documentos;

    public DocumentoController(DocumentoService documentos) {
        this.documentos = documentos;
    }

    @PostMapping
    public ResponseEntity<DocumentoResponse> cadastrar(@RequestBody @Valid DocumentoRequest dados) {
        DocumentoResponse documento = documentos.cadastrar(dados);
        URI endereco = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(documento.id()).toUri();
        return ResponseEntity.created(endereco).body(documento);
    }

    /** Ex: /documentos?situacao=VENCIDO ou /documentos?tipo=MOPP */
    @GetMapping
    public List<DocumentoResponse> listar(@RequestParam(required = false) Situacao situacao,
                                          @RequestParam(required = false) TipoDocumento tipo) {
        return documentos.listar(situacao, tipo);
    }

    @GetMapping("/{id}")
    public DocumentoResponse consultar(@PathVariable Long id) {
        return documentos.consultar(id);
    }

    @PutMapping("/{id}/renovacao")
    public DocumentoResponse renovar(@PathVariable Long id, @RequestBody @Valid RenovacaoRequest dados) {
        return documentos.renovar(id, dados);
    }

    /** Vencidos e os que vencem nos proximos N dias (padrao 30). */
    @GetMapping("/vencendo")
    public List<DocumentoResponse> vencendo(@RequestParam(defaultValue = "30") int dias) {
        return documentos.vencendo(dias);
    }

    @GetMapping("/painel")
    public PainelResponse painel() {
        return documentos.painel();
    }
}
