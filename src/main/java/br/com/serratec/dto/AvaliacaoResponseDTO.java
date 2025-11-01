package br.com.serratec.dto;

import java.io.Serializable;

public class AvaliacaoResponseDTO implements Serializable {

    private Long id;
    private Integer nota;
    private String comentario;
    private Long produtoId;
    private String produtoNome;
    private Long clienteId;
    private String clienteNome;

    public AvaliacaoResponseDTO() {}

    public AvaliacaoResponseDTO(Long id, Integer nota, String comentario,
                                Long produtoId, String produtoNome,
                                Long clienteId, String clienteNome) {
        this.id = id;
        this.nota = nota;
        this.comentario = comentario;
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getProdutoNome() {
		return produtoNome;
	}

	public void setProdutoNome(String produtoNome) {
		this.produtoNome = produtoNome;
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

    
}