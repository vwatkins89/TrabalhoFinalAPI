package br.com.serratec.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.serratec.dto.PedidoRequestDTO;
import br.com.serratec.dto.PedidoResponseDTO;
import br.com.serratec.entity.Cliente;
import br.com.serratec.entity.Pedido;
import br.com.serratec.enums.StatusPedido;
import br.com.serratec.exception.NotFoundException;
import br.com.serratec.exception.UsuarioException;
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
		if(pedidoRequestDto.getClienteId() == null) {
			throw new UsuarioException("O id do cliente é obrigatório");
		}
		Cliente cliente = clienteRepository.findById(pedidoRequestDto.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));
        Pedido pedidoEntity = pedidoMapper.toEntity(pedidoRequestDto);
        pedidoEntity.setCliente(cliente);
        pedidoEntity.setDataCriacao(LocalDateTime.now());
        pedidoEntity.setStatus(StatusPedido.ENTREGUE);
        pedidoEntity.setNumeroPedido("PEDIDO-" + System.currentTimeMillis());

        pedidoRepository.save(pedidoEntity);

        PedidoResponseDTO response = pedidoMapper.toResponseDto(pedidoEntity);

        return response;
		
	}
	 public List<PedidoResponseDTO> listar() {
	        List<Pedido> pedidos = pedidoRepository.findAll();

	        List<PedidoResponseDTO> response = pedidos.stream()
	                .map(pedidoMapper::toResponseDto)
	                .toList();

	        return response;
	}

	    public PedidoResponseDTO buscarPorId(Long id) {
	        Pedido pedidoEntity = pedidoRepository.findById(id)
	                .orElseThrow(() -> new NotFoundException("Pedido não encontrado."));

	        PedidoResponseDTO response = pedidoMapper.toResponseDto(pedidoEntity);

	        return response;
	    }

	    @Transactional
	    public PedidoResponseDTO atualizar(Long id, PedidoRequestDTO pedidoRequestDTO) {
	        Pedido pedidoExistente = pedidoRepository.findById(id)
	                .orElseThrow(() -> new NotFoundException("Pedido não encontrado."));

	        if (pedidoRequestDTO.getClienteId() == null) {
	            throw new UsuarioException("O id do cliente é obrigatório.");
	        }

	        Cliente cliente = clienteRepository.findById(pedidoRequestDTO.getClienteId())
	                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

	        Pedido pedidoAtualizado = pedidoMapper.toEntity(pedidoRequestDTO);

	        
	        pedidoAtualizado.setId(pedidoExistente.getId()); // preserva registros existente que não vêm do dto
	        pedidoAtualizado.setCliente(cliente);
	        pedidoAtualizado.setDataCriacao(pedidoExistente.getDataCriacao());
	        pedidoAtualizado.setNumeroPedido(pedidoExistente.getNumeroPedido());
	        pedidoAtualizado.setStatus(pedidoExistente.getStatus());

	        pedidoRepository.save(pedidoAtualizado);

	        PedidoResponseDTO response = pedidoMapper.toResponseDto(pedidoAtualizado);

	        return response;
	    }
	  
	    @Transactional
	    public void deletar(Long id) {
	        Pedido pedidoEntity = pedidoRepository.findById(id)
	                .orElseThrow(() -> new NotFoundException("Pedido não encontrado."));
	        pedidoRepository.delete(pedidoEntity);
	    }
	}

	
	
	


