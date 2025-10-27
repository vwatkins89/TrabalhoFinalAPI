package br.com.serratec.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.serratec.dto.ItemPedidoRequestDTO;
import br.com.serratec.dto.PedidoRequestDTO;
import br.com.serratec.dto.PedidoResponseDTO;
import br.com.serratec.entity.Cliente;
import br.com.serratec.entity.ItemPedido;
import br.com.serratec.entity.Pedido;
import br.com.serratec.entity.Produto;
import br.com.serratec.enums.StatusPedido;
import br.com.serratec.exception.NotFoundException;
import br.com.serratec.repository.ClienteRepository;
import br.com.serratec.repository.ItemPedidoRepository;
import br.com.serratec.repository.PedidoRepository;
import br.com.serratec.repository.ProdutoRepository;
import jakarta.transaction.Transactional;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	public List<PedidoResponseDTO> listar() {
        List<PedidoResponseDTO> pedidosDTO = new ArrayList<>();
        for (Pedido pedido : pedidoRepository.findAll()) {
            pedidosDTO.add(new PedidoResponseDTO(pedido.getId(), pedido.getNumeroPedido(), pedido.getStatus().name()));
        }
        return pedidosDTO;
    }

    @Transactional
    public PedidoResponseDTO inserir(PedidoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(dto.getNumeroPedido());
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setStatus(StatusPedido.RECEBIDO);
        pedido.setCliente(cliente);
        pedido = pedidoRepository.save(pedido);

        for (ItemPedidoRequestDTO itemDTO : dto.getItens()) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(itemDTO.getPrecoUnitario());

            itemPedidoRepository.save(item);
        }

        return new PedidoResponseDTO(pedido.getId(), pedido.getNumeroPedido(), pedido.getStatus().name());
    }
}

