package com.hectorkaul.documentos.service;

import com.hectorkaul.documentos.dto.LiberacaoResponse;
import com.hectorkaul.documentos.model.Documento;
import com.hectorkaul.documentos.model.Dono;
import com.hectorkaul.documentos.model.Motorista;
import com.hectorkaul.documentos.model.Situacao;
import com.hectorkaul.documentos.model.TipoDocumento;
import com.hectorkaul.documentos.model.Veiculo;
import com.hectorkaul.documentos.repository.DocumentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Responde se o motorista e o caminhao podem sair para a viagem.
 * Bloqueia se algum documento obrigatorio estiver vencido ou nem tiver sido cadastrado.
 */
@Service
public class LiberacaoService {

    private static final DateTimeFormatter DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final DocumentoRepository documentos;
    private final DocumentoService documentoService;
    private final CadastroService cadastro;

    public LiberacaoService(DocumentoRepository documentos, DocumentoService documentoService, CadastroService cadastro) {
        this.documentos = documentos;
        this.documentoService = documentoService;
        this.cadastro = cadastro;
    }

    @Transactional(readOnly = true)
    public LiberacaoResponse motorista(Long motoristaId) {
        Motorista motorista = cadastro.motorista(motoristaId);
        List<String> pendencias = new ArrayList<>();
        List<String> avisos = new ArrayList<>();
        if (!motorista.isAtivo()) {
            pendencias.add("Motorista " + motorista.getNome() + " esta desativado.");
        }
        verificar(Dono.MOTORISTA, documentos.findByMotoristaIdOrderByValidade(motoristaId),
                motorista.getNome(), pendencias, avisos);
        return new LiberacaoResponse(pendencias.isEmpty(), pendencias, avisos);
    }

    @Transactional(readOnly = true)
    public LiberacaoResponse veiculo(Long veiculoId) {
        Veiculo veiculo = cadastro.veiculo(veiculoId);
        List<String> pendencias = new ArrayList<>();
        List<String> avisos = new ArrayList<>();
        if (!veiculo.isAtivo()) {
            pendencias.add("Veiculo " + veiculo.getPlaca() + " esta desativado.");
        }
        verificar(Dono.VEICULO, documentos.findByVeiculoIdOrderByValidade(veiculoId),
                "veiculo " + veiculo.getPlaca(), pendencias, avisos);
        return new LiberacaoResponse(pendencias.isEmpty(), pendencias, avisos);
    }

    /** Viagem = motorista + veiculo. So libera se os dois estiverem em ordem. */
    @Transactional(readOnly = true)
    public LiberacaoResponse viagem(Long motoristaId, Long veiculoId) {
        LiberacaoResponse doMotorista = motorista(motoristaId);
        LiberacaoResponse doVeiculo = veiculo(veiculoId);

        List<String> pendencias = new ArrayList<>(doMotorista.pendencias());
        pendencias.addAll(doVeiculo.pendencias());
        List<String> avisos = new ArrayList<>(doMotorista.avisos());
        avisos.addAll(doVeiculo.avisos());
        return new LiberacaoResponse(pendencias.isEmpty(), pendencias, avisos);
    }

    private void verificar(Dono dono, List<Documento> cadastrados, String nome,
                           List<String> pendencias, List<String> avisos) {
        Map<TipoDocumento, Documento> porTipo = cadastrados.stream()
                .collect(Collectors.toMap(Documento::getTipo, Function.identity()));

        for (TipoDocumento tipo : TipoDocumento.obrigatoriosPara(dono)) {
            if (!porTipo.containsKey(tipo)) {
                pendencias.add(tipo.getDescricao() + " de " + nome + " nao cadastrado.");
            }
        }

        for (Documento documento : cadastrados) {
            Situacao situacao = documentoService.situacao(documento);
            String descricao = documento.getTipo().getDescricao() + " de " + nome;
            String validade = documento.getValidade().format(DATA_BR);
            if (situacao == Situacao.VENCIDO && documento.getTipo().isObrigatorio()) {
                pendencias.add(descricao + " vencido desde " + validade + ".");
            } else if (situacao == Situacao.VENCIDO) {
                avisos.add(descricao + " vencido desde " + validade + " (nao bloqueia a viagem).");
            } else if (situacao == Situacao.VENCE_EM_BREVE) {
                avisos.add(descricao + " vence em " + validade + ".");
            }
        }
    }
}
