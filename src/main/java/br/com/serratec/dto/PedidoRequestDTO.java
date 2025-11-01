package br.com.serratec.dto;

import java.util.List;

import br.com.serratec.enums.StatusPedido;

public class PedidoRequestDTO {

    private Long clienteId;

    private Long cupomId;

    private StatusPedido status;

    private List<ItemPedidoRequestDTO> produtos;

    public PedidoRequestDTO() {}

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getCupomId() {
        return cupomId;
    }

    public void setCupomId(Long cupomId) {
        this.cupomId = cupomId;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public List<ItemPedidoRequestDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ItemPedidoRequestDTO> produtos) {
        this.produtos = produtos;
    }
}