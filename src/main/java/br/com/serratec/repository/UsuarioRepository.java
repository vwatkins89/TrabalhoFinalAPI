package br.com.serratec.repository;

import br.com.serratec.entity.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    @Query("select u from Usuario u left join fetch u.usuarioPerfis up left join fetch up.perfil where u.email = :email")
    Optional<Usuario> findByEmailWithPerfis(@Param("email") String email);
}