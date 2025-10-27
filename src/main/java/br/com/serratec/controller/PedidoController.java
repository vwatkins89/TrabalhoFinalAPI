package br.com.serratec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.serratec.dto.PedidoRequestDTO;
import br.com.serratec.dto.PedidoResponseDTO;
import br.com.serratec.dto.StatusRequestDTO;
import br.com.serratec.entity.Pedido;
import br.com.serratec.service.PedidoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @GetMapping
    public ResponseEntity<?> listar(@RequestParam(required = false) String numero) {
        if (numero == null || numero.isBlank()) {
            List<Pedido> lista = service.listarTodos();
            return ResponseEntity.ok(lista);
        }
        PedidoResponseDTO pedido = service.buscarPorNumero(numero);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        PedidoResponseDTO dto = service.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criarPedido(@Valid @RequestBody PedidoRequestDTO pedidoRequest) {
        PedidoResponseDTO criado = service.criarPedido(pedidoRequest);
        return ResponseEntity.status(201).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> atualizarPedido(@PathVariable Long id,
            @Valid @RequestBody PedidoRequestDTO pedidoRequest) {
        PedidoResponseDTO atualizado = service.atualizarPedido(id, pedidoRequest);
        return ResponseEntity.ok(atualizado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long id,
            @Valid @RequestBody StatusRequestDTO statusRequest) {
        PedidoResponseDTO atualizado = service.atualizarStatus(id, statusRequest.getStatus());
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluirPedido(id);
        return ResponseEntity.noContent().build();
    }
}