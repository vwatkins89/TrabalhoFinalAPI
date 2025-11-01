package br.com.serratec.service;

import br.com.serratec.entity.Usuario;
import br.com.serratec.entity.Perfil;
import br.com.serratec.entity.UsuarioPerfil;
import br.com.serratec.repository.UsuarioRepository;
import java.util.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Usuario registrar(String nome, String email, String rawPassword, Set<String> roles) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email é obrigatório");
        if (rawPassword == null || rawPassword.isBlank()) throw new IllegalArgumentException("Senha é obrigatória");

        if (repo.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Usuário com esse email já existe");
        }

        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setSenha(encoder.encode(rawPassword));

        Set<UsuarioPerfil> ups = new HashSet<>();
        if (roles == null || roles.isEmpty()) {
            roles = Set.of("ROLE_USER");
        }

        for (String roleName : roles) {
            Perfil perfil = em.createQuery("select p from Perfil p where p.nome = :nome", Perfil.class)
                    .setParameter("nome", roleName)
                    .getResultStream()
                    .findFirst()
                    .orElseGet(() -> {
                        Perfil np = new Perfil();
                        np.setNome(roleName);
                        em.persist(np);
                        return np;
                    });

            
            UsuarioPerfil up = new UsuarioPerfil();
            up.setUsuario(u);
            up.setPerfil(perfil);

           
            ups.add(up);
          
            try {
                if (perfil.getUsuarioPerfis() != null) perfil.getUsuarioPerfis().add(up);
            } catch (Exception ignored) {}

           
        }

        u.setUsuarioPerfis(ups);
      
        return repo.save(u);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return repo.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmailComPerfis(String email) {
        return repo.findByEmailWithPerfis(email);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return repo.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return repo.findAll();
    }

    @Transactional
    public Usuario atualizarDados(Long id, String novoNome, String novoEmail) {
        Usuario u = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        if (novoEmail != null && !novoEmail.isBlank() && !novoEmail.equals(u.getEmail())) {
            repo.findByEmail(novoEmail).ifPresent(existing -> {
                if (!existing.getId().equals(id)) throw new IllegalStateException("Email já em uso por outro usuário");
            });
            u.setEmail(novoEmail);
        }
        if (novoNome != null && !novoNome.isBlank()) {
            u.setNome(novoNome);
        }
        return repo.save(u);
    }

    @Transactional
    public void atualizarSenha(Long id, String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) throw new IllegalArgumentException("Senha inválida");
        Usuario u = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        u.setSenha(encoder.encode(rawPassword));
        repo.save(u);
    }

    @Transactional
    public Usuario adicionarRole(Long id, String roleName) {
        Usuario u = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        boolean jaTem = u.getUsuarioPerfis().stream()
                .anyMatch(up -> up.getPerfil() != null && roleName.equals(up.getPerfil().getNome()));
        if (jaTem) return u;

        Perfil perfil = em.createQuery("select p from Perfil p where p.nome = :nome", Perfil.class)
                .setParameter("nome", roleName)
                .getResultStream()
                .findFirst()
                .orElseGet(() -> {
                    Perfil np = new Perfil();
                    np.setNome(roleName);
                    em.persist(np);
                    return np;
                });

        UsuarioPerfil up = new UsuarioPerfil();
        up.setUsuario(u);
        up.setPerfil(perfil);

        // adiciona nas coleções em memória
        u.getUsuarioPerfis().add(up);
        try {
            if (perfil.getUsuarioPerfis() != null) perfil.getUsuarioPerfis().add(up);
        } catch (Exception ignored) {}

        return repo.save(u);
    }

    @Transactional
    public Usuario removerRole(Long id, String roleName) {
        Usuario u = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

   
        u.getUsuarioPerfis().removeIf(up -> up.getPerfil() != null && roleName.equals(up.getPerfil().getNome()));

        return repo.save(u);
    }

    @Transactional
    public void excluirPorId(Long id) {
        if (!repo.existsById(id)) throw new NoSuchElementException("Usuário não encontrado");
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        return repo.findByEmail(email).isPresent();
    }

    @Transactional(readOnly = true)
    public Set<String> obterRolesComoStrings(Long id) {
        Usuario u = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        return u.getRolesAsStrings();
    }
}