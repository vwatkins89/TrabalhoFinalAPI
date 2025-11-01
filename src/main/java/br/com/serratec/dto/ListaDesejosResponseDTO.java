package br.com.serratec.dto;

import java.io.Serializable;
import java.util.Set;

public class ListaDesejosResponseDTO implements Serializable {

    private Long id;
    private Long clienteId;
    private String clienteNome;
    private Set<Long> produtoIds;
    private Set<String> produtoNomes;

    public ListaDesejosResponseDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public Set<Long> getProdutoIds() {
        return produtoIds;
    }

    public void setProdutoIds(Set<Long> produtoIds) {
        this.produtoIds = produtoIds;
    }

    public Set<String> getProdutoNomes() {
        return produtoNomes;
    }

    public void setProdutoNomes(Set<String> produtoNomes) {
        this.produtoNomes = produtoNomes;
    }
}