package com.repro.dto.usuario;

import lombok.Data;

@Data
public class CrearUsuarioRequestDTO {

    private String username;
    private String password;
    private Long rolId;
}

