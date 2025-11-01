package br.com.serratec.dto;

import java.io.Serializable;

public class PerfilResponseDTO implements Serializable {

    private Long id;
    private String nome;

    public PerfilResponseDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}