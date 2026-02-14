package com.repro.dto.plato;

import com.repro.model.Enum.EventoPlato;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlatoSocketDTO {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private Long categoriaId;
    private String categoriaNombre;
    private Boolean disponible;
    private EventoPlato evento;
}
