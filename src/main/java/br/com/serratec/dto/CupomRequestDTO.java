package br.com.serratec.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CupomRequestDTO implements Serializable {

    @NotNull(message = "codigo é obrigatório")
    @Size(max = 100)
    private String codigo;

    @NotNull(message = "percentualDesconto é obrigatório")
    private BigDecimal percentualDesconto;

    @NotNull(message = "dataValidade é obrigatória")
    private LocalDate dataValidade;

    @NotNull(message = "ativo é obrigatório")
    private Boolean ativo;

    private Long clienteId;

    public CupomRequestDTO() {}

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getPercentualDesconto() {
        return percentualDesconto;
    }

    public void setPercentualDesconto(BigDecimal percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}