package com.repro.dto.pedido;

import lombok.Data;

import java.util.List;
@Data
public class PedidoRequestDTO {
    private Integer mesaNumero;
    private List<DetallePedidoRequestDTO> detalles;
    private String observaciones;
}

