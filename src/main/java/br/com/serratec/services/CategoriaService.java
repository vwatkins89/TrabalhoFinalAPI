package br.com.serratec.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    
    public List<CategoriaResponseDTO> buscarTodos() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream()
                .map(categoriaMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    
    public CategoriaResponseDTO buscarPorId(Long id) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);

        Categoria categoria = categoriaOptional.orElseThrow(() -> new NotFoundException("Categoria não encontrada com o ID: " + id));
        
        return categoriaMapper.toResponseDto(categoria);
    }

    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO categoriaRequestDTO) {
        if (categoriaRequestDTO.getNome() == null || categoriaRequestDTO.getNome().trim().isEmpty()) {
            throw new UsuarioException("O nome da categoria é obrigatório.");
        }

        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada para atualização com o ID: " + id));

        categoriaExistente.setNome(categoriaRequestDTO.getNome());

        categoriaRepository.save(categoriaExistente);

        return categoriaMapper.toResponseDto(categoriaExistente);
    }

    public void deletar(Long id) {

        if (categoriaRepository.existsById(id)) {
            throw new NotFoundException("Categoria não encontrada para deleção com o ID: " + id);
        }
        
        categoriaRepository.deleteById(id);
    }
}