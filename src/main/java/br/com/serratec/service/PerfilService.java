package br.com.serratec.service;

import br.com.serratec.dto.PerfilRequestDTO;
import br.com.serratec.dto.PerfilResponseDTO;
import br.com.serratec.entity.Perfil;
import br.com.serratec.exception.ResourceNotFoundException;
import br.com.serratec.repository.PerfilRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    public List<PerfilResponseDTO> listarTodos() {
        return perfilRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public PerfilResponseDTO buscarPorId(Long id) {
        Perfil p = perfilRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com id: " + id));
        return toResponse(p);
    }

    @Transactional
    public PerfilResponseDTO criar(PerfilRequestDTO dto) {
        if (perfilRepository.existsByNome(dto.getNome().trim())) {
            throw new IllegalArgumentException("Perfil com esse nome já existe");
        }
        Perfil p = new Perfil();
        p.setNome(dto.getNome().trim());
        Perfil salvo = perfilRepository.save(p);
        return toResponse(salvo);
    }

    @Transactional
    public PerfilResponseDTO atualizar(Long id, PerfilRequestDTO dto) {
        Perfil p = perfilRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com id: " + id));
        if (!p.getNome().equals(dto.getNome().trim()) && perfilRepository.existsByNome(dto.getNome().trim())) {
            throw new IllegalArgumentException("Perfil com esse nome já existe");
        }
        p.setNome(dto.getNome().trim());
        Perfil atualizado = perfilRepository.save(p);
        return toResponse(atualizado);
    }

    @Transactional
    public void excluir(Long id) {
        Perfil p = perfilRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com id: " + id));
        perfilRepository.delete(p);
    }

    private PerfilResponseDTO toResponse(Perfil p) {
        PerfilResponseDTO dto = new PerfilResponseDTO();
        dto.setId(p.getId());
        dto.setNome(p.getNome());
        return dto;
    }
}