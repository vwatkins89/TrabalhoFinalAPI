package br.com.serratec.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.serratec.dto.ProdutoRequestDTO;
import br.com.serratec.dto.ProdutoResponseDTO;
import br.com.serratec.entity.Produto;
import br.com.serratec.exception.NotFoundException;
import br.com.serratec.exception.UsuarioException;
import br.com.serratec.mapper.ProdutoMapper;
import br.com.serratec.repository.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	ProdutoRepository produtoRepository;

	@Autowired
	ProdutoMapper produtoMapper;

	public ProdutoResponseDTO criar(ProdutoRequestDTO produtoRequestDTO) {
		if (produtoRequestDTO.getNome() == null || produtoRequestDTO.getNome().trim().isEmpty()) {
			throw new UsuarioException("O nome do produto é obrigatório.");
		}

		Produto produtoEntity = produtoMapper.toEntity(produtoRequestDTO);
		produtoRepository.save(produtoEntity);

		return produtoMapper.toResponseDto(produtoEntity);
	}

	public List<ProdutoResponseDTO> buscarTodos() {
		return produtoRepository.findAll().stream().map(produtoMapper::toResponseDto).collect(Collectors.toList());
	}

	public ProdutoResponseDTO buscarPorId(Long id) {
		Produto produto = produtoRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Produto não encontrado com o ID: " + id));

		return produtoMapper.toResponseDto(produto);
	}

	public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO produtoRequestDTO) {
		Produto produtoExistente = produtoRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Produto não encontrado com o ID: " + id));

		if (produtoRequestDTO.getNome() == null || produtoRequestDTO.getNome().trim().isEmpty()) {
			throw new UsuarioException("O nome do produto não pode ser vazio.");
		}

		Produto produtoAtualizado = produtoMapper.toEntity(produtoRequestDTO);

		produtoAtualizado.setId(produtoExistente.getId());

		produtoRepository.save(produtoAtualizado);

		return produtoMapper.toResponseDto(produtoAtualizado);
	}

	public void deletar(Long id) {
		Produto produto = produtoRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Produto não encontrado com o ID: " + id));

		produtoRepository.delete(produto);
	}
}
