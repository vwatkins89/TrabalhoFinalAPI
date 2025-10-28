package br.com.serratec.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.serratec.dto.ClienteRequestDTO;
import br.com.serratec.dto.ClienteResponseDTO;
import br.com.serratec.dto.EnderecoResponseDTO;
import br.com.serratec.entity.Cliente;
import br.com.serratec.exception.RecursoNaoEncontradoException;
import br.com.serratec.mapper.ClienteMapper;
import br.com.serratec.repository.ClienteRepository;

@Service
public class ClienteService {

   
    private final String erroClienteNaoEncontrado = "Cliente com ID não foi encontrado.";

    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ClienteMapper clienteMapper; 
    
    @Autowired
    private EmailService emailService; 
    
    @Autowired
    private EnderecoService enderecoService; 
    
    
    @Transactional 
    public ClienteResponseDTO criarCliente(ClienteRequestDTO clienteRequestDTO) { 
        
        // Busca endereço 
        EnderecoResponseDTO endereco = enderecoService.buscarCep(clienteRequestDTO.getCep());
        
        
        Cliente cliente = clienteMapper.toEntity(clienteRequestDTO); 
        
        // Preenche endereço
        cliente.setLogradouro(endereco.logradouro());
        cliente.setBairro(endereco.bairro());
        cliente.setCidade(endereco.localidade());
        cliente.setEstado(endereco.uf());
        
        Cliente novoCliente = clienteRepository.save(cliente);

        
        emailService.enviarEmailCadastro(novoCliente);
        
        return clienteMapper.toResponseDto(novoCliente); 
    }

   
    @Transactional
    public ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO clienteRequestDTO) { 
        
      
        Cliente clienteExistente = clienteRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException(String.format(erroClienteNaoEncontrado, id)));
        
        // Mapeamento mantem o ID
        Cliente clienteAtualizado = clienteMapper.toEntity(clienteRequestDTO);
        clienteAtualizado.setId(id);

        //Consulta se o CEP mudou
        if (!clienteExistente.getCep().equals(clienteRequestDTO.getCep())) {
            
            EnderecoResponseDTO novoEndereco = enderecoService.buscarCep(clienteRequestDTO.getCep());
            
             clienteAtualizado.setLogradouro(novoEndereco.logradouro());
             clienteAtualizado.setBairro(novoEndereco.bairro());
             clienteAtualizado.setCidade(novoEndereco.localidade());
             clienteAtualizado.setEstado(novoEndereco.uf());
        } else {
             
             clienteAtualizado.setLogradouro(clienteExistente.getLogradouro());
             clienteAtualizado.setBairro(clienteExistente.getBairro());
             clienteAtualizado.setCidade(clienteExistente.getCidade());
             clienteAtualizado.setEstado(clienteExistente.getEstado());
        }
        
        
        Cliente clienteSalvo = clienteRepository.save(clienteAtualizado);
        emailService.enviarEmailCadastro(clienteSalvo); 
        
        return clienteMapper.toResponseDto(clienteSalvo);
    }
    
    
    public ClienteResponseDTO buscarClientePorId(Long id) { 
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException(String.format(erroClienteNaoEncontrado, id)));
        
        return clienteMapper.toResponseDto(cliente); 
    }

   
    public Page<ClienteResponseDTO> listarClientes(Pageable pageable) { 
        Page<Cliente> clientesPage = clienteRepository.findAll(pageable);
        
        return clientesPage.map(clienteMapper::toResponseDto);
    }

    
    @Transactional
    public void excluirCliente(Long id) { 
    	//exists vai ao banco e retorna true ou false
        if (!clienteRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException(String.format(erroClienteNaoEncontrado, id));
        }
        clienteRepository.deleteById(id);
    }
}