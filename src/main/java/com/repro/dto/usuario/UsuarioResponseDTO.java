package com.repro.dto.usuario;

import com.repro.model.Usuario;
import lombok.Data;

@Data
public class UsuarioResponseDTO {

    private Long id;
    private String nombre;
    private String username;
    private String rol;

    public static UsuarioResponseDTO from(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setUsername(usuario.getUsername());
        dto.setRol(usuario.getRol().getNombre());
        return dto;
    }
}

