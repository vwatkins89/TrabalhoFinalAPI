package br.com.serratec.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.serratec.entity.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	public Page<Produto> findByNomeContaining(String nome, Pageable pageable);
}
