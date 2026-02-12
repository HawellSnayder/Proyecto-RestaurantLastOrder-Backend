package com.repro.dto.pedido;

import lombok.Data;

import java.util.List;
@Data
public class EditarPedidoRequestDTO {

    private String observaciones;
    private List<DetallePedidoRequestDTO> detalles;
}

