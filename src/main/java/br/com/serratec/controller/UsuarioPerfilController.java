package br.com.serratec.controller;

import br.com.serratec.dto.UsuarioPerfilRequestDTO;
import br.com.serratec.dto.UsuarioPerfilResponseDTO;
import br.com.serratec.service.UsuarioPerfilService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/usuario-perfis")
public class UsuarioPerfilController {

    @Autowired
    private UsuarioPerfilService usuarioPerfilService;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<UsuarioPerfilResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(usuarioPerfilService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioPerfilResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioPerfilService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioPerfilResponseDTO> criar(@Valid @RequestBody UsuarioPerfilRequestDTO dto) {
        UsuarioPerfilResponseDTO criado = usuarioPerfilService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        usuarioPerfilService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}