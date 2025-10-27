package br.com.serratec.services;

import br.com.serratec.entity.Categoria;
import br.com.serratec.repository.CategoriaRepository;
import br.com.serratec.exception.NotFoundException;
import br.com.serratec.exception.UsuarioException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    
    private final CategoriaRepository repositorio;

    public CategoriaService(CategoriaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Categoria criar(Categoria categoria) {
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            throw new UsuarioException("O nome da categoria é obrigatório.");
        }
        
        return repositorio.save(categoria);
    }
    
    public List<Categoria> buscarTodos() {
        return repositorio.findAll();
    }
    
    public Categoria buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria"));
    }
    
    public Categoria atualizar(Long id, Categoria categoriaDetalhes) {
        Categoria categoriaExistente = buscarPorId(id); 

        if (categoriaDetalhes.getNome() == null || categoriaDetalhes.getNome().trim().isEmpty()) {
            throw new UsuarioException("O nome da categoria não pode ser vazio.");
        }

        categoriaExistente.setNome(categoriaDetalhes.getNome());
        
        return repositorio.save(categoriaExistente);
    }

    public void deletar(Long id) {
        Categoria categoria = buscarPorId(id); 
        repositorio.delete(categoria);
    }
}