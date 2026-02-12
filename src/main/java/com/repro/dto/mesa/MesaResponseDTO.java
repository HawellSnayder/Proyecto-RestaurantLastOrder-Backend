package com.repro.dto.mesa;

import lombok.Data;

@Data
public class MesaResponseDTO {

    private Long id;
    private Integer numero;
    private Integer capacidad;
    private String estado;
}

