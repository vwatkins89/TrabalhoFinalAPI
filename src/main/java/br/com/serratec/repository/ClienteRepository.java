package br.com.serratec.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.serratec.entity.Cliente;


public interface ClienteRepository extends JpaRepository<Cliente, Long>  {
	
	//buscar clientes por nome
	public Page<Cliente> findByContainingIgnoreCase(String nome, Pageable pageable);

}
