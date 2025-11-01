package br.com.serratec.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.serratec.dto.ItemPedidoRequestDTO;
import br.com.serratec.dto.ItemPedidoResponseDTO;
import br.com.serratec.entity.ItemPedido;
import br.com.serratec.entity.Pedido;
import br.com.serratec.entity.Produto;
import br.com.serratec.exception.ResourceNotFoundException;
import br.com.serratec.repository.ItemPedidoRepository;
import br.com.serratec.repository.PedidoRepository;
import br.com.serratec.repository.ProdutoRepository;

@Service
public class ItemPedidoService {

    private final ItemPedidoRepository itemRepo;
    private final ProdutoRepository produtoRepo;
    private final PedidoRepository pedidoRepo;

    public ItemPedidoService(ItemPedidoRepository itemRepo,
                             ProdutoRepository produtoRepo,
                             PedidoRepository pedidoRepo) {
        this.itemRepo = itemRepo;
        this.produtoRepo = produtoRepo;
        this.pedidoRepo = pedidoRepo;
    }

    @Transactional
    public ItemPedido criar(ItemPedidoRequestDTO dto) {
        Produto produto = produtoRepo.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        Pedido pedido = pedidoRepo.findById(dto.getPedidoId())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        ItemPedido item = new ItemPedido();
        item.setProduto(produto);
        item.setQuantidade(dto.getQuantidade());
        item.setPrecoUnitario(produto.getPreco());
        pedido.addItem(item);

        Pedido salvo = pedidoRepo.save(pedido);

        List<ItemPedido> itens = salvo.getItens();
        ItemPedido salvoItem = itens.get(itens.size() - 1);

        recalcularTotalDoPedido(salvo);

        return salvoItem;
    }

    @Transactional(readOnly = true)
    public List<ItemPedidoResponseDTO> listarTodos() {
        return itemRepo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ItemPedido buscarPorId(Long id) {
        return itemRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ItemPedido não encontrado"));
    }

    @Transactional
    public ItemPedido atualizar(Long id, ItemPedidoRequestDTO dto) {
        ItemPedido item = buscarPorId(id);

        if (dto.getQuantidade() != null) {
            item.setQuantidade(dto.getQuantidade());
        }

        if (dto.getProdutoId() != null && (item.getProduto() == null || !dto.getProdutoId().equals(item.getProduto().getId()))) {
            Produto novoProduto = produtoRepo.findById(dto.getProdutoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
            item.setProduto(novoProduto);
            item.setPrecoUnitario(novoProduto.getPreco());
        }

        if (dto.getPedidoId() != null && (item.getPedido() == null || !dto.getPedidoId().equals(item.getPedido().getId()))) {
            Pedido novoPedido = pedidoRepo.findById(dto.getPedidoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));
            Pedido antigo = item.getPedido();
            if (antigo != null) {
                antigo.removeItem(item);
                pedidoRepo.save(antigo);
            }
            novoPedido.addItem(item);
            pedidoRepo.save(novoPedido);
        }

        ItemPedido atualizado = itemRepo.save(item);

        Pedido pedidoParaRecalcular = atualizado.getPedido();
        if (pedidoParaRecalcular != null) {
            recalcularTotalDoPedido(pedidoParaRecalcular);
        }

        return atualizado;
    }

    @Transactional
    public void deletar(Long id) {
        ItemPedido item = buscarPorId(id);
        Pedido pedido = item.getPedido();
        if (pedido != null) {
            pedido.removeItem(item);
            pedidoRepo.save(pedido);
            recalcularTotalDoPedido(pedido);
        } else {
            itemRepo.delete(item);
        }
    }

    public ItemPedidoResponseDTO toResponse(ItemPedido item) {
        ItemPedidoResponseDTO dto = new ItemPedidoResponseDTO();
        dto.setId(item.getId());
        dto.setProdutoId(item.getProduto() != null ? item.getProduto().getId() : null);
        dto.setProdutoNome(item.getProduto() != null ? item.getProduto().getNome() : null);
        dto.setQuantidade(item.getQuantidade());
        dto.setPrecoUnitario(item.getPrecoUnitario());
        dto.setSubtotal(item.getSubtotal());
        dto.setPedidoId(item.getPedido() != null ? item.getPedido().getId() : null);
        return dto;
    }

    private void recalcularTotalDoPedido(Pedido pedido) {
        BigDecimal total = pedido.getItens().stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (pedido.getCupom() != null) {
            BigDecimal desconto = pedido.getCupom().getDesconto();
            if (desconto != null) {
                total = total.subtract(desconto);
                if (total.compareTo(BigDecimal.ZERO) < 0) {
                    total = BigDecimal.ZERO;
                }
            }
        }

        pedido.setTotal(total);
        pedidoRepo.save(pedido);
    }
}