package br.com.serratec.services;

import br.com.serratec.entity.Cliente;
import br.com.serratec.entity.ItemPedido;
import br.com.serratec.entity.Pedido;
import br.com.serratec.entity.Produto;
import br.com.serratec.exception.NotFoundException;
import br.com.serratec.repository.ClienteRepository;
import br.com.serratec.repository.PedidoRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ItemPedidoService{

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private PedidoService ordemService;

    private Cliente cliente;
    private Produto produto1;
    private Produto produto2;

    @BeforeEach
    void setup() {
        cliente = new Cliente();
        cliente.setId(1L);

        produto1 = new Produto();
        produto1.setId(10L);
        produto1.setPreco(new BigDecimal("100.00"));

        produto2 = new Produto();
        produto2.setId(20L);
        produto2.setPreco(new BigDecimal("50.00"));
    }

    @Test
    void criar_DeveCalcularCorretamenteOTotalComEDescontos() {
        ItemPedido item1 = new ItemPedido();
        item1.setProdutoId(10L);
        item1.setQuantidade(2);
        item1.setPreco(produto1.getPreco()); 
        item1.setDesconto(BigDecimal.ZERO);

        ItemPedido item2 = new ItemPedido();
        item2.setProdutoId(20L);
        item2.setQuantidade(3);
        item2.setPreco(produto2.getPreco());
        item2.setDesconto(new BigDecimal("10.00"));

        Pedido pedido = new Pedido();
        pedido.setItens(Arrays.asList(item1, item2));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido pedidoSalvo = ordemService.criar(pedido, 1L);

        assertEquals(new BigDecimal("200.00").setScale(2), item1.getSubtotal());
        assertEquals(new BigDecimal("140.00").setScale(2), item2.getSubtotal());
        assertEquals(new BigDecimal("340.00").setScale(2), pedidoSalvo.getTotal());
        assertEquals("CRIADO", pedidoSalvo.getStatus());
    }

    @Test
    void criar_DeveLancarExcecaoQuandoClienteNaoEncontrado() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            ordemService.criar(new Pedido(), 99L);
        });
    }
}