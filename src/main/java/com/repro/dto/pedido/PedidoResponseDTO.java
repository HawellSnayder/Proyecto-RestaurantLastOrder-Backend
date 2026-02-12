package com.repro.dto.pedido;

import com.repro.model.Pedido;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class PedidoResponseDTO {

    private Long id;
    private Integer mesaNumero;
    private String mesero;
    private String estado;
    private BigDecimal total;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private List<DetallePedidoResponseDTO> detalles;

    public static PedidoResponseDTO from(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();

        dto.id = pedido.getId();
        dto.mesaNumero = pedido.getMesa().getNumero();
        dto.mesero = pedido.getMesero().getNombre(); // o username
        dto.estado = pedido.getEstado().getNombre();
        dto.total = pedido.getTotal();
        dto.observaciones = pedido.getObservaciones();
        dto.fechaCreacion = pedido.getFechaPedido();

        dto.detalles = pedido.getDetalles()
                .stream()
                .map(DetallePedidoResponseDTO::from)
                .toList();

        return dto;
    }
}

