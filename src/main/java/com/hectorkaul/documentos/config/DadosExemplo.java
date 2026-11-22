package com.hectorkaul.documentos.config;

import com.hectorkaul.documentos.dto.DocumentoRequest;
import com.hectorkaul.documentos.dto.MotoristaRequest;
import com.hectorkaul.documentos.dto.VeiculoRequest;
import com.hectorkaul.documentos.model.TipoDocumento;
import com.hectorkaul.documentos.repository.MotoristaRepository;
import com.hectorkaul.documentos.service.CadastroService;
import com.hectorkaul.documentos.service.DocumentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;

/**
 * Dados ficticios para testar a API sem cadastrar tudo na mao.
 * So roda com o perfil demo:  mvn spring-boot:run -Dspring-boot.run.profiles=demo
 * As datas sao calculadas a partir de hoje, entao sempre tem documento vencido, vencendo e em dia.
 */
@Component
@Profile("demo")
public class DadosExemplo implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DadosExemplo.class);

    private final CadastroService cadastro;
    private final DocumentoService documentos;
    private final MotoristaRepository motoristas;
    private final Clock relogio;

    public DadosExemplo(CadastroService cadastro, DocumentoService documentos,
                        MotoristaRepository motoristas, Clock relogio) {
        this.cadastro = cadastro;
        this.documentos = documentos;
        this.motoristas = motoristas;
        this.relogio = relogio;
    }

    @Override
    public void run(String... args) {
        if (motoristas.count() > 0) {
            log.info("Banco ja tem dados, nao gerei o exemplo.");
            return;
        }
        LocalDate hoje = LocalDate.now(relogio);

        long ana = cadastro.cadastrarMotorista(new MotoristaRequest("Ana Paula Ritter", "M001")).getId();
        long bruno = cadastro.cadastrarMotorista(new MotoristaRequest("Bruno Schmitt", "M002")).getId();
        long carlos = cadastro.cadastrarMotorista(new MotoristaRequest("Carlos Eduardo Lima", "M003")).getId();

        long tanque = cadastro.cadastrarVeiculo(new VeiculoRequest("IAB1C23", "Scania R450 - tanque 30 mil litros")).getId();
        long bau = cadastro.cadastrarVeiculo(new VeiculoRequest("IXY-4567", "Volvo FH 460 - bau")).getId();

        // Ana: tudo em dia
        motorista(ana, TipoDocumento.CNH, hoje.plusYears(3));
        motorista(ana, TipoDocumento.MOPP, hoje.plusYears(2));
        motorista(ana, TipoDocumento.ASO, hoje.plusMonths(8));
        motorista(ana, TipoDocumento.TOXICOLOGICO, hoje.plusMonths(20));

        // Bruno: MOPP vencido, ASO vencendo -> nao pode viajar
        motorista(bruno, TipoDocumento.CNH, hoje.plusYears(1));
        motorista(bruno, TipoDocumento.MOPP, hoje.minusDays(12));
        motorista(bruno, TipoDocumento.ASO, hoje.plusDays(9));
        motorista(bruno, TipoDocumento.TOXICOLOGICO, hoje.plusMonths(5));

        // Carlos: falta o toxicologico
        motorista(carlos, TipoDocumento.CNH, hoje.plusMonths(14));
        motorista(carlos, TipoDocumento.MOPP, hoje.plusDays(25));
        motorista(carlos, TipoDocumento.ASO, hoje.plusMonths(6));

        veiculo(tanque, TipoDocumento.CRLV, hoje.plusMonths(4));
        veiculo(tanque, TipoDocumento.CIV, hoje.plusMonths(7));
        veiculo(tanque, TipoDocumento.CIPP, hoje.plusDays(18));
        veiculo(tanque, TipoDocumento.TACOGRAFO, hoje.plusMonths(11));

        veiculo(bau, TipoDocumento.CRLV, hoje.plusMonths(4));
        veiculo(bau, TipoDocumento.CIV, hoje.minusDays(3));
        veiculo(bau, TipoDocumento.CIPP, hoje.plusMonths(9));
        veiculo(bau, TipoDocumento.TACOGRAFO, hoje.plusMonths(2));
        veiculo(bau, TipoDocumento.SEGURO_RCTRC, hoje.minusDays(30));

        log.info("Dados de exemplo criados: 3 motoristas, 2 veiculos.");
    }

    private void motorista(long id, TipoDocumento tipo, LocalDate validade) {
        documentos.cadastrar(new DocumentoRequest(tipo, numero(tipo), validade, id, null, null));
    }

    private void veiculo(long id, TipoDocumento tipo, LocalDate validade) {
        documentos.cadastrar(new DocumentoRequest(tipo, numero(tipo), validade, null, id, null));
    }

    private String numero(TipoDocumento tipo) {
        return tipo.name().substring(0, 3) + "-" + (10000 + (int) (Math.random() * 89999));
    }
}
