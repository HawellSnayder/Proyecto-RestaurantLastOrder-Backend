package com.repro.dto.plato;

import com.repro.model.Plato;

import java.math.BigDecimal;

public class PlatoResponseDTO {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private String categoria;
    private Boolean disponible;

    public static PlatoResponseDTO from(Plato plato) {
        PlatoResponseDTO dto = new PlatoResponseDTO();

        dto.id = plato.getId();
        dto.nombre = plato.getNombre();
        dto.precio = plato.getPrecio();
        dto.categoria = plato.getCategoria().getNombre();
        dto.disponible = plato.getDisponible();

        return dto;
    }
}
