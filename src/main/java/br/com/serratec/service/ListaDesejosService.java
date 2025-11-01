package br.com.serratec.service;

import br.com.serratec.dto.ListaDesejosAddItemDTO;
import br.com.serratec.dto.ListaDesejosResponseDTO;
import br.com.serratec.entity.Cliente;
import br.com.serratec.entity.ListaDesejos;
import br.com.serratec.entity.Produto;
import br.com.serratec.exception.ResourceNotFoundException;
import br.com.serratec.repository.ClienteRepository;
import br.com.serratec.repository.ListaDesejosRepository;
import br.com.serratec.repository.ProdutoRepository;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class ListaDesejosService {

    @Autowired
    private ListaDesejosRepository listaDesejosRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public ListaDesejosResponseDTO buscarPorCliente(Long clienteId) {
        ListaDesejos lista = listaDesejosRepository.findByClienteId(clienteId)
            .orElseThrow(() -> new ResourceNotFoundException("Lista de desejos não encontrada para cliente id: " + clienteId));
        return toResponse(lista);
    }

    @Transactional
    public ListaDesejosResponseDTO criarParaCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + clienteId));
        if (listaDesejosRepository.existsByClienteId(clienteId)) {
            throw new IllegalArgumentException("Cliente já possui lista de desejos");
        }
        ListaDesejos lista = new ListaDesejos();
        lista.setCliente(cliente);
        ListaDesejos salvo = listaDesejosRepository.save(lista);
        return toResponse(salvo);
    }

    @Transactional
    public ListaDesejosResponseDTO adicionarItem(ListaDesejosAddItemDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + dto.getClienteId()));
        Produto produto = produtoRepository.findById(dto.getProdutoId())
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + dto.getProdutoId()));

        ListaDesejos lista = listaDesejosRepository.findByClienteId(cliente.getId())
            .orElseGet(() -> {
                ListaDesejos nova = new ListaDesejos();
                nova.setCliente(cliente);
                return listaDesejosRepository.save(nova);
            });

        // evitar duplicata no Set (equals/hashCode de Produto deve ser consistente)
        lista.getProdutos().add(produto);
        ListaDesejos salvo = listaDesejosRepository.save(lista);
        return toResponse(salvo);
    }

    @Transactional
    public ListaDesejosResponseDTO removerItem(Long clienteId, Long produtoId) {
        ListaDesejos lista = listaDesejosRepository.findByClienteId(clienteId)
            .orElseThrow(() -> new ResourceNotFoundException("Lista de desejos não encontrada para cliente id: " + clienteId));
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + produtoId));
        lista.getProdutos().remove(produto);
        ListaDesejos salvo = listaDesejosRepository.save(lista);
        return toResponse(salvo);
    }

    @Transactional
    public void excluirLista(Long clienteId) {
        ListaDesejos lista = listaDesejosRepository.findByClienteId(clienteId)
            .orElseThrow(() -> new ResourceNotFoundException("Lista de desejos não encontrada para cliente id: " + clienteId));
        listaDesejosRepository.delete(lista);
    }

    private ListaDesejosResponseDTO toResponse(ListaDesejos lista) {
        ListaDesejosResponseDTO dto = new ListaDesejosResponseDTO();
        dto.setId(lista.getId());
        dto.setClienteId(lista.getCliente() != null ? lista.getCliente().getId() : null);
        dto.setClienteNome(lista.getCliente() != null ? lista.getCliente().getNome() : null);
        Set<Long> ids = lista.getProdutos().stream().map(Produto::getId).collect(Collectors.toSet());
        Set<String> nomes = lista.getProdutos().stream().map(Produto::getNome).collect(Collectors.toSet());
        dto.setProdutoIds(ids);
        dto.setProdutoNomes(nomes);
        return dto;
    }
}