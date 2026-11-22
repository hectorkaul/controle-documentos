package com.hectorkaul.documentos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;

@Configuration
@EnableScheduling
public class Configuracao {

    /**
     * Todo calculo de vencimento usa este relogio em vez de LocalDate.now().
     * Nos testes ele e trocado por um relogio fixo, assim o resultado nao depende do dia em que o teste roda.
     */
    @Bean
    Clock relogio() {
        return Clock.systemDefaultZone();
    }
}
