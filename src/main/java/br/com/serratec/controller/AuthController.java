package br.com.serratec.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import br.com.serratec.dto.RespostaAuthDTO;
import br.com.serratec.dto.LoginDTO;
import br.com.serratec.dto.RegistroDTO;
import br.com.serratec.entity.Usuario;
import br.com.serratec.security.JwtUtil;
import br.com.serratec.service.UsuarioService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<RespostaAuthDTO> login(@RequestBody LoginDTO login) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(login.username(), login.password())
        );

        var principal = auth.getPrincipal();
        String username;
        List<String> roles;
        if (principal instanceof org.springframework.security.core.userdetails.User u) {
            username = u.getUsername();
            roles = u.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        } else {
            username = login.username();
            roles = List.of();
        }

        String token = jwtUtil.gerarToken(username, roles);
        return ResponseEntity.ok(new RespostaAuthDTO(token, "Bearer"));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registrar(@RequestBody RegistroDTO req) {
        Usuario created = usuarioService.registrar(req.nome(), req.email(), req.password(), req.roles());
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado: " + created.getEmail());
    }
}