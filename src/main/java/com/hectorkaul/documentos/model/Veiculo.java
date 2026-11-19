package com.hectorkaul.documentos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 7)
    private String placa;

    @Column(nullable = false)
    private String descricao;

    private boolean ativo = true;

    protected Veiculo() {
        // exigido pelo JPA
    }

    public Veiculo(String placa, String descricao) {
        this.placa = normalizarPlaca(placa);
        this.descricao = descricao.trim();
    }

    /** "abc-1d23" e "ABC1D23" sao a mesma placa. */
    public static String normalizarPlaca(String placa) {
        return placa.replace("-", "").replace(" ", "").toUpperCase();
    }

    public Long getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void desativar() {
        this.ativo = false;
    }
}
