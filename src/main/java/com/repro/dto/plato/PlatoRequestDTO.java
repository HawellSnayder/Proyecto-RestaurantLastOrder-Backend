package com.repro.dto.plato;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlatoRequestDTO {

    private String nombre;
    private BigDecimal precio;
    private Long categoriaId;
    private Boolean disponible;
}

