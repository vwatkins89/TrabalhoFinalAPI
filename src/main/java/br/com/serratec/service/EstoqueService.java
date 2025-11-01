package br.com.serratec.service;

import br.com.serratec.dto.EstoqueRequestDTO;
import br.com.serratec.dto.EstoqueResponseDTO;
import br.com.serratec.entity.Estoque;
import br.com.serratec.entity.Produto;
import br.com.serratec.exception.ResourceNotFoundException;
import br.com.serratec.repository.EstoqueRepository;
import br.com.serratec.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepo;
    private final ProdutoRepository produtoRepo;

    public EstoqueService(EstoqueRepository estoqueRepo, ProdutoRepository produtoRepo) {
        this.estoqueRepo = estoqueRepo;
        this.produtoRepo = produtoRepo;
    }

    @Transactional
    public Estoque criar(EstoqueRequestDTO dto) {
        if (estoqueRepo.existsByProdutoId(dto.getProdutoId())) {
            throw new IllegalArgumentException("Produto já possui estoque");
        }

        Produto produto = produtoRepo.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        Estoque estoque = new Estoque();
        estoque.setQuantidade(dto.getQuantidade());
        estoque.setProduto(produto);

        return estoqueRepo.save(estoque);
    }

    @Transactional(readOnly = true)
    public List<EstoqueResponseDTO> listarTodos() {
        return estoqueRepo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Estoque buscarPorId(Long id) {
        return estoqueRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));
    }

    @Transactional
    public Estoque atualizar(Long id, EstoqueRequestDTO dto) {
        Estoque estoque = buscarPorId(id);
        estoque.setQuantidade(dto.getQuantidade());

        Produto produto = produtoRepo.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        estoque.setProduto(produto);
        return estoqueRepo.save(estoque);
    }

    @Transactional
    public void deletar(Long id) {
        Estoque estoque = buscarPorId(id);
        estoqueRepo.delete(estoque);
    }

    public EstoqueResponseDTO toResponse(Estoque e) {
        EstoqueResponseDTO dto = new EstoqueResponseDTO();
        dto.setId(e.getId());
        dto.setQuantidade(e.getQuantidade());
        dto.setProdutoId(e.getProduto().getId());
        dto.setProdutoNome(e.getProduto().getNome());
        return dto;
    }
}