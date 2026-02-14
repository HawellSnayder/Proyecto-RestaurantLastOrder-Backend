package com.repro.dto.categoria;

import com.repro.model.Enum.EventoCategoria;
import lombok.Data;

@Data
public class CategoriaSocketDTO {

    private Long id;
    private String nombre;
    private Boolean activo;
    private EventoCategoria evento;
}

