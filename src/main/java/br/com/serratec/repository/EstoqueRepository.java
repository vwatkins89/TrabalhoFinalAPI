package br.com.serratec.repository;

import br.com.serratec.entity.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    boolean existsByProdutoId(Long produtoId);
    Estoque findByProdutoId(Long produtoId);
}