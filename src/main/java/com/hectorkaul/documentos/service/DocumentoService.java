package com.hectorkaul.documentos.service;

import com.hectorkaul.documentos.dto.DocumentoRequest;
import com.hectorkaul.documentos.dto.DocumentoResponse;
import com.hectorkaul.documentos.dto.PainelResponse;
import com.hectorkaul.documentos.dto.RenovacaoRequest;
import com.hectorkaul.documentos.exception.JaExisteException;
import com.hectorkaul.documentos.exception.NaoEncontradoException;
import com.hectorkaul.documentos.exception.RegraDeNegocioException;
import com.hectorkaul.documentos.model.Documento;
import com.hectorkaul.documentos.model.Dono;
import com.hectorkaul.documentos.model.Motorista;
import com.hectorkaul.documentos.model.Situacao;
import com.hectorkaul.documentos.model.TipoDocumento;
import com.hectorkaul.documentos.model.Veiculo;
import com.hectorkaul.documentos.repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentoService {

    private final DocumentoRepository documentos;
    private final CadastroService cadastro;
    private final Clock relogio;
    private final int diasDeAviso;

    public DocumentoService(DocumentoRepository documentos, CadastroService cadastro, Clock relogio,
                            @Value("${documentos.dias-de-aviso:30}") int diasDeAviso) {
        this.documentos = documentos;
        this.cadastro = cadastro;
        this.relogio = relogio;
        this.diasDeAviso = diasDeAviso;
    }

    @Transactional
    public DocumentoResponse cadastrar(DocumentoRequest dados) {
        TipoDocumento tipo = dados.tipo();
        LocalDateTime agora = LocalDateTime.now(relogio);
        Documento documento;

        if (tipo.getDono() == Dono.MOTORISTA) {
            exigirSoUmDono(dados.motoristaId(), dados.veiculoId(), tipo, "motoristaId");
            Motorista motorista = cadastro.motorista(dados.motoristaId());
            documentos.findByMotoristaIdAndTipo(motorista.getId(), tipo).ifPresent(existente -> {
                throw new JaExisteException(motorista.getNome() + " ja tem " + tipo.getDescricao()
                        + " cadastrado (documento " + existente.getId() + "). Use a renovacao.");
            });
            documento = Documento.doMotorista(motorista, tipo, dados.numero(), dados.validade(), dados.observacao(), agora);
        } else {
            exigirSoUmDono(dados.veiculoId(), dados.motoristaId(), tipo, "veiculoId");
            Veiculo veiculo = cadastro.veiculo(dados.veiculoId());
            documentos.findByVeiculoIdAndTipo(veiculo.getId(), tipo).ifPresent(existente -> {
                throw new JaExisteException("O veiculo " + veiculo.getPlaca() + " ja tem " + tipo.getDescricao()
                        + " cadastrado (documento " + existente.getId() + "). Use a renovacao.");
            });
            documento = Documento.doVeiculo(veiculo, tipo, dados.numero(), dados.validade(), dados.observacao(), agora);
        }
        return resposta(documentos.save(documento));
    }

    private void exigirSoUmDono(Long idEsperado, Long idDoOutro, TipoDocumento tipo, String campo) {
        if (idEsperado == null) {
            throw new RegraDeNegocioException(tipo.getDescricao() + " e um documento de "
                    + tipo.getDono().name().toLowerCase() + ": informe o " + campo + ".");
        }
        if (idDoOutro != null) {
            throw new RegraDeNegocioException("Informe so o " + campo + " para " + tipo.getDescricao() + ".");
        }
    }

    /** Documento novo substitui o antigo: a validade nova precisa ser depois da atual. */
    @Transactional
    public DocumentoResponse renovar(Long id, RenovacaoRequest dados) {
        Documento documento = buscar(id);
        if (!dados.validade().isAfter(documento.getValidade())) {
            throw new RegraDeNegocioException("A nova validade (" + dados.validade()
                    + ") precisa ser depois da atual (" + documento.getValidade() + ").");
        }
        documento.renovar(dados.numero(), dados.validade(), LocalDateTime.now(relogio));
        return resposta(documento);
    }

    @Transactional(readOnly = true)
    public DocumentoResponse consultar(Long id) {
        return resposta(buscar(id));
    }

    /** Lista com filtros opcionais. */
    @Transactional(readOnly = true)
    public List<DocumentoResponse> listar(Situacao situacao, TipoDocumento tipo) {
        return documentos.todosAtivos().stream()
                .filter(d -> tipo == null || d.getTipo() == tipo)
                .map(this::resposta)
                .filter(r -> situacao == null || r.situacao() == situacao)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DocumentoResponse> doMotorista(Long motoristaId) {
        cadastro.motorista(motoristaId);
        return documentos.findByMotoristaIdOrderByValidade(motoristaId).stream().map(this::resposta).toList();
    }

    @Transactional(readOnly = true)
    public List<DocumentoResponse> doVeiculo(Long veiculoId) {
        cadastro.veiculo(veiculoId);
        return documentos.findByVeiculoIdOrderByValidade(veiculoId).stream().map(this::resposta).toList();
    }

    /** Vencidos + os que vencem nos proximos N dias. */
    @Transactional(readOnly = true)
    public List<DocumentoResponse> vencendo(int dias) {
        if (dias < 0) {
            throw new RegraDeNegocioException("O numero de dias nao pode ser negativo.");
        }
        return documentos.vencendoAte(hoje().plusDays(dias)).stream().map(this::resposta).toList();
    }

    @Transactional(readOnly = true)
    public PainelResponse painel() {
        List<DocumentoResponse> todos = listar(null, null);
        Map<Situacao, Long> totais = new EnumMap<>(Situacao.class);
        for (Situacao s : Situacao.values()) {
            totais.put(s, todos.stream().filter(d -> d.situacao() == s).count());
        }
        List<DocumentoResponse> proximos = todos.stream()
                .filter(d -> d.situacao() != Situacao.EM_DIA)
                .limit(10)
                .toList();
        return new PainelResponse(totais, proximos);
    }

    public Situacao situacao(Documento documento) {
        return Situacao.de(documento.getValidade(), hoje(), diasDeAviso);
    }

    LocalDate hoje() {
        return LocalDate.now(relogio);
    }

    private Documento buscar(Long id) {
        return documentos.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Documento " + id + " nao encontrado."));
    }

    DocumentoResponse resposta(Documento d) {
        return new DocumentoResponse(
                d.getId(),
                d.getTipo(),
                d.getTipo().getDescricao(),
                d.getNumero(),
                d.getValidade(),
                ChronoUnit.DAYS.between(hoje(), d.getValidade()),
                situacao(d),
                d.nomeDoDono(),
                d.getMotorista() == null ? null : d.getMotorista().getId(),
                d.getVeiculo() == null ? null : d.getVeiculo().getId());
    }
}
