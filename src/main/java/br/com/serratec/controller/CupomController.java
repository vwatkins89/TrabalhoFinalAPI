package br.com.serratec.controller;

import br.com.serratec.entity.Cupom;
import br.com.serratec.service.CupomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cupoms")
public class CupomController {

    private final CupomService service;

    public CupomController(CupomService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Cupom> criar(@Valid @RequestBody Cupom cupom) {
        Cupom criado = service.criar(cupom);
        URI location = URI.create("/api/cupoms/" + criado.getId());
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping
    public ResponseEntity<List<Cupom>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cupom> buscarPorId(@PathVariable Long id) {
        Cupom cupom = service.buscarPorId(id);
        return ResponseEntity.ok(cupom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cupom> atualizar(@PathVariable Long id, @Valid @RequestBody Cupom dados) {
        Cupom atualizado = service.atualizar(id, dados);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}