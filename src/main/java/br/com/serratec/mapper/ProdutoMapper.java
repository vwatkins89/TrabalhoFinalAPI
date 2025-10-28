package br.com.serratec.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.serratec.dto.ProdutoRequestDTO;
import br.com.serratec.dto.ProdutoResponseDTO;
import br.com.serratec.entity.Produto;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
	ProdutoMapper INSTANCE = Mappers.getMapper(ProdutoMapper.class);
	
	ProdutoRequestDTO toDto(Produto produto);
	Produto toEntity(ProdutoRequestDTO produtoRequestDTO);
	
	ProdutoResponseDTO toResponseDto(Produto produto);
	Produto toEntity(ProdutoResponseDTO produtoResponseDTO);
	
}
