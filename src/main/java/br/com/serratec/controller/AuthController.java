package br.com.serratec.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.serratec.dto.request.AutenticacaoRequestDTO;
import br.com.serratec.dto.response.AutenticacaoResponseDTO;
import br.com.serratec.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public ResponseEntity<Void> registrar(@Valid @RequestBody Object usuarioRegistroRequest) {
        service.registrar(usuarioRegistroRequest);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AutenticacaoResponseDTO> login(@Valid @RequestBody AutenticacaoRequestDTO requisicao) {
        AutenticacaoResponseDTO resposta = service.autenticar(requisicao);
        return ResponseEntity.ok(resposta);
    }
}