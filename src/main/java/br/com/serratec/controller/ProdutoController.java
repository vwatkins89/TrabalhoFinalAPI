package br.com.serratec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.serratec.dto.ProdutoRequestDTO;
import br.com.serratec.dto.ProdutoResponseDTO;
import br.com.serratec.entity.Produto;
import br.com.serratec.services.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Produto", description = "Listagem, Cadastro, Alteração e Remoção de Produtos")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoService service;

	@Operation(summary = "Lista todos os produtos", description = "A resposta lista todos os produtos")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", content = {
			@Content(schema = @Schema(implementation = Produto.class), mediaType = "application/json") }, description = "Retorna todos os produtos"),
			@ApiResponse(responseCode = "401", description = "Erro de autenticação"),
			@ApiResponse(responseCode = "403", description = "Não há permissão para acessar o recurso"),
			@ApiResponse(responseCode = "404", description = "Recurso não encontrado"),
			@ApiResponse(responseCode = "505", description = "Exceção interna da aplicação") })
	
	@GetMapping
	public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
		List<ProdutoResponseDTO> produtos = service.buscarTodos();
		return ResponseEntity.ok(produtos);
	}

	@Operation(summary = "Busca um Produto", description = "A resposta é o produto encontrado pelo ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", content = {
			@Content(schema = @Schema(implementation = Produto.class), mediaType = "application/json") }, description = "Produto encontrado com sucesso"),
			@ApiResponse(responseCode = "401", description = "Erro de autenticação"),
			@ApiResponse(responseCode = "403", description = "Não há permissão para acessar o recurso"),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado"),
			@ApiResponse(responseCode = "505", description = "Exceção interna da aplicação") })
	
	@GetMapping("/{id}")
	public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
		ProdutoResponseDTO dto = service.buscarPorId(id);
		return ResponseEntity.ok(dto);
	}

	@Operation(summary = "Cadastra um Produto", description = "A resposta é o produto salvo ")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", content = {
			@Content(schema = @Schema(implementation = Produto.class), mediaType = "application/json") }, description = "Produto cadastrado com sucesso! "),
			@ApiResponse(responseCode = "401", description = "Erro de autenticação"),
			@ApiResponse(responseCode = "403", description = "Não há permissão para acessar o recurso"),
			@ApiResponse(responseCode = "404", description = "Recurso não encontrado"),
			@ApiResponse(responseCode = "505", description = "Exceção interna da aplicação") })
	
	@PostMapping
	public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody ProdutoRequestDTO produtoRequest) {
		ProdutoResponseDTO criado = service.criar(produtoRequest);
		return ResponseEntity.status(201).body(criado);
	}

	@Operation(summary = "Atualiza um Produto", description = "A resposta é o produto atualizado pelo ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", content = {
			@Content(schema = @Schema(implementation = Produto.class), mediaType = "application/json") }, description = "Produto atualizado com sucesso"),
			@ApiResponse(responseCode = "401", description = "Erro de autenticação"),
			@ApiResponse(responseCode = "403", description = "Não há permissão para acessar o recurso"),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado"),
			@ApiResponse(responseCode = "505", description = "Exceção interna da aplicação") })
	
	@PutMapping("/{id}")
	public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id,
			@Valid @RequestBody ProdutoRequestDTO produtoRequest) {
		ProdutoResponseDTO atualizado = service.atualizar(id, produtoRequest);
		return ResponseEntity.ok(atualizado);
	}

	@Operation(summary = "Deleta um Produto", description = "A resposta é o produto removido pelo ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", content = {
			@Content(schema = @Schema(implementation = Produto.class), mediaType = "application/json") }, description = "Produto deletado com sucesso"),
			@ApiResponse(responseCode = "401", description = "Erro de autenticação"),
			@ApiResponse(responseCode = "403", description = "Não há permissão para acessar o recurso"),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado"),
			@ApiResponse(responseCode = "505", description = "Exceção interna da aplicação") })
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}
}
