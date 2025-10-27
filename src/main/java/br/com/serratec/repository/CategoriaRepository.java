package br.com.serratec.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.serratec.entity.Categoria;


public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
	public Page<Categoria> findByNomeContaining(String nome, Pageable pageable);

}
