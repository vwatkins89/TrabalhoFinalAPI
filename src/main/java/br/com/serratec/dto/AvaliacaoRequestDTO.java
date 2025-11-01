package br.com.serratec.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AvaliacaoRequestDTO implements Serializable {

    @NotNull(message = "nota é obrigatória")
    @Min(value = 1, message = "nota mínima é 1")
    @Max(value = 5, message = "nota máxima é 5")
    private Integer nota;

    @Size(max = 2000, message = "comentario pode ter no máximo 2000 caracteres")
    private String comentario;

    @NotNull(message = "produtoId é obrigatório")
    private Long produtoId;

    @NotNull(message = "clienteId é obrigatório")
    private Long clienteId;

    public AvaliacaoRequestDTO() {}

    public AvaliacaoRequestDTO(Integer nota, String comentario, Long produtoId, Long clienteId) {
        this.nota = nota;
        this.comentario = comentario;
        this.produtoId = produtoId;
        this.clienteId = clienteId;
    }

	public Integer getNota() {
		return nota;
	}

	public void setNota(Integer nota) {
		this.nota = nota;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Long getProdutoId() {
		return produtoId;
	}

	public void setProdutoId(Long produtoId) {
		this.produtoId = produtoId;
	}

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

    
}