package br.com.serratec.controller;

import br.com.serratec.dto.ClienteRequestDTO;
import br.com.serratec.dto.ClienteResponseDTO;
import br.com.serratec.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criar(@Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO criado = clienteService.criar(dto);
        // monta location /api/clientes/{id}
        URI location = URI.create(String.format("/api/clientes/%d", criado.getId()));
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        ClienteResponseDTO dto = clienteService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    // endpoint opcional para listar todos os clientes
    @GetMapping
    public ResponseEntity<?> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }
}