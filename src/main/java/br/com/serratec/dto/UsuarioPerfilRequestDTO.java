package br.com.serratec.dto;

import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Para criar um vínculo você pode enviar:
 * - usuarioId ou usuarioEmail (um dos dois obrigatórios)
 * - perfilId ou perfilNome (um dos dois obrigatórios)
 */
public class UsuarioPerfilRequestDTO implements Serializable {

    private Long usuarioId;
    private String usuarioEmail;

    private Long perfilId;
    private String perfilNome;

    public UsuarioPerfilRequestDTO() {}

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public Long getPerfilId() {
        return perfilId;
    }

    public void setPerfilId(Long perfilId) {
        this.perfilId = perfilId;
    }

    public String getPerfilNome() {
        return perfilNome;
    }

    public void setPerfilNome(String perfilNome) {
        this.perfilNome = perfilNome;
    }
}