package br.com.serratec.service;

import br.com.serratec.dto.UsuarioPerfilRequestDTO;
import br.com.serratec.dto.UsuarioPerfilResponseDTO;
import br.com.serratec.entity.Perfil;
import br.com.serratec.entity.Usuario;
import br.com.serratec.entity.UsuarioPerfil;
import br.com.serratec.exception.ResourceNotFoundException;
import br.com.serratec.repository.PerfilRepository;
import br.com.serratec.repository.UsuarioPerfilRepository;
import br.com.serratec.repository.UsuarioRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class UsuarioPerfilService {

    @Autowired
    private UsuarioPerfilRepository usuarioPerfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    public List<UsuarioPerfilResponseDTO> listarPorUsuario(Long usuarioId) {
        return usuarioPerfilRepository.findByUsuarioId(usuarioId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public UsuarioPerfilResponseDTO buscarPorId(Long id) {
        UsuarioPerfil up = usuarioPerfilRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("UsuarioPerfil não encontrado com id: " + id));
        return toResponse(up);
    }

    @Transactional
    public UsuarioPerfilResponseDTO criar(UsuarioPerfilRequestDTO dto) {
        Usuario usuario = resolveUsuario(dto);
        Perfil perfil = resolvePerfil(dto);

        if (usuarioPerfilRepository.existsByUsuarioIdAndPerfilId(usuario.getId(), perfil.getId())) {
            throw new IllegalArgumentException("Vínculo já existe entre usuário e perfil");
        }

        UsuarioPerfil up = new UsuarioPerfil();
        up.setUsuario(usuario);
        up.setPerfil(perfil);

        UsuarioPerfil salvo = usuarioPerfilRepository.save(up);
        return toResponse(salvo);
    }

    @Transactional
    public void excluir(Long id) {
        UsuarioPerfil up = usuarioPerfilRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("UsuarioPerfil não encontrado com id: " + id));
        usuarioPerfilRepository.delete(up);
    }

    private Usuario resolveUsuario(UsuarioPerfilRequestDTO dto) {
        if (dto.getUsuarioId() != null) {
            return usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + dto.getUsuarioId()));
        } else if (dto.getUsuarioEmail() != null && !dto.getUsuarioEmail().isBlank()) {
            return usuarioRepository.findByEmail(dto.getUsuarioEmail().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com email: " + dto.getUsuarioEmail()));
        } else {
            throw new IllegalArgumentException("É necessário informar usuarioId ou usuarioEmail");
        }
    }

    private Perfil resolvePerfil(UsuarioPerfilRequestDTO dto) {
        if (dto.getPerfilId() != null) {
            return perfilRepository.findById(dto.getPerfilId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com id: " + dto.getPerfilId()));
        } else if (dto.getPerfilNome() != null && !dto.getPerfilNome().isBlank()) {
            return perfilRepository.findByNome(dto.getPerfilNome().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com nome: " + dto.getPerfilNome()));
        } else {
            throw new IllegalArgumentException("É necessário informar perfilId ou perfilNome");
        }
    }

    private UsuarioPerfilResponseDTO toResponse(UsuarioPerfil up) {
        UsuarioPerfilResponseDTO dto = new UsuarioPerfilResponseDTO();
        dto.setId(up.getId());
        if (up.getUsuario() != null) {
            dto.setUsuarioId(up.getUsuario().getId());
            dto.setUsuarioNome(up.getUsuario().getNome());
            dto.setUsuarioEmail(up.getUsuario().getEmail());
        }
        if (up.getPerfil() != null) {
            dto.setPerfilId(up.getPerfil().getId());
            dto.setPerfilNome(up.getPerfil().getNome());
        }
        return dto;
    }
}