package br.com.serratec.service;

import br.com.serratec.dto.ProdutoRequestDTO;
import br.com.serratec.dto.ProdutoResponseDTO;
import br.com.serratec.entity.Categoria;
import br.com.serratec.entity.Produto;
import br.com.serratec.exception.ResourceNotFoundException;
import br.com.serratec.repository.CategoriaRepository;
import br.com.serratec.repository.ProdutoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<ProdutoResponseDTO> listarPorCategoria(Long categoriaId) {
        return produtoRepository.findByCategoriaId(categoriaId).stream().map(this::toResponse).toList();
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto p = produtoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));
        return toResponse(p);
    }

    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        Produto p = new Produto();
        p.setNome(dto.getNome().trim());
        p.setDescricao(dto.getDescricao());
        p.setPreco(dto.getPreco());

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id: " + dto.getCategoriaId()));
            p.setCategoria(categoria);
        } else {
            p.setCategoria(null);
        }

        Produto salvo = produtoRepository.save(p);
        return toResponse(salvo);
    }

    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto p = produtoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));

        p.setNome(dto.getNome().trim());
        p.setDescricao(dto.getDescricao());
        p.setPreco(dto.getPreco());

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id: " + dto.getCategoriaId()));
            p.setCategoria(categoria);
        } else {
            p.setCategoria(null);
        }

        Produto atualizado = produtoRepository.save(p);
        return toResponse(atualizado);
    }

    @Transactional
    public void excluir(Long id) {
        Produto p = produtoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));
        produtoRepository.delete(p);
    }

    private ProdutoResponseDTO toResponse(Produto p) {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.setId(p.getId());
        dto.setNome(p.getNome());
        dto.setDescricao(p.getDescricao());
        dto.setPreco(p.getPreco());
        if (p.getCategoria() != null) {
            dto.setCategoriaId(p.getCategoria().getId());
            dto.setCategoriaNome(p.getCategoria().getNome());
        }
        return dto;
    }
}