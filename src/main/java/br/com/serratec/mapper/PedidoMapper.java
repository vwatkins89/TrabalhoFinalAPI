package br.com.serratec.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.serratec.dto.PedidoRequestDTO;
import br.com.serratec.dto.PedidoResponseDTO;
import br.com.serratec.entity.Pedido;


@Mapper(componentModel = "spring")
public interface PedidoMapper {
	PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);
	
	PedidoRequestDTO toDto(Pedido pedido);
	Pedido toEntity(PedidoRequestDTO pedidoRequestDTO);
	
	PedidoResponseDTO toResponseDto(Pedido pedido);
	Pedido toEntity(PedidoResponseDTO pedidoResponseDTO);
	
}
