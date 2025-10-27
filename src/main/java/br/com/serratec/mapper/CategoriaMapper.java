package br.com.serratec.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.serratec.dto.CategoriaRequestDTO;
import br.com.serratec.dto.CategoriaResponseDTO;
import br.com.serratec.entity.Categoria;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
	CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);
	
	CategoriaRequestDTO toDto(Categoria categoria);
	Categoria toEntity(CategoriaRequestDTO categoriaRequestDTO);
	
	CategoriaResponseDTO toResponseDto(Categoria categoria);
	Categoria toEntity(CategoriaResponseDTO categoriaResponseDTO);
	
}
