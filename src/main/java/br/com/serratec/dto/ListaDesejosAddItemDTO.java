package br.com.serratec.dto;

import java.io.Serializable;
import jakarta.validation.constraints.NotNull;

public class ListaDesejosAddItemDTO implements Serializable {

    @NotNull(message = "clienteId é obrigatório")
    private Long clienteId;

    @NotNull(message = "produtoId é obrigatório")
    private Long produtoId;

    public ListaDesejosAddItemDTO() {}

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }
}