package br.com.serratec.service;

import br.com.serratec.dto.ClienteRequestDTO;
import br.com.serratec.dto.ClienteResponseDTO;
import br.com.serratec.dto.ViaCepResponseDTO;
import br.com.serratec.entity.Cliente;
import br.com.serratec.entity.Endereco;
import br.com.serratec.entity.Usuario;
import br.com.serratec.exception.ResourceNotFoundException;
import br.com.serratec.repository.ClienteRepository;
import br.com.serratec.repository.EnderecoRepository;
import br.com.serratec.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final ViaCepService viaCepService;

    public ClienteService(ClienteRepository clienteRepository,
                          UsuarioRepository usuarioRepository,
                          EnderecoRepository enderecoRepository,
                          ViaCepService viaCepService) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.enderecoRepository = enderecoRepository;
        this.viaCepService = viaCepService;
    }

    @Transactional
    public ClienteResponseDTO criar(ClienteRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("ClienteRequestDTO não pode ser null");
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + dto.getUsuarioId()));

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setTelefone(dto.getTelefone());
        cliente.setNumero(dto.getNumero());
        cliente.setComplemento(dto.getComplemento());
        cliente.setUsuario(usuario);
        Cliente salvoCliente = clienteRepository.save(cliente);

        ViaCepResponseDTO via = viaCepService.consultarCep(dto.getCep());

        Endereco endereco = new Endereco();
        endereco.setCep(via.getCep());
        endereco.setLogradouro(via.getLogradouro());
        endereco.setBairro(via.getBairro());
        endereco.setCidade(via.getLocalidade());
        endereco.setEstado(via.getUf());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento() != null && !dto.getComplemento().isBlank()
            ? dto.getComplemento()
            : via.getComplemento());
        endereco.setCliente(salvoCliente);

        Endereco enderecoSalvo = enderecoRepository.save(endereco);

        return buildResponse(salvoCliente, enderecoSalvo);
    }

    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + id));

        Endereco endereco = enderecoRepository.findByClienteId(cliente.getId()).stream().findFirst().orElse(null);

        return buildResponse(cliente, endereco);
    }

    public List<ClienteResponseDTO> listarTodos() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(c -> {
                    Endereco endereco = enderecoRepository.findByClienteId(c.getId()).stream().findFirst().orElse(null);
                    return buildResponse(c, endereco);
                })
                .collect(Collectors.toList());
    }

    private ClienteResponseDTO buildResponse(Cliente cliente, Endereco endereco) {
        ClienteResponseDTO resp = new ClienteResponseDTO();
        resp.setId(cliente.getId());
        resp.setNome(cliente.getNome());
        resp.setCpf(cliente.getCpf());
        resp.setTelefone(cliente.getTelefone());
        resp.setNumero(cliente.getNumero());
        resp.setComplemento(cliente.getComplemento());

        if (endereco != null) {
            resp.setEnderecoId(endereco.getId());
            resp.setCep(endereco.getCep());
            resp.setLogradouro(endereco.getLogradouro());
            resp.setBairro(endereco.getBairro());
            resp.setCidade(endereco.getCidade());
            resp.setEstado(endereco.getEstado());
        }

        return resp;
    }
}