package com.hectorkaul.documentos.repository;

import com.hectorkaul.documentos.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    boolean existsByPlaca(String placa);

    List<Veiculo> findByAtivoTrueOrderByPlaca();
}
