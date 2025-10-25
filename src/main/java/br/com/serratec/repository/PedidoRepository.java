package br.com.serratec.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.serratec.entity.Pedido;


public interface PedidoRepository extends JpaRepository<Pedido, Long> {
	
	//Busca o pedido pelo número
	Optional<Pedido> findByNumeroPedido(String numeroPedido);
	
	//Lista o pedido por partes de um número
	public Page<Pedido> findByNumeroPedidoContaining(String numeroPedido, Pageable pageable);
	
	//lista todos os pedidos
	public Page<Pedido> findAll(Pageable pageable);

}
