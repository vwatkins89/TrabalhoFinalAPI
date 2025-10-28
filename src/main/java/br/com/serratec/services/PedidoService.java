package br.com.serratec.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.serratec.dto.PedidoRequestDTO;
import br.com.serratec.dto.PedidoResponseDTO;
import br.com.serratec.entity.Pedido;
import br.com.serratec.enums.StatusPedido;
import br.com.serratec.exception.NotFoundException;
import br.com.serratec.mapper.PedidoMapper;
import br.com.serratec.repository.ClienteRepository;
import br.com.serratec.repository.PedidoRepository;
import jakarta.transaction.Transactional;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private PedidoMapper pedidoMapper;
	
	//Criar um Pedido
	@Transactional
	public PedidoResponseDTO criar(PedidoRequestDTO pedidoRequestDto) {
		
		//estou utilizando var. Var se adapta a cada string que ele é utilizado, buscando a qual string ele se referencia.
		
		var cliente = clienteRepository.findById(pedidoRequestDto.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));
        var pedidoEntity = pedidoMapper.toEntity(pedidoRequestDto);
        pedidoEntity.setCliente(cliente);
        pedidoEntity.setDataCriacao(LocalDateTime.now());
        pedidoEntity.setStatus(StatusPedido.ENTREGUE);
        pedidoEntity.setNumeroPedido("PEDIDO-" + System.currentTimeMillis());

        pedidoRepository.save(pedidoEntity);
        
        var response = pedidoMapper.toResponseDto(pedidoEntity);
        return response;	
	}
	
	//listar todos
	 public List<PedidoResponseDTO> listar() {
	       
		 	List<Pedido> pedidos = pedidoRepository.findAll();
	        List<PedidoResponseDTO> response = pedidos.stream()
	                .map(pedidoMapper::toResponseDto)
	                .toList();
	        return response;
	}

	//listar por id 
	 public PedidoResponseDTO buscarPorId(Long id) {
	    	
	        var pedidoEntity = pedidoRepository.findById(id)
	                .orElseThrow(() -> new NotFoundException("Pedido não encontrado."));
	        var response = pedidoMapper.toResponseDto(pedidoEntity);
	        return response;
	    }

	 
	 //Atualizar pedido
	 @Transactional
	 public PedidoResponseDTO atualizar(Long id, PedidoRequestDTO pedidoRequestDTO) {
	        var pedidoExistente = pedidoRepository.findById(id)
	                .orElseThrow(() -> new NotFoundException("Pedido não encontrado."));

	        var cliente = clienteRepository.findById(pedidoRequestDTO.getClienteId())
	                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

	        var pedidoAtualizado = pedidoMapper.toEntity(pedidoRequestDTO);

	        
	        pedidoAtualizado.setId(pedidoExistente.getId()); // preserva registros existente que não vêm do dto
	        pedidoAtualizado.setCliente(cliente);
	        pedidoAtualizado.setDataCriacao(pedidoExistente.getDataCriacao());
	        pedidoAtualizado.setNumeroPedido(pedidoExistente.getNumeroPedido());
	        pedidoAtualizado.setStatus(pedidoExistente.getStatus());

	        pedidoRepository.save(pedidoAtualizado);

	        var response = pedidoMapper.toResponseDto(pedidoAtualizado);
	        return response;
	    }
	  
	 
	 //Deletar pedido
	 @Transactional
	 public String deletar(Long id) {
	     var pedidoEntity = pedidoRepository.findById(id) //find traz o objeto junto, o existisById apenas diz se ele existe ou nao(true or false
	             .orElseThrow(() -> new NotFoundException("Pedido não encontrado."));
	     pedidoRepository.delete(pedidoEntity);	
	     return String.format("Pedido id: % deletado com sucesso", id);
	     }
}

	
	
	


