package br.com.serratec.repository;

import br.com.serratec.entity.ListaDesejos;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListaDesejosRepository extends JpaRepository<ListaDesejos, Long> {
    Optional<ListaDesejos> findByClienteId(Long clienteId);
    boolean existsByClienteId(Long clienteId);
}