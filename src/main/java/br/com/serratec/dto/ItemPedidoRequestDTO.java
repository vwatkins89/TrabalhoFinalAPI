package br.com.serratec.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ItemPedidoRequestDTO implements Serializable {

    @NotNull(message = "quantidade é obrigatória")
    @Min(value = 1, message = "quantidade mínima é 1")
    private Integer quantidade;

    @NotNull(message = "precoUnitario é obrigatório")
    private BigDecimal precoUnitario;

    @NotNull(message = "produtoId é obrigatório")
    private Long produtoId;

    @NotNull(message = "pedidoId é obrigatório")
    private Long pedidoId;

    public ItemPedidoRequestDTO() {}

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }
}