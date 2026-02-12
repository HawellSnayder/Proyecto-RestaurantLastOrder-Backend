package com.repro.dto.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoResumenDTO {

    private Long id;
    private Integer mesaNumero;
    private String estado;
    private BigDecimal total;
    private LocalDateTime fechaCreacion;
}

