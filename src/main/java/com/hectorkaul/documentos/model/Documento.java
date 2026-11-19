package com.hectorkaul.documentos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Um documento pertence a um motorista OU a um veiculo, nunca aos dois.
 * Quem garante isso e o DocumentoService, que confere o dono esperado pelo tipo.
 */
@Entity
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private TipoDocumento tipo;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private LocalDate validade;

    @ManyToOne(fetch = FetchType.LAZY)
    private Motorista motorista;

    @ManyToOne(fetch = FetchType.LAZY)
    private Veiculo veiculo;

    private String observacao;

    private LocalDateTime atualizadoEm;

    protected Documento() {
        // exigido pelo JPA
    }

    private Documento(TipoDocumento tipo, String numero, LocalDate validade, String observacao, LocalDateTime agora) {
        this.tipo = tipo;
        this.numero = numero.trim();
        this.validade = validade;
        this.observacao = observacao;
        this.atualizadoEm = agora;
    }

    public static Documento doMotorista(Motorista motorista, TipoDocumento tipo, String numero,
                                        LocalDate validade, String observacao, LocalDateTime agora) {
        Documento documento = new Documento(tipo, numero, validade, observacao, agora);
        documento.motorista = motorista;
        return documento;
    }

    public static Documento doVeiculo(Veiculo veiculo, TipoDocumento tipo, String numero,
                                      LocalDate validade, String observacao, LocalDateTime agora) {
        Documento documento = new Documento(tipo, numero, validade, observacao, agora);
        documento.veiculo = veiculo;
        return documento;
    }

    public void renovar(String novoNumero, LocalDate novaValidade, LocalDateTime agora) {
        this.numero = novoNumero.trim();
        this.validade = novaValidade;
        this.atualizadoEm = agora;
    }

    public String nomeDoDono() {
        return motorista != null ? motorista.getNome() : "Veiculo " + veiculo.getPlaca();
    }

    public Long getId() {
        return id;
    }

    public TipoDocumento getTipo() {
        return tipo;
    }

    public String getNumero() {
        return numero;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public String getObservacao() {
        return observacao;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}
