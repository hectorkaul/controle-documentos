package com.hectorkaul.documentos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Motorista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    /** Matricula interna da empresa. Uso ela em vez do CPF para nao guardar dado pessoal sem necessidade. */
    @Column(nullable = false, unique = true)
    private String matricula;

    private boolean ativo = true;

    protected Motorista() {
        // exigido pelo JPA
    }

    public Motorista(String nome, String matricula) {
        this.nome = nome.trim();
        this.matricula = matricula.trim().toUpperCase();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void desativar() {
        this.ativo = false;
    }
}
