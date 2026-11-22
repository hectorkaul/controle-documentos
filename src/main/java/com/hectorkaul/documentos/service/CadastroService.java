package com.hectorkaul.documentos.service;

import com.hectorkaul.documentos.dto.MotoristaRequest;
import com.hectorkaul.documentos.dto.VeiculoRequest;
import com.hectorkaul.documentos.exception.JaExisteException;
import com.hectorkaul.documentos.exception.NaoEncontradoException;
import com.hectorkaul.documentos.model.Motorista;
import com.hectorkaul.documentos.model.Veiculo;
import com.hectorkaul.documentos.repository.MotoristaRepository;
import com.hectorkaul.documentos.repository.VeiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Cadastro de motoristas e veiculos. */
@Service
public class CadastroService {

    private final MotoristaRepository motoristas;
    private final VeiculoRepository veiculos;

    public CadastroService(MotoristaRepository motoristas, VeiculoRepository veiculos) {
        this.motoristas = motoristas;
        this.veiculos = veiculos;
    }

    @Transactional
    public Motorista cadastrarMotorista(MotoristaRequest dados) {
        Motorista motorista = new Motorista(dados.nome(), dados.matricula());
        if (motoristas.existsByMatricula(motorista.getMatricula())) {
            throw new JaExisteException("Ja existe um motorista com a matricula " + motorista.getMatricula() + ".");
        }
        return motoristas.save(motorista);
    }

    @Transactional(readOnly = true)
    public List<Motorista> motoristasAtivos() {
        return motoristas.findByAtivoTrueOrderByNome();
    }

    @Transactional(readOnly = true)
    public Motorista motorista(Long id) {
        return motoristas.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Motorista " + id + " nao encontrado."));
    }

    /** Desativa em vez de apagar: os documentos antigos continuam no historico. */
    @Transactional
    public void desativarMotorista(Long id) {
        motorista(id).desativar();
    }

    @Transactional
    public Veiculo cadastrarVeiculo(VeiculoRequest dados) {
        Veiculo veiculo = new Veiculo(dados.placa(), dados.descricao());
        if (veiculos.existsByPlaca(veiculo.getPlaca())) {
            throw new JaExisteException("Ja existe um veiculo com a placa " + veiculo.getPlaca() + ".");
        }
        return veiculos.save(veiculo);
    }

    @Transactional(readOnly = true)
    public List<Veiculo> veiculosAtivos() {
        return veiculos.findByAtivoTrueOrderByPlaca();
    }

    @Transactional(readOnly = true)
    public Veiculo veiculo(Long id) {
        return veiculos.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Veiculo " + id + " nao encontrado."));
    }

    @Transactional
    public void desativarVeiculo(Long id) {
        veiculo(id).desativar();
    }
}
