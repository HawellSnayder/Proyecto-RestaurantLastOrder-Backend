package com.repro.dto.auth;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private String nombre;
    private String username;
    private String rol;
}
