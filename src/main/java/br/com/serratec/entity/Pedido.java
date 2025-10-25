package br.com.serratec.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.com.serratec.enums.StatusPedido;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Pedido {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String numeroPedido;
	
	
	@ManyToOne (fetch = FetchType.LAZY) //Lazy é mais rápido, só carrega quando é chamado               
	@JoinColumn (name = "cliente_id", nullable = false) //nullable = false é para atrelar um novo pedido a um cliente
	private Cliente cliente;
	
	//para passar a data exata da criação do pedido
	private LocalDateTime dataCriacao = LocalDateTime.now();
	
	@Enumerated(EnumType.STRING) //String para mostrar o texto no status, 0 aguardando pagamento e 1 pago
	private StatusPedido status = StatusPedido.AGUARDANDO_PAGAMENTO;
	
	//Arredonda a casa decimal
	private BigDecimal total;
	
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true) //sincroniza com o banco uma exclusão
	private List<ItemPedido> itens;

	public Pedido() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public StatusPedido getStatus() {
		return status;
	}

	public void setStatus(StatusPedido status) {
		this.status = status;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedido> itens) {
		this.itens = itens;
	}
	
	
}

