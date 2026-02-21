package com.repro.dto.plato;

import com.repro.model.Plato;
import lombok.Data; // <--- Importante
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data // 🔥 Esto genera Getters, Setters, toString, etc.
@NoArgsConstructor
@AllArgsConstructor
public class PlatoResponseDTO {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private String categoria;
    private Boolean disponible;
    private String imagenBase64;

    public static PlatoResponseDTO from(Plato plato) {
        PlatoResponseDTO dto = new PlatoResponseDTO();
        dto.setId(plato.getId());
        dto.setNombre(plato.getNombre());
        dto.setPrecio(plato.getPrecio());
        dto.setCategoria(plato.getCategoria().getNombre());
        dto.setDisponible(plato.getDisponible());

        if (plato.getImagen() != null) {
            dto.setImagenBase64(java.util.Base64.getEncoder().encodeToString(plato.getImagen()));
        }
        return dto;
    }
}