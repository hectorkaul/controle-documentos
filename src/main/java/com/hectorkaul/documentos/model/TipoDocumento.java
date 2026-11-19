package com.hectorkaul.documentos.model;

import java.util.Arrays;
import java.util.List;

/**
 * Documentos comuns no transporte rodoviario de produtos perigosos.
 * Os marcados como obrigatorios bloqueiam a viagem se estiverem vencidos ou faltando.
 */
public enum TipoDocumento {

    CNH("CNH", Dono.MOTORISTA, true),
    MOPP("Curso MOPP (produtos perigosos)", Dono.MOTORISTA, true),
    ASO("Exame medico (ASO)", Dono.MOTORISTA, true),
    TOXICOLOGICO("Exame toxicologico", Dono.MOTORISTA, true),
    TREINAMENTO_DIRECAO_DEFENSIVA("Treinamento de direcao defensiva", Dono.MOTORISTA, false),

    CRLV("CRLV", Dono.VEICULO, true),
    CIV("CIV - inspecao veicular", Dono.VEICULO, true),
    CIPP("CIPP - inspecao do equipamento", Dono.VEICULO, true),
    TACOGRAFO("Afericao do tacografo", Dono.VEICULO, true),
    SEGURO_RCTRC("Seguro RCTR-C", Dono.VEICULO, false);

    private final String descricao;
    private final Dono dono;
    private final boolean obrigatorio;

    TipoDocumento(String descricao, Dono dono, boolean obrigatorio) {
        this.descricao = descricao;
        this.dono = dono;
        this.obrigatorio = obrigatorio;
    }

    public String getDescricao() {
        return descricao;
    }

    public Dono getDono() {
        return dono;
    }

    public boolean isObrigatorio() {
        return obrigatorio;
    }

    public static List<TipoDocumento> obrigatoriosPara(Dono dono) {
        return Arrays.stream(values())
                .filter(t -> t.dono == dono && t.obrigatorio)
                .toList();
    }
}
