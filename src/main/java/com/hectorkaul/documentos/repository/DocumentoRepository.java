package com.hectorkaul.documentos.repository;

import com.hectorkaul.documentos.model.Documento;
import com.hectorkaul.documentos.model.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    List<Documento> findByMotoristaIdOrderByValidade(Long motoristaId);

    List<Documento> findByVeiculoIdOrderByValidade(Long veiculoId);

    Optional<Documento> findByMotoristaIdAndTipo(Long motoristaId, TipoDocumento tipo);

    Optional<Documento> findByVeiculoIdAndTipo(Long veiculoId, TipoDocumento tipo);

    /** Vencidos e os que vencem ate a data limite, so de motoristas e veiculos ativos. */
    @Query("""
            select d from Documento d
              left join fetch d.motorista m
              left join fetch d.veiculo v
             where d.validade <= :limite
               and (m.ativo = true or v.ativo = true)
             order by d.validade
            """)
    List<Documento> vencendoAte(LocalDate limite);

    @Query("""
            select d from Documento d
              left join fetch d.motorista m
              left join fetch d.veiculo v
             where (m.ativo = true or v.ativo = true)
             order by d.validade
            """)
    List<Documento> todosAtivos();
}
