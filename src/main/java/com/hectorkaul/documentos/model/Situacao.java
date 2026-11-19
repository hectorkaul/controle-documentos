package com.hectorkaul.documentos.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public enum Situacao {
    VENCIDO,
    VENCE_EM_BREVE,
    EM_DIA;

    /**
     * O documento vale ate o fim do dia da validade. Exemplo com aviso de 30 dias:
     * validade hoje = VENCE_EM_BREVE, ontem = VENCIDO, daqui a 31 dias = EM_DIA.
     */
    public static Situacao de(LocalDate validade, LocalDate hoje, int diasDeAviso) {
        long dias = ChronoUnit.DAYS.between(hoje, validade);
        if (dias < 0) {
            return VENCIDO;
        }
        return dias <= diasDeAviso ? VENCE_EM_BREVE : EM_DIA;
    }
}
