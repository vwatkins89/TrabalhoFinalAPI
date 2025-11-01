package br.com.serratec.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PositiveOrZero;

public class ProdutoRequestDTO implements Serializable {

    @NotBlank(message = "nome é obrigatório")
    @Size(max = 255)
    private String nome;

    @Size(max = 2000)
    private String descricao;

    @NotNull(message = "preco é obrigatório")
    @PositiveOrZero(message = "preco deve ser maior ou igual a zero")
    private BigDecimal preco;

    private Long categoriaId;

    public ProdutoRequestDTO() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}