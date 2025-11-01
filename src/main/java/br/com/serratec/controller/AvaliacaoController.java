package br.com.serratec.controller;

import br.com.serratec.dto.AvaliacaoRequestDTO;
import br.com.serratec.dto.AvaliacaoResponseDTO;
import br.com.serratec.service.AvaliacaoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @GetMapping
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(avaliacaoService.listarTodos());
    }
    
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarPorProduto(@PathVariable Long produtoId) {
        List<AvaliacaoResponseDTO> lista = avaliacaoService.listarPorProduto(produtoId);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarPorCliente(@PathVariable Long clienteId) {
        List<AvaliacaoResponseDTO> lista = avaliacaoService.listarPorCliente(clienteId);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        AvaliacaoResponseDTO dto = avaliacaoService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @Transactional
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<AvaliacaoResponseDTO> criar(@Valid @RequestBody AvaliacaoRequestDTO dto) {
        AvaliacaoResponseDTO criado = avaliacaoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @Transactional
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<AvaliacaoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody AvaliacaoRequestDTO dto) {
        AvaliacaoResponseDTO atualizado = avaliacaoService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        avaliacaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}