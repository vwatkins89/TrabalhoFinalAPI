package br.com.serratec.controller;

import br.com.serratec.dto.ItemPedidoRequestDTO;
import br.com.serratec.dto.ItemPedidoResponseDTO;
import br.com.serratec.entity.ItemPedido;
import br.com.serratec.service.ItemPedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/itens-pedido")
public class ItemPedidoController {

    private final ItemPedidoService service;

    public ItemPedidoController(ItemPedidoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ItemPedidoResponseDTO> criar(@Valid @RequestBody ItemPedidoRequestDTO dto) {
        ItemPedido criado = service.criar(dto);
        ItemPedidoResponseDTO response = service.toResponse(criado);
        URI location = URI.create("/api/itens-pedido/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ItemPedidoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemPedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        ItemPedido item = service.buscarPorId(id);
        return ResponseEntity.ok(service.toResponse(item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemPedidoResponseDTO> atualizar(@PathVariable Long id,
                                                           @Valid @RequestBody ItemPedidoRequestDTO dto) {
        ItemPedido atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(service.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}