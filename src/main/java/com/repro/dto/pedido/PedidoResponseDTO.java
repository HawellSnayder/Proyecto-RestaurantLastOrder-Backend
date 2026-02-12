package com.repro.dto.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoResponseDTO {

    private Long id;
    private Integer mesaNumero;
    private String mesero;
    private String estado;
    private BigDecimal total;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private List<DetallePedidoResponseDTO> detalles;
}

