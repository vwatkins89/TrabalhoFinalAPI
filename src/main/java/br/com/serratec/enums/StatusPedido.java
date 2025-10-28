package br.com.serratec.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import br.com.serratec.exception.EnumException;

public enum StatusPedido {
	
	AGUARDANDO_PAGAMENTO, PAGO, ENVIADO, ENTREGUE, CANCELADO;

	@JsonCreator
	public static StatusPedido verificaStatusPedido(String value) {
		for (StatusPedido status: StatusPedido.values()) {
			if (status.name().equals(value)) {
				return status;
			}
		}
		throw new EnumException("Status Inválido!");
	}
}
