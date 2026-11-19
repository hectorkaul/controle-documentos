package com.hectorkaul.documentos.repository;

import com.hectorkaul.documentos.model.Motorista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MotoristaRepository extends JpaRepository<Motorista, Long> {

    boolean existsByMatricula(String matricula);

    List<Motorista> findByAtivoTrueOrderByNome();
}
