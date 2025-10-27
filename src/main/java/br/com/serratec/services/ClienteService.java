package br.com.serratec.services;

import br.com.serratec.entity.Cliente;
import br.com.serratec.repository.ClienteRepository;
import br.com.serratec.integration.ViaCepCliente;
import br.com.serratec.integration.ViaCepResposta;
import br.com.serratec.exception.NotFoundException;
import br.com.serratec.exception.UsuarioException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClienteService {

	private final ClienteRepository clienteRepository;
	private final ViaCepCliente viaCepCliente;

	public ClienteService(ClienteRepository clienteRepository, ViaCepCliente viaCepCliente) {
        this.clienteRepository = clienteRepository;
        this.viaCepCliente = viaCepCliente;
    }

	private void validarCampos(Cliente cliente) {
		if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
			throw new UsuarioException("O nome do cliente é obrigatório.");
		}
		if (cliente.getCpf() == null || cliente.getCpf().length() != 11) {
			throw new UsuarioException("CPF inválido. Deve conter 11 dígitos.");
		}
		if (cliente.getEmail() == null || !cliente.getEmail().contains("@")) {
			throw new UsuarioException("E-mail inválido.");
		}
	}

	private void preencherEnderecoViaCep(Cliente cliente) {
		if (cliente.getCep() != null && !cliente.getCep().isEmpty()) {
			ViaCepResposta endereco = viaCepCliente.obterEnderecoPorCep(cliente.getCep());

			cliente.setLogradouro(endereco.getLogradouro());
			cliente.setCidade(endereco.getLocalidade());
			cliente.setEstado(endereco.getUf());
		}
	}

	@Transactional
	public Cliente criar(Cliente cliente) {
		validarCampos(cliente);
		preencherEnderecoViaCep(cliente);

		Cliente clienteSalvo = clienteRepository.save(cliente);
		return clienteSalvo;
	}

	@Transactional
	public Cliente atualizar(Long id, Cliente detalhesCliente) {
		Cliente clienteExistente = clienteRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Cliente"));

		validarCampos(detalhesCliente);

		clienteExistente.setNome(detalhesCliente.getNome());
		clienteExistente.setEmail(detalhesCliente.getEmail());

		if (detalhesCliente.getCep() != null && !detalhesCliente.getCep().equals(clienteExistente.getCep())) {
			clienteExistente.setCep(detalhesCliente.getCep());
			preencherEnderecoViaCep(clienteExistente);
		}

		return clienteRepository.save(clienteExistente);
	}

	public Optional<Cliente> buscarPorId(Long id) {
		return clienteRepository.findById(id);
	}
}