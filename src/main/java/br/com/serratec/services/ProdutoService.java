package br.com.serratec.services;

import br.com.serratec.entity.Categoria;
import br.com.serratec.entity.Produto;
import br.com.serratec.repository.CategoriaRepository;
import br.com.serratec.repository.ProdutoRepository;
import br.com.serratec.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public Produto criar(Produto produto, Long categoriaId) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
            .orElseThrow(() -> new NotFoundException("Categoria"));

        produto.setCategoria(categoria);
        return produtoRepository.save(produto);
    }
    
    @Transactional
    public Produto atualizar(Long id, Produto produtoDetalhes, Long categoriaId) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto"));
        
        Categoria novaCategoria = categoriaRepository.findById(categoriaId)
            .orElseThrow(() -> new NotFoundException("Categoria"));

        produtoExistente.setNome(produtoDetalhes.getNome());
        produtoExistente.setPreco(produtoDetalhes.getPreco());
        produtoExistente.setCategoria(novaCategoria);
        
        return produtoRepository.save(produtoExistente);
    }
    
    public Produto buscarPrecoEEstoqueDoProduto(Long produtoId) {
        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> new NotFoundException("Produto"));
    }
    
    public List<Produto> buscarTodos() {
        return produtoRepository.findAll();
    }
    
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto"));
    }

    public void deletar(Long id) {
        Produto produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }
}