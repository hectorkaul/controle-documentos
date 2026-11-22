package com.hectorkaul.documentos.service;

import com.hectorkaul.documentos.dto.DocumentoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Todo dia de manha lista o que esta vencido ou vencendo.
 * Aqui so vai para o log. Numa empresa, o proximo passo seria mandar por e-mail para o setor.
 */
@Component
public class AlertaDeVencimentos {

    private static final Logger log = LoggerFactory.getLogger(AlertaDeVencimentos.class);

    private final DocumentoService documentos;
    private final int diasDeAviso;

    public AlertaDeVencimentos(DocumentoService documentos, @Value("${documentos.dias-de-aviso:30}") int diasDeAviso) {
        this.documentos = documentos;
        this.diasDeAviso = diasDeAviso;
    }

    @Scheduled(cron = "${documentos.alerta-cron:0 0 7 * * *}")
    public void verificar() {
        List<DocumentoResponse> lista = documentos.vencendo(diasDeAviso);
        if (lista.isEmpty()) {
            log.info("Alerta de vencimentos: nenhum documento vencido ou vencendo nos proximos {} dias.", diasDeAviso);
            return;
        }
        log.warn("Alerta de vencimentos: {} documento(s) precisam de atencao.", lista.size());
        for (DocumentoResponse d : lista) {
            log.warn("  [{}] {} - {} (validade {}, {} dias)", d.situacao(), d.descricao(), d.dono(),
                    d.validade(), d.diasParaVencer());
        }
    }
}
