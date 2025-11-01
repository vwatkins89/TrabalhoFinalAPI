package br.com.serratec.repository;

import br.com.serratec.entity.UsuarioPerfil;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, Long> {
    List<UsuarioPerfil> findByUsuarioId(Long usuarioId);
    List<UsuarioPerfil> findByPerfilId(Long perfilId);
    boolean existsByUsuarioIdAndPerfilId(Long usuarioId, Long perfilId);
}