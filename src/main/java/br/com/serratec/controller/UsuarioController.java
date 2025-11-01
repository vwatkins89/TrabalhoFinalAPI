package br.com.serratec.controller;

import br.com.serratec.dto.RegistroDTO;
import br.com.serratec.dto.RespostaAuthDTO;
import br.com.serratec.service.UsuarioService;
import br.com.serratec.entity.Usuario;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    
    public static record AtualizaUsuarioDTO(String nome, String email) {}
    public static record AtualizaSenhaDTO(String novaSenha) {}

    
    @PostMapping
    public ResponseEntity<String> criar(@RequestBody RegistroDTO registro) {
        Usuario criado = usuarioService.registrar(registro.nome(), registro.email(), registro.password(), registro.roles());
        URI uri = URI.create("/usuarios/" + criado.getId());
        return ResponseEntity.created(uri).body("Usuário criado: " + criado.getEmail());
    }

    
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        List<Usuario> todos = usuarioService.listarTodos();
        return ResponseEntity.ok(todos);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarDados(@PathVariable Long id, @RequestBody AtualizaUsuarioDTO dto) {
        Usuario atualizado = usuarioService.atualizarDados(id, dto.nome(), dto.email());
        return ResponseEntity.ok(atualizado);
    }

    
    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> atualizarSenha(@PathVariable Long id, @RequestBody AtualizaSenhaDTO dto) {
        usuarioService.atualizarSenha(id, dto.novaSenha());
        return ResponseEntity.noContent().build();
    }

  
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        usuarioService.excluirPorId(id);
        return ResponseEntity.noContent().build();
    }
}