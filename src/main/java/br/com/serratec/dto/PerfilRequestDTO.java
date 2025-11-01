package br.com.serratec.dto;

import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PerfilRequestDTO implements Serializable {

    @NotBlank(message = "nome é obrigatório")
    @Size(max = 100, message = "nome pode ter no máximo 100 caracteres")
    private String nome;

    public PerfilRequestDTO() {}

    public PerfilRequestDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}