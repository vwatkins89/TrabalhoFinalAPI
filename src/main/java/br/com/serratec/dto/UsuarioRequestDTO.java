package br.com.serratec.dto;

import java.io.Serializable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioRequestDTO implements Serializable {

    @NotBlank(message = "nome é obrigatório")
    @Size(max = 255)
    private String nome;

    @NotBlank(message = "email é obrigatório")
    @Email(message = "email deve ser válido")
    @Size(max = 255)
    private String email;

    @NotBlank(message = "senha é obrigatória")
    @Size(min = 6, max = 255, message = "senha deve ter ao menos 6 caracteres")
    private String senha;

    // opcional: ROLE_ADMIN ou ROLE_USER
    @Size(max = 100)
    private String perfilNome;

    public UsuarioRequestDTO() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPerfilNome() {
        return perfilNome;
    }

    public void setPerfilNome(String perfilNome) {
        this.perfilNome = perfilNome;
    }
}