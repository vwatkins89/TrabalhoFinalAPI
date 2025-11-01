package br.com.serratec.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.serratec.dto.EnderecoRequestDTO;
import br.com.serratec.dto.EnderecoResponseDTO;
import br.com.serratec.dto.ViaCepResponseDTO;
import br.com.serratec.entity.Cliente;
import br.com.serratec.entity.Endereco;
import br.com.serratec.exception.ResourceNotFoundException;
import br.com.serratec.repository.ClienteRepository;
import br.com.serratec.repository.EnderecoRepository;
import jakarta.transaction.Transactional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ViaCepService viaCepService;

    public List<EnderecoResponseDTO> listarPorCliente(Long clienteId) {
        return enderecoRepository.findByClienteId(clienteId)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public EnderecoResponseDTO buscarPorId(Long id) {
        Endereco e = enderecoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com id: " + id));
        return toResponse(e);
    }

    @Transactional
    public EnderecoResponseDTO criar(EnderecoRequestDTO dto) {
        Endereco e = new Endereco();

        
        String cepLimpo = dto.getCep().trim().replaceAll("\\D", "");
        e.setCep(cepLimpo);

        
        ViaCepResponseDTO viaCep = viaCepService.consultarCep(cepLimpo);

        
        e.setLogradouro(viaCep.getLogradouro());
        e.setBairro(viaCep.getBairro());
        e.setCidade(viaCep.getLocalidade());
        e.setEstado(viaCep.getUf());

       
        e.setComplemento(dto.getComplemento());
        e.setNumero(dto.getNumero());

       
        if (dto.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + dto.getClienteId()));
            e.setCliente(cliente);
        }

        Endereco salvo = enderecoRepository.save(e);
        return toResponse(salvo);
    }


    @Transactional
    public EnderecoResponseDTO atualizar(Long id, EnderecoRequestDTO dto) {
        Endereco e = enderecoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com id: " + id));

        e.setCep(dto.getCep().trim());
        e.setLogradouro(dto.getLogradouro());
        e.setComplemento(dto.getComplemento());
        e.setBairro(dto.getBairro());
        e.setCidade(dto.getCidade());
        e.setEstado(dto.getEstado());
        e.setNumero(dto.getNumero());

        if (dto.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + dto.getClienteId()));
            e.setCliente(cliente);
        } else {
            e.setCliente(null);
        }

        Endereco atualizado = enderecoRepository.save(e);
        return toResponse(atualizado);
    }

    @Transactional
    public void excluir(Long id) {
        Endereco e = enderecoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com id: " + id));
        enderecoRepository.delete(e);
    }

    private EnderecoResponseDTO toResponse(Endereco e) {
        EnderecoResponseDTO dto = new EnderecoResponseDTO();
        dto.setId(e.getId());
        dto.setCep(e.getCep());
        dto.setLogradouro(e.getLogradouro());
        dto.setComplemento(e.getComplemento());
        dto.setBairro(e.getBairro());
        dto.setCidade(e.getCidade());
        dto.setEstado(e.getEstado());
        dto.setNumero(e.getNumero());
        if (e.getCliente() != null) {
            dto.setClienteId(e.getCliente().getId());
            dto.setClienteNome(e.getCliente().getNome());
        }
        return dto;
    }
}