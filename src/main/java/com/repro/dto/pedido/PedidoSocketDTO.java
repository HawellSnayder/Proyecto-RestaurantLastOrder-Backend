package com.repro.dto.pedido;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class PedidoSocketDTO {

    private Long id;
    private Integer mesaNumero;
    private String estado;
    private BigDecimal total;
}

