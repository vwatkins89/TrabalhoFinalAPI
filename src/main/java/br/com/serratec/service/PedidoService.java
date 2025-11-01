package br.com.serratec.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.serratec.dto.ItemPedidoRequestDTO;
import br.com.serratec.dto.ItemPedidoResponseDTO;
import br.com.serratec.dto.PedidoRequestDTO;
import br.com.serratec.dto.PedidoResponseDTO;
import br.com.serratec.entity.Cliente;
import br.com.serratec.entity.Cupom;
import br.com.serratec.entity.ItemPedido;
import br.com.serratec.entity.Pedido;
import br.com.serratec.entity.Produto;
import br.com.serratec.exception.ResourceNotFoundException;
import br.com.serratec.repository.ClienteRepository;
import br.com.serratec.repository.CupomRepository;
import br.com.serratec.repository.PedidoRepository;
import br.com.serratec.repository.ProdutoRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepo;
    private final ProdutoRepository produtoRepo;
    private final ClienteRepository clienteRepo;
    private final CupomRepository cupomRepo;

    public PedidoService(PedidoRepository pedidoRepo,
                         ProdutoRepository produtoRepo,
                         ClienteRepository clienteRepo,
                         CupomRepository cupomRepo) {
        this.pedidoRepo = pedidoRepo;
        this.produtoRepo = produtoRepo;
        this.clienteRepo = clienteRepo;
        this.cupomRepo = cupomRepo;
    }

    @Transactional
    public Pedido criar(PedidoRequestDTO dto) {
        Cliente cliente = clienteRepo.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Pedido pedido = new Pedido();
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setStatus(dto.getStatus());
        pedido.setCliente(cliente);

        if (dto.getCupomId() != null) {
            Cupom cupom = cupomRepo.findById(dto.getCupomId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cupom não encontrado"));
            pedido.setCupom(cupom);
        }

        if (dto.getProdutos() != null) {
            for (ItemPedidoRequestDTO itDto : dto.getProdutos()) {
                Produto produto = produtoRepo.findById(itDto.getProdutoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

                ItemPedido item = new ItemPedido();
                item.setProduto(produto);
                item.setQuantidade(itDto.getQuantidade());
                item.setPrecoUnitario(produto.getPreco());
                pedido.addItem(item);
            }
        }

        BigDecimal total = pedido.getItens().stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (pedido.getCupom() != null && pedido.getCupom().getDesconto() != null) {
            BigDecimal desconto = pedido.getCupom().getDesconto();
            total = total.subtract(desconto);
            if (total.compareTo(BigDecimal.ZERO) < 0) {
                total = BigDecimal.ZERO;
            }
        }

        pedido.setTotal(total);
        return pedidoRepo.save(pedido);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long id) {
        return pedidoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));
    }

    @Transactional
    public Pedido atualizar(Long id, PedidoRequestDTO dto) {
        Pedido pedido = buscarPorId(id);

        if (dto.getStatus() != null) {
            pedido.setStatus(dto.getStatus());
        }

        if (dto.getCupomId() != null) {
            Cupom cupom = cupomRepo.findById(dto.getCupomId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cupom não encontrado"));
            pedido.setCupom(cupom);
        } else {
            pedido.setCupom(null);
        }

        BigDecimal total = pedido.getItens().stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (pedido.getCupom() != null && pedido.getCupom().getDesconto() != null) {
            BigDecimal desconto = pedido.getCupom().getDesconto();
            total = total.subtract(desconto);
            if (total.compareTo(BigDecimal.ZERO) < 0) {
                total = BigDecimal.ZERO;
            }
        }

        pedido.setTotal(total);
        return pedidoRepo.save(pedido);
    }

    @Transactional
    public void deletar(Long id) {
        Pedido pedido = buscarPorId(id);
        pedidoRepo.delete(pedido);
    }

    public PedidoResponseDTO toResponse(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setDataCriacao(pedido.getDataCriacao());
        dto.setStatus(pedido.getStatus());
        dto.setTotal(pedido.getTotal());
        dto.setClienteNome(pedido.getCliente() != null ? pedido.getCliente().getNome() : null);
        dto.setItens(pedido.getItens().stream().map(item -> {
            ItemPedidoResponseDTO i = new ItemPedidoResponseDTO();
            i.setId(item.getId());
            i.setProdutoId(item.getProduto().getId());
            i.setProdutoNome(item.getProduto().getNome());
            i.setQuantidade(item.getQuantidade());
            i.setPrecoUnitario(item.getPrecoUnitario());
            i.setSubtotal(item.getSubtotal());
            i.setPedidoId(item.getPedido() != null ? item.getPedido().getId() : null);
            return i;
        }).collect(Collectors.toList()));
        return dto;
    }
}