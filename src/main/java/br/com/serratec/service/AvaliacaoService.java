package br.com.serratec.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.serratec.dto.AvaliacaoRequestDTO;
import br.com.serratec.dto.AvaliacaoResponseDTO;
import br.com.serratec.entity.Avaliacao;
import br.com.serratec.entity.Cliente;
import br.com.serratec.entity.Produto;
import br.com.serratec.exception.ResourceNotFoundException;
import br.com.serratec.repository.AvaliacaoRepository;
import br.com.serratec.repository.ClienteRepository;
import br.com.serratec.repository.ProdutoRepository;
import jakarta.transaction.Transactional;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;
    
    public List<AvaliacaoResponseDTO> listarTodos() {
        return avaliacaoRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<AvaliacaoResponseDTO> listarPorProduto(Long produtoId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByProdutoId(produtoId);
        return avaliacoes.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<AvaliacaoResponseDTO> listarPorCliente(Long clienteId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByClienteId(clienteId);
        return avaliacoes.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public AvaliacaoResponseDTO buscarPorId(Long id) {
        Avaliacao a = avaliacaoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada com id: " + id));
        return toResponse(a);
    }

    @Transactional
    public AvaliacaoResponseDTO criar(AvaliacaoRequestDTO dto) {
        Produto produto = produtoRepository.findById(dto.getProdutoId())
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + dto.getProdutoId()));
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + dto.getClienteId()));

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNota(dto.getNota());
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setProduto(produto);
        avaliacao.setCliente(cliente);

        Avaliacao salvo = avaliacaoRepository.save(avaliacao);
        return toResponse(salvo);
    }

    @Transactional
    public AvaliacaoResponseDTO atualizar(Long id, AvaliacaoRequestDTO dto) {
        Avaliacao a = avaliacaoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada com id: " + id));

        Produto produto = produtoRepository.findById(dto.getProdutoId())
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + dto.getProdutoId()));
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + dto.getClienteId()));

        a.setNota(dto.getNota());
        a.setComentario(dto.getComentario());
        a.setProduto(produto);
        a.setCliente(cliente);

        Avaliacao atualizado = avaliacaoRepository.save(a);
        return toResponse(atualizado);
    }

    @Transactional
    public void excluir(Long id) {
        Avaliacao a = avaliacaoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada com id: " + id));
        avaliacaoRepository.delete(a);
    }

    private AvaliacaoResponseDTO toResponse(Avaliacao a) {
        AvaliacaoResponseDTO dto = new AvaliacaoResponseDTO();
        dto.setId(a.getId());
        dto.setNota(a.getNota());
        dto.setComentario(a.getComentario());
        dto.setProdutoId(a.getProduto() != null ? a.getProduto().getId() : null);
        dto.setProdutoNome(a.getProduto() != null ? a.getProduto().getNome() : null);
        dto.setClienteId(a.getCliente() != null ? a.getCliente().getId() : null);
        dto.setClienteNome(a.getCliente() != null ? a.getCliente().getNome() : null);
        return dto;
    }
}