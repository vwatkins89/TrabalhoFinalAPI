package br.com.serratec.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.serratec.dto.ClienteRequestDTO;
import br.com.serratec.dto.ClienteResponseDTO;
import br.com.serratec.entity.Cliente;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
	ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);
	
	ClienteRequestDTO toDto(Cliente cliente);
	Cliente toEntity(ClienteRequestDTO clienteRequestDTO);
	
	ClienteResponseDTO toResponseDto(Cliente cliente);
	Cliente toEntity(ClienteResponseDTO clienteResponseDTO);
	
}
