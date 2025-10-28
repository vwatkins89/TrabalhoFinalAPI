package br.com.serratec.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class PedidoRequestDTO {

    @NotNull
    private Long clienteId;
    
    @NotNull
    private BigDecimal total;

    @NotEmpty
    private List<ItemPedidoRequestDTO> itens;

  
    public PedidoRequestDTO() {}

  
    public BigDecimal getTotal() {
		return total;
	}


	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemPedidoRequestDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoRequestDTO> itens) {
        this.itens = itens;
    }
}