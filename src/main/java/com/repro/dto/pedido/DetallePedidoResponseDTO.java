package com.repro.dto.pedido;

import com.repro.model.DetallePedido;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class DetallePedidoResponseDTO {
    private Long platoId;
    private String plato;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public static DetallePedidoResponseDTO from(DetallePedido detalle) {
        DetallePedidoResponseDTO dto = new DetallePedidoResponseDTO();
        dto.setPlatoId(detalle.getPlato().getId());
        dto.plato = detalle.getPlato().getNombre();
        dto.cantidad = detalle.getCantidad();
        dto.precioUnitario = detalle.getPrecioUnitario();
        dto.subtotal = detalle.getSubtotal();

        return dto;
    }
}

