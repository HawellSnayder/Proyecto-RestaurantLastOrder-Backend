package com.repro.dto.pedido;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class PedidoResumenDTO {

    private Long id;
    private Integer mesaNumero;
    private String estado;
    private BigDecimal total;
    private LocalDateTime fechaCreacion;
}

