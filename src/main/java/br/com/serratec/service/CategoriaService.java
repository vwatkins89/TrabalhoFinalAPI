package br.com.serratec.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.serratec.dto.CategoriaRequestDTO;
import br.com.serratec.dto.CategoriaResponseDTO;
import br.com.serratec.entity.Categoria;
import br.com.serratec.exception.ResourceNotFoundException;
import br.com.serratec.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository repo;

    public CategoriaService(CategoriaRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Categoria criar(CategoriaRequestDTO dto) {
        Categoria c = new Categoria();
        c.setNome(dto.getNome());
        c.setDescricao(dto.getDescricao());
        return repo.save(c);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listar() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public Categoria buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada: " + id));
    }

    @Transactional
    public Categoria atualizar(Long id, CategoriaRequestDTO dto) {
        Categoria c = buscar(id);
        c.setNome(dto.getNome());
        c.setDescricao(dto.getDescricao());
        return repo.save(c);
    }

    @Transactional
    public Categoria atualizarParcial(Long id, CategoriaRequestDTO dto) {
        Categoria c = buscar(id);
        if (dto.getNome() != null && !dto.getNome().isBlank()) c.setNome(dto.getNome());
        if (dto.getDescricao() != null) c.setDescricao(dto.getDescricao());
        return repo.save(c);
    }

    @Transactional
    public void deletar(Long id) {
        Categoria c = buscar(id);
        repo.delete(c);
    }

    public CategoriaResponseDTO toResponse(Categoria c) {
        CategoriaResponseDTO r = new CategoriaResponseDTO();
        r.setId(c.getId());
        r.setNome(c.getNome());
        r.setDescricao(c.getDescricao());
        return r;
    }
}