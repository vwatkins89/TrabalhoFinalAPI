package br.com.serratec.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class EstoqueRequestDTO {

    @NotNull
    @Min(0)
    private Integer quantidade;

    @NotNull
    private Long produtoId;

    public EstoqueRequestDTO() {}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Long getProdutoId() {
		return produtoId;
	}

	public void setProdutoId(Long produtoId) {
		this.produtoId = produtoId;
	}

    
}