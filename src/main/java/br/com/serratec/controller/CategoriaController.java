package br.com.serratec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.serratec.dto.CategoriaRequestDTO;
import br.com.serratec.dto.CategoriaResponseDTO;
import br.com.serratec.services.CategoriaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@Valid @RequestBody CategoriaRequestDTO categoriaRequest) {
        var criado = service. criar(categoriaRequest); //var ja entende o tipo do metodo que ele esta sendo usado
        return ResponseEntity.status(201).body(criado);
    }
    
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> buscarTodos() {
    	var categorias = service.buscarTodos(); 
    	return ResponseEntity.status(201).body(categorias);       
    }
    

    @GetMapping
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable Long id) {
        var dto = service.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO categoriaRequest) {
        var atualizado = service.atualizar(id, categoriaRequest);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping
    public ResponseEntity<CategoriaResponseDTO> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}