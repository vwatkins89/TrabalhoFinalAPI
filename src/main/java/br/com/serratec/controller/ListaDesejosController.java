package br.com.serratec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.serratec.dto.ListaDesejosAddItemDTO;
import br.com.serratec.dto.ListaDesejosResponseDTO;
import br.com.serratec.service.ListaDesejosService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/listas-desejos")
public class ListaDesejosController {

    @Autowired
    private ListaDesejosService listaDesejosService;

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ListaDesejosResponseDTO> buscarPorCliente(@PathVariable("clienteId") Long clienteId) {
        return ResponseEntity.ok(listaDesejosService.buscarPorCliente(clienteId));
    }

    @PostMapping("/cliente/{clienteId}")
    public ResponseEntity<ListaDesejosResponseDTO> criarParaCliente(@PathVariable("clienteId") Long clienteId) {
        ListaDesejosResponseDTO criado = listaDesejosService.criarParaCliente(clienteId);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PostMapping("/itens")
    public ResponseEntity<ListaDesejosResponseDTO> adicionarItem(@Valid @RequestBody ListaDesejosAddItemDTO dto) {
        ListaDesejosResponseDTO resp = listaDesejosService.adicionarItem(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @DeleteMapping("/cliente/{clienteId}/itens/{produtoId}")
    public ResponseEntity<ListaDesejosResponseDTO> removerItem(@PathVariable("clienteId") Long clienteId,
                                                               @PathVariable("produtoId") Long produtoId) {
        ListaDesejosResponseDTO resp = listaDesejosService.removerItem(clienteId, produtoId);
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/cliente/{clienteId}")
    public ResponseEntity<Void> excluirLista(@PathVariable("clienteId") Long clienteId) {
        listaDesejosService.excluirLista(clienteId);
        return ResponseEntity.noContent().build();
    }
}