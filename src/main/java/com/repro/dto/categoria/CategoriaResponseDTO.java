package com.repro.dto.categoria;

import com.repro.model.CategoriaPlato;
import lombok.Data;

@Data
public class CategoriaResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean activo;

    public static CategoriaResponseDTO from(CategoriaPlato categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.id = categoria.getId();
        dto.nombre = categoria.getNombre();
        dto.descripcion=categoria.getDescripcion();
        dto.activo=categoria.getActivo();
        return dto;
    }
}
