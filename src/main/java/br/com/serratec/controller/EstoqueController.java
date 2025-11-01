package br.com.serratec.controller;

import br.com.serratec.dto.EstoqueRequestDTO;
import br.com.serratec.dto.EstoqueResponseDTO;
import br.com.serratec.entity.Estoque;
import br.com.serratec.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estoques")
public class EstoqueController {

    private final EstoqueService service;

    public EstoqueController(EstoqueService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EstoqueResponseDTO> criar(@Valid @RequestBody EstoqueRequestDTO dto) {
        Estoque estoque = service.criar(dto);
        return ResponseEntity.status(201).body(service.toResponse(estoque));
    }

    @GetMapping
    public ResponseEntity<List<EstoqueResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstoqueResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.toResponse(service.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstoqueResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody EstoqueRequestDTO dto) {
        Estoque atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(service.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}