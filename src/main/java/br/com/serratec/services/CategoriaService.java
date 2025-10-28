package br.com.serratec.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.serratec.dto.CategoriaRequestDTO;
import br.com.serratec.dto.CategoriaResponseDTO;
import br.com.serratec.entity.Categoria;
import br.com.serratec.exception.NotFoundException;
import br.com.serratec.exception.UsuarioException;
import br.com.serratec.mapper.CategoriaMapper;
import br.com.serratec.repository.CategoriaRepository;

@Service
public class CategoriaService {
    
	@Autowired
	CategoriaRepository categoriaRepository;
	@Autowired
	CategoriaMapper categoriaMapper;
	
    public CategoriaResponseDTO criar(CategoriaRequestDTO categoriaRequestDTO) {
        if (categoriaRequestDTO.getNome() == null || categoriaRequestDTO.getNome().trim().isEmpty()) {
            throw new UsuarioException("O nome da categoria é obrigatório.");
        }
        
        Categoria categoriaEntity = categoriaMapper.toEntity(categoriaRequestDTO);
        
        categoriaRepository.save(categoriaEntity);
        
        CategoriaResponseDTO response = categoriaMapper.toResponseDto(categoriaEntity);
        
        return response;
    }
    
    public List<Categoria> buscarTodos() {
        return categoriaRepository.findAll();
    }
    
    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria"));
    }
    
    public Categoria atualizar(Long id, Categoria categoriaDetalhes) {
        Categoria categoriaExistente = buscarPorId(id); 

        if (categoriaDetalhes.getNome() == null || categoriaDetalhes.getNome().trim().isEmpty()) {
            throw new UsuarioException("O nome da categoria não pode ser vazio.");
        }

        categoriaExistente.setNome(categoriaDetalhes.getNome());
        
        return categoriaRepository.save(categoriaExistente);
    }

    public void deletar(Long id) {
        Categoria categoria = buscarPorId(id); 
        categoriaRepository.delete(categoria);
    }

}